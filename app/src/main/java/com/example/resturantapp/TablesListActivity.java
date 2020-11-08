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
import android.widget.Button;
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
    private RecyclerView myrecyclerview1;
    private ArrayList<ModelTablesList> tablesLists;
    private AdapterTablesList adaptertablesList;
    private AdapterEmptyTablesList adapteremptytablesList;
    private FloatingActionButton show_desc_Btn;
    private Button log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables_list);

        myrecyclerview = findViewById(R.id.usersRv111);
        //myrecyclerview1 = findViewById(R.id.usersRv);
        log=findViewById(R.id.log);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Tables List");

        firebaseAuth = FirebaseAuth.getInstance();
        //Log.d("user:",firebaseAuth.getCurrentUser().getUid());
        tablesLists = new ArrayList<ModelTablesList>();

        adaptertablesList = new AdapterTablesList(TablesListActivity.this, tablesLists,firebaseAuth.getInstance().getUid());
        adapteremptytablesList = new AdapterEmptyTablesList(TablesListActivity.this, tablesLists);

        LinearLayoutManager llm = new LinearLayoutManager(TablesListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        //LinearLayoutManager llm1 = new LinearLayoutManager(TablesListActivity.this);
        //llm1.setOrientation(LinearLayoutManager.VERTICAL);

        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adaptertablesList);

        //myrecyclerview1.setLayoutManager(llm1);
        //myrecyclerview1.setAdapter(adaptertablesList);

        /*LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapterwaitersList);*/
        loadInfo();
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TablesListActivity.this,RangePickActivity.class);
                intent.putExtra("w_id",firebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });

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
                        tablesLists.add(model);
                        adaptertablesList = new AdapterTablesList(TablesListActivity.this, tablesLists,firebaseAuth.getInstance().getUid());
                        myrecyclerview.setAdapter(adaptertablesList);
                        //WaitersList.getInstance().friends.add(waiter);
                        /*if(t_status.equals("occupied")){
                            tablesLists.add(model);
                            adaptertablesList = new AdapterTablesList(TablesListActivity.this, tablesLists);
                            myrecyclerview.setAdapter(adaptertablesList);
                        }
                        else{
                            tablesLists.add(model);
                            adapteremptytablesList = new AdapterEmptyTablesList(TablesListActivity.this, tablesLists);
                            myrecyclerview1.setAdapter(adapteremptytablesList);

                        }*/
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