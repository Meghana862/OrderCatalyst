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

public class TablesListActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    private RecyclerView myrecyclerview;
    private ArrayList<ModelTablesList> tablesLists;
    private AdapterTablesList adaptertablesList;
    private FloatingActionButton show_desc_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables_list);

        myrecyclerview = findViewById(R.id.usersRv111);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Tables List");

        firebaseAuth = FirebaseAuth.getInstance();
        //Log.d("user:",firebaseAuth.getCurrentUser().getUid());
        tablesLists = new ArrayList<ModelTablesList>();
        adaptertablesList = new AdapterTablesList(TablesListActivity.this, tablesLists);
        LinearLayoutManager llm = new LinearLayoutManager(TablesListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adaptertablesList);

        /*LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapterwaitersList);*/
        loadInfo();

    }

    private void loadInfo(){
        //String user_id = FirebaseAuth.getInstance().toString();
        CollectionReference rootref = FirebaseFirestore.getInstance().collection("tables");
        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        //String t_bill = doc.get("bill").toString();
                        String t_custId = doc.get("customerId").toString();
                        String t_waitId = doc.get("waiterId").toString();
                        String t_status = doc.get("status").toString();
                        String t_name = doc.get("name").toString();

                        ModelTablesList model = new ModelTablesList(t_name, t_custId, t_waitId, t_status);
                        //WaitersList.getInstance().friends.add(waiter);
                        tablesLists.add(model);
                        adaptertablesList = new AdapterTablesList(TablesListActivity.this, tablesLists);
                        myrecyclerview.setAdapter(adaptertablesList);
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
            Toast.makeText(TablesListActivity.this,"Signed out",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(TablesListActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}