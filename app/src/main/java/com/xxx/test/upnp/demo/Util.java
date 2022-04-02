package com.xxx.test.upnp.demo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static final String TAG = "Util";

    public static String getIPAddress(String netType) {
        Log.i(TAG, "getIPAddress netType:" + netType);
        Map<String, String> ipMap = new HashMap<>();
        try {
            // 在有些设备上wifi和有线同时存在，获得的ip会有两个
            Enumeration<NetworkInterface> en = null;
            try {
                en = NetworkInterface.getNetworkInterfaces();
            } catch (Exception e) {
                Log.w(TAG, e);
            }

            while (en.hasMoreElements()) {
                NetworkInterface element = en.nextElement();
                Enumeration<InetAddress> inetAddresses = element.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        String ip = inetAddress.getHostAddress();
                        String displayName = element.getDisplayName();

                        Log.i(TAG, "getIPAddress getIPAddress: " + ip + " name:" + displayName);
                        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(displayName)) {
                            continue;
                        }
                        if (displayName.equals(netType)) {
                            return ip;
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return "";
    }


}