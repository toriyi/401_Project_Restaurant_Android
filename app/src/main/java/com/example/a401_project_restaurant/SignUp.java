package com.example.a401_project_restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText emailU,passwordU,name,phoneNum;
    private SeekBar distance;
    private boolean location;
    private Button signup;
    private FirebaseAuth mAuth;
    private DocumentReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

//        emailU = findViewById(R.id.editText5);
//        passwordU = findViewById(R.id.editText4);

        mAuth = FirebaseAuth.getInstance();


        initializeUI();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
                userInformation();
            }
        });
    }

    private void registerNewUser() {

        String email, password, username, pNum;
        email = emailU.getText().toString();
        password = passwordU.getText().toString();
        username = name.getText().toString();
        pNum = phoneNum.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Please enter name!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pNum)) {
            Toast.makeText(getApplicationContext(), "Please enter phone number!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initializeUI() {
        emailU = findViewById(R.id.editText5);
        passwordU = findViewById(R.id.editText4);
        name = findViewById(R.id.editText3);
        phoneNum = findViewById(R.id.editText6);
        location = true;
        distance = findViewById(R.id.seekBar);
        signup = findViewById(R.id.button);
    }

    private void userInformation() {
        String userName = name.getText().toString().trim();
        String userEmail = emailU.getText().toString().trim();
        String phoneno = phoneNum.getText().toString().trim();
        Boolean locationEn = location;
        SeekBar maxDistance = distance;
        UserInfo userinformation = new UserInfo(userName, locationEn, maxDistance, userEmail, phoneno);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = db.collection("users").document(user.getUid());
//        databaseReference.child(user.getUid()).setValue(userinformation);
        Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
    }

}
