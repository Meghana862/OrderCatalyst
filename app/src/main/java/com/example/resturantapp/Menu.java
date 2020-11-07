package com.example.resturantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    private Button starter;
    private Button biryani;
    private Button soup;
    private Button noodles;
    private Button drinks;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        starter = findViewById(R.id.starter);
        biryani = findViewById(R.id.biryani);
        noodles = findViewById(R.id.rice);
        soup = findViewById(R.id.soup);
        drinks = findViewById(R.id.drinks);

        Intent iin=getIntent();
        Bundle b=iin.getExtras();
        if(b!=null){
            time=(String)b.get("time");
        }

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
}