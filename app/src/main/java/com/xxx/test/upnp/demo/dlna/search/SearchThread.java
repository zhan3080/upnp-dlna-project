package com.xxx.test.upnp.demo.dlna.search;

import android.util.Log;

import com.xxx.test.upnp.demo.Constants;
import com.xxx.test.upnp.demo.Util;
import com.xxx.test.upnp.demo.parser.XmlParser;

import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class SearchThread extends Thread {
    private static final String TAG = "SearchThread";
    private DatagramSocket mDatagramSocket;
    private byte[] buf = new byte[1024];
    private DatagramPacket mDatagramPakcet = new DatagramPacket(buf, 1024);
    private String mSearchString = null;

    /********************  forTest ********************/
    private static int LOCAL_PORT = 16080;
    public static final String CRLF = "\r\n";

    //搜索用的命令
    private String getSearchString(String st){
        StringBuffer str = new StringBuffer();
        str.append("M-SEARCH * HTTP/1.1" + CRLF);
        str.append("ST: " + st + CRLF);
        str.append("MX: 3" + CRLF);
        str.append("MAN: \"ssdp:discover\"" + CRLF);
        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
        str.append("HOST: 239.255.255.250:1900" + CRLF);
        str.append(CRLF);
        String content = str.toString();
        return content;
    }

    @Override
    public void run() {
        super.run();
        //mDatagramSocket 用来接收搜索设备的回应
        while (mDatagramSocket != null && !mDatagramSocket.isClosed()) {
            try {
                Log.i(TAG, "SearchThread receive " + mDatagramSocket.isClosed());
                mDatagramSocket.receive(mDatagramPakcet);
            } catch (Exception e) {

            }
            Log.i(TAG, "SearchThread mDatagramPakcet.getData len " + mDatagramPakcet.getLength());
            //收到回应后解析xml得到服务信息
            if (mDatagramPakcet != null && mDatagramPakcet.getData().length > 0) {
                String packetData = new String(mDatagramPakcet.getData(), 0, mDatagramPakcet.getLength());
                Log.i(TAG, "receive packetData string:" + packetData);
//                XmlParser.parserSSDP(mDatagramPakcet.getData());
                XmlParser.getLocation(packetData);
            }
        }
    }

    //开始先建立本地监听mDatagramSocket
    public SearchThread() {
        Log.i(TAG, "SearchThread localIp:" + Util.getIPAddress("wlan0"));
        try {
            InetSocketAddress bindInetAddr = new InetSocketAddress(Util.getIPAddress("wlan0"), LOCAL_PORT);
            mDatagramSocket = new DatagramSocket(bindInetAddr);
            Log.i(TAG, "SearchThread mDatagramSocket:" + mDatagramSocket);
        } catch (Exception e) {
            Log.i(TAG, "SearchThread e:" + e);
        }
    }

    //主动往SSDP_ADDRESS SSDP_PORT发广播消息（M-SEARCH），本地先要建立mDatagramSocket监听
    public void startSearch() {
        Log.i(TAG, "startSearch ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDatagramSocket != null) {
                    try {
                        InetAddress inetAddr = InetAddress.getByName(Constants.SSDP_ADDRESS);
//                        mSearchString = getSearchString(Constants.ROOTDEVICE_ST);
//                        DatagramPacket packet = new DatagramPacket(mSearchString.getBytes(), mSearchString.length(),inetAddr,1900);
//                        Log.i(TAG, "startSearch send:\n" + mSearchString);
//                        mDatagramSocket.send(packet);
//                        sleep(100);
                        mSearchString = getSearchString(Constants.DMR_ST);
                        DatagramPacket packet1 = new DatagramPacket(mSearchString.getBytes(), mSearchString.length(),inetAddr,Constants.SSDP_PORT);
                        Log.i(TAG, "startSearch send1:\n" + mSearchString);
                        mDatagramSocket.send(packet1);
                        Log.i(TAG, "startSearch send end");
                    } catch (Exception e) {

                    }
                }

            }
        }).start();

    }
}
