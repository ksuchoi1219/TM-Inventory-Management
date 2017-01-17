package com.TM.kwangsu.inventoryquery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxing.integration.android.IntentIntegrator;
import com.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;


public class Purchase extends AppCompatActivity implements OnClickListener {

    //Calendar: Variables
    private Button changeDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_PICKER_ID = 1111;
    private TextView displayDate;

    //Barcode Scan: Variables
    private Button scannerButton;
    private EditText displayBarcode;

    private Button doneButton;
    private ConnectionClass connectionClass;
    private EditText productName;
    private EditText quantity;
    private EditText price;
    private EditText vendor;
    private EditText description;
    private ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase);

        connectionClass = new ConnectionClass();
        //Calendar: Buttons
        changeDate = (Button)findViewById(R.id.pickDate);
        displayDate = (TextView)findViewById(R.id.userDate);
        final Calendar c = Calendar.getInstance();
        mYear  = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay   = c.get(Calendar.DAY_OF_MONTH);

        //Barcode Scan: Buttons
        scannerButton = (Button) findViewById(R.id.scanButton);
        displayBarcode = (EditText) findViewById(R.id.userBarcode);
        scannerButton.setOnClickListener(this);


        //Calendar: Functions
        displayDate.setText(new StringBuilder()
                .append(mMonth + 1).append("-").append(mDay).append("-")
                .append(mYear).append(" "));
        changeDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);

            }

        });

        //Import: Done
        productName = (EditText)findViewById(R.id.userItemName);
        quantity = (EditText)findViewById(R.id.userQuantity);
        price = (EditText)findViewById(R.id.userPrice);
        vendor = (EditText)findViewById(R.id.userVendor);
        description =(EditText) findViewById(R.id.userDescription);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DoImport doImport = new DoImport();
                doImport.execute("");

            }
        });

    }
    //Calendar: Date Picker
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, mYear, mMonth, mDay);
        }
        return null;
    }
    //Calendar: Date Picker (display date)
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            mYear  = selectedYear;
            mMonth = selectedMonth;
            mDay   = selectedDay;
            displayDate.setText(new StringBuilder().append(mMonth + 1)
                    .append("-").append(mDay).append("-").append(mYear)
                    .append(" "));

        }
    };
    //Barcode Scan: Onclick Function
    public void onClick(View v) {
        if (v.getId() == R.id.scanButton) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    //Barcode Scan: Scanner
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            displayBarcode.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //Import: Done Function
    public class DoImport extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String userScanned = displayBarcode.getText().toString();
        String userPName = productName.getText().toString();
        String userQuantity = quantity.getText().toString();
        String userPrice = price.getText().toString();
        String userVendor = vendor.getText().toString();
        String userDate = displayDate.getText().toString();
        String userDescription = description.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Purchase.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(Purchase.this, Dashboard.class);
                startActivity(i);
                finish();
            }

        }
        @Override
        protected String doInBackground(String... params) {

            if(userScanned.trim().equals("") || userPName.trim().equals("") || userQuantity.trim().equals("") || userPrice.trim().equals("") || userVendor.trim().equals(""))
                z = "Please enter all required fields!";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "INSERT INTO Producttbl (sku, pName, quantity, price, vendor, date, description) VALUES ('" + userScanned + "','" + userPName + "','" + userQuantity + "','" + userPrice + "', '" + userVendor + "', '" + userDate + "', '" + userDescription + "');";
                        Statement stmt = con.createStatement();
                        z = "Imported successfully!";
                        stmt.executeUpdate(query);
                        isSuccess = true;
                    }
                }
                catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

}





