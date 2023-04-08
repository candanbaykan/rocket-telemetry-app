package com.candanbaykan.tcpclient;

import com.candanbaykan.model.Rocket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TelemetryTcpClient {
    private String host;
    private Integer port;
    private Socket socket;
    private DataInputStream inputStream;
    private boolean isConnected;
    private int retry;

    public TelemetryTcpClient(String host, Integer port) throws IOException {
        this.host = host;
        this.port = port;
        this.retry = 0;
        this.connect();
    }

    public Rocket getTelemetry() throws IOException {
        try {
            if (!isConnected) {
                this.connect();
            }

            byte[] buffer = new byte[36];
            int count = this.inputStream.read(buffer, 0, 36);

            if (count == -1) {
                throw new SocketTimeoutException();
            }

            retry = 0;
            return new Rocket(buffer);
        } catch (SocketTimeoutException e) {
            ++retry;
            if (retry == 5) {
                this.isConnected = false;
                throw new IOException();
            }
        }
        return null;
    }

    private void connect() throws IOException {
        this.socket = new Socket(this.host, this.port);
        this.socket.setSoTimeout(2000);
        this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.isConnected = true;
    }
}
