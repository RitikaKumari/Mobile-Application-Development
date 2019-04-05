package com.ritika.knowyourgovernment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    public TextView officeV;
    public TextView nameV;
    public TextView locationV;
    public ImageView imageV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setContentView(R.layout.activity_photo);

        locationV = findViewById(R.id.locationID);
        officeV = findViewById(R.id.officeID);
        nameV = findViewById(R.id.nameID);
        imageV = findViewById(R.id.imageID);

        Intent intent = this.getIntent();
        String header = intent.getStringExtra("header");
        locationV.setText(header.toString());
        officeV.setText(intent.getStringExtra("office"));
        nameV.setText(intent.getStringExtra("name"));
        String color = intent.getStringExtra("color");

        if (color.equals("red")) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.darkRed));
        }
        if (color.equals("blue")) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.darkBlue));
        }
        if(color.equals("black")){

        }

        if(connected()) {
            final String photoUrl = intent.getStringExtra("photoUrl");
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
        } else{
            imageV.setImageResource(R.drawable.placeholder);
        }

    }
    private boolean connected(){
        ConnectivityManager connmngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connmngr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}