package com.TM.kwangsu.inventoryquery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


public class Dashboard extends AppCompatActivity {

    private Button addButton;
    private Button updateButton;
    private TextView greeting;
    private String username = "";
    private Toolbar toolbar;

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
            greeting.setText("Good morning, \n" + username + "!");
        } else if (timeofDay >= 12 && timeofDay < 16) {
            greeting.setText("Good afternoon, \n" + username + "!");
        } else if (timeofDay >= 16 && timeofDay < 21) {
            greeting.setText("Good evening, \n" + username + "!");
        } else {
            greeting.setText("Good night, \n" + username + "!");
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

    }

}


