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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.log_email);
        password=findViewById(R.id.log_password);
        register=findViewById(R.id.reg);
        login=findViewById(R.id.login);

        actionBar = getSupportActionBar();
        actionBar.hide();

        radioGroup=findViewById(R.id.radioGroup2);

        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
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
                        loginUser1(email1,password1);
                        /*CollectionReference rootref = FirebaseFirestore.getInstance().collection("admins");
                        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String ad_email = doc.get("admin_email").toString();
                                        String id = doc.getId();
                                        if(email1.equals(ad_email)){
                                            loginUser1(email1,password1);
                                            var[0]++;
                                        }
                                    }
                                }
                                else {
                                    Log.d("FAILED", "Error getting documents: ", task.getException());
                                }
                            }
                        });*/
                    }
                    else{
                        loginUser2(email1,password1);
                        /*CollectionReference rootref = FirebaseFirestore.getInstance().collection("waiters");
                        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String ad_email = doc.get("waiterEmail").toString();
                                        String id = doc.getId();
                                        if(email1.equals(ad_email)){
                                            loginUser2(email1,password1);
                                            var[0]++;
                                        }
                                    }
                                }
                                else {
                                    Log.d("FAILED", "Error getting documents: ", task.getException());
                                }
                            }
                        });*/

                    }
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

    private void loginUser1(String email1,String password1){
        auth.signInWithEmailAndPassword(email1,password1)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,WaitersListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
    private void loginUser2(String email1,String password1){
        auth.signInWithEmailAndPassword(email1,password1)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,TablesListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, WaiterHomepageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{

        }
    }*/


}
