package com.example.resturantapp;

import android.content.Context;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterOrderedItems extends RecyclerView.Adapter<com.example.resturantapp.AdapterOrderedItems.HolderItemsList> {

    private Context context;
    private String time;
    private ArrayList<ModelCategorywiseItem> itemsList;

    public AdapterOrderedItems(Context context, ArrayList<ModelCategorywiseItem> itemsList,String time) {
        this.context = context;
        this.itemsList = itemsList;
        this.time=time;
    }

    @NonNull
    @Override
    public com.example.resturantapp.AdapterOrderedItems.HolderItemsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_display, parent, false);

        return new com.example.resturantapp.AdapterOrderedItems.HolderItemsList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final com.example.resturantapp.AdapterOrderedItems.HolderItemsList holder, int position) {
        ModelCategorywiseItem model = itemsList.get(position);
        final String w_name= model.getName();
        final String w_cost= model.getCost();
        final String id=model.getItemId();

        holder.name.setText(w_name);
        final CollectionReference rootRef = FirebaseFirestore.getInstance().collection("customers");
        rootRef.document(time).collection("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        if (id.equals(document.getId())) {
                            holder.quantity.setText(document.get("qantity").toString());
                            int x=Integer.parseInt(document.get("qantity").toString());
                            int y=Integer.parseInt(w_cost);
                            int pro=(x*y);
                            Log.d("pro:", String.valueOf(pro));
                            holder.price.setText(String.valueOf(pro));
                        }
                    }

                } else {
                    Log.d("FAILED", "Error getting documents: ", task.getException());
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

        private TextView name;
        private TextView quantity;
        private TextView price;

        public HolderItemsList(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.itemName);
            quantity = itemView.findViewById(R.id.actualQuan);
            price = itemView.findViewById(R.id.actualPrice);

        }
    }

}
