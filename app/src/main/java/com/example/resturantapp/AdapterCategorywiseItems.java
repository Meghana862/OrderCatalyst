package com.example.resturantapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
public class AdapterCategorywiseItems extends RecyclerView.Adapter<com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList> {

        private Context context;
        private ArrayList<ModelCategorywiseItem> itemsList;
        public ElegantNumberButton numberButton;

        public AdapterCategorywiseItems(Context context, ArrayList<ModelCategorywiseItem> itemsList) {
            this.context = context;
            this.itemsList = itemsList;
        }

        @NonNull
        @Override
        public com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_food_categorywise, parent, false);

            return new com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.resturantapp.AdapterCategorywiseItems.HolderItemsList holder, int position) {
            ModelCategorywiseItem model = itemsList.get(position);
            final String w_name= model.getName();
            String w_cost= model.getCost();
            final String id=model.getItemId();

            holder.w_name.setText(w_name);
            holder.w_cost.setText(w_cost);

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
            //private FloatingActionButton actionButton1;


            public HolderItemsList(@NonNull View itemView) {
                super(itemView);

                w_name = itemView.findViewById(R.id.waiter_name);
                w_cost = itemView.findViewById(R.id.act_w_email);
               // actionButton1=itemView.findViewById(R.id.des_check_btn);
                numberButton = (ElegantNumberButton)itemView.findViewById(R.id.quantityBtn);
                 numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         String num=numberButton.getNumber();
                     }
                 });



            }
        }

}
