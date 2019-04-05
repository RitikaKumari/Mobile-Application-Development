package com.example.a17739.teperatureconverter;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    boolean start;
    TextView output;
    TextView history;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.output);
        history = findViewById(R.id.history);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    public void onStartInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restoring the UI state from the savedInstanceState.

        String rotatehistory = savedInstanceState.getString("SavedHistoryVal");
        String outputConverted = savedInstanceState.getString("FinalValue");
        history.setText(rotatehistory);
        output.setText(outputConverted);
        history.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("SavedHistoryVal", history.getText().toString());
        outState.putString("FinalValue", output.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restoring the UI state from the savedInstanceState.

        String rotatehistory = savedInstanceState.getString("SavedHistoryVal");
        String outputConverted = savedInstanceState.getString("FinalValue");
        history.setText(rotatehistory);
        history.setMovementMethod(new ScrollingMovementMethod());
        output.setText(outputConverted);
    }

// Functions for maintaining the life cycle.
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
//Functions for refreshing value of TextView on changing the value of EditText
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TextView output=(TextView) findViewById(R.id.output);
        output.setText(null);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TextView output=(TextView) findViewById(R.id.output);
        output.setText(null);
        return super.onKeyDown(keyCode, event);
    }
//Functions for validating the null input value.
    public void alert(String a, String b) {
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle(a);
        alBuilder.setMessage(b);
        alBuilder.setPositiveButton("OK", null);
        alBuilder.setCancelable(true);
        alBuilder.create().show();

    }

    public void onClickCel(View v) {
        EditText CelsiusToFahrenheit = findViewById(R.id.input);
        TextView FahrenheitToCelsius = findViewById(R.id.output);
        CelsiusToFahrenheit.setText(null);
        FahrenheitToCelsius.setText(null);

    }

    public void onClickFeh(View v) {
        EditText CelsiusToFahrenheit = findViewById(R.id.input);
        TextView FahrenheitToCelsius = findViewById(R.id.output);
        CelsiusToFahrenheit.setText(null);
        FahrenheitToCelsius.setText(null);
    }
//Function for Temperature Conversion
    public void tempConvert(View V) {


        Log.d(TAG, "tempConvert: Start");
        RadioButton CelsiusToFahrenheit = findViewById(R.id.radioButtonCelsiusToFahrenheit);
        RadioButton FahrenheitToCelsius = findViewById(R.id.radioButtonFahrenheitToCelsius);
        EditText input = findViewById(R.id.input);
        String value = input.getText().toString();
        if (!CelsiusToFahrenheit.isChecked() && !FahrenheitToCelsius.isChecked()) {
            alert("Alert", "Please Select Radio button");

        } else if (value.equals("") || value == "" || value == null) {

            alert("Alert", "Enter Any Value");
        } else {

            double previous = Double.parseDouble(input.getText().toString());
            double current = 0.0;
            String prefix = "";
            if (CelsiusToFahrenheit.isChecked()) {
                current = previous * (9.0 / 5.0) + 32.0;
                prefix = " C to F:  " +  previous + "  ->  " + String.format("%.1f", current);
            }

            if (FahrenheitToCelsius.isChecked()) {
                current = (previous - 32.0) * (5.0 / 9.0);
                prefix = " F to C:  " + previous + "  ->  " + String.format("%.1f", current);
            }

            output.setText(String.format("%.1f", current));

            String currentHistory = history.getText().toString();
            currentHistory = prefix + "\n" + currentHistory;
            history.setText(currentHistory);
            Log.d(TAG, "tempConvert: Done");

            if (start = true) {
                history.setMovementMethod(new ScrollingMovementMethod());
            }

        }
    }
}
