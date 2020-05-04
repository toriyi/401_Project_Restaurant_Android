package com.example.a401_project_restaurant;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.internal.StringUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccountFragment extends Fragment {
    private  TextView name;
    private EditText editName;
    private EditText editPassword;
    private EditText editEmail;
    private  EditText editPhone;
    private Switch enableLocationSwitch;
    private Button saveChangesButton;
    final private User user;
    private Boolean passwordChanged;
    private FirebaseLoader firebaseLoader = FirebaseLoader.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public UserAccountFragment() {
        user = firebaseLoader.getUser();
        passwordChanged = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View userView = inflater.inflate(R.layout.fragment_user_account, container, false);
        name = userView.findViewById(R.id.nameTitleText);
        editName = userView.findViewById(R.id.nameEntryText);
        editPassword = userView.findViewById(R.id.passwordText);
        editEmail = userView.findViewById(R.id.emailText);
        editPhone = userView.findViewById(R.id.phoneText);
        enableLocationSwitch = userView.findViewById(R.id.locationServicesSwitch);
        saveChangesButton = userView.findViewById(R.id.saveChangesButton);
        return userView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set user data in user profile page
        name.setText(this.user.getName());
        editName.setText(this.user.getName());
        editPassword.setText(generatePasswordFillerString(this.user.getPasswordLength()));
        editEmail.setText(this.user.getEmail());
        editPhone.setText(this.user.getPhoneNumber());
        enableLocationSwitch.setChecked(this.user.getLocationServicesEnabled());

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //grab content from text fields
                String newPassword = editPassword.getText().toString();
                String newEmail = editEmail.getText().toString();

                //if the password is not empty and the email is not empty
                if((newPassword != null && !newPassword.equals("")) && ((newEmail != null) && (!newEmail.equals("")))) {

                    //create alert dialog box to get user confirmation for profile changes
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Password Change");
                    builder.setMessage("If you changed your password, please enter your old password below. If you " +
                            "did not change your password, please enter your current password below. Then press confirm to save your changes.");

                    // Set up the input section for the alert box
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final String oldPassword = input.getText().toString();

                                    //build hashmap with new user data
                                    Map<String, Object> currUserInfo = new HashMap<>();
                                    currUserInfo.put("name", editName.getText().toString());
                                    currUserInfo.put("location enabled", enableLocationSwitch.isChecked());
                                    currUserInfo.put("email", editEmail.getText().toString());
                                    currUserInfo.put("phone", editPhone.getText().toString());

                                    name.setText(editName.getText().toString());

                                    //populate database with new user data
                                    db.collection("users").document((String)user.getUid())
                                            .set(currUserInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    System.out.println("DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("Error saving changes");
                                                }
                                            });

                                    FirebaseUser currFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                    // Get auth credentials from the user for re-authentication
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(currFirebaseUser.getEmail(), oldPassword); // Current Login Credentials \\

                                    //reauthenticate user
                                    currFirebaseUser.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //Now change your email address \\
                                                    //----------------Code for Changing Email Address----------\\
                                                    FirebaseUser currFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


                                                    //if the user email changed
                                                    if(!editEmail.getText().toString().equals(currFirebaseUser.getEmail())){
                                                        currFirebaseUser.updateEmail(editEmail.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "An error occurred with saving your email. Please try to save your changes again.", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    }

                                                    //if password changed
                                                    if(passwordChanged == true){
                                                        currFirebaseUser.updatePassword(editPassword.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "An error occurred with saving your password. Please try to save your changes again.", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "An error occurred with your confirmation password. Please try to save your changes again.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            });

                    builder.setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "Error saving changes. Please try again.", Toast.LENGTH_LONG).show();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{
                    Toast.makeText(getContext(), "Name and password cannot be empty. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public String generatePasswordFillerString(int passwordLength) {
        StringBuilder passwordFiller = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            passwordFiller.append("*");
        }
        return passwordFiller.toString();
    }

}
