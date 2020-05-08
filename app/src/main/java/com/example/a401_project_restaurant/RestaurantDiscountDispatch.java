package com.example.a401_project_restaurant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class RestaurantDiscountDispatch extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_discount_dispatch);

        button = (Button) findViewById(R.id.sendOutDiscountButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "Discount Successfully Sent", Toast.LENGTH_LONG).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCurrentHour(Integer currentHour){
        TimePicker simpleTimePicker=(TimePicker)findViewById(R.id.simpleTimePick); // initiate a time picker
// set the value for current hours
        //simpleTimePicker.setCurrentHour(5); // before api level 23
        simpleTimePicker.setHour(5); // from api level 23
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCurrentMinute(Integer currentMinute){
        TimePicker simpleTimePicker=(TimePicker)findViewById(R.id.simpleTimePick); // initiate a time picker
// set the value for current hours
        //simpleTimePicker.setCurrentMinute(35); // before api level 23
        simpleTimePicker.setMinute(35); // from api level 23
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCurrentHour(){
        TimePicker simpleTimePicker = (TimePicker)findViewById(R.id.simpleTimePick); // initiate a time
        //pickerint hours =simpleTimePicker.getCurrentHour(); // before api level 23
        int hours =simpleTimePicker.getHour(); // after api level 23
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCurrentMinute(){
        TimePicker simpleTimePicker = (TimePicker)findViewById(R.id.simpleTimePick); // initiate a time picker
        //int minutes = simpleTimePicker.getCurrentMinute(); // before api level 23
        int minutes = simpleTimePicker.getMinute(); // after api level 23
    }

    public void setIs24HourView(Boolean is24HourView){
        TimePicker simpleTimePicker = (TimePicker)findViewById(R.id.simpleTimePick); // initiate a time picker
        simpleTimePicker.setIs24HourView(true); // set 24 hours mode for the time picker
    }

    public void is24HourView(){
        TimePicker simpleTimePicker = (TimePicker)findViewById(R.id.simpleTimePick); // initiate a time picker
        Boolean mode=simpleTimePicker.is24HourView(); // check the current mode of the time picker
    }

    public void setOnTimeChangedListener(TimePicker.OnTimeChangedListener onTimeChangedListener){
        TimePicker simpleTimePicker = (TimePicker)findViewById(R.id.simpleTimePick); // initiate a time picker

        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });
    }



}
