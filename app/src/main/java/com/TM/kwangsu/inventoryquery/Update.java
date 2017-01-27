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
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.zxing.integration.android.IntentIntegrator;
import com.zxing.integration.android.IntentResult;

public class Update extends AppCompatActivity{

    private ConnectionClass connectionClass;

    private Button scannerButton;
    private Button updateButton;
    private Button findButton;
    private Button poButton;

    private EditText userSKU;
    private EditText userPname;
    private EditText userStock;
    private EditText userDescription;
    private EditText userPrice;

    private ProgressBar pbbar;

    private TextView prePName;
    private TextView preStock;
    private TextView prePrice;
    private TextView preDescription;



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

        prePName = (TextView) findViewById(R.id.nameTV);
        preStock = (TextView) findViewById(R.id.stockTV);
        prePrice = (TextView) findViewById(R.id.priceTV);
        preDescription = (TextView) findViewById(R.id.desTV);

        scannerButton = (Button) findViewById(R.id.scanButton);
        scannerButton.setOnClickListener((View.OnClickListener) this);
        updateButton = (Button) findViewById(R.id.updateButton);
        findButton = (Button) findViewById(R.id.findButton);
        poButton = (Button) findViewById(R.id.poButton);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userBar = userSKU.getText().toString();
                Connection con = connectionClass.CONN();
                String query = "select product_name, stock, price, description from dbo.products where pos_sku='" + userBar + "';";
                ResultSet rs;
                try {
                    Statement stmt = con.createStatement();
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        prePName.setText(rs.getString(1));
                        preStock.setText(rs.getString(2));
                        prePrice.setText(rs.getString(3));
                        preDescription.setText(rs.getString(4));
                    }
                    con.close();

                } catch (Exception ex) {
                    ex.getMessage();
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
                Intent intent = new Intent(context, Update.class);
                    startActivity(intent);
            }
        });
    }

}


