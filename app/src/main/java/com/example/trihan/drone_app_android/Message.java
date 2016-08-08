package com.example.trihan.drone_app_android;

/**
 * Created by trihan on 7/19/16.
 *
 * Message contains the data that is shuttled between App and Server
 */
public class Message {

    public static final Integer COMMAND = 1;
    public static final Integer RESPONSE = 2;

    private String command;
    private String response;

    public Message() {
        // no-args constructor
    }

    public String read(int section) {
        switch(section) {
            case 1:
                return this.command;
            case 2:
                return this.response;
            default:
                return "Not a valid section";
        }
    }

    public void write(int section, String text) {
        switch (section) {
            case 1:
                command = text;
                break;
            case 2:
                this.response = text;
                break;
            default:
                break;
        }
    }
}
