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
import java.text.SimpleDateFormat;

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
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {
    private EditText cname;
    private EditText cphone;
    private EditText cAadhaar;
    private Button btn;
    String t_name;

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

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            t_name=(String)b.get("t_name");
        }

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
                            Calendar calender=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        String currentDate= simpleDateFormat.format(calender.getTime());
                            hashMap1.put("date",currentDate);
                            Log.d("uid ", uid);
                            final String g_timestamp = "" + System.currentTimeMillis();

                            db.collection("customers").document(g_timestamp).set(hashMap1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(CustomerDetailsActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                                            db.collection("tables").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (final QueryDocumentSnapshot document : task.getResult()) {
                                                            if (t_name.equals(document.get("name"))) {
                                                                String id=document.getId();
                                                                final HashMap<String,String> hashMap=new HashMap<>();
                                                                hashMap.put("status","occupied");
                                                                hashMap.put("customerId",g_timestamp);
                                                                hashMap.put("waiterId",firebaseAuth.getInstance().getUid());
                                                                final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("tables");
                                                                rootRef1.document(id).set(hashMap, SetOptions.merge())
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(CustomerDetailsActivity.this,"Updated successfully",Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(CustomerDetailsActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }

                                                    } else {
                                                        Log.d("FAILED", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                            Intent intent=new Intent(CustomerDetailsActivity.this,Menu.class);
                                            intent.putExtra("name",name );
                                            intent.putExtra("phoneNo",phone);
                                            intent.putExtra("aadhaarNo",aadhaar );
                                            intent.putExtra("waiterId",firebaseAuth.getInstance().getUid());
                                            intent.putExtra("time",g_timestamp);
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
        /*Toast.makeText(CustomerDetailsActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(CustomerDetailsActivity.this,Menu.class);
        intent.putExtra("name",name );
        intent.putExtra("phoneNo",phone);
        intent.putExtra("aadhaarNo",aadhaar );
        intent.putExtra("waiterId",firebaseAuth.getInstance().getUid());
        startActivity(intent);
        finish();*/
    }
}