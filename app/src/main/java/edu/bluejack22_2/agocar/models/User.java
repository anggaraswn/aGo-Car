package edu.bluejack22_2.agocar.models;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import at.favre.lib.crypto.bcrypt.BCrypt;
import edu.bluejack22_2.agocar.RegisterActivity;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.OnUserExistListener;

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

    public boolean insert() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", this.username);
        user.put("email", this.email);
        user.put("password", this.password);
        user.put("role", this.role);
        user.put("preference", this.preference);


        AtomicBoolean isSuccess = new AtomicBoolean(false);
        Database.getInstance().collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        isSuccess.set(true);
                      // countdown the latch when onSuccess is called
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isSuccess.set(false);
                      // countdown the latch when onFailure is called
                    }
                });

     // wait for the latch to be counted down before returning
        return isSuccess.get();
    }

    public static void checkUserExist(String email, OnUserExistListener listener) {
        Database.getInstance().collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // User exists
                        listener.onUserExist(true);
                    } else {
                        // User does not exist
                        listener.onUserExist(false);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
                    listener.onUserExist(false);
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
