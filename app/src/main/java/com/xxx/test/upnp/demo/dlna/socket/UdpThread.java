package com.xxx.test.upnp.demo.dlna.socket;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class UdpThread extends Thread {

    private static final String TAG = "UdpThread";
    public static final int PORT = 1900;

    /**
     * Default IPv4 multicast address for SSDP messages
     */
    public static final String ADDRESS = "239.255.255.250";

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

    private DatagramSocket mSendSocket;
    private MulticastSocket mMulticastSocket;


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

    // 不用搜索就能收到别都设备的广播
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

    public boolean post() {
        return post(ADDRESS,PORT,getSearchString());
    }

    public boolean post(String addr, int port, String msg) {
        if(mMulticastSocket == null){
            Log.i(TAG,"post fail mMulticastSocket is null");
        }
        try {
            InetAddress inetAddr = InetAddress.getByName(addr);
            DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), inetAddr, port);
            mMulticastSocket.send(dgmPacket);
        } catch (Exception e) {
            Log.d(TAG, null, e);
            return false;
        }
        Log.i(TAG,"post success msg:" + msg);
        return true;
    }

    public UdpThread() {


        try {
            open("239.255.255.250",1900,InetAddress.getByName("192.168.31.243"));
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

    public final static String getHostAddress() {
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = (InetAddress) addrs.nextElement();
                    String host = addr.getHostAddress();
                    Log.i(TAG, "getHostAddress host:" + host);
//                    return host;
                }
            }
        } catch (Exception e) {

        }
        return "";
    }



}
