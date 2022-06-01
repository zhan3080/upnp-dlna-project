package com.xxx.test.upnp.demo.dlna.Device;

import android.text.TextUtils;
import android.util.Log;

import com.xxx.test.upnp.demo.DlnaCmd;
import com.xxx.test.upnp.demo.Util;
import com.xxx.test.upnp.demo.parser.PackageParser;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class Searcher {

    private static final String TAG = "Searcher";
    private MulticastSocket mMulticastSocket;
    private IBrowserListener mBrowserListener;

    // 不用搜索就能收到设备的广播
    public String receive(MulticastSocket socket) {
        //创建字节数组
        byte[] by = new byte[1024];
        //创建DatagramPacket对象,用来接收数据，以字节数组形式接收
        DatagramPacket packet = new DatagramPacket(by, by.length);
        if (socket != null && !socket.isClosed()) {
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
        String location = PackageParser.getLocation(str);
        Log.i(TAG, "receive location:" + location);
        if(!TextUtils.isEmpty(location) && mBrowserListener != null){
            Log.i(TAG, "receive location--:" + location);
            mBrowserListener.onLocationCallback(location);
        }
        return str;
    }

    public void setBrowserListener(IBrowserListener listener){
        mBrowserListener = listener;
    }

    public void start(){
        try {
            start(DlnaCmd.SSDP_ADDRESS, DlnaCmd.SSDP_PORT, InetAddress.getByName(Util.getIPAddress("wlan0")));
        }catch (Exception e){

        }
    }

    // 开始监听组播
    public boolean start(String addr, int port, InetAddress bindAddr) {
        try {
            // 创建MulticastSocket用于接收多播包
            mMulticastSocket = new MulticastSocket(null);
            mMulticastSocket.setReuseAddress(true);
            // 绑定监听多播端口
            InetSocketAddress bindSockAddr = new InetSocketAddress(port);
            mMulticastSocket.bind(bindSockAddr);
            // 把ssdpMultiGroup加入到组播里
            InetSocketAddress ssdpMultiGroup = new InetSocketAddress(InetAddress.getByName(addr), port);
            NetworkInterface ssdpMultiIf = NetworkInterface.getByInetAddress(bindAddr);
            mMulticastSocket.joinGroup(ssdpMultiGroup, ssdpMultiIf);
            // 这样也行了
//            mMulticastSocket = new MulticastSocket(port);
//            mMulticastSocket.setReuseAddress(true);
//            mMulticastSocket.joinGroup(InetAddress.getByName(addr));
        } catch (Exception e) {
            Log.d(TAG, null, e);
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMulticastSocket != null) {
                    receive(mMulticastSocket);
                    try {
                        Thread.sleep(3000);
                    }catch (Exception e){

                    }

                }
            }
        }).start();
        return true;
    }

    public void stop(){
        if(mMulticastSocket != null) {
            mMulticastSocket.close();
            mMulticastSocket = null;
        }
    }

}
