package com.example.resturantapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterCategorywiseItems extends RecyclerView.Adapter<com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList> {

        private Context context;
        private String time;
        private ArrayList<ModelCategorywiseItem> itemsList;
        public ElegantNumberButton numberButton;

        public AdapterCategorywiseItems(Context context, ArrayList<ModelCategorywiseItem> itemsList,String time) {
            this.context = context;
            this.itemsList = itemsList;
            this.time=time;
        }

        @NonNull
        @Override
        public com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_food_categorywise, parent, false);

            return new com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList holder, int position) {
            ModelCategorywiseItem model = itemsList.get(position);
            final String w_name= model.getName();
            String w_cost= model.getCost();
            final String id=model.getItemId();

            holder.w_name.setText(w_name);
            holder.w_cost.setText(w_cost);
            final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("customers");
            rootRef.document(time).collection("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            if (id.equals(document.getId())) {
                                holder.numberButton.setNumber(document.get("qantity").toString());
                            }
                        }

                    } else {
                        Log.d("FAILED", "Error getting documents: ", task.getException());
                    }
                }
            });

            holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, final int newValue) {
                    //String num=numberButton.getNumber();

                    Log.d("num:", String.valueOf(newValue));
                    if(newValue!=0){
                        final HashMap<String,Integer> hashMap=new HashMap<>();
                        hashMap.put("qantity",newValue);
                        //Log.d("temp", String.valueOf(temp));
                        Log.d("newValue", String.valueOf(newValue));
                        final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("customers");
                        rootRef1.document(time).collection("order").document(id).set(hashMap, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Updated successfully",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                /*if (task.isSuccessful()) {
                                    int var=0;
                                    int temp=0;
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        if (id.equals(document.getId())) {
                                            var++;
                                            temp = Integer.parseInt(document.get("qantity").toString());

                                        }
                                    }
                                    if(var==1){
                                        final HashMap<String,Integer> hashMap=new HashMap<>();
                                        hashMap.put("qantity",temp+newValue);
                                        Log.d("temp", String.valueOf(temp));
                                        Log.d("newValue", String.valueOf(newValue));
                                        final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("customers");
                                        rootRef1.document(time).collection("order").document(id).set(hashMap, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(context,"Updated successfully",Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else{
                                        final HashMap<String,Integer> hashMap=new HashMap<>();
                                        hashMap.put("qantity",newValue);
                                        final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("customers");
                                        rootRef1.document(time).collection("order").document(id).set(hashMap, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(context,"Added successfully",Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }

                                } else {
                                    Log.d("FAILED", "Error getting documents: ", task.getException());
                                }*/

                    }
                    else{
                        final CollectionReference rootRef1 = FirebaseFirestore.getInstance().collection("customers");
                        rootRef1.document(time).collection("order").document(id).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"removed successfully",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });


        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WaiterEditActivity.class);
                intent.putExtra("w_name",w_name);
                context.startActivity(intent);
            }
        });*/

           /* holder.actionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,WaiterEditActivity.class);
                    intent.putExtra("wId",id);
                    context.startActivity(intent);
                }
            });*/

        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }

        class HolderItemsList extends RecyclerView.ViewHolder {

            private TextView w_name;
            private TextView w_cost;
            private ElegantNumberButton numberButton;
            //int num;
            //private FloatingActionButton actionButton1;


            public HolderItemsList(@NonNull View itemView) {
                super(itemView);

                w_name = itemView.findViewById(R.id.waiter_name);
                w_cost = itemView.findViewById(R.id.act_w_email);
               // actionButton1=itemView.findViewById(R.id.des_check_btn);
                numberButton = itemView.findViewById(R.id.quantityBtn);
                 /*numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                     @Override
                     public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                         String num=numberButton.getNumber();
                         Log.d("num:",num);
                     }
                 });*/



            }
        }

}
