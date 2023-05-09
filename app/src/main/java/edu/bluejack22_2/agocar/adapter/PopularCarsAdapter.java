package edu.bluejack22_2.agocar.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.bluejack22_2.agocar.AddCarActivity;
import edu.bluejack22_2.agocar.CarDetailActivity;
import edu.bluejack22_2.agocar.CarsActivity;
import edu.bluejack22_2.agocar.HomeActivity;
import edu.bluejack22_2.agocar.LoginActivity;
import edu.bluejack22_2.agocar.NewsActivity;
import edu.bluejack22_2.agocar.R;
import edu.bluejack22_2.agocar.conn.Database;
import edu.bluejack22_2.agocar.models.Article;
import edu.bluejack22_2.agocar.models.Brand;
import edu.bluejack22_2.agocar.models.Car;
import edu.bluejack22_2.agocar.other.OnSuccessListener;
import edu.bluejack22_2.agocar.other.RetrievedBrandsListener;
import edu.bluejack22_2.agocar.other.RetrievedCarsListener;

public class PopularCarsAdapter extends RecyclerView.Adapter<PopularCarsAdapter.HomeViewHolder> {

    public ArrayList<Car> cars = new ArrayList<>();
    private Context context;
    private Uri imageUri;
    public int currentlyEditingPosition = -1; // initialize to -1 to indicate no currently editing position
    LayoutInflater inflater;
    public int position;
    ArrayList<String> brandNames = new ArrayList<>();


    public PopularCarsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void updateImage(Uri imageUri, int pos){
        cars.get(pos).setImageUri(imageUri);
        this.imageUri = imageUri;
        Log.d("Uriasd", ""+cars.get(currentlyEditingPosition).getImageUri());
        notifyItemChanged(pos);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_popular_cars_card, parent, false);
        return new PopularCarsAdapter.HomeViewHolder(view, inflater, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Car car = cars.get(position);
        Picasso.get().load(car.getImage()).into(holder.ivImage);
        holder.tvName.setText(car.getName());

        double number = car.getPrice();
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(3);

        String formattedNumber = formatter.format(number);

        holder.tvPrice.setText("$" +formattedNumber);
        holder.tvRatings.setText(car.getRating()+"");
        holder.tvHP.setText(car.getEngine()+"");
        holder.tvTransmission.setText(car.getTransmission());

        if(car.getImageUri() != null){
            holder.showModal(currentlyEditingPosition);

        }
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
        ImageView ivImage, ivClose, ivDisplayedImage;
        TextView tvName, tvRatings, tvHP, tvTransmission, tvPrice;
        EditText etCarName, etCarEngineHp, etCarPrice, etCarRating, etCarDescription;
        Spinner spCarTransmission, spCarBrand, spCarFuel;
        Button btnSave;
        private static final int IMAGE_PICKER_REQUEST_CODE = 1001;
        private Map<String, String> cloudinaryConfig = new HashMap<>();
        AlertDialog dialog;
        String[] transmissions = new String[]{"Automatic", "Manual", "Hybrid"};
        String[] fuels = new String[]{"Petrol", "Electric", "Hybrid"};
        private String spBrandValue, spTransmissionValue, spFuelValue;
        private ArrayList<Brand> brandList;
        LayoutInflater inflater;
        Context context;
        CardView cardView;



        void setConnection(){
            cloudinaryConfig.put("cloud_name", "dmpbgjnrc");
            cloudinaryConfig.put("api_key", "596936696592152");
            cloudinaryConfig.put("api_secret", "m5pavlf12IXv1Y4maOCv5OE5DNU");
        }

        void setSpinnerValue(){
            //Car Transmission Spinner
            ArrayAdapter<String> transmissionAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, transmissions);
            spCarTransmission.setAdapter(transmissionAdapter);

            spCarTransmission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spTransmissionValue = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //Car Brand Spinner

            ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, brandNames);
            spCarBrand.setAdapter(brandAdapter);

            spCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spBrandValue = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            //Car Fuel Spinner
            ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, fuels);
            spCarFuel.setAdapter(fuelAdapter);

            spCarFuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spFuelValue = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


        void setElements(View modalView){

            Car selectedCars = cars.get(getPosition());
            etCarName = modalView.findViewById(R.id.etCarName);
            spCarTransmission = modalView.findViewById(R.id.spCarTransmission);
            spCarBrand = modalView.findViewById(R.id.spCarBrand);
            spCarFuel = modalView.findViewById(R.id.spCarFuel);
            etCarEngineHp = modalView.findViewById(R.id.etCarEngineHP);
            etCarPrice = modalView.findViewById(R.id.etCarPrice);
            etCarRating = modalView.findViewById(R.id.etCarRating);
            etCarDescription = modalView.findViewById(R.id.etCarDescription);
            ivDisplayedImage = modalView.findViewById(R.id.ivDisplayedImage);
            btnSave= modalView.findViewById(R.id.button2);

            Brand.getBrands(new RetrievedBrandsListener() {
                @Override
                public void retrievedBrands(ArrayList<Brand> brands) {
                    if(brands != null){
                        brandList = brands;
                        for(Brand brand : brandList){
                            brandNames.add(brand.getName());
                        }
                        setSpinnerValue();
                        if (imageUri == null) {
                            Picasso.get().load(selectedCars.getImage()).into(ivDisplayedImage);
                        } else {
                            ivDisplayedImage.setImageURI(imageUri);
                        }
                        etCarName.setText(selectedCars.getName());
                        int positionTransmission = 0;
                        for (String s: transmissions) {
                            if(s.equals(selectedCars.getTransmission())) break;
                            positionTransmission++;
                        }
                        spCarTransmission.setSelection(positionTransmission);
                        int positionBrand = 0;
                        for (Brand b : brandList){
                            if(b.getDocumentID().equals(selectedCars.getBrandID())) break;
                            positionBrand++;
                        }
                        spCarBrand.setSelection(positionBrand);
                        int positionFuel = 0;
                        for (String s: fuels) {
                            if(s.equals(selectedCars.getFuel())) break;
                            positionFuel++;
                        }
                        spCarFuel.setSelection(positionFuel);
                    }
                }
            });


            etCarEngineHp.setText(""+selectedCars.getEngine());
            etCarPrice.setText(""+selectedCars.getPrice());
            etCarRating.setText(""+selectedCars.getRating());
            etCarDescription.setText(selectedCars.getDescription());
        }

        void updateData(Car updatedCar ,String imageUrl){
            Log.d("test", "AGin");
            Log.d("asd", imageUrl);
            updatedCar.setImage(imageUrl);

            updatedCar.update(new OnSuccessListener() {
                @Override
                public void onSuccess(boolean success) {
                    Log.d("ttttttttttttttttt", "Success");
                    dialog.dismiss();
                    CarsActivity.loadCars();
                    Toast.makeText(context, "Successfully Update a Car!", Toast.LENGTH_LONG).show();
                }
            });
        }

        void uploadImage(Car updatedCar){
            Log.d("uplimg", "Test");
            Cloudinary cloudinary = new Cloudinary(cloudinaryConfig);

            try {
                ContentResolver resolver = context.getContentResolver();
                InputStream inputStream = resolver.openInputStream(updatedCar.getImageUri());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > -1 ) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                byte[] imageData = baos.toByteArray();


                new Thread(new Runnable() {
                    public void run() {

                        // Perform Cloudinary upload operation here
                        try {
                            Map uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.emptyMap());
                            String imageUrl = (String) uploadResult.get("secure_url");

                            if(imageUrl != null){
                                imageUri = null;
                                cars.get(currentlyEditingPosition).setImageUri(null);
                                currentlyEditingPosition = -1;
                                updateData(updatedCar, imageUrl);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Brand getCarBrand(){
            for(Brand brand : brandList){
                if(brand.getName().equals(spBrandValue)){
                    return brand;
                }
            }
            return null;
        }

        public void showModal(int itemPos){
            Log.d("qwer", "Called");
            setConnection();
            LayoutInflater inflater = this.inflater;
            View modalView = inflater.inflate(R.layout.edit_car_modal, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
            builder.setView(modalView);
            dialog = builder.create();
            // Set the window properties of the dialog
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }


            //Set Selected Data to TextField
            setElements(modalView);
            dialog.show();

            ivClose = modalView.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call dialog.dismiss() to close the dialog
                    dialog.dismiss();
                }
            });



            ivDisplayedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("bnmx", ""+  getAdapterPosition());
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((CarsActivity) context).startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
                    dialog.dismiss();
                }
            });

            btnSave.setOnClickListener(e -> {
                Log.d("asdcposition", ""+ currentlyEditingPosition);
                Log.d("zxc", ""+getAdapterPosition());

                String name, transmission, fuel, description;
                double engine, rating, price;
                name = etCarName.getText().toString();
                transmission = spTransmissionValue;
                fuel = spFuelValue;
                engine = Double.parseDouble(etCarEngineHp.getText().toString());
                price = Double.parseDouble(etCarPrice.getText().toString());
                rating = Double.parseDouble(etCarRating.getText().toString());
                description = etCarDescription.getText().toString();

                Brand brand = getCarBrand();
                Car car = cars.get(itemPos);
                Car updatedCar = new Car(car.getId(), name, transmission, "", fuel, description, brand.getDocumentID(), engine, rating, price);
                updatedCar.setImageUri(imageUri);

                if(imageUri != null){
                    uploadImage(updatedCar);
                }else{
                    updateData(updatedCar, car.getImage());
                }
            });
        }


        public HomeViewHolder(@NonNull View itemView, LayoutInflater inflater, Context context) {
            super(itemView);
            this.inflater = inflater;
            this.context = context;

            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnCreateContextMenuListener(this);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvRatings = itemView.findViewById(R.id.tvRatings);
            tvHP = itemView.findViewById(R.id.tvHP);
            tvTransmission = itemView.findViewById(R.id.tvTransmission);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Car car = cars.get(position);
            Intent i = new Intent(v.getContext(), CarDetailActivity.class);
            i.putExtra("carID", car.getId());
            v.getContext().startActivity(i);
        }

        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (!HomeActivity.user.getRole().equals("Admin")) return;
            contextMenu.setHeaderTitle("Select Action");
            contextMenu.add(getAdapterPosition(), 1001, 0, "Update");
            contextMenu.add(getAdapterPosition(), 1002, 1, "Delete");
            position = getAdapterPosition();
        }

    }

    public void setCars(ArrayList<Car> newCars) {
        cars.clear();
        cars.addAll(newCars);
        notifyDataSetChanged();
    }
}