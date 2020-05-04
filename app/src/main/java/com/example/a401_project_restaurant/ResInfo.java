package com.example.a401_project_restaurant;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ResInfo extends Fragment {
    int minteger = 0;
    private Button button;
    private ImageView backArrow;
    private String previousFragment; //variable so application knows which fragment to return to after reservation made
    private TextView displayInteger;
    private Restaurant currRestaurant; //the restaurant whose details are currently being displayed


    public ResInfo (String passedPreviousFragment, Restaurant passedRestaurant){
        this.previousFragment = passedPreviousFragment;
        this.currRestaurant = passedRestaurant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View resInfo = inflater.inflate(R.layout.resinfo, container, false);

        //initialize page text and button elements
        button = resInfo.findViewById(R.id.button2);
        displayInteger = resInfo.findViewById(R.id.textView21);
        backArrow = resInfo.findViewById(R.id.backArrow);

        //when user clicks on button
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getContext(),
                        "Reservation Successful", Toast.LENGTH_LONG).show();

            }
        });

        //when user clicks on back arrow
        backArrow.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //if user is coming from the discounts page
                if(previousFragment.equals("Discounts Page")){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    DiscountsPageFragment discountsPageFragment = new DiscountsPageFragment();
                    fragmentTransaction.replace(R.id.frameLayout, discountsPageFragment);
                    fragmentTransaction.commit();

                    //if user is coming from the map page
                }else if(previousFragment.equals("Map Page")){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    MapPageFragment mapPageFragment = new MapPageFragment();
                    fragmentTransaction.replace(R.id.frameLayout, mapPageFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        return resInfo;
    }



    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {
        minteger = minteger - 1;
        display(minteger);
    }

    private void display(int number) {
        displayInteger.setText("" + number);
    }
}
