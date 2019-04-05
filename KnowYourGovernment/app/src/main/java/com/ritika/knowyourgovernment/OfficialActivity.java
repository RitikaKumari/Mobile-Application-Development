package com.ritika.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    public TextView locationV;
    public TextView officeV;
    public TextView nameV;
    public TextView partyV;
    public ImageView imageV;
    public TextView addressV;
    public TextView phoneV;
    public TextView emailV;
    public TextView websiteV;

    public TextView addressLbl;
    public TextView phoneLbl;
    public TextView emailLbl;
    public TextView websiteLbl;

    public ImageView youtubeBttn;
    public ImageView googleplusBttn;
    public ImageView twitterBttn;
    public ImageView facebookBttn;

    public Official official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        locationV = findViewById(R.id.locationID); locationV.setTextColor(Color.WHITE);
        officeV = findViewById(R.id.officeID);
        nameV = findViewById(R.id.nameID);
        partyV = findViewById(R.id.partyID);
        imageV = findViewById(R.id.imageID);
        addressV = findViewById(R.id.addressID);
        phoneV = findViewById(R.id.phoneID);
        emailV = findViewById(R.id.emailID);
        websiteV = findViewById(R.id.websiteID);

        this.addressLbl = findViewById(R.id.addressLbl);
        this.phoneLbl = findViewById(R.id.phoneLbl);
        this.emailLbl = findViewById(R.id.emailLbl);
        this.websiteLbl = findViewById(R.id.websiteLbl);
        addressLbl.setText(("Address:").toString());
        phoneLbl.setText(("Phone:").toString());
        emailLbl.setText(("Email:").toString());
        websiteLbl.setText(("Website:").toString());
        addressLbl.setTextColor(Color.WHITE);
        phoneLbl.setTextColor(Color.WHITE);
        emailLbl.setTextColor(Color.WHITE);
        websiteLbl.setTextColor(Color.WHITE);

        this.youtubeBttn = findViewById(R.id.youtubeBttn);
        this.googleplusBttn = findViewById(R.id.googleplusBttn);
        this.twitterBttn = findViewById(R.id.twitterBttn);
        this.facebookBttn = findViewById(R.id.facebookBttn);

        youtubeBttn.setImageResource(R.drawable.youtubeicon);
        googleplusBttn.setImageResource(R.drawable.googleplusicon);
        twitterBttn.setImageResource(R.drawable.twittericon);
        facebookBttn.setImageResource(R.drawable.facebookicon);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        official = (Official) bundle.getSerializable("official");
        locationV.setText(intent.getStringExtra("header"));

        if( official.getOffice().equals("No Data Provided")){ hideView(officeV);}
        else{
            officeV.setText(official.getOffice());
            officeV.setTextColor(Color.WHITE);}
        if( official.getName().equals("No Data Provided")){ hideView(nameV);}
        else{
            nameV.setText(official.getName());
            nameV.setTextColor(Color.WHITE);}
        if( official.getParty().equals("Unknown")){ hideView(partyV);}
        else{
            partyV.setText("(" + official.getParty() + ")"); partyV.setTextColor(Color.WHITE);
            if(official.getParty().equals("Democratic") || official.getParty().equals("Democrat")){
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(  R.color.darkBlue));
            }
            if(official.getParty().equals("Republican")){
                getWindow().getDecorView().setBackgroundColor( getResources().getColor(  R.color.darkRed));
            }
        }

        if(connected()) {
            imageV.setImageResource(R.drawable.placeholder);

            if (official.getPhotoURL().equals("No Data Provided")) {
                imageV.setImageResource(R.drawable.missingimage);
            } else {
                final String photoUrl = official.getPhotoURL();
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        final String changedUrl = photoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(imageV);

                    }
                }).build();

                picasso.load(photoUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(imageV);
            }

        } else {
            imageV.setImageResource(R.drawable.placeholder);
        }

        addressV.setText(official.getAddress()); addressV.setTextColor(Color.WHITE);
        phoneV.setText(official.getPhone()); phoneV.setTextColor(Color.WHITE);
        emailV.setText(official.getEmail()); emailV.setTextColor(Color.WHITE);
        websiteV.setText(official.getWebsiteUrl()); websiteV.setTextColor(Color.WHITE);



        if(official.getYoutubeId().equals("No Data Provided")){
            hideView(youtubeBttn);
        }
        if(official.getGooglePlusId().equals("No Data Provided")){
            hideView(googleplusBttn);
        }
        if(official.getTwitterId().equals("No Data Provided")){
            hideView(twitterBttn);
        }
        if(official.getFacebookId().equals("No Data Provided")){
            hideView(facebookBttn);
        }
        Linkify.addLinks(addressV,Linkify.MAP_ADDRESSES);
        Linkify.addLinks(phoneV,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(emailV,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(websiteV,Linkify.WEB_URLS);

    }

    private boolean connected(){
        ConnectivityManager connmngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connmngr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private static void hideView(View v){
        v.setVisibility(View.GONE);
    }

    public void openPhotoActivity(View v){
        if(official.getPhotoURL().equals("No Data Provided")){
            return;
        }
        Intent intent = new Intent(OfficialActivity.this, ImageActivity.class);
        intent.putExtra("header", locationV.getText().toString());
        intent.putExtra("office", official.getOffice());
        intent.putExtra("name", official.getName());

        if(official.getParty().equals("Democratic") || official.getParty().equals("Democrat")){
            intent.putExtra("color","blue");
        }
        if(official.getParty().equals("Republican")){
            intent.putExtra("color","red");
        }
        if(official.getParty().equals("Unknown")){
            intent.putExtra("color", "black");
        }
        intent.putExtra("photoUrl", official.getPhotoURL());

        startActivity(intent);

    }

    public void youtubeOnClick(View v){

        String name = official.getYoutubeId();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://www.youtube.com/" + name)));

        }
    }

    public void googleplusOnClick(View v){
        String name = official.getGooglePlusId();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void twitterOnClick(View v){
        Intent intent;
        String id = official.getTwitterId();
        try {
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }catch (Exception e){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + id));
        }
        startActivity(intent);
    }
    public void facebookOnClick(View v){

        String FACEBOOK_URL = "https://www.facebook.com/" + official.getFacebookId();
        String urlToUse;

        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlToUse = "fb://page/" + official.getFacebookId();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }
}