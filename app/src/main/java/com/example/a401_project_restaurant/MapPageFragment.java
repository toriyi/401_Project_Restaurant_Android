package com.example.a401_project_restaurant;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import java.util.Map;
import java.util.Vector;

//import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapPageFragment extends Fragment implements OnMapReadyCallback{
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener


    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE= 101;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap gMap;
    private GoogleApiClient googleApiClient;
//    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private LocationCallback locationCallback;

    public MapPageFragment() {
        // Required empty public constructor
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
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
////        {
////            checkUserLocationPermission();
////        }
        SupportMapFragment fragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        if(this != null) {
            System.out.println("It's null!!!");
            fragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation();

    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println("WE HAVE A SUCCESSSSS");
                if(location != null)
                {
                    currentLocation = location;
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 14));
//                    Toast.makeText(getActivity().getApplicationContext(), currentLocation.getLatitude()
//                            + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                    if(this.getClass().getSimpleName() == ""){
//                        System.out.println("nulllllll class");
//                        System.out.println("Object type: " + this.getClass().getName());
//                    }else {
//                        System.out.println("Object type: " + this.getClass().getSimpleName());
//                    }
//                    SupportMapFragment fragment = (SupportMapFragment)((Fragment.this).getSupportFragmentManager().findFragmentById(R.id.map));
//                    if(this != null) {
//                        System.out.println("It's null!!!");
//                        fragment.getMapAsync(OnMapReadyCallback.this);
//                    }
                }else{
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
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 14));
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        }
                    };
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;

//        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            buildGoogleApiClient();
            gMap.setMyLocationEnabled(true);
//        }


        System.out.println("I'm ready!!!");
        if(currentLocation != null) {
//            LatLng marker = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
//            googleMap.addMarker(new MarkerOptions().title("Home").position(marker));
        }
        Vector<LatLng> markers = new Vector<LatLng>();
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LatLng marker = null;
                            int i = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(getContext() == null){
                                    System.out.println("context is null!!!");
                                }
                                IconGenerator iconFactory = new IconGenerator(getContext());
                                Map<String, Object> restaurantInfo = document.getData();
                                marker = new LatLng((Double)restaurantInfo.get("latitude"), (Double)restaurantInfo.get("longitude"));
//                                markers.add(marker);
                                gMap.addMarker(new MarkerOptions().title((String) restaurantInfo.get("name")).position(marker)
                                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(String.valueOf(i)))));
                                i++;


                            }
//                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        System.out.println("VECTOR SIZE: " + markers.size());
        for(int i = 0; i < markers.size(); i++){
//            googleMap.addMarker(new MarkerOptions().title("Home").position(markers.get(i)));
        }

//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers.get(0), 13));



//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (mMap != null) {
//                mMap.setMyLocationEnabled(true);
//            }
//        } else {
//            // Permission to access the location is missing. Show rationale and request permission
//            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        }
    }

    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

            }
            else{
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else{
            return true;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch(requestCode)
//        {
//            case Request_User_Location_Code:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        if(googleApiClient == null)
//                        {
//                            buildGoogleApiClient();
//                        }
//                        gMap.setMyLocationEnabled(true);
//
//                    }
//                }else
//                {
//                    Toast.makeText(this.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//
//                }
//                return;
//        }
//    }

//    protected synchronized void buildGoogleApiClient()
//    {
//        googleApiClient = new GoogleApiClient.Builder(this.getContext()).addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        googleApiClient.connect();
//
//    }
//    @Override
//    public void onLocationChanged(Location location){
//        lastLocation = location;
//        if(currentUserLocationMarker != null)
//        {
//            currentUserLocationMarker.remove();
//        }
//
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng).title("User Current Location");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//
//        currentUserLocationMarker = gMap.addMarker(markerOptions);
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        gMap.moveCamera(CameraUpdateFactory.zoomBy(15));
//
//        if(googleApiClient != null)
//        {
////            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
//            fusedLocationProviderClient.removeLocationUpdates(this.);
//        }
//    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(1100);
//        locationRequest.setFastestInterval(1100);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
////        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
////            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
////        }
////        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        SupportMapFragment fragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
//        fragment.getMapAsync(this);
//    }

}
