package edu.bluejack22_2.agocar.models;

import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.OnUserExistListener;

public class User {
    private String username;
    private String email;
    private String password;
    private String role;

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
