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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        starter = findViewById(R.id.starter);
        biryani = findViewById(R.id.biryani);
        noodles = findViewById(R.id.rice);
        soup = findViewById(R.id.soup);
        drinks = findViewById(R.id.drinks);

        starter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Starters");
                startActivity(intent);
            }
        });
        soup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Soup");
                startActivity(intent);
            }
        });
        biryani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Biryani");
                startActivity(intent);
            }
        });
        noodles.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Noodles");
                startActivity(intent);
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent intent = new Intent(Menu.this, CateorywiseItemsActivity.class);
                intent.putExtra("category","Drinks");
                startActivity(intent);
            }
        });
    }
}