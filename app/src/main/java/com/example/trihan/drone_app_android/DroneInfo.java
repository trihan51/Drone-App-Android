package com.example.trihan.drone_app_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DroneInfo extends AppCompatActivity {

    public static final String IPADDRESS = "IPADDRESS";

    private String mDroneIPAddress;

    private Button mGoToFindDevicesButton;
    private Button mGoToControlRoomButton;

    public static Intent newIntent(Context packageContext, String ipAddress) {
        Intent intent = new Intent(packageContext, DroneInfo.class);
        intent.putExtra(IPADDRESS, ipAddress);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_info);

        mDroneIPAddress = this.getIntent().getStringExtra(IPADDRESS);

        mGoToFindDevicesButton = (Button) findViewById(R.id.button_go_to_find_devices);
        mGoToControlRoomButton = (Button) findViewById(R.id.button_go_to_control_room);

        mGoToFindDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FindDevicesActivity.newIntent(DroneInfo.this);
                startActivity(intent);
            }
        });

        mGoToControlRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ControlRoom.newIntent(DroneInfo.this, mDroneIPAddress);
                startActivity(intent);
            }
        });
    }
}
