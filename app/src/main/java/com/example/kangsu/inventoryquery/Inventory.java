package com.example.kangsu.inventoryquery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.Calendar;


public class Inventory extends AppCompatActivity implements OnClickListener {

//========= C A L E N D A R ===========//
    private Button changeDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_PICKER_ID = 1111;
    private TextView displayDate;

//========== S C A N N E R ============//
    private Button scannerButton;
    private TextView displayBarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_dashboard);

        changeDate = (Button)findViewById(R.id.pickDate);
        displayDate = (TextView)findViewById(R.id.userDate);

        scannerButton = (Button) findViewById(R.id.scanButton);
        displayBarcode = (TextView) findViewById(R.id.userBarcode);
        scannerButton.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        mYear  = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay   = c.get(Calendar.DAY_OF_MONTH);

        displayDate.setText(new StringBuilder()
                .append(mMonth + 1).append("-").append(mDay).append("-")
                .append(mYear).append(" "));
        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            mYear  = selectedYear;
            mMonth = selectedMonth;
            mDay   = selectedDay;

            // Show selected date
            displayDate.setText(new StringBuilder().append(mMonth + 1)
                    .append("-").append(mDay).append("-").append(mYear)
                    .append(" "));

        }
    };

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
            //String scanFormat = scanningResult.getFormatName();
            displayBarcode.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}





