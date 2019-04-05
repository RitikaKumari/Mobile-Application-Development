package com.ritika.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.JsonWriter;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private MainActivity mainAct = this;
    private RecyclerView recycler;
    private List<Official> officialLs = new ArrayList<>();
    private OfficialAdapter officialAdptr;
    private TextView locationV;
    private Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor( getResources().getColor( R.color.purple));

        recycler = findViewById(R.id.recycler);
        officialAdptr = new OfficialAdapter(officialLs, this);
        recycler.setAdapter(officialAdptr);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        locationV = findViewById(R.id.locationID);
        locationV.setTextColor(getResources().getColor(R.color.white));

        if(connected()) {
            locator = new Locator(this);
            locator.shutdown();
        } else{
            locationV.setText("No Data For Location");
            noNetDialog();
        }

    }

    private void saveData(){

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(("  "));
            writer.beginObject();
            writer.name("norminput").value(locationV.getText().toString());
            writer.endObject();
            writer.beginArray();
            for(int i = 0; i < officialLs.size(); i++){
                writer.beginObject();
                writer.endObject();
            }

            writer.endArray();
            
            writer.close();

        } catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == 5) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.setUpLocationManager();
                        locator.determineLocation();
                        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();

                    }
                }
            }
        }
    }

    public void doLocationWork(double latitude, double longitude) {

        List<Address> addresses = null;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address ad = addresses.get(0);
                String zip = ad.getPostalCode();
                new LoadDataAsyncTask(mainAct).execute(zip);

            } catch (IOException e) {
                Toast.makeText(mainAct,"Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();

            }
    }

    @Override
    public void onClick(View v){

        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);

        int pos = recycler.getChildLayoutPosition(v);
        Official o = officialLs.get(pos);

        intent.putExtra("header", locationV.getText().toString() );
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", o);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    @Override
    public boolean onLongClick(View v){
        int pos = recycler.getChildLayoutPosition(v);
        onClick(v);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.location:
                if(connected()){
                    searchDialog();
                }
                else{
                    noNetDialog();
                }
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean connected(){
        ConnectivityManager connmngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connmngr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void searchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText edittxt = new EditText(this);
        edittxt.setInputType(InputType.TYPE_CLASS_TEXT );
        edittxt.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(edittxt);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = edittxt.getText().toString();
                new LoadDataAsyncTask(mainAct).execute(input);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        builder.setMessage("Enter a City, State, or Zip Code:");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void noNetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
        builder.setTitle("No Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void setOfficialLs(Object[] results){

        if(results == null){
            locationV.setText("No Data For Location");
            officialLs.clear();
        }
        else{
            locationV.setText(results[0].toString());
            officialLs.clear();
            ArrayList<Official> offList = (ArrayList<Official>) results[1];
            for(int i = 0; i < offList.size(); i++){
                officialLs.add( offList.get(i));
            }
        }
        officialAdptr.notifyDataSetChanged();

    }
    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

}