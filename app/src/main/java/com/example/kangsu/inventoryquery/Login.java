package com.example.kangsu.inventoryquery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Login extends AppCompatActivity{

    private Button lButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addListenerOnButton();

        lButton = (Button)findViewById(R.id.loginButton);
        lButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Dashboard.class);
                startActivityForResult(myIntent, 0);
            }
        });



    }

    private void addListenerOnButton() {
        final Context context = this;
        lButton = (Button) findViewById(R.id.loginButton);
        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                    startActivity(intent);
            }
        });
    }

}


