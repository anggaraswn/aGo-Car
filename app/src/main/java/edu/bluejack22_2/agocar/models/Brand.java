package edu.bluejack22_2.agocar.models;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;
import edu.bluejack22_2.agocar.other.RetrievedUserListener;

public class Brand {

    private String documentID;
    private String name;
    private String image;
    private boolean isSelected;

    public Brand(String documentID, String name, String image) {
        this.documentID = documentID;
        this.name = name;
        this.image = image;
        this.isSelected = false;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static void getBrands(RetrievedBrandsListener listener){
        ArrayList<Brand> brands = new ArrayList<>();
        Database.getInstance().collection("brands")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot b : queryDocumentSnapshots){
                            String documentID = b.getId();
                            String name = b.getString("name");
                            String image = b.getString("image");
                            brands.add(new Brand(documentID, name, image));
                        }
                        listener.retrievedBrands(brands);
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred
//                    listener.retrievedUser(null);
                });
    };

}
