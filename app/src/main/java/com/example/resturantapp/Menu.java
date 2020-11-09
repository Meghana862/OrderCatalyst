package com.example.resturantapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Menu extends AppCompatActivity {
    private Button starter;
    private Button biryani;
    private Button soup;
    private Button noodles;
    private Button drinks;
    private String time;
    private ActionBar actionBar;
    private String t_name;
    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        starter = findViewById(R.id.starter);
        biryani = findViewById(R.id.biryani);
        noodles = findViewById(R.id.rice);
        soup = findViewById(R.id.soup);
        drinks = findViewById(R.id.drinks);

        actionBar = getSupportActionBar();
        actionBar.hide();

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            time=(String)b.get("time");
            t_name=(String)b.get("t_name");
        }
        actionButton = findViewById(R.id.fab_btn111);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Menu.this,cartList.class);
                intent.putExtra("t_name",t_name);
                intent.putExtra("time",time );
                startActivity(intent);

            }
        });

        starter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Starters");
                intent.putExtra("time",time );
                startActivity(intent);
            }
        });
        soup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Soup");
                intent.putExtra("time",time );
                startActivity(intent);
            }
        });
        biryani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Biryani");
                intent.putExtra("time",time );
                startActivity(intent);
            }
        });
        noodles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Noodles");
                intent.putExtra("time",time );
                startActivity(intent);
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Drinks");
                intent.putExtra("time",time );
                startActivity(intent);
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
        Intent intent = new Intent(Menu.this,TablesListActivity.class);
        startActivity(intent);
        finish();
    }
}