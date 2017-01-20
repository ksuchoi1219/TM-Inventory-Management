package com.TM.kwangsu.inventoryquery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class Add extends AppCompatActivity implements OnClickListener {

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
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);


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

        SharedPreferences prefs = getSharedPreferences("MA", MODE_PRIVATE);
        String username = prefs.getString("UN", "UNKNOWN");
        String userScanned = displayBarcode.getText().toString();
        String userPName = productName.getText().toString();
        String userQuantity = quantity.getText().toString();
        String userPrice = price.getText().toString();
        String userDate = displayDate.getText().toString();
        String userDescription = description.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Add.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(Add.this, Dashboard.class);
                startActivity(i);
                finish();
            }

        }
        @Override
        protected String doInBackground(String... params) {

            if(userScanned.trim().equals("") || userPName.trim().equals("") || userQuantity.trim().equals("") || userPrice.trim().equals("") || userDescription.trim().equals(""))
                z = "Please enter all required fields!";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "insert into dbo.products" +
                                "(" +
                                "MerchantMasterID, Merchant, brand_id, mftr_id, is_main," +
                                "main_img, main_img_I, upc_code, lava_code, sku_code," +
                                "colorCode, sizeIdx, other, pos_sku, product_sku, " +
                                "product_name, location_code, material, brief, description, " +
                                "description_html, bullet_1, bullet_2, bullet_3, bullet_4, " +
                                "bullet_5, weight, cost, wholesalePrice, price, " +
                                "msrp, price_r, min_qty, stock, soldout_date, " +
                                "display_order, is_on, is_on_r, is_on_i, is_new, " +
                                "date_insert, date_update, note, Iprice_A, Iprice_B," +
                                "Iprice_C, openpricecheck)" +
                                
                                "values" +
                                "(" +
                                " NULL, '"+username+"', NULL, NULL, 1," +
                                " NULL, 'no_photo.jpg', NULL, NULL, '"+username+"-"+userScanned+"'," +
                                " '-', NULL, NULL, '"+userScanned+"', '"+username+"-"+userScanned+"'," +
                                " NULL, 'N/A', NULL, NULL, '"+userDescription+"'," +
                                " 1, NULL, NULL, NULL, NULL," +
                                " NULL, 0, 0.00, 0.00, "+userPrice+"," +
                                " 0.00, 0.00, 1, "+userQuantity+", NULL," +
                                " NULL, 0, 0, 0, 0," +
                                " '"+userDate+"', '"+userDate+"', NULL, 0.00, 0.00," +
                                " 0.00, 'NO');";

                        Statement stmt = con.createStatement();
                        z = "Imported successfully!";
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

}





