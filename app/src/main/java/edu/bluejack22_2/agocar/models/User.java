package edu.bluejack22_2.agocar.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;

public class User {
    private String username;
    private String email;
    private String password;
    private String role;

    private String preference;

    public User(String username, String email, String password, String role, String preference) {
        this.username = username;
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        //Check Password Benar ato ga
//        BCrypt.Result result = BCrypt.verifyer().verify(string, string);
//        if(result.verified){
//
//        }else{
//
//        }
        this.role = role;
        this.preference = preference;
    }

    public void insert(edu.bluejack22_2.agocar.other.OnSuccessListener listener) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", this.username);
        user.put("email", this.email);
        user.put("password", this.password);
        user.put("role", this.role);
        user.put("preference", this.preference);



        Database.getInstance().collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onSuccess(true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onSuccess(false);
                    }
                });


    }

    public static void getUser(String email, String password, RetrievedUserListener listener){
        Database.getInstance().collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), queryDocumentSnapshots.getDocuments().get(0).getString("password").toString());
                        if(result.verified){
                            String retEmail = queryDocumentSnapshots.getDocuments().get(0).getString("email").toString();
                            String retPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password").toString();
                            String retRole = queryDocumentSnapshots.getDocuments().get(0).getString("role").toString();
                            String retPreference = null;
                            if(queryDocumentSnapshots.getDocuments().get(0).getString("preference") != null){
                                retPreference = queryDocumentSnapshots.getDocuments().get(0).getString("preference").toString();
                            }

                            String retUsername = queryDocumentSnapshots.getDocuments().get(0).getString("username").toString();

                            listener.retrievedUser(new User(retUsername, retEmail, retPassword, retRole, retPreference));
                        }else{
                            listener.retrievedUser(null);
                        }

                    } else {
                        listener.retrievedUser(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.retrievedUser(null);
                });
    }

    public static void checkUserExist(String email, edu.bluejack22_2.agocar.other.OnSuccessListener listener) {
        Database.getInstance().collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // User exists
                        listener.onSuccess(true);
                    } else {
                        // User does not exist
                        listener.onSuccess(false);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.onSuccess(false);
                });
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
