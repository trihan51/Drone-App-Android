package com.example.trihan.drone_app_android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class FindDevicesActivity extends AppCompatActivity {

    private GridView mGridViewDevicesFound;
    private ScrollView mScrollViewOfGridView;
    private Button mLogoutButton;
    private Button mTestConnect;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, FindDevicesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_devices);

        mGridViewDevicesFound = (GridView) findViewById(R.id.gridView_devices_found);
        mScrollViewOfGridView = (ScrollView) findViewById(R.id.scrollView_of_gridview);
        mLogoutButton = (Button) findViewById(R.id.button_logout);
        mTestConnect = (Button) findViewById(R.id.button_test_connect);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = MainActivity.newIntent(FindDevicesActivity.this);
                startActivity(intent);
            }
        });

        mTestConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierConnectTask().execute();
            }
        });
    }

    private class CarrierConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Carrier c = new Carrier("http://192.168.1.119:8080/connect");
                Message m = new Message();
                m.setCommand("do this, do that. muahahah");

                String response = c.sendMessage(m);
                System.out.println(response);
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
            return null;
        }
    }
}
