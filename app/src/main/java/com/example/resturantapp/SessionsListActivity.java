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
    private String from_date,to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            from_date=(String)b.get("date1");
            to_date=(String)b.get("date2");
        }
        myrecyclerview = findViewById(R.id.usersRv111);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(from_date+" - "+to_date);

        Log.d("w_id:",firebaseAuth.getInstance().getUid());

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
        loadInfo(from_date,to_date);

    }

    private void loadInfo(String date1,String date2){
        //String user_id = FirebaseAuth.getInstance().toString();
        String[] st1=date1.split("/");
        String[] st2=date2.split("/");
        final String nst1 = st1[2] + "/" + st1[1] + "/" + st1[0];
        final String nst2 = st2[2] + "/" + st2[1] + "/" + st2[0];
        Log.d("nst1",nst1);
        Log.d("nst2",nst2);
        int flag=0;
        for(int i=0;i<st1.length;i++){
            if(Integer.parseInt(st1[i])<=Integer.parseInt(st2[i])){
                flag++;
            }
        }
        if(!(date1.equals("")) && !(date2.equals(""))) {

            CollectionReference rootref = FirebaseFirestore.getInstance().collection("customers");
            rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String date = doc.get("date").toString();
                            String cust = doc.get("name").toString();
                            String waitId = doc.get("waiterId").toString();
                            String id = doc.getId();
                            if (waitId.equals(firebaseAuth.getInstance().getUid())) {
                                Log.d("date",date);
                                String[] st11=date.split("/");
                                String nst11=st11[2]+"/";
                                if(st11[1].charAt(0)=='0'){
                                    nst11 = nst11+st11[1].charAt(1) + "/" ;
                                }
                                else{
                                    nst11 = nst11 + st11[1] + "/" ;
                                }
                                if(st11[0].charAt(0)=='0'){
                                    nst11 = nst11+ st11[0].charAt(1);
                                }
                                else{
                                    nst11 =nst11 + st11[0];
                                }
                                Log.d("nst11:",nst11);
                                Log.d("nst1:",nst1);
                                Log.d("nst2:",nst2);
                                if(nst11.compareTo(nst1) >= 0 && nst11.compareTo(nst2) <= 0){
                                    ModelSessionsList model = new ModelSessionsList(date, cust, waitId);
                                    //WaitersList.getInstance().friends.add(waiter);
                                    sessionsLists.add(model);
                                    adaptersessionsList = new AdapterSessionsList(SessionsListActivity.this, sessionsLists);
                                    myrecyclerview.setAdapter(adaptersessionsList);
                                }

                            }
                        }
                    } else {
                        Log.d("FAILED", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
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