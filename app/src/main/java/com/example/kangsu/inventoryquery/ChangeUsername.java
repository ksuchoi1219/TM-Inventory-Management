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


public class ChangeUsername extends AppCompatActivity{


    private ConnectionClass connectionClass;
    private Button updateButton;
    private EditText oldUN;
    private EditText newUN;
    private EditText confirmUN;
    private ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username);
        addListenerOnButton();

        connectionClass = new ConnectionClass();
        oldUN = (EditText) findViewById(R.id.oldUN);
        newUN = (EditText) findViewById(R.id.newUN);
        confirmUN = (EditText) findViewById(R.id.confirmUN);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        updateButton = (Button) findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoUpdate doUpdate = new DoUpdate();
                doUpdate.execute("");
            }
        });

    }
    public class DoUpdate extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        String oldU = oldUN.getText().toString();
        String newU = newUN.getText().toString();
        String confNewU = confirmUN.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(ChangeUsername.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(ChangeUsername.this, Dashboard.class);
                startActivity(i);
                finish();
            }

        }
        @Override
        protected String doInBackground(String... params) {

            if(oldU.trim().equals("")|| newU.trim().equals("") || confNewU.trim().equals(""))
                z = "Please enter Username and Password!";
            else if (!newU.trim().equals(confNewU))
                z = "Usernames do not match!";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server!";
                    } else {
                        String query = "UPDATE Usertbl SET userName=" + "'" + newU + "' WHERE usernName='" + oldU + "';";
                        Statement stmt = con.createStatement();
                        z = "Updated successfully!";
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
        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                    startActivity(intent);
            }
        });
    }

}


