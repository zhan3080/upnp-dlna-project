package com.xxx.test.upnp.demo.dlna;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xxx.test.upnp.demo.DlnaCmd;
import com.xxx.test.upnp.demo.dlna.Device.DeviceBean;
import com.xxx.test.upnp.demo.dlna.Device.IBrowserListener;
import com.xxx.test.upnp.demo.dlna.Device.Receiver;
import com.xxx.test.upnp.demo.dlna.Device.Searcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public static final String TAG = "Controller";
    public static final String CRLF = "\r\n";
    MyStreamSocket mySocket = null;
    private Receiver mReceiver = null;
    private Searcher mSearcher = null;
    private static final int WHAT_BROWSER_STOP = 100;
    public static final int DELAY_BROWSER_STOP = 10000; //搜索时长
    private List<String> mLocationList = new ArrayList<>();
    private List<DeviceBean> mDeviceList = new ArrayList<>();

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case WHAT_BROWSER_STOP:
                    stopBrowse();
                    break;
            }
            return false;
        }
    });

    private IBrowserListener mBrowserListener = new IBrowserListener() {
        @Override
        public void onLocationCallback(String location) {
            if(!mLocationList.contains(location)){
                Log.i(TAG,"onLocationCallback location:" + location);
                mLocationList.add(location);
                DeviceManager.getInstance().getdevice(location);
            }
        }

        @Override
        public void onDeviceCallback(DeviceBean bean) {
            if(bean != null){
                Log.i(TAG,"onDeviceCallback device:" + bean.toString());
                if(!mDeviceList.contains(bean)){
                    mDeviceList.add(bean);
                }
            }
        }
    };

    public void startService(){
        if(mSearcher == null){
            mSearcher = new Searcher();
        }
        mSearcher.start();
    }

    public void stopService(){
        if(mSearcher != null){
            mSearcher.stop();
            mSearcher = null;
        }

    }

    public void sartBrowse(){
        if(mReceiver == null){
            mReceiver = new Receiver();
        }
        mReceiver.setBrowserListener(mBrowserListener);
        mReceiver.start();

        if(mSearcher == null){
            mSearcher = new Searcher();
        }
        mSearcher.setBrowserListener(mBrowserListener);
        mSearcher.start();

        DeviceManager.getInstance().setDeviceListener(mBrowserListener);
        if(mHandler != null){
            mHandler.sendEmptyMessageDelayed(WHAT_BROWSER_STOP,DELAY_BROWSER_STOP);
        }
    }

    public void stopBrowse(){
        if(mLocationList != null && mLocationList.size()>0){
            mLocationList.clear();
        }
        if(mReceiver != null){
            mReceiver.stop();
            mReceiver = null;
        }
        if(mSearcher != null){
            mSearcher.stop();
            mSearcher = null;
        }
    }



//    POST /AVTransport/9c443d47158b-dmr/control.xml HTTP/1.1
//    HOST: 10.2.9.152
//    Content-Type: text/xml; charset="utf-8"
//    SOAPAction: "urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI"
//    <?xml version="1.0"?>
//  <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
//  <s:Body>
//    <u:SetAVTransportURI xmlns:u="urn:schemas-upnp-org:service:AVTransport:1">
//      <InstanceID>0</InstanceID>
//      <CurrentURI>yourAVURI</CurrentURI>
//    </u:SetAVTransportURI>
//  </s:Body>
//</s:Envelope>

    private String AVTransportBody = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
            "  <s:Body>\n" +
            "    <u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
            "      <InstanceID>0</InstanceID>\n" +
            "      <CurrentURI>http://video.hpplay.cn/demo/aom.mp4</CurrentURI>\n" +
            "    </u:SetAVTransportURI>\n" +
            "  </s:Body>\n" +
            "</s:Envelope>";
    private String getAVTransportPost(String host, int port){
        StringBuffer str = new StringBuffer();
        str.append(" POST /_urn:schemas-upnp-org:service:AVTransport_control HTTP/1.1" + CRLF);
        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
        str.append("HOST: " + host + ":" + port + CRLF);
        str.append("CONTENT-LENGTH: " + AVTransportBody.getBytes().length + CRLF);
        str.append("Content-Type: text/xml; charset=\"utf-8\"" + CRLF);
        str.append("SOAPAction: \"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\"" + CRLF);
        str.append(CRLF);
        str.append(AVTransportBody);
        String content = str.toString();
        return content;
    }


    //获取服务信息
    public void setAVTransport(String ip, int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"setAVTransport");
                String response;
                StringBuilder stringBuilder = new StringBuilder();
                try{
                    Log.i(TAG,"setAVTransport mySocket:" + mySocket);
                    if(mySocket == null) {
                        mySocket = new MyStreamSocket(ip, port);
                    }
                    Log.i(TAG,"setAVTransport mySocket11:" + mySocket);
                    mySocket.sendMessage(getAVTransportPost(ip,port));
                    Log.i(TAG,"setAVTransport mySocket1111:" + mySocket);
                    response = mySocket.receiveMessage();
                    Log.i(TAG,"setAVTransport response1:" + response);
                    while (response != null) {
                        stringBuilder.append(response);
                        response = mySocket.receiveMessage();
                    }
                    Log.i(TAG,"setAVTransport response:" + stringBuilder);
                } catch (Exception e) {
                }
            }
        }).start();
    }

}
