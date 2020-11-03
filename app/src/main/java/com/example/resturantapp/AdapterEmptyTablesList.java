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

public class AdapterEmptyTablesList extends RecyclerView.Adapter<AdapterEmptyTablesList.HolderEmptyTablesList> {

    private Context context;
    private ArrayList<ModelTablesList> TablesList;

    public AdapterEmptyTablesList(Context context, ArrayList<ModelTablesList> tablesList) {
        this.context = context;
        this.TablesList = tablesList;
    }

    @NonNull
    @Override
    public AdapterEmptyTablesList.HolderEmptyTablesList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_empty_tables, parent, false);
        return new AdapterEmptyTablesList.HolderEmptyTablesList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmptyTablesList.HolderEmptyTablesList holder, int position) {
        ModelTablesList model = TablesList.get(position);
        final String t_name= model.getName();
        String t_status= model.getStatus();

        holder.t_name.setText(t_name);
        holder.t_status.setText(t_status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,CustomerDetailsActivity.class);
                intent.putExtra("t_name",t_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return TablesList.size();
    }

    class HolderEmptyTablesList extends RecyclerView.ViewHolder {

        private TextView t_name;
        private TextView t_status;

        public HolderEmptyTablesList(@NonNull View itemView) {
            super(itemView);

            t_name = itemView.findViewById(R.id.table_name);
            t_status = itemView.findViewById(R.id.act_t_status);


        }
    }
}
