package com.example.trihan.drone_app_android;

/**
 * Created by trihan on 7/19/16.
 *
 * Message contains the data that is shuttled between App and Server
 */
public class Message {
    private String command;

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String aCommand) {
        command = aCommand;
    }
}
