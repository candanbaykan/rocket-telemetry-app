package com.candanbaykan.form;

import com.candanbaykan.model.Rocket;
import com.candanbaykan.model.Telemetry;
import com.candanbaykan.tcpclient.TelemetryTcpClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

public class MainForm extends JFrame {
    private JPanel panel;
    private JTable tblTelemetry;

    private List<Rocket> rockets;

    public MainForm(List<Rocket> rockets) {
        this.rockets = rockets;

        add(panel);

        String[] columns = {"Id", "Altitude", "Speed", "Acceleration", "Thrust", "Temperature", "Connection"};
        String[][] data = new String[rockets.size()][];

        for (int i = 0; i < rockets.size(); ++i) {
            data[i] = new String[columns.length];
            data[i][0] = rockets.get(i).getId();
            data[i][1] = rockets.get(i).getAltitude().toString();
            data[i][2] = rockets.get(i).getSpeed().toString();
            data[i][3] = rockets.get(i).getAcceleration().toString();
            data[i][4] = rockets.get(i).getThrust().toString();
            data[i][5] = rockets.get(i).getTemperature().toString();
            data[i][6] = "Down";
        }

        DefaultTableModel model = new DefaultTableModel(data, columns);
        tblTelemetry.setModel(model);
        tblTelemetry.getTableHeader().setReorderingAllowed(false);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Rocket Telemetry App");
        setVisible(true);
        startBackgroundThreads(model);
    }

    private void startBackgroundThreads(DefaultTableModel model) {
        for (int i = 0; i < rockets.size(); ++i) {
            int finalI = i;
            new Thread(() -> {
                Telemetry telemetry = rockets.get(finalI).getTelemetry();
                try {
                    TelemetryTcpClient client = new TelemetryTcpClient(telemetry.getHost(), telemetry.getPort());
                    while (true) {
                        Rocket rocket = client.getTelemetry();
                        if (nonNull(rocket)) {
                            model.setValueAt(rocket.getAltitude().toString(), finalI, 1);
                            model.setValueAt(rocket.getSpeed().toString(), finalI, 2);
                            model.setValueAt(rocket.getAcceleration().toString(), finalI, 3);
                            model.setValueAt(rocket.getThrust().toString(), finalI, 4);
                            model.setValueAt(rocket.getTemperature().toString(), finalI, 5);
                            model.setValueAt("Up", finalI, 6);
                        } else {
                            model.setValueAt("Down", finalI, 6);
                            Thread.sleep(1000);
                        }
                    }
                } catch (IOException e) {
                    model.setValueAt("Failed", finalI, 6);
                    System.err.printf("Error: Connection to %s failed!\n", rockets.get(finalI).getId());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
