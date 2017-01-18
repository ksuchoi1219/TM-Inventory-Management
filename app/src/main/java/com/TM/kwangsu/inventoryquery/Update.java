package com.TM.kwangsu.inventoryquery;

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


public class Update extends AppCompatActivity{


    private ConnectionClass connectionClass;
    private Button updateButton;
    private Button poButton;
    private EditText userSKU;
    private EditText userPname;
    private EditText userStock;
    private EditText userDescription;
    private EditText userPrice;
    private ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        addListenerOnButton();

        connectionClass = new ConnectionClass();
        userSKU = (EditText) findViewById(R.id.userBarcode);
        userPname = (EditText) findViewById(R.id.userItemName);
        userStock = (EditText) findViewById(R.id.userQuantity);
        userDescription = (EditText) findViewById(R.id.userDescription);
        userPrice = (EditText) findViewById(R.id.userPrice);

        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        updateButton = (Button) findViewById(R.id.updateButton);
        poButton = (Button) findViewById(R.id.poButton);
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

        String userBar = userSKU.getText().toString();
        String newName = userPname.getText().toString();
        String newStock = userStock.getText().toString();
        String newDescription = userDescription.getText().toString();
        String newPrice = userPrice.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Update.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(Update.this, Dashboard.class);
                startActivity(i);
                finish();
            }

        }
        @Override
        protected String doInBackground(String... params) {
            if(userBar.trim().equals("")||newName.trim().equals("")||newStock.trim().equals("")||newDescription.trim().equals("")||newPrice.trim().equals(""))
                z = "Please enter all required fields!";

            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server!";
                    } else {
                        String query = "update dbo.products set product_name = '"+newName+"', stock = '"+newStock+"', description = '"+newDescription+"', price= '"+newPrice+"' where pos_sku = '"+userBar+"';";
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


