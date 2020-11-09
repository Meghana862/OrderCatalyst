package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private Button register;
    private Button login;
    private EditText email;
    private EditText password;
    private ActionBar actionBar;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

   private FirebaseAuth auth;
   static int var;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.log_email);
        password=findViewById(R.id.log_password);
        //register=findViewById(R.id.reg);
        login=findViewById(R.id.login);

        actionBar = getSupportActionBar();
        actionBar.hide();

        radioGroup=findViewById(R.id.radioGroup2);

        auth=FirebaseAuth.getInstance();

       /* register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email1=email.getText().toString();
                final String password1=password.getText().toString();
                int radioId=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(radioId);
                String role=radioButton.getText().toString();
                if(TextUtils.isEmpty(email1)||TextUtils.isEmpty(password1)){
                    Toast.makeText(LoginActivity.this,"Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else if(password1.length()>=6){
                    //registerUser(email1,password1);
                    //final int[] var = {0};
                    if(role.equals("Admin")){
                        //loginUser1(email1,password1);
                        auth.signInWithEmailAndPassword(email1,password1)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        CollectionReference rootref = FirebaseFirestore.getInstance().collection("admins");
                                        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        String ad_email = doc.get("admin_email").toString();
                                                        Log.d("email:",ad_email);
                                                        String id = doc.getId();
                                                        if(email1.equals(ad_email)){
                                                            Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(LoginActivity.this,WaitersListActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }
                                                else {
                                                    Log.d("FAILED", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                        //Toast.makeText(LoginActivity.this,"Access denied",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        //loginUser2(email1,password1);
                        auth.signInWithEmailAndPassword(email1,password1)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        CollectionReference rootref = FirebaseFirestore.getInstance().collection("waiters");
                                        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        String ad_email = doc.get("waiterEmail").toString();
                                                        String id = doc.getId();
                                                        if(email1.equals(ad_email)){
                                                            Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(LoginActivity.this,TablesListActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }
                                                else {
                                                    Log.d("FAILED", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                        //Toast.makeText(LoginActivity.this,"Access denied",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    //Toast.makeText(LoginActivity.this,"Invalid userId", Toast.LENGTH_SHORT).show();
                    /*if(var[0]==0){
                        Toast.makeText(LoginActivity.this,"Invalid userId", Toast.LENGTH_SHORT).show();
                    }*/
                }
                else{
                    Toast.makeText(LoginActivity.this,"Enter 5+ character password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void checkBtn(View v){

        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        Toast.makeText(LoginActivity.this,""+radioButton.getText(),Toast.LENGTH_SHORT).show();

    }


    /*@Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, TablesListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{

        }
    }*/


            /*CollectionReference rootref = FirebaseFirestore.getInstance().collection("waiters");
            rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String ad_email = doc.get("waiterEmail").toString();
                            String id = doc.getId();
                            if (id.equals(FirebaseAuth.getInstance().getCurrentUser())) {
                                Intent intent = new Intent(LoginActivity.this, TablesListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    } else {
                        Log.d("FAILED", "Error getting documents: ", task.getException());
                    }
                }
            });
            CollectionReference rootref1 = FirebaseFirestore.getInstance().collection("admins");
            rootref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc1 : task.getResult()) {
                            String ad_email = doc1.get("admin_email").toString();
                            String id = doc1.getId();
                            if (id.equals(FirebaseAuth.getInstance().getCurrentUser())) {
                                Intent intent = new Intent(LoginActivity.this, WaitersListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    } else {
                        Log.d("FAILED", "Error getting documents: ", task.getException());
                    }
                }
            });*/


}
