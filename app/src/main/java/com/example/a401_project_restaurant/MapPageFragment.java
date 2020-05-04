package com.example.a401_project_restaurant;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapPageFragment extends Fragment implements OnMapReadyCallback{


    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE= 101;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap gMap;
    private LocationCallback locationCallback;
    private FirebaseLoader firebaseLoader = FirebaseLoader.getInstance();
    private List<Restaurant> restaurants;

    public MapPageFragment() {

        restaurants = firebaseLoader.getRestaurants();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_page, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment fragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);

        if(this != null) {
            fragment.getMapAsync(this);
        }

        //set up location services and get user's last location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();
    }

    private void fetchLastLocation() {

        //if user has not given permission to access their location
        if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null)
                {
                    currentLocation = location;
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 18));
                }else{

                    //find user's current location
                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(5000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            if(locationResult == null)
                            {
                                return;
                            }
                            currentLocation = locationResult.getLastLocation();
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 18));
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        }
                    };
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMyLocationEnabled(true);

        //get restaurants with active discounts and display their locations on map in order of descending ratings
        db.collection("restaurants")
                .orderBy("ratings", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LatLng marker = null;
                            int i = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                IconGenerator iconFactory = new IconGenerator(getContext());
                                Map<String, Object> restaurantInfo = document.getData();

                                if ((restaurantInfo != null) && ((Boolean) restaurantInfo.get("discountActive") == true)) {
                                    marker = new LatLng((Double) restaurantInfo.get("latitude"), (Double) restaurantInfo.get("longitude"));
                                    gMap.addMarker(new MarkerOptions().title((String) restaurantInfo.get("name")).position(marker)
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(String.valueOf(i)))));
                                    i++;
                                }


                            }
                        }
                    }
                });

        //when click on marker info window, go to detailed discount page for that restaurant
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ResInfo detailedDiscountTestFragment = new ResInfo("Map Page", findRestaurant(marker.getTitle()));
                fragmentTransaction.replace(R.id.frameLayout, detailedDiscountTestFragment);
                fragmentTransaction.commit();
            }

            public Restaurant findRestaurant(String restaurantTitle){
                Restaurant targetRestaurant = null;
                for(int i = 0; i < restaurants.size(); i++){
                    if(restaurants.get(i).getName().equals(restaurantTitle)){
                        targetRestaurant = restaurants.get(i);
                    }
                }
                return targetRestaurant;
            }
        });
    }

}
