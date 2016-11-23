package com.example.kangsu.inventoryquery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


public class Dashboard extends AppCompatActivity {

    private Button inventoryButton;
    private Button importsButton;
    private Button settingButton;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        addListenerOnButton();



        greeting = (TextView) findViewById(R.id.greeting);
        Calendar c = Calendar.getInstance();
        int timeofDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeofDay >= 0 && timeofDay < 12) {
            greeting.setText("Good Monring, ");
        } else if (timeofDay >= 12 && timeofDay < 16) {
            greeting.setText("Good Afternoon, ");
        } else if (timeofDay >= 16 && timeofDay < 21) {
            greeting.setText("Good Evening, ");
        } else {
            greeting.setText("Good Night, ");
        }

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


