package edu.bluejack22_2.agocar.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String role;

    private ArrayList<String> preference;

    public User(String id, String username, String email, String password, String role, ArrayList<String> preference) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.role = role;
        this.preference = preference;
    }
    public User(String username, String email, String password, String role, ArrayList<String> preference) {
        this.id = null;
        this.username = username;
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.role = role;
        this.preference = preference;
    }

    public void update(edu.bluejack22_2.agocar.other.OnSuccessListener listener){
        Database.getInstance().collection("users").document(this.id).update("email",
                this.email, "password", this.password, "role", this.role, "username",
                this.username, "preference", this.preference).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onSuccess(false);
            }
        });
    }

    public void storeUserToSharedPref(){}
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
                        id = documentReference.getId().toString();
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
                            String retId = queryDocumentSnapshots.getDocuments().get(0).getId().toString();
                            String retEmail = queryDocumentSnapshots.getDocuments().get(0).getString("email").toString();
                            String retPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password").toString();
                            String retRole = queryDocumentSnapshots.getDocuments().get(0).getString("role").toString();
                            ArrayList<String> retPreference = null;
                            if(queryDocumentSnapshots.getDocuments().get(0).get("preference") != null){
                                retPreference = (ArrayList<String>)queryDocumentSnapshots.getDocuments().get(0).get("preference");
                            }

                            String retUsername = queryDocumentSnapshots.getDocuments().get(0).getString("username").toString();

                            listener.retrievedUser(new User(retId, retUsername, retEmail, retPassword, retRole, retPreference));

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPreference() {
        return preference;
    }

    public void setPreference(ArrayList<String> preference) {
        this.preference = preference;
    }
}
