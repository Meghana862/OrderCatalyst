package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText mobile;
    private TextInputEditText user_password;
    private TextInputEditText aadhaar;
    private TextInputEditText confirm_password;
    private TextInputEditText pan;
    private Button register;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email_edit);
        name = findViewById(R.id.name_edit);
        mobile = findViewById(R.id.mobile_edit);
        aadhaar = findViewById(R.id.aadhaar_edit);
        pan = findViewById(R.id.pan_edit);
        user_password=findViewById(R.id.pass_edit);
        confirm_password=findViewById(R.id.conf_pass_edit);
        register=findViewById(R.id.sub_btn);

        auth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final String email1=email.getText().toString();
        final String password1=user_password.getText().toString();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = name.getText().toString();
                final String user_email = email.getText().toString();
                final String number =  mobile.getText().toString();
                final String password = user_password.getText().toString();
                final String conf_pass = confirm_password.getText().toString();
                final String aadhaarSt =  aadhaar.getText().toString();
                final String panSt =  pan.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    name.setError("Username is required");
                    return;
                }

                if (TextUtils.isEmpty(user_email)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    user_password.setError("Password is required");
                    return;
                }

                if (TextUtils.isEmpty(conf_pass)) {
                    user_password.setError("Password is required");
                    return;
                }

                if (!password.equals(conf_pass)) {
                    Toast.makeText(RegisterActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (number.isEmpty() || number.length() < 10) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(aadhaarSt)||aadhaarSt.length()<12) {
                    aadhaar.setError("Please Enter Valid Aadhaar Number");
                    return;
                }
                if (TextUtils.isEmpty(panSt)||panSt.length()<10) {
                    name.setError("Please Enter Valid PAN Number");
                    return;
                }
                else {
                    Log.d("email : ",user_email);
                    Log.d("password : ",password);
                    registerUser(user_email,password,username,number,aadhaarSt,panSt);
                }
            }
        });
    }
    private void registerUser(final String email1, final String password1, final String username, final String number, final String aadhaar, final String pan){

        auth.createUserWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //addAdmin(email1,password1);
                            FirebaseUser f_user = FirebaseAuth.getInstance().getCurrentUser();
                            final String uid = f_user.getUid();
                            Map<String, Object> note = new HashMap<>();

                            note.put("admin_username", username);
                            note.put("admin_email",email1);
                            note.put("admin_mobile",number);
                            note.put("admin_aadhaar",aadhaar);
                            note.put("admin_pan",pan);

                            Log.d("uid ", uid);
                            db.collection("admins").document(uid).set(note)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Toast.makeText(RegisterActivity.this, uid, Toast.LENGTH_LONG).show();
                                            Toast.makeText(RegisterActivity.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(RegisterActivity.this,WaitersListActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        else{
                            Log.d("FAILED", "Error getting documents: ", task.getException());
                            Toast.makeText(RegisterActivity.this,"Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}