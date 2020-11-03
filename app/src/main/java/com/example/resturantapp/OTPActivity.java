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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    String name;
    String phoneNo;
    String aadhaarNo;
    String waiterId;
    MaterialButton signin_btn;
    private String verificationId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private TextInputEditText otp_edit_text;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otp_edit_text.setText(code);
                verifyCode(code);
            }
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

        progressBar = findViewById(R.id.progressBar);
        otp_edit_text = findViewById(R.id.otp_edit);
        signin_btn = findViewById(R.id.sign_in_btn);

        //Intent i = getIntent();
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        aadhaarNo = b.getString("aadhaarNo");
        phoneNo = b.getString("phoneNo");
        waiterId = b.getString("waiterId");

        sendVerificationCode(phoneNo);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp_edit_text.getText().toString().trim();

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
        /*mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //extend(user_email, password);
                            send_data(name, phoneNo, aadhaarNo,waiterId);
                        } else {
                            Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OTPActivity.this, CustomerDetailsActivity.class));
                            finish();
                        }
                    }
                });*/
        send_data(name, phoneNo, aadhaarNo,waiterId);
    }

    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phoneNumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    public void send_data(String name, String phoneNo, String aadhaarNo,String waiterId) {

        FirebaseUser f_user = FirebaseAuth.getInstance().getCurrentUser();

        if (f_user != null) {
            final String uid = f_user.getUid();
            Map<String, Object> note = new HashMap<>();
            note.put("name", name);
            note.put("phoneNo", phoneNo);
            note.put("waiterId", waiterId);
            note.put("aadhaarId", aadhaarNo);

            Log.d("uid ", uid);

            final String g_timestamp = "" + System.currentTimeMillis();

            db.collection("customers").document(g_timestamp).set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(OTPActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(OTPActivity.this,OTPActivity.class);
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
        } else {
            Toast.makeText(OTPActivity.this, "user not logged in", Toast.LENGTH_LONG).show();
        }

    }

}
