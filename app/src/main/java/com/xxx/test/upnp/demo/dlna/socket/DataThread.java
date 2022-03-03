package com.xxx.test.upnp.demo.dlna.socket;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class DataThread extends Thread{

    private static final String TAG = "DataThread";
    private DatagramSocket mPackageSocket;
    private DatagramPacket mDatagramPacket;
    @Override
    public void run() {
        while (true){
            receiveTargetDescription();
            try {
                sleep(2000);
            }catch (Exception e){

            }

        }
    }


    public boolean open(String bindAddr, int bindPort) {
        try {
            InetSocketAddress bindInetAddr = new InetSocketAddress(bindAddr, bindPort);
            mPackageSocket = new DatagramSocket(bindInetAddr);
        } catch (Exception e) {
            Log.d(TAG, null, e);
        }
        return true;
    }

    public boolean requestDescription(String addr, int port, String msg){
        Log.i(TAG,"requestDescription addr:" + addr + ", port:" + port + ",msg:" + msg);
        try {
            InetAddress inetAddr = InetAddress.getByName(addr);
            DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), inetAddr, port);
            mPackageSocket.send(dgmPacket);
        } catch (Exception e) {
            Log.d(TAG, null, e);
            return false;
        }
        return true;
    }

    public String receiveTargetDescription() {
        String content = null;
        if(mPackageSocket != null){
            try {
                mPackageSocket.receive(mDatagramPacket);
            }catch (Exception e){

            }
        }
        if(mDatagramPacket != null){
            content = new String(mDatagramPacket.getData());
        }
        Log.i(TAG,"receiveTargetDescription content:" + content);
        return content;
    }

}
