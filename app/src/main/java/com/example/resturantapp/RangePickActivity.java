package com.example.resturantapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class RangePickActivity extends AppCompatActivity {

    CardView cardview;
    private ActionBar actionBar;
    TextView from_date;
    TextView to_date;
    DatePickerDialog.OnDateSetListener setListener;
    DatePickerDialog.OnDateSetListener setListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_pick);

        cardview = findViewById(R.id.cardview);
        from_date = findViewById(R.id.from_date);
        to_date = findViewById(R.id.to_date);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Pick an interval");

        Calendar calendar= Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RangePickActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                from_date.setText(date);
            }
        };

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RangePickActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date = day+"/"+month+"/"+year;
                        from_date.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //to date
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RangePickActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener1,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                to_date.setText(date);
            }
        };

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RangePickActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date = day+"/"+month+"/"+year;
                        to_date.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        //submit

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RangePickActivity.this, SessionsListActivity.class);
                String date1=from_date.getText().toString();
                String date2=to_date.getText().toString();
                Log.d("date1",date1);
                Log.d("date2",date2);
                String[] st1=date1.split("/");
                String[] st2=date2.split("/");
                String nst1 = st1[2] + "/" + st1[1] + "/" + st1[0];
                String nst2 = st2[2] + "/" + st2[1] + "/" + st2[0];
                Log.d("nst1",nst1);
                Log.d("nst2",nst2);
                int flag=0;
                for(int i=0;i<st1.length;i++){
                    if(Integer.parseInt(st1[i])<=Integer.parseInt(st2[i])){
                        flag++;
                    }
                }
                if(!(date1.equals("")) && !(date2.equals("")) && nst2.compareTo(nst1) >= 0){
                    intent.putExtra("date1",date1 );
                    intent.putExtra("date2",date2);
                    // Toast.makeText(RangeActivity.this, date1+","+date2, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RangePickActivity.this,"fill valid dates", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(RangePickActivity.this,TablesListActivity.class);
        startActivity(intent);
        finish();
    }
}
