package com.xxx.test.upnp.demo.dlna.Device;

import android.text.TextUtils;
import android.util.Log;

import com.xxx.test.upnp.demo.Util;
import com.xxx.test.upnp.demo.dlna.CmdUtils;
import com.xxx.test.upnp.demo.parser.PackageParser;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import static java.lang.Thread.sleep;

public class Receiver{
    private static final String TAG = "Receiver";
    private DatagramSocket mDatagramSocket;
    private static boolean mIsListening = false;
    private byte[] buf = new byte[1024];
    private DatagramPacket mDatagramPakcet = new DatagramPacket(buf, 1024);
    private String mSearchString = null;
    private IBrowserListener mBrowserListener;

    /********************  forTest ********************/
    private static int LOCAL_PORT = 16080;

    public void setBrowserListener(IBrowserListener listener){
        mBrowserListener = listener;
    }

    public void start(){
        startListener();
        startSearch();
    }

    public void startListener() {
        Log.i(TAG, "startListener");
        // 开始先建立本地监听mDatagramSocket
        if(mDatagramSocket == null){
            try {
                InetSocketAddress bindInetAddr = new InetSocketAddress(Util.getIPAddress("wlan0"), LOCAL_PORT);
                mDatagramSocket = new DatagramSocket(bindInetAddr);
                Log.i(TAG, "startListener mDatagramSocket:" + mDatagramSocket);
            } catch (Exception e) {
                Log.i(TAG, "startListener e:" + e);
                return;
            }
        }

        if(mIsListening){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mDatagramSocket != null && !mDatagramSocket.isClosed()) {
                    mIsListening = true;
                    try {
                        Log.i(TAG, "startListener receive " + mDatagramSocket.isClosed());
                        mDatagramSocket.receive(mDatagramPakcet);
                    } catch (Exception e) {

                    }
                    Log.i(TAG, "startListener mDatagramPakcet.getData len " + mDatagramPakcet.getLength());
                    //收到回应后解析xml得到服务信息
                    if (mDatagramPakcet != null && mDatagramPakcet.getData().length > 0) {
                        String packetData = new String(mDatagramPakcet.getData(), 0, mDatagramPakcet.getLength());
                        Log.i(TAG, "startListener packetData string:" + packetData);
//                XmlParser.parserSSDP(mDatagramPakcet.getData());
                        String location = PackageParser.getLocation(packetData);
                        Log.i(TAG, "startListener location:" + location);
                        if(!TextUtils.isEmpty(location) && mBrowserListener != null){
                            mBrowserListener.onLocationCallback(location);
                        }
                    }
                }
                mIsListening = false;
            }
        }).start();
    }

    //主动往SSDP_ADDRESS SSDP_PORT发广播消息（M-SEARCH），本地先要建立mDatagramSocket监听
    public void startSearch() {
        Log.i(TAG, "startSearch ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDatagramSocket != null) {
                    try {
                        InetAddress inetAddr = InetAddress.getByName(CmdUtils.SSDP_ADDRESS);
//                        mSearchString = DlnaCmd.getSearchString(DlnaCmd.ROOTDEVICE_ST);
//                        DatagramPacket packet = new DatagramPacket(mSearchString.getBytes(), mSearchString.length(),inetAddr,1900);
//                        Log.i(TAG, "startSearch send:\n" + mSearchString);
//                        mDatagramSocket.send(packet);
//                        sleep(100);
                        mSearchString = CmdUtils.getSearchString(CmdUtils.DMR_ST);
                        DatagramPacket packet1 = new DatagramPacket(mSearchString.getBytes(), mSearchString.length(),inetAddr,CmdUtils.SSDP_PORT);
                        Log.i(TAG, "startSearch send1:\n" + mSearchString);
                        mDatagramSocket.send(packet1);

                        // 发两次，保证搜索成功率
                        sleep(100);
                        mDatagramSocket.send(packet1);
                        Log.i(TAG, "startSearch send end");
                    } catch (Exception e) {

                    }
                }

            }
        }).start();

    }

    public void stop(){
        mIsListening = false;
        if(mDatagramSocket!=null){
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }


}
