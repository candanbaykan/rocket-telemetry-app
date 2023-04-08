package com.candanbaykan.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Rocket {
    private String id;
    private Telemetry telemetry;
    private Float altitude;
    private Float speed;
    private Float acceleration;
    private Float thrust;
    private Float temperature;

    public Rocket() {
    }

    public Rocket(byte[] bytes) {
        if (bytes.length == 36) {
            this.id = new String(Arrays.copyOfRange(bytes, 1, 11), StandardCharsets.UTF_8);

            this.altitude = ByteBuffer
                    .wrap(Arrays.copyOfRange(bytes, 13, 17))
                    .order(ByteOrder.BIG_ENDIAN)
                    .getFloat();

            this.speed = ByteBuffer
                    .wrap(Arrays.copyOfRange(bytes, 17, 21))
                    .order(ByteOrder.BIG_ENDIAN)
                    .getFloat();

            this.acceleration = ByteBuffer
                    .wrap(Arrays.copyOfRange(bytes, 21, 25))
                    .order(ByteOrder.BIG_ENDIAN)
                    .getFloat();

            this.thrust = ByteBuffer
                    .wrap(Arrays.copyOfRange(bytes, 25, 29))
                    .order(ByteOrder.BIG_ENDIAN)
                    .getFloat();

            this.temperature = ByteBuffer
                    .wrap(Arrays.copyOfRange(bytes, 29, 33)).
                    order(ByteOrder.BIG_ENDIAN)
                    .getFloat();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public Float getAltitude() {
        return altitude;
    }

    public void setAltitude(Float altitude) {
        this.altitude = altitude;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Float acceleration) {
        this.acceleration = acceleration;
    }

    public Float getThrust() {
        return thrust;
    }

    public void setThrust(Float thrust) {
        this.thrust = thrust;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }
}
