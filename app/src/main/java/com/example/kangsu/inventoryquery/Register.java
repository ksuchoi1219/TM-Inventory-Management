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

import java.io.Console;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;


public class Register extends AppCompatActivity{


    private ConnectionClass connectionClass;
    private Button registerButton;
    private EditText firstName;
    private EditText lastName;
    private EditText id;
    private EditText password;
    private EditText confirmPassword;
    private ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dashboard);
        addListenerOnButton();

        connectionClass = new ConnectionClass();
        firstName = (EditText) findViewById(R.id.userFName);
        lastName = (EditText) findViewById(R.id.userLName);
        id = (EditText) findViewById(R.id.userID);
        password = (EditText) findViewById(R.id.userPassword);
        confirmPassword = (EditText) findViewById(R.id.userConfirm);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoRegister doLogin = new DoRegister();
                doLogin.execute("");
            }
        });

    }
    public class DoRegister extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        String userFN = firstName.getText().toString();
        String userLN = lastName.getText().toString();
        String userID = id.getText().toString();
        String userPass = password.getText().toString();
        String confirm = confirmPassword.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Register.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(Register.this, Dashboard.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            Date date = new Date();
            String stringDate = DateFormat.getDateTimeInstance().format(date);
          if(userFN.trim().equals("")|| userLN.trim().equals("") || userID.trim().equals("") || userPass.trim().equals("") || confirm.trim().equals(""))
                z = "Please enter Username and Password!";
            else if (!userPass.trim().equals(confirm))
                z = "Confirm password and password do not match!";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server!";
                    } else {
                        String query = "INSERT INTO Usertbl (firstName, lastName, userName, password, regTime) VALUES ('" + userFN + "','" + userLN + "','" + userID + "','" + userPass + "', '" + stringDate + "')";
                        Statement stmt = con.createStatement();
                        z = "Registered successfully!";
                        stmt.executeUpdate(query);
                        isSuccess = true;

                    }
                }
                catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }
    private void addListenerOnButton() {
        final Context context = this;
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                    startActivity(intent);
            }
        });
    }

}


