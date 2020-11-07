package com.example.resturantapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterSessionsList extends RecyclerView.Adapter<AdapterSessionsList.HolderSessionsList> {

    private Context context;
    private ArrayList<ModelSessionsList> sessionsList;

    public AdapterSessionsList(Context context, ArrayList<ModelSessionsList> sessionsList) {
        this.context = context;
        this.sessionsList = sessionsList;
    }

    @NonNull
    @Override
    public AdapterSessionsList.HolderSessionsList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_col_session, parent, false);
        return new AdapterSessionsList.HolderSessionsList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSessionsList.HolderSessionsList holder, int position) {
        ModelSessionsList model = sessionsList.get(position);
        final String date= model.getDate();
        final String cust= model.getCust();
        final String waitId=model.getWaitId();

        holder.t_name.setText(date);
        holder.t_status.setText(cust);

    }

    @Override
    public int getItemCount() {
        return sessionsList.size();
    }

    class HolderSessionsList extends RecyclerView.ViewHolder {

        private TextView t_name;
        private TextView t_status;

        public HolderSessionsList(@NonNull View itemView) {
            super(itemView);

            t_name = itemView.findViewById(R.id.table_name);
            t_status = itemView.findViewById(R.id.act_t_status);


        }
    }
}
