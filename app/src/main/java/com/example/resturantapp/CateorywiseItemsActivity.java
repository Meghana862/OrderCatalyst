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

public class CateorywiseItemsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    FloatingActionButton actionButton;
    private FirebaseAuth firebaseAuth;
    private RecyclerView myrecyclerview;
    private ArrayList<ModelCategorywiseItem> itemsLists;
    private AdapterCategorywiseItems adapteritemsList;
    private FloatingActionButton show_desc_Btn;
    private String category;
    private String time;
    private String t_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cateorywise_items);
        myrecyclerview = findViewById(R.id.usersRv111);

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            category=(String)b.get("category");
            time=(String)b.get("time");
            t_name=(String)b.get("t_name");
        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(category);

        firebaseAuth = FirebaseAuth.getInstance();
        //Log.d("user:",firebaseAuth.getCurrentUser().getUid());
        itemsLists = new ArrayList<ModelCategorywiseItem>();
        adapteritemsList = new AdapterCategorywiseItems(CateorywiseItemsActivity.this, itemsLists,time);
        LinearLayoutManager llm = new LinearLayoutManager(CateorywiseItemsActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapteritemsList);

        /*LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapterwaitersList);*/
        loadInfo();

        actionButton = findViewById(R.id.fab_btn111);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CateorywiseItemsActivity.this, com.example.resturantapp.Menu.class);
                intent.putExtra("time",time );
                intent.putExtra("t_name",t_name );
                startActivity(intent);

            }
        });

    }

    private void loadInfo(){
        //String user_id = FirebaseAuth.getInstance().toString();
        CollectionReference rootref = FirebaseFirestore.getInstance().collection("food");
        rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String w_name = doc.get("name").toString();
                        String w_cost = doc.get("cost").toString();
                        String w_category = doc.get("category").toString();
                        String w_id = doc.get("id").toString();
                       // String w_id = doc.getId();
                        if(w_category.equals(category)){
                            ModelCategorywiseItem model = new ModelCategorywiseItem(w_name, w_category, w_cost, w_id);
                            //WaitersList.getInstance().friends.add(waiter);
                            itemsLists.add(model);
                            adapteritemsList = new AdapterCategorywiseItems(CateorywiseItemsActivity.this, itemsLists,time);
                            myrecyclerview.setAdapter(adapteritemsList);
                        }
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
            Toast.makeText(CateorywiseItemsActivity.this,"Signed out",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(CateorywiseItemsActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(CateorywiseItemsActivity.this, Menu.class);
        intent.putExtra("t_name",t_name);
        intent.putExtra("time",time );
        startActivity(intent);
        finish();
    }
}