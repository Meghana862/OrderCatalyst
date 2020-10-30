package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class WaiterAddActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText mobile;
    private TextInputEditText aadhaar;
    private TextInputEditText pan;
    private TextInputEditText password;
    private Button submitBtn;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_add);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Add Waiter");

        email = findViewById(R.id.email_edit);
        name = findViewById(R.id.name_edit);
        mobile = findViewById(R.id.mobile_edit);
        aadhaar = findViewById(R.id.aadhaar_edit);
        pan = findViewById(R.id.pass_edit);
        password=findViewById(R.id.pass_edit);
        pan=findViewById(R.id.pan_edit);
        submitBtn = findViewById(R.id.sub_btn);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //checkUser();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdding();
            }
        });

    }

    private void startAdding() {
        //sending code to friend and once friend accept it add it into database with amount 0;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Waiter");

        final String waiterEmail = email.getText().toString().trim();
        String waiterName = name.getText().toString().trim();
        String waiterPhone = mobile.getText().toString().trim();
        String waiterAadhaar = aadhaar.getText().toString().trim();
        String waiterPan = pan.getText().toString().trim();
        String waiterPass = password.getText().toString().trim();

        if (TextUtils.isEmpty(waiterEmail)||TextUtils.isEmpty(waiterName)||TextUtils.isEmpty(waiterAadhaar)||TextUtils.isEmpty(waiterPass)||TextUtils.isEmpty(waiterPan)) {
            Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        if (waiterPhone.length() < 10) {
            Toast.makeText(WaiterAddActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        //final String w_timestamp = "" + System.currentTimeMillis();
        //createGroup("" + g_timestamp, "" + grpTitleSt, "" + grpDescriptionSt);

        final String w_timestamp = "" + System.currentTimeMillis();
        final String waiterNamest = "" + waiterName;
        final String waiterEmailst = "" + waiterEmail;
        final String waiterPhonest = "" + waiterPhone;
        final String waiterAadhaarst = "" + waiterAadhaar;
        final String waiterPassst = "" + waiterPass;
        final String waiterPanst = "" + waiterPan;

        checking(w_timestamp,waiterNamest,waiterEmailst,waiterPhonest,waiterAadhaarst,waiterPassst,firebaseAuth.getCurrentUser().getUid(),waiterPan);

    }

    public void checking(final String id, final String name, final String email, final String phone, final String aadhaar,final String pass,final String added,final String pan) {

        /*firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(WaiterAddActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {*/

                            //addAdmin(email1,password1);
                            final int[] flag = {0};
                            final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("waiters");
                            rootRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (final QueryDocumentSnapshot document : task.getResult()) {
                                            final String fid = document.getId();
                                            final String email1 = document.get("waiterEmail").toString();
                                            //checking for users id
                                            if (email1.equals(email)) {
                                                Toast.makeText(WaiterAddActivity.this, "Email id already registered", Toast.LENGTH_SHORT).show();
                                                flag[0] = 1;
                                                break;
                                            }
                                        }
                                        if (flag[0] == 0) {
                                            final HashMap<String, String> hashMap1 = new HashMap<>();

                                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                                                    .addOnCompleteListener(WaiterAddActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful())
                                                            //hashMap1.put("waiterId", id);
                                                            hashMap1.put("waiterName", name);
                                                            hashMap1.put("waiterEmail", email);
                                                            hashMap1.put("waiterPhone", phone);
                                                            hashMap1.put("waiterAadhaar", aadhaar);
                                                            hashMap1.put("waiterPassword", pass);
                                                            hashMap1.put("waiterPan", pan);
                                                            //hashMap1.put("addedBy", added);
                                                            hashMap1.put("waiterId", "" + firebaseAuth.getInstance().getUid());
                                                            db.collection("waiters").document(firebaseAuth.getInstance().getUid()).set(hashMap1)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(WaiterAddActivity.this, "Waiter Added", Toast.LENGTH_SHORT).show();
                                                                            addWaiterLocal(name, email, phone, aadhaar, pass, id);
                                                                            //FirebaseAuth.getInstance().signOut();
                                                                            //FirebaseAuth.getInstance().signInWithEmailAndPassword(adEmail,adPass);
                                                                            //FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass);
                                                                            startActivity(new Intent(WaiterAddActivity.this, WaitersListActivity.class));
                                                                            finish();

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(WaiterAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }

                                                    });
                                        }

                                    } else {
                                        Log.d("FAILED", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        /*} else {
                            Log.d("FAILED", "Error getting documents: ", task.getException());
                            Toast.makeText(WaiterAddActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
        //FirebaseAuth.getInstance().signOut();
    }

        /*final int[] flag = {0};
        final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("waiters");
        rootRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        final String fid = document.getId();
                        final String email1 = document.get("waiterEmail").toString();
                        //checking for users id
                        if (email1.equals(email)) {
                            Toast.makeText(WaiterAddActivity.this, "Email id already registered" , Toast.LENGTH_SHORT).show();
                            flag[0] =1;
                            break;
                        }
                    }
                    if(flag[0]==0){

                        final HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("waiterId", id);
                        hashMap1.put("waiterName", name);
                        hashMap1.put("waiterEmail", email);
                        hashMap1.put("waiterPhone", phone);
                        hashMap1.put("waiterAadhaar", aadhaar);
                        hashMap1.put("waiterPassword", pass);
                        hashMap1.put("addedBy", "" + firebaseAuth.getCurrentUser().getUid());

                        db.collection("waiters").document(id).set(hashMap1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(WaiterAddActivity.this, "Waiter Added", Toast.LENGTH_SHORT).show();
                                        //addWaiterLocal(name,email,phone,aadhaar,pass,id);
                                        startActivity(new Intent(WaiterAddActivity.this, WaitersListActivity.class));
                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(WaiterAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                } else {
                    Log.d("FAILED", "Error getting documents: ", task.getException());
                }
            }
        });

    }*/

    private void addWaiterLocal(String name,String email,String phone,String aadhaar,String pan,String id) {
        ModelWaitersList modelFriendList = new ModelWaitersList(name, email, phone, id, aadhaar,pan);
        WaitersList.getInstance().waiters.add(modelFriendList);

        for (int i = 0; i < WaitersList.getInstance().waiters.size(); i++) {
            Log.d("All Waiters", WaitersList.getInstance().waiters.get(i).getName());
        }

    }

    /*private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            actionBar.setSubtitle(user.getEmail());
        }
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
