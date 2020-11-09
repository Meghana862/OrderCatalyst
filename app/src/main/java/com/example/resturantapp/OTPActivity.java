package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    String name;
    String phoneNo;
    String aadhaarNo;
    String waiterId;
    private String time;
    MaterialButton signin_btn;
    private String verificationId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextInputEditText otp_edit_text;
    private String code;
    private String t_name;
    private String currentDate;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            /*code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otp_edit_text.setText(code);
                verifyCode(code);
            }*/
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            time=(String)b.get("time");
            name = b.getString("name");
            aadhaarNo = b.getString("aadhaarNo");
            phoneNo = b.getString("phoneNo");
            waiterId = b.getString("waiterId");
            t_name= b.getString("t_name");
            currentDate= b.getString("currentDate");
        }

        progressBar = findViewById(R.id.progressBar);
        otp_edit_text = findViewById(R.id.otp_edit);
        signin_btn = findViewById(R.id.sign_in_btn);


        //Intent i = getIntent();
        sendVerificationCode(phoneNo);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = otp_edit_text.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    otp_edit_text.setError("Enter Code...");
                    otp_edit_text.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //extend(user_email, password);
                            send_data(name, phoneNo, aadhaarNo,waiterId,currentDate);
                            Toast.makeText(OTPActivity.this,"successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OTPActivity.this, "recheck otp", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(OTPActivity.this, TablesListActivity.class);
                            intent.putExtra("t_name",t_name);
                            finish();
                        }
                    }
                });
        //String otp_code = credential.getSmsCode();
       /* if(otp_code.equals(code)){
            send_data(name, phoneNo, aadhaarNo,waiterId,currentDate);
        }
        else{
            //Toast.makeText(OTPActivity.this, credential.getSmsCode(), Toast.LENGTH_LONG).show();
            //Toast.makeText(OTPActivity.this, code, Toast.LENGTH_LONG).show();
            //Log.d("sent:",credential.getSmsCode());
            //Log.d("received:",code);
            //Log.d("typed:",code);
            Toast.makeText(OTPActivity.this, "Recheck OTP", Toast.LENGTH_SHORT).show();
        }
       //Toast.makeText(OTPActivity.this, credential.getSmsCode(), Toast.LENGTH_LONG).show();
        //Toast.makeText(OTPActivity.this, code, Toast.LENGTH_LONG).show();*/


    }

    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phoneNumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    public void send_data(String name, String phoneNo, String aadhaarNo, final String waiterId, String currentDate) {

            Map<String, Object> note = new HashMap<>();
            note.put("name", name);
            note.put("phoneNo", phoneNo);
            note.put("waiterId", waiterId);
            note.put("aadhaarId", aadhaarNo);
            note.put("date",currentDate);

            db.collection("customers").document(time).set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(OTPActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                            db.collection("tables").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (final QueryDocumentSnapshot document : task.getResult()) {
                                            if (t_name.equals(document.get("name"))) {
                                                String id=document.getId();
                                                final HashMap<String,String> hashMap=new HashMap<>();
                                                hashMap.put("status","occupied");
                                                hashMap.put("customerId",time);
                                                hashMap.put("waiterId",waiterId);
                                                final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("tables");
                                                rootRef1.document(id).set(hashMap, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(OTPActivity.this,"Updated successfully",Toast.LENGTH_SHORT).show();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(OTPActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }

                                    } else {
                                        Log.d("FAILED", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                            Intent intent=new Intent(OTPActivity.this,Menu.class);
                            intent.putExtra("time",time);
                            intent.putExtra("t_name",t_name);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OTPActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

    }

}
