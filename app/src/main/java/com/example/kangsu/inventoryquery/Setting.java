package com.example.kangsu.inventoryquery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;


public class Setting extends AppCompatActivity{


    private Button changeUNButton;
    private Button changePButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_dashboard);
        addListenerOnButton();

    }
    private void addListenerOnButton() {
        final Context context = this;
        changeUNButton = (Button) findViewById(R.id.changeUNB);
        changeUNButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeUsername.class);
                startActivity(intent);
            }
        });

        changePButton = (Button)findViewById(R.id.changePB);
        changePButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), ChangePassword.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

}


