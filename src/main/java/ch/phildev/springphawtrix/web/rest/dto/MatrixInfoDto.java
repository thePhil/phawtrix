package ch.phildev.springphawtrix.web.rest.dto;

import java.net.InetAddress;

import lombok.Data;

@Data
public class MatrixInfoDto {
    private String type;
    private String version;
    private int wifirssi;
    private int wifiquality;
    private String wifissid;
    private InetAddress ip;
    private int lux;
    private double Temp;
    private double Hum;
    private double hPa;

}
