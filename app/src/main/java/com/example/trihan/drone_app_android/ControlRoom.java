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

    private Button mUpButton;
    private Button mDownButton;
    private Button mForwardButton;
    private Button mBackwardButton;
    private Button mRotateLeftButton;
    private Button mRotateRightButton;

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
        mUpButton = (Button) findViewById(R.id.button_up);
        mDownButton = (Button) findViewById(R.id.button_down);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        mBackwardButton = (Button) findViewById(R.id.button_backward);
        mRotateLeftButton = (Button) findViewById(R.id.button_rotate_left);
        mRotateRightButton = (Button) findViewById(R.id.button_rotate_right);

        mGoToDroneInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DroneInfo.newIntent(ControlRoom.this, mDroneIPAddress);
                startActivity(intent);
            }
        });

        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierCommandTask().execute(mDroneIPAddress, Commands.MOVE_UP);
            }
        });

        mDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierCommandTask().execute(mDroneIPAddress, Commands.MOVE_DOWN);
            }
        });

        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierCommandTask().execute(mDroneIPAddress, Commands.MOVE_FORWARD);
            }
        });

        mBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierCommandTask().execute(mDroneIPAddress, Commands.MOVE_BACKWARD);
            }
        });

        mRotateLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierCommandTask().execute(mDroneIPAddress, Commands.ROTATE_LEFT);
            }
        });

        mRotateRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CarrierCommandTask().execute(mDroneIPAddress, Commands.ROTATE_RIGHT);
            }
        });
    }

    private class CarrierCommandTask extends AsyncTask<String, Void, Message> {
        @Override
        protected Message doInBackground(String... params) {
            String DroneIpAddress = params[0];
            String command = params[1];

            Message response;
            try{
                Carrier c = new Carrier("http://" + DroneIpAddress + ":8080/commands");
                Message m = new Message();
                m.write(Message.COMMAND, command);
                response = c.sendMessage(m);
            } catch (IOException ioe) {
                response = new Message();
                response.write(Message.RESPONSE, "IOException Error");
                ioe.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(Message m) {
            System.out.println("Response: " + m.read(Message.RESPONSE));
        }
    }
}
