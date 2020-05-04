package com.example.a401_project_restaurant;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseLoader {

    private static FirebaseLoader instance = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private ArrayList<Restaurant> restaurants;
    final private User user;

    //to be used later to implement customer vs. restaurant log in
    final private Restaurant currRestaurant;


    private FirebaseLoader()
    {
        //initialize variables the different fragments will be accessing to get
        //restaurant and user data
        restaurants = new ArrayList<Restaurant>();
        user = new User();
        currRestaurant = new Restaurant();
    }

    public static FirebaseLoader getInstance(){
        if(instance == null)
        {
            instance = new FirebaseLoader();
        }
        return instance;
    }

    public ArrayList<Restaurant> loadRestaurants(){

        db.collection("restaurants")
                .orderBy("ratings", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> restaurantInfo = document.getData();
                                Restaurant currRestaurant = new Restaurant();

                                //if the restaurant has an active discount
                                if((Boolean)restaurantInfo.get("discountActive") == true){

                                     //load restaurant data
                                     currRestaurant.setName((String)restaurantInfo.get("name"));
                                     currRestaurant.setDescription((String)restaurantInfo.get("description"));
                                     currRestaurant.setRating(((Number)restaurantInfo.get("ratings")).doubleValue());
                                     currRestaurant.setDiscountAmount(((Number)restaurantInfo.get("discountAmt")).intValue());
                                     currRestaurant.setEmail((String) restaurantInfo.get("email"));
                                     currRestaurant.setDiscountActive((Boolean)restaurantInfo.get("discountActive"));
                                     currRestaurant.setReservationSpots(((Number)restaurantInfo.get("reservationSpots")).intValue());
                                     currRestaurant.setTimestamp((Timestamp)restaurantInfo.get("discountEndTime"));
                                    restaurants.add(currRestaurant);

                                }

                            }

                        }
                    }
                });

        return restaurants;
    }

    public String loadUser(String email, int passwordLength){

        final String passableEmail = email;
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> userInfo = document.getData();

                                //if user is the user we are currently looking for
                                if(userInfo.get("email").equals(passableEmail)){
                                    user.setUid(document.getId());
                                    user.setName((String)userInfo.get("name"));
                                    user.setLocationServicesEnabled((boolean)userInfo.get("location enabled"));
                                    user.setPhoneNumber((String)userInfo.get("phone"));
                                }

                            }

                        }
                    }
                });

        user.setEmail(email);
        user.setPasswordLength(passwordLength);
        return "user";
    }

    public ArrayList<Restaurant> getRestaurants(){
        return restaurants;
    }

    public User getUser(){
        return user;
    }



}
