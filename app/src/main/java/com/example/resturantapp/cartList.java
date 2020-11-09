package com.example.resturantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class cartList extends AppCompatActivity {
    private String time;
    private Button addItems;
    private Button end;
    private String t_name;
    private TextView total;
    static int num1;

    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    private RecyclerView myrecyclerview;
    private ArrayList<ModelCategorywiseItem> itemsLists;
    private AdapterOrderedItems adapteritemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            time=(String)b.get("time");
            t_name=(String)b.get("t_name");
        }
        actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        myrecyclerview = findViewById(R.id.usersRv111);
        //Log.d("user:",firebaseAuth.getCurrentUser().getUid());
        itemsLists = new ArrayList<ModelCategorywiseItem>();
        adapteritemsList = new AdapterOrderedItems(cartList.this, itemsLists,time);
        LinearLayoutManager llm = new LinearLayoutManager(cartList.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myrecyclerview.setLayoutManager(llm);
        myrecyclerview.setAdapter(adapteritemsList);

        addItems=findViewById(R.id.addItems);
        end=findViewById(R.id.endSession);
        total=findViewById(R.id.orderTotalTextView);
        //final int num = find(time);
        final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("customers");
        rootRef.document(time).collection("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    num1=0;
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        final String id=document.getId();
                        final String quantity=document.get("qantity").toString();
                        Log.d("quantity", quantity);
                        final String[] individual_price = new String[1];

                        final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("food");
                        rootRef1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final QueryDocumentSnapshot document1 : task.getResult()) {
                                        if (id.equals(document1.getId())) {
                                            individual_price[0] = document1.get("cost").toString();
                                            Log.d("cost", individual_price[0]);
                                            int x=Integer.parseInt(quantity);
                                            int y=Integer.parseInt(individual_price[0]);
                                            num1 = num1 +(x*y);
                                            total.setText("Total:"+num1);
                                            Log.d("total", String.valueOf(num1));

                                            String w_name = document1.get("name").toString();
                                            String w_cost = document1.get("cost").toString();
                                            String w_category = document1.get("category").toString();
                                            String w_id = document1.get("id").toString();
                                            ModelCategorywiseItem model = new ModelCategorywiseItem(w_name, w_category, w_cost, w_id);
                                            //WaitersList.getInstance().friends.add(waiter);
                                            itemsLists.add(model);
                                            adapteritemsList = new AdapterOrderedItems(cartList.this, itemsLists,time);
                                            myrecyclerview.setAdapter(adapteritemsList);
                                        }
                                    }

                                } else {
                                    Log.d("FAILED", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                        //int x=Integer.parseInt(quantity);
                        //int y=Integer.parseInt(individual_price[0]);
                        //num[0] = num[0] +(x*y);
                        //Log.d("total", String.valueOf(num[0]));
                    }

                } else {
                    Log.d("FAILED", "Error getting documents: ", task.getException());
                }
            }
            //total.setText("Total:"+ num[0]);
        });
        Log.d("total1", String.valueOf(num1));
        //total.setText("Total:"+num1);

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(cartList.this, Menu.class);
                intent.putExtra("time",time );
                intent.putExtra("t_name",t_name );
                startActivity(intent);

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference rootref = FirebaseFirestore.getInstance().collection("tables");
                rootref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                if (t_name.equals(document.get("name"))) {
                                    String id=document.getId();
                                    final HashMap<String,String> hashMap=new HashMap<>();
                                    hashMap.put("status","unoccupied");
                                    hashMap.put("customerId","null");
                                    hashMap.put("waiterId","null");
                                    final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("tables");
                                    rootRef1.document(id).set(hashMap, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(cartList.this,"Session Ended",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(cartList.this, TablesListActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(cartList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                        } else {
                            Log.d("FAILED", "Error getting documents: ", task.getException());
                        }
                    }
                });

            }
        });

    }

}