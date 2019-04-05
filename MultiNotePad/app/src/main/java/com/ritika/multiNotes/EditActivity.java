package com.ritika.multiNotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    public static final int RESULT_NO_MODIFICATION = 5;

    private EditText editText;
    private EditText editTitle;
    private String nativeText = "";
    private String nativeTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = (EditText) findViewById(R.id.editText);
        editTitle = (EditText) findViewById(R.id.editTitle);

        editText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            nativeTitle = intent.getStringExtra("Title");
            editTitle.setText(nativeTitle);
            nativeText = intent.getStringExtra("Text");
            editText.setText(nativeText);
        }
        else {
            editTitle.setText("");
            editText.setText("");
        }

        Log.d(TAG, " Open ");

    }

    public void saveNote () {
        if(editTitle.getText().toString().equals("")){
            setResult(RESULT_CANCELED);
        }
        else {
            if (editTitle.getText().toString().equals(nativeTitle) && editText.getText().toString().equals(nativeText) && !editText.getText().toString().equals("")){
                setResult(RESULT_NO_MODIFICATION);
            }
            else {

                Intent data = new Intent(); // Used to hold results data to be returned to original activity
                data.putExtra("NEW_NOTE_TEXT", editText.getText().toString());
                data.putExtra("NEW_NOTE_TITLE", editTitle.getText().toString());
                data.putExtra("NEW_NOTE_DATE", "" + new SimpleDateFormat("dd MMM yyyy - HH:mm").format(Calendar.getInstance().getTime()));
                setResult(RESULT_OK, data);
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.edit_item_save:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                saveNote();
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ( !editText.getText().toString().equals(nativeText) || !editTitle.getText().toString().equals(nativeTitle)){

            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Confirmation")
                    .setMessage("Your note is not yet saved ! \n Would you like to save note \" "+editTitle.getText()+"\" ?")
                    .setPositiveButton("Save",dialogClickListener)
                    .setNegativeButton("Do not save the note",dialogClickListener)
                    .show();

        }
        else {
            super.onBackPressed();
        }
    }

    public void onSoftBackPressed() {
        if ( !editText.getText().toString().equals(nativeText)){

            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Confirmation")
                    .setMessage("Your note is not yet saved ! \n Would you like to save note \" "+editTitle.getText()+"\" ?")
                    .setPositiveButton("Save",dialogClickListener)
                    .setNegativeButton("Do not save the note",dialogClickListener)
                    .show();

        }
        else {
            finish();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Log.d(TAG, " Button : YES ");
                    saveNote();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    Log.d(TAG, " Button : NO ");
                    finish();
                    break;
            }
        }
    };
}


