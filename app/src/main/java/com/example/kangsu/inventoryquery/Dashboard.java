package com.example.kangsu.inventoryquery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;


public class Dashboard extends AppCompatActivity{

    private Button inventoryButton;
    private Button importsButton;
    private Button settingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        addListenerOnButton();
    }

    private void addListenerOnButton() {
        final Context context = this;
        inventoryButton = (Button) findViewById(R.id.inventoryButton);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Inventory.class);
                    startActivity(intent);
            }
        });
        importsButton = (Button)findViewById(R.id.importButton);
        importsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Import.class);
                startActivityForResult(myIntent, 0);
            }
        });
        settingButton = (Button)findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Setting.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

}


