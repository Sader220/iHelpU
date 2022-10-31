package com.example.ihelpu.tools.mouse;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class  RegisterMouse extends AsyncTask<String, Void, Void> {
    Socket socket;
    DataOutputStream dos;
    PrintWriter printWriter;

    @Override
    protected Void doInBackground(String... voids) {
        String message = voids[0];
        try {
            socket = new Socket("192.168.1.100", 6000);

            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write(message);
            printWriter.flush();
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}