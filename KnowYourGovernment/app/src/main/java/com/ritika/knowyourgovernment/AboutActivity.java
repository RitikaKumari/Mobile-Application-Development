package com.ritika.knowyourgovernment;

import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;


public class AboutActivity extends AppCompatActivity {

    private TextView title;
    private TextView copyright;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        title = findViewById(R.id.titleID);
        copyright = findViewById(R.id.copyrightID);
        version = findViewById(R.id.versionID);

        title.setText("Know Your Government");
        copyright.setText("© 2018, Ritika Kumari");
        version.setText("Version 1.0");
    }
}
