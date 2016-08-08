package com.example.trihan.drone_app_android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class ControlRoom extends AppCompatActivity {

    public static final String IPADDRESS = "IPADDRESS";

    private String mDroneIPAddress;

    private Button mGoToDroneInfoButton;
    private Button mTestConnect;

    public static Intent newIntent(Context packageContext, String ipAddress) {
        Intent intent = new Intent(packageContext, ControlRoom.class);
        intent.putExtra(IPADDRESS, ipAddress);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_room);

        mDroneIPAddress = this.getIntent().getStringExtra(IPADDRESS);

        mGoToDroneInfoButton = (Button) findViewById(R.id.button_go_to_drone_info);
        mTestConnect = (Button) findViewById(R.id.button_test_connect);

        mGoToDroneInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DroneInfo.newIntent(ControlRoom.this, mDroneIPAddress);
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
                Carrier c = new Carrier("http://" + mDroneIPAddress + ":8080/commands");
                Message m = new Message();
                m.write(Message.COMMAND, "do this, do that. muahahah");

                Message responseMessage = c.sendMessage(m);
                System.out.println(responseMessage.read(Message.RESPONSE));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        }
    }
}
