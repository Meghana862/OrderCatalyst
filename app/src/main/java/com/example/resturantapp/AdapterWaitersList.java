package com.example.resturantapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdapterWaitersList extends RecyclerView.Adapter<AdapterWaitersList.HolderWaitersList> {

    private Context context;
    private ArrayList<ModelWaitersList> WaitersList;

    public AdapterWaitersList(Context context, ArrayList<ModelWaitersList> waitersList) {
        this.context = context;
        this.WaitersList = waitersList;
    }

    @NonNull
    @Override
    public HolderWaitersList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_waiters, parent, false);

        return new HolderWaitersList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderWaitersList holder, int position) {
        ModelWaitersList model = WaitersList.get(position);
        final String w_name= model.getName();
        String w_email= model.getEmail();
        final String id=model.getId();

        holder.w_name.setText(w_name);
        holder.w_email.setText(w_email);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WaiterEditActivity.class);
                intent.putExtra("w_name",w_name);
                context.startActivity(intent);
            }
        });*/

        holder.actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WaiterEditActivity.class);
                intent.putExtra("wId",id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return WaitersList.size();
    }

    class HolderWaitersList extends RecyclerView.ViewHolder {

        private TextView w_name;
        private TextView w_email;
        private FloatingActionButton actionButton1;

        public HolderWaitersList(@NonNull View itemView) {
            super(itemView);

            w_name = itemView.findViewById(R.id.waiter_name);
            w_email = itemView.findViewById(R.id.act_w_email);
            actionButton1=itemView.findViewById(R.id.des_check_btn);


        }
    }

}
