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


public class ChangePassword extends AppCompatActivity{


    private ConnectionClass connectionClass;
    private Button updateButton;
    private EditText oldPass;
    private EditText newPass;
    private EditText confirmPass;
    private ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        addListenerOnButton();

        connectionClass = new ConnectionClass();
        oldPass = (EditText) findViewById(R.id.userPassword);
        newPass = (EditText) findViewById(R.id.newPassword);
        confirmPass = (EditText) findViewById(R.id.confirmNewPass);
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

        String old = oldPass.getText().toString();
        String newP = newPass.getText().toString();
        String confNew = confirmPass.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(ChangePassword.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(ChangePassword.this, Dashboard.class);
                startActivity(i);
                finish();
            }

        }
        @Override
        protected String doInBackground(String... params) {
            if(old.trim().equals("")|| newP.trim().equals("") || confNew.trim().equals(""))
                z = "Please fill all the required fields!";
            else if (!newP.trim().equals(confNew))
                z = "Confirm password and password do not match!";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server!";
                    } else {
                        String query = "UPDATE Usertbl SET password=" + "'" + newP + "' WHERE password='" + old + "';";
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


