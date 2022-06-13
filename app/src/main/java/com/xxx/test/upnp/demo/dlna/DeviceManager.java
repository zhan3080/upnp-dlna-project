package com.xxx.test.upnp.demo.dlna;

import android.text.TextUtils;
import android.util.Log;

import com.xxx.test.upnp.demo.dlna.Device.DeviceBean;
import com.xxx.test.upnp.demo.dlna.Device.IBrowserListener;

import java.net.URL;

public class DeviceManager {
    private static final String TAG = "DeviceManager";

    private static DeviceManager sInstance = null;
    private IBrowserListener mBrowserListener = null;


    private DeviceManager() {
    }

    public static synchronized DeviceManager getInstance() {
        if (sInstance == null) {
            synchronized (DeviceManager.class) {
                if (sInstance == null) {
                    sInstance = new DeviceManager();
                }
            }
        }
        return sInstance;
    }

    public void setDeviceListener(IBrowserListener listener) {
        mBrowserListener = listener;
    }

    //获取服务信息
    public void getdevice(String locationUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    URL aa = new URL(locationUrl);
                    String ip = aa.getHost();
                    int port = aa.getPort();
                    DeviceBean deviceBean = null;
                    MyStreamSocket mySocket = new MyStreamSocket(aa.getHost(), aa.getPort());
//                    Log.i(TAG, "getdevice mySocket:" + mySocket);
                    mySocket.sendMessage(CmdUtils.getRequest(ip, port));
                    response = mySocket.receiveMessage();
//                    Log.i(TAG, "receive1:" + response);

                    while (response != null) {
                        Log.i(TAG, "receive:" + response);//打印服务器响应信息。包括状态行。头部内容。空行。web对象内容
                        if (response.startsWith("<deviceType>") && response.contains("urn:schemas-upnp-org:device:MediaRenderer")) {
                            Log.i(TAG, "receive create deviceBean");
                            deviceBean = new DeviceBean();
                            deviceBean.ip = ip;
                            deviceBean.port = port;
                        }
                        if (deviceBean != null && response.startsWith("<friendlyName>")) {
                            deviceBean.name = parseSubfix(response, "<friendlyName>", "</friendlyName>");
                            Log.i(TAG, "receive  deviceBean.name:" + deviceBean.name);
                        }
                        if (deviceBean != null && response.startsWith("<UDN>uuid:")) {
                            deviceBean.uid = parseSubfix(response, "<UDN>uuid:", "</UDN>");
                            Log.i(TAG, "receive  deviceBean.uid:" + deviceBean.uid);
                        }
                        if(deviceBean != null
                                &&!TextUtils.isEmpty(deviceBean.uid)
                                &&!TextUtils.isEmpty(deviceBean.name)){
                            deviceBean.location = locationUrl;
//                            Log.i(TAG, "receive deviceBean:" + deviceBean);
                            mBrowserListener.onDeviceCallback(deviceBean);
                            return;
                        }
                        response = mySocket.receiveMessage();

                    }
//                    Log.i(TAG, "receive stringBuilder:" + stringBuilder);
                } catch (Exception e) {
                }
            }
        }).start();
        return;
    }

    public String parseSubfix(String source, String start, String end) {
        String str = source.substring(start.length(), source.indexOf(end));
//        Log.i(TAG, "parseSubfix str:" + str);
        return str;
    }


}
