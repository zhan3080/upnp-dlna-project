package com.xxx.test.upnp.demo.dlna.search;

import android.util.Log;

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

    private static final String DMR_ST = "urn:schemas-upnp-org:device:MediaRenderer:1";
    private static final String ROOTDEVICE_ST = "upnp:rootdevice";
    private String mSearchString = null;

    /********************  forTest ********************/
//    private static String SOURCE_IP = "192.168.31.120";
    private static String SOURCE_IP = "192.168.0.13";
    private static int SOURCE_PORT = 16080;
    private static String LOCATION = "http://192.168.0.12:49152/description.xml";

    public static final String CRLF = "\r\n";

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
        while (mDatagramSocket != null && !mDatagramSocket.isClosed()) {
            try {
                Log.i(TAG, "SearchThread receive " + mDatagramSocket.isClosed());
                mDatagramSocket.receive(mDatagramPakcet);
            } catch (Exception e) {

            }
            Log.i(TAG, "SearchThread mDatagramPakcet.getData len " + mDatagramPakcet.getLength());
            if (mDatagramPakcet != null && mDatagramPakcet.getData().length > 0) {
                String packetData = new String(mDatagramPakcet.getData(), 0, mDatagramPakcet.getLength());
                Log.i(TAG, "receive packetData string:" + packetData);
//                XmlParser.parserSSDP(mDatagramPakcet.getData());
                XmlParser.getLocation(packetData);
            }
        }
    }

    public SearchThread() {
        Log.i(TAG, "SearchThread");
        try {
            InetSocketAddress bindInetAddr = new InetSocketAddress(SOURCE_IP, SOURCE_PORT);
            mDatagramSocket = new DatagramSocket(bindInetAddr);
            Log.i(TAG, "SearchThread mDatagramSocket:" + mDatagramSocket);
        } catch (Exception e) {
            Log.i(TAG, "SearchThread e:" + e);
        }
    }


    public void startSearch() {
        Log.i(TAG, "startSearch ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDatagramSocket != null) {
                    try {
                        InetAddress inetAddr = InetAddress.getByName("239.255.255.250");
//                        mSearchString = getSearchString(ROOTDEVICE_ST);
//                        DatagramPacket packet = new DatagramPacket(mSearchString.getBytes(), mSearchString.length(),inetAddr,1900);
//                        Log.i(TAG, "startSearch send:\n" + mSearchString);
//                        mDatagramSocket.send(packet);
//                        sleep(100);
                        mSearchString = getSearchString(DMR_ST);
                        DatagramPacket packet1 = new DatagramPacket(mSearchString.getBytes(), mSearchString.length(),inetAddr,1900);
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
