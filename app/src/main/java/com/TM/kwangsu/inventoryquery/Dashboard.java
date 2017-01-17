package com.TM.kwangsu.inventoryquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


public class Dashboard extends AppCompatActivity {

    private Button addButton;
    private Button updateButton;
    private Button poButton;
    private TextView greeting;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        addListenerOnButton();


        SharedPreferences prefs = getSharedPreferences("MA", MODE_PRIVATE);
        username = prefs.getString("UN", "UNKNOWN");
        greeting = (TextView) findViewById(R.id.greeting);
        Calendar c = Calendar.getInstance();
        int timeofDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeofDay >= 0 && timeofDay < 12) {
            greeting.setText("Good Morning, " + username + "!");
        } else if (timeofDay >= 12 && timeofDay < 16) {
            greeting.setText("Good Afternoon, " + username + "!");
        } else if (timeofDay >= 16 && timeofDay < 21) {
            greeting.setText("Good Evening, " + username + "!");
        } else {
            greeting.setText("Good Night, " + username + "!");
        }

    }

    private void addListenerOnButton() {
        final Context context = this;
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add.class);
                    startActivity(intent);
            }
        });
        updateButton = (Button)findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Update.class);
                startActivityForResult(myIntent, 0);
            }
        });
        poButton = (Button)findViewById(R.id.poButton);
        poButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Purchase.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

}


