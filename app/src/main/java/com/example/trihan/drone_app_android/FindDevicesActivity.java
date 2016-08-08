package com.example.trihan.drone_app_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FindDevicesActivity extends AppCompatActivity {

    private GridView mGridViewDevicesFound;
    private ScrollView mScrollViewOfGridView;
    private Button mLogoutButton;

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

        ArrayList<String> ipAddresses = new ArrayList<String>();
        ipAddresses.add("192.168.1.119");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, ipAddresses);
        mGridViewDevicesFound.setAdapter(adapter);
        mGridViewDevicesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String ipAddress = (String) adapterView.getItemAtPosition(position);

                Intent intent = DroneInfo.newIntent(FindDevicesActivity.this, ipAddress);
                startActivity(intent);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = MainActivity.newIntent(FindDevicesActivity.this);
                startActivity(intent);
            }
        });
    }
}
