package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {
    private EditText cname;
    private EditText cphone;
    private EditText cAadhaar;
    private Button btn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        cname=findViewById(R.id.cname);
        cphone=findViewById(R.id.phoneno);
        cAadhaar=findViewById(R.id.aadharno);
        btn=findViewById(R.id.button1);
        db = FirebaseFirestore.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=cname.getText().toString();
                String phone=cphone.getText().toString();
                String aadhaar=cAadhaar.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(aadhaar)){
                    Toast.makeText(CustomerDetailsActivity.this,"Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else if(phone.length()<10){
                    Toast.makeText(CustomerDetailsActivity.this,"Enter a valid phone number", Toast.LENGTH_SHORT).show();
                }
                else if(aadhaar.length()<12){
                    Toast.makeText(CustomerDetailsActivity.this,"Enter a valid aadhaar number", Toast.LENGTH_SHORT).show();
                }
                else{
                    storeDetails(name,phone,aadhaar);
                }

            }
        });
    }
    public  void storeDetails(final String name, final String phone, final String aadhaar){

                            //addAdmin(email1,password1);
                            FirebaseUser f_user = FirebaseAuth.getInstance().getCurrentUser();
                            final String uid = f_user.getUid();
                            final HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("name", name);
                            hashMap1.put("phoneNo", phone);
                            hashMap1.put("aadhaarNo", aadhaar);
                            hashMap1.put("waiterId",""+firebaseAuth.getInstance().getUid());
                            Log.d("uid ", uid);
                            final String g_timestamp = "" + System.currentTimeMillis();

                            db.collection("customers").document(g_timestamp).set(hashMap1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(CustomerDetailsActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(CustomerDetailsActivity.this,OTPActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CustomerDetailsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
    }
}