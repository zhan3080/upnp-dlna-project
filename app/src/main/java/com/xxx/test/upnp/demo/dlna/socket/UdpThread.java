package com.xxx.test.upnp.demo.dlna.socket;

import android.util.Log;

import com.xxx.test.upnp.demo.Constants;
import com.xxx.test.upnp.demo.Util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class UdpThread extends Thread {

    private static final String TAG = "UdpThread";

    private MulticastSocket mMulticastSocket;

    public static final String CRLF = "\r\n";
    private String getSearchString(){
        StringBuffer str = new StringBuffer();
        str.append("M-SEARCH * HTTP/1.1" + CRLF);
        str.append("ST: " + "urn:schemas-upnp-org:device:MediaRenderer:1" + CRLF);
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
        while (mMulticastSocket != null) {
            receive(mMulticastSocket);
            try {
                Thread.sleep(3000);
            }catch (Exception e){

            }

        }
    }

    // 不用搜索就能收到设备的广播
    public String receive(MulticastSocket socket) {
        //创建字节数组
        byte[] by = new byte[1024];
        //创建DatagramPacket对象,用来接收数据，以字节数组形式接收
        DatagramPacket packet = new DatagramPacket(by, by.length);
        if (socket != null) {
            try {
                if (packet != null) {
                    socket.receive(packet); // throws IOException
                }
            } catch (Exception e) {
                Log.w(TAG, e);
            }

        }

        Log.i(TAG, "received");
        //获得用户发送的数据
        byte[] data = packet.getData();
        //获得客户端发送过来数据的长度以及端口
        int length = packet.getLength();
        int port = packet.getPort();
        String str = new String(data, 0, length);
        Log.i(TAG, "receive str.lend:" + length + ", port:" + port + "\nstr:" + str);
        return str;
    }

    public UdpThread() {
        try {
            open(Constants.SSDP_ADDRESS,Constants.SSDP_PORT,InetAddress.getByName(Util.getIPAddress("wlan0")));
            Log.i(TAG, "UdpThread create success");
        } catch (Exception e) {
            Log.d(TAG, null, e);
        }
    }

    public boolean open(String addr, int port, InetAddress bindAddr) {
        try {
            mMulticastSocket = new MulticastSocket(null);
            mMulticastSocket.setReuseAddress(true);
            InetSocketAddress bindSockAddr = new InetSocketAddress(port);
            mMulticastSocket.bind(bindSockAddr);
            InetSocketAddress ssdpMultiGroup = new InetSocketAddress(InetAddress.getByName(addr), port);
            NetworkInterface ssdpMultiIf = NetworkInterface.getByInetAddress(bindAddr);
            mMulticastSocket.joinGroup(ssdpMultiGroup, ssdpMultiIf);
        } catch (Exception e) {
            Log.d(TAG, null, e);
            return false;
        }
        return true;
    }



}
