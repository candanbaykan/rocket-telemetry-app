package com.candanbaykan;

import com.candanbaykan.exception.NoRocketException;
import com.candanbaykan.exception.RestConnectionException;
import com.candanbaykan.form.MainForm;
import com.candanbaykan.model.Rocket;
import com.candanbaykan.restclient.RocketRestClient;
import com.candanbaykan.tcpclient.TelemetryTcpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            setUiTheme();

            List<Rocket> rockets = Optional.ofNullable(getRocketsMetadata())
                    .orElseThrow(NoRocketException::new);

            if (rockets.isEmpty()) {
                throw new NoRocketException();
            }

            new MainForm(rockets);
        } catch (RestConnectionException | NoRocketException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Unexpected exception occurred!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(-1);
        }
    }

    private static List<Rocket> getRocketsMetadata() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RocketRestClient rocketRestClient = retrofit.create(RocketRestClient.class);

        Call<List<Rocket>> call = rocketRestClient.getAll();
        List<Rocket> rockets = null;
        for (int retry = 0; retry < 5; ++retry) {
            try {
                Response<List<Rocket>> response = call.execute();
                if (response.isSuccessful()) {
                    rockets = response.body();
                    break;
                }
            } catch (Exception e) {
                call = call.clone();

                if (retry == 4) {
                    throw new RestConnectionException();
                }
            }
        }
        return rockets;
    }

    private static void setUiTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            System.err.println("Warning: Couldn't get native look and feel.");
        }
    }
}