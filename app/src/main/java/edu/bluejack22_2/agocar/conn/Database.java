package edu.bluejack22_2.agocar.conn;

import com.google.firebase.firestore.FirebaseFirestore;

public class Database {
    private static FirebaseFirestore db = null;

    public static FirebaseFirestore getInstance(){
        if(db == null){
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

}
