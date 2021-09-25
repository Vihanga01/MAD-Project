package com.example.mobilehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class cart extends AppCompatActivity {

    Button addtocart, view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        addtocart = findViewById(R.id.btn_ac);
        view = findViewById(R.id.btn_vc);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Addtocart.class);
                startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),viewcart.class);
                startActivity(i);
            }
        });
    }
}