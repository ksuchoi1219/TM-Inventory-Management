package com.example.kangsu.inventoryquery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;


public class Dashboard extends AppCompatActivity {

    private Button inventoryButton;
    private Button importsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        addListenerOnButton();

        inventoryButton = (Button)findViewById(R.id.inventory);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Inventory.class);
                startActivityForResult(myIntent, 0);
            }
        });

        importsButton = (Button)findViewById(R.id.imports);
        importsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Inventory.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    private void addListenerOnButton() {
        final Context context = this;
        inventoryButton = (Button) findViewById(R.id.inventory);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Inventory.class);
                    startActivity(intent);
            }
        });
    }

}


