package com.example.a401_project_restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DiscountCreationPage extends AppCompatActivity {

    private TextView discountAmountDiscCreation;
    private SeekBar discountSetterSeekBar;
    private TextView restaurantTitleDiscCreation;
    private EditText dateAndTimeSelector;
    private Button sendOutDiscountButton;
    private FirebaseLoader firebaseLoader = FirebaseLoader.getInstance();
    private Timestamp discountEndDate;
    private int discountAmt;
    private User currUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DiscountCreationPage(){
        currUser = firebaseLoader.getUser();
        discountEndDate = null;
        discountAmt = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_creation_page);

        discountAmountDiscCreation = (TextView) findViewById(R.id.discountAmountDiscCreation);
        discountSetterSeekBar = (SeekBar) findViewById(R.id.discountSetterSeekBar);
        restaurantTitleDiscCreation = (TextView) findViewById(R.id.restaurantTitleDiscCreation);
        dateAndTimeSelector = (EditText) findViewById(R.id.dateAndTimeSelector);
        sendOutDiscountButton = (Button) findViewById(R.id.sendOutDiscountButton);
        dateAndTimeSelector.setInputType(InputType.TYPE_NULL);

        discountSetterSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                discountAmountDiscCreation.setText(String.valueOf(progress) + "%");
                discountAmt = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dateAndTimeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog();
            }
        });

        sendOutDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(discountEndDate == null){
                    Toast.makeText(getApplicationContext(), "An end date for the discount needs to be selected. Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(discountAmt <= 0){
                    Toast.makeText(getApplicationContext(), "Discount needs to be greater than 0. Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                DocumentReference restaurantRef = null;
                restaurantRef = db.collection("restaurants").document(currUser.getUid());

                if(restaurantRef != null){
                    restaurantRef.update("discountActive", true);
                    restaurantRef.update("discountAmt", discountAmt);
                    restaurantRef.update("discountEndTime", discountEndDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Discount successfully sent!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        restaurantTitleDiscCreation.setText(currUser.getName());
    }

    private void showDateTimeDialog(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy hh:mm aa");
                        dateAndTimeSelector.setText(simpleDateFormat.format(calendar.getTime()));
                        discountEndDate = new Timestamp(calendar.getTime());
                    }
                };

                new TimePickerDialog(DiscountCreationPage.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(DiscountCreationPage.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
