package com.ritika.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class Locator {

    private MainActivity owner;
    private LocationManager locManager;
    private LocationListener locListener;

    public Locator(MainActivity act){
        owner = act;

        if(checkPermission()){
            setUpLocationManager();
            determineLocation();
        }
    }

    public void setUpLocationManager(){

        if(locManager != null){return;}
        if(!checkPermission()){ return;}

        locManager = (LocationManager) owner.getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                owner.doLocationWork(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,1000, 0, locListener);

    }

    public android.location.LocationListener getLocListener()
    {
        return locListener;
    }

   public void determineLocation(){

        if(!checkPermission()){return;}

        if(locManager == null){setUpLocationManager();}

       if (locManager != null) {
           Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           if (loc != null) {
               owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
               Toast.makeText(owner, "Using " + LocationManager.NETWORK_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
               return;
           }
       }

       if (locManager != null) {
           Location loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
           if (loc != null) {
               owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
               Toast.makeText(owner, "Using " + LocationManager.PASSIVE_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
               return;
           }
       }

       if (locManager != null) {
           Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           if (loc != null) {
               owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
               Toast.makeText(owner, "Using " + LocationManager.GPS_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
               return;
           }
       }

       owner.noLocationAvailable();
       return;
   }

   private boolean checkPermission(){
       if (ContextCompat.checkSelfPermission(owner, Manifest.permission.ACCESS_FINE_LOCATION) !=
               PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(owner,
                   new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
           return false;
       }
       return true;
   }

    public void shutdown(){
        locManager.removeUpdates(locListener);
        locManager = null;
    }
}
