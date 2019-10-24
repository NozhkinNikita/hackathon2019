package com.sb.wifistart.dto;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Device {
    private String id;
    private String model;
    private String osVersion;
    private String manufacturer;
    private String brand;
    private String deviceId;
    private String device;
    private String mac;
    private String ipV4;
    private String release;
    private String product;
    private String serial;
    private String user;
    private String host;

    public Device() {
        id = android.os.Build.ID;
        model = android.os.Build.MODEL;
        osVersion = System.getProperty("os.version");
        manufacturer = android.os.Build.MANUFACTURER;
        brand = android.os.Build.BRAND;
        device = android.os.Build.DEVICE;
        release = android.os.Build.VERSION.RELEASE;
        product = android.os.Build.PRODUCT;
        serial = android.os.Build.SERIAL;
        user = android.os.Build.USER;
        host = android.os.Build.HOST;
        mac = getMac("wlan0");
        ipV4 = getIpV4();
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public String getMac(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get IP address from first non-localhost interface
     * @return  address or empty string
     */
    public String getIpV4() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (isIPv4)
                            return sAddr;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
