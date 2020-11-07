package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SessionsListActivity extends AppCompatActivity {
    private ActionBar actionBar;
    FloatingActionButton actionButton;
    private FirebaseAuth firebaseAuth;
    private RecyclerView myrecyclerview;
    private ArrayList<ModelSessionsList> sessionsLists;
    private AdapterSessionsList adaptersessionsList;
    private FloatingActionButton show_desc_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);
        myrecyclerview = findViewById(R.id.usersRv111);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Sessions");

        firebaseAuth = FirebaseAuth.getInstance();
        //Log.d("user:",firebaseAuth.getCurrentUser().getUid());
        sessionsLists = new ArrayList<ModelSessionsList>();
        adaptersessionsList = new AdapterSessionsList(SessionsListActivity.this, sessionsLists);
        LinearLayoutManager llm = new LinearLayoutManager(SessionsListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adaptersessionsList);

        /*LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapterwaitersList);*/
        loadInfo();

    }

    private void loadInfo(){
        //String user_id = FirebaseAuth.getInstance().toString();
        CollectionReference rootref = FirebaseFirestore.getInstance().collection("customers");
        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String date = doc.get("date").toString();
                        String cust = doc.get("name").toString();
                        String waitId = doc.get("waiterId").toString();
                        String id = doc.getId();
                        //String w_aadhaar = doc.get("waiterAadhaar").toString();
                        //String w_pan = doc.get("waiterPan").toString();
                        //String w_id = doc.get("waiterId").toString();

                        String w_id = doc.getId();

                        ModelSessionsList model = new ModelSessionsList(date, cust, waitId);
                        //WaitersList.getInstance().friends.add(waiter);
                        sessionsLists.add(model);
                        adaptersessionsList = new AdapterSessionsList(SessionsListActivity.this, sessionsLists);
                        myrecyclerview.setAdapter(adaptersessionsList);
                    }
                }
                else {
                    Log.d("FAILED", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sign_out) {
            // do something
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(SessionsListActivity.this,"Signed out",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SessionsListActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}