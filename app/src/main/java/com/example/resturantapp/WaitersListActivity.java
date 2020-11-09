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
import android.view.Window;
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

public class WaitersListActivity extends AppCompatActivity {
    private ActionBar actionBar;
    FloatingActionButton actionButton;
    private FirebaseAuth firebaseAuth;
    private RecyclerView myrecyclerview;
    private ArrayList<ModelWaitersList> waitersLists;
    private AdapterWaitersList adapterwaitersList;
    private FloatingActionButton show_desc_Btn;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiters_list);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        myrecyclerview = findViewById(R.id.usersRv111);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Waiters List");

        firebaseAuth = FirebaseAuth.getInstance();
        //Log.d("user:",firebaseAuth.getCurrentUser().getUid());
        waitersLists = new ArrayList<ModelWaitersList>();
        adapterwaitersList = new AdapterWaitersList(WaitersListActivity.this, waitersLists);
        LinearLayoutManager llm = new LinearLayoutManager(WaitersListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapterwaitersList);

        /*LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapterwaitersList);*/
        loadInfo();

        actionButton = findViewById(R.id.fab_btn111);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WaitersListActivity.this,WaiterAddActivity.class);
                startActivity(intent);

            }
        });

    }

    private void loadInfo(){
        //String user_id = FirebaseAuth.getInstance().toString();
        CollectionReference rootref = FirebaseFirestore.getInstance().collection("waiters");
        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String w_name = doc.get("waiterName").toString();
                        String w_email = doc.get("waiterEmail").toString();
                        String w_phone = doc.get("waiterPhone").toString();
                        String w_pass = doc.get("waiterPassword").toString();
                        String w_aadhaar = doc.get("waiterAadhaar").toString();
                        //String w_pan = doc.get("waiterPan").toString();
                        //String w_id = doc.get("waiterId").toString();

                        String w_id = doc.getId();

                        ModelWaitersList model = new ModelWaitersList(w_name, w_email, w_phone, w_id, w_aadhaar, w_pass);
                        //WaitersList.getInstance().friends.add(waiter);
                        waitersLists.add(model);
                        adapterwaitersList = new AdapterWaitersList(WaitersListActivity.this, waitersLists);
                        myrecyclerview.setAdapter(adapterwaitersList);
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
            Toast.makeText(WaitersListActivity.this,"Signed out",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(WaitersListActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){

        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();;
            return;
        }
        else{
            backToast=Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
        /*Intent intent = new Intent(home.this,home.class);
        startActivity(intent);
        finish();*/
    }


}