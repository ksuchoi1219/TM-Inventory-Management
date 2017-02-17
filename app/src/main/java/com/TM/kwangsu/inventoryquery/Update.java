package com.TM.kwangsu.inventoryquery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.zxing.integration.android.IntentIntegrator;
import com.zxing.integration.android.IntentResult;

public class Update extends AppCompatActivity implements View.OnClickListener {

    private ConnectionClass connectionClass;

    private Button scannerButton;
    private Button updateButton;
    private Button findButton;

    private EditText userSKU;
    private EditText userPname;
    private EditText userStock;
    private EditText userNewStock;
    private EditText userDescription;
    private EditText userPrice;
    private ProgressBar pbbar;
    private String totalStock;
    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        addListenerOnButton();
        SharedPreferences prefs = getSharedPreferences("MA", MODE_PRIVATE);
        username = prefs.getString("UN", "UNKNOWN");
        connectionClass = new ConnectionClass();
        userPname = (EditText) findViewById(R.id.userItemName);
        userStock = (EditText) findViewById(R.id.userQuantity);
        userNewStock = (EditText) findViewById(R.id.userNewQuantity);
        userPrice = (EditText) findViewById(R.id.userPrice);
        userDescription = (EditText) findViewById(R.id.userDescription);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);


        scannerButton = (Button) findViewById(R.id.scanButton);
        userSKU = (EditText) findViewById(R.id.userBarcode);
        scannerButton.setOnClickListener(this);
        updateButton = (Button) findViewById(R.id.updateButton);
        findButton = (Button) findViewById(R.id.findButton);


        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userBar = userSKU.getText().toString();
                if (userBar.equals("")) {
                    Toast.makeText(getBaseContext(), "Please enter barcode above!",  Toast.LENGTH_SHORT).show();

                } else {
                    Connection con = connectionClass.CONN();
                    String query = "select brief, stock, price, description from dbo.products where pos_sku='" + userBar + "' and Merchant = '" + username + "';";
                    ResultSet rs;
                    try {
                        Statement stmt = con.createStatement();
                        rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            userPname.setText(rs.getString(1), TextView.BufferType.EDITABLE);
                            userStock.setText(rs.getString(2), TextView.BufferType.EDITABLE);
                            userPrice.setText(rs.getString(3), TextView.BufferType.EDITABLE);
                            userDescription.setText(rs.getString(4), TextView.BufferType.EDITABLE);
                            userNewStock.setText("0");
                        }
                        con.close();
                        if (userPname.getText().toString().equals("") && userStock.getText().toString().equals("") && userPrice.getText().toString().equals("") && userDescription.getText().toString().equals("")) {
                            Toast.makeText(getBaseContext(), "No such item in database!",  Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoUpdate doUpdate = new DoUpdate();
                doUpdate.execute("");
            }
        });

    }

    public void onClick(View v) {
        if (v.getId() == R.id.scanButton) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            userSKU.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public class DoUpdate extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        String userBar = userSKU.getText().toString();
        String newName = userPname.getText().toString();
        String newStock = userStock.getText().toString();
        String newDescription = userDescription.getText().toString();
        String newPrice = userPrice.getText().toString();
        String unewStock = userNewStock.getText().toString();


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
                        if (unewStock != null) {
                            int original = Integer.parseInt(newStock);
                            int newItems = Integer.parseInt(unewStock);

                            totalStock = Integer.toString(original + newItems);
                        }
                        String query = "update dbo.products set brief = '"+newName+"', stock = '"+totalStock+"', description = '"+newDescription+"', price= '"+newPrice+"' where pos_sku = '"+userBar+"' and Merchant = '" + username + "';";
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
                Intent intent = new Intent(context, Update.class);
                    startActivity(intent);
            }
        });
    }

}


