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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class WaiterEditActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText mobile;
    private TextInputEditText aadhaar;
    private TextInputEditText pan;
    private TextInputEditText password;
    private Button submitBtn1;
    private Button submitBtn2;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    String waiterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_edit);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Add Waiter");

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            waiterId=(String)b.get("wId");
        }

        email = findViewById(R.id.email_edit);
        name = findViewById(R.id.name_edit);
        mobile = findViewById(R.id.mobile_edit);
        aadhaar = findViewById(R.id.aadhaar_edit);
        password=findViewById(R.id.pass_edit);
        pan=findViewById(R.id.pan_edit);
        submitBtn1 = findViewById(R.id.sub_btn1);
        submitBtn2 = findViewById(R.id.sub_btn2);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //checkUser();
        display(waiterId);
        submitBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdating();
            }
        });
        submitBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeleting();
            }
        });

    }
    private void display(final String waiterId){
        final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("waiters");
        rootRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        if (waiterId.equals(document.getId())) {
                            email.setText(document.get("waiterEmail").toString());
                            name.setText(document.get("waiterName").toString());
                            mobile.setText(document.get("waiterPhone").toString());
                            aadhaar.setText(document.get("waiterAadhaar").toString());
                            pan.setText(document.get("waiterPan").toString());
                            password.setText(document.get("waiterPassword").toString());
                        }
                    }

                } else {
                    Log.d("FAILED", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void startUpdating() {
        //sending code to friend and once friend accept it add it into database with amount 0;

        final String waiterEmail = email.getText().toString().trim();
        String waiterName = name.getText().toString().trim();
        String waiterPhone = mobile.getText().toString().trim();
        String waiterAadhaar = aadhaar.getText().toString().trim();
        String waiterPan = pan.getText().toString().trim();
        String waiterPass = password.getText().toString().trim();

        if (TextUtils.isEmpty(waiterEmail) || TextUtils.isEmpty(waiterName) || TextUtils.isEmpty(waiterAadhaar) || TextUtils.isEmpty(waiterPass) || TextUtils.isEmpty(waiterPan)) {
            Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        if (waiterPhone.length() < 10) {
            Toast.makeText(WaiterEditActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }

        //final String w_timestamp = "" + System.currentTimeMillis();
        final String waiterNamest = "" + waiterName;
        final String waiterEmailst = "" + waiterEmail;
        final String waiterPhonest = "" + waiterPhone;
        final String waiterAadhaarst = "" + waiterAadhaar;
        final String waiterPassst = "" + waiterPass;
        final String waiterPanst = "" + waiterPan;

        final HashMap<String, String> hashMap1 = new HashMap<>();
        //hashMap1.put("waiterId", id);
        hashMap1.put("waiterName", waiterNamest);
        hashMap1.put("waiterEmail", waiterEmailst);
        hashMap1.put("waiterPhone", waiterPhonest);
        hashMap1.put("waiterAadhaar", waiterAadhaarst);
        hashMap1.put("waiterPan", waiterPanst);

        final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("waiters");
        rootRef.document(waiterId).set(hashMap1, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(WaiterEditActivity.this, "Details Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WaiterEditActivity.this, WaitersListActivity.class));
                finish();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(WaiterEditActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private  void startDeleting(){
        final CollectionReference rootRef11 = FirebaseFirestore.getInstance().collection("waiters");
        rootRef11.document(waiterId).delete()
           .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(WaiterEditActivity.this,"Waiter Removed",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WaiterEditActivity.this, WaitersListActivity.class));
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

    }

    private void addWaiterLocal(String name,String email,String phone,String aadhaar,String pan,String id) {
        ModelWaitersList modelFriendList = new ModelWaitersList(name, email, phone, id, aadhaar,pan);
        WaitersList.getInstance().waiters.add(modelFriendList);

        for (int i = 0; i < WaitersList.getInstance().waiters.size(); i++) {
            Log.d("All Waiters", WaitersList.getInstance().waiters.get(i).getName());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
