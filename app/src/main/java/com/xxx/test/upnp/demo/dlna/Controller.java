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
import com.xxx.test.upnp.demo.dlna.Device.IDeviceListener;
import com.xxx.test.upnp.demo.dlna.Device.Receiver;
import com.xxx.test.upnp.demo.dlna.Device.Searcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    public static final String TAG = "Controller";
    MyStreamSocket mySocket = null;
    private Receiver mReceiver = null;
    private Searcher mSearcher = null;
    private static final int WHAT_BROWSER_STOP = 100;
    public static final int DELAY_BROWSER_STOP = 10000; //搜索时长
    private List<String> mLocationList = new ArrayList<>();
    private List<DeviceBean> mDeviceList = new ArrayList<>();
    private Map<String, DeviceBean> deviceMap = new HashMap<String, DeviceBean>();
    private IDeviceListener mIDeviceListener;

    public void setDeviceListener(IDeviceListener listener) {
        mIDeviceListener = listener;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
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
            if (!mLocationList.contains(location)) {
                Log.i(TAG, "onLocationCallback location:" + location);
                mLocationList.add(location);
                DeviceManager.getInstance().getdevice(location);
            }
        }

        @Override
        public void onDeviceCallback(DeviceBean bean) {
            if (bean != null) {
                // contains 默认是用bean的equals判断，这个判断就过滤不了重复的设备了，所以DeviceBean重写一下equals
                if (!mDeviceList.contains(bean)) {
                    Log.i(TAG, "mDeviceList add:" + bean);
                    deviceMap.put(bean.uid, bean);
                    mDeviceList.add(bean);
                    mIDeviceListener.onUpdateDevices(mDeviceList);
                }
            }
        }
    };

    public void startService() {
        if (mSearcher == null) {
            mSearcher = new Searcher();
        }
        mSearcher.start();
    }

    public void stopService() {
        if (mSearcher != null) {
            mSearcher.stop();
            mSearcher = null;
        }

    }

    public void sartBrowse() {
        if (mReceiver == null) {
            mReceiver = new Receiver();
        }
        mReceiver.setBrowserListener(mBrowserListener);
        mReceiver.start();

        if (mSearcher == null) {
            mSearcher = new Searcher();
        }
        mSearcher.setBrowserListener(mBrowserListener);
        mSearcher.start();

        DeviceManager.getInstance().setDeviceListener(mBrowserListener);
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(WHAT_BROWSER_STOP, DELAY_BROWSER_STOP);
        }
    }

    public void stopBrowse() {
        if (mLocationList != null && mLocationList.size() > 0) {
            mLocationList.clear();
        }
        if (mReceiver != null) {
            mReceiver.stop();
            mReceiver = null;
        }
        if (mSearcher != null) {
            mSearcher.stop();
            mSearcher = null;
        }
    }

    public void setAVTransport(DeviceBean device, String url) {
        if (device == null || TextUtils.isEmpty(device.location)) {
            return;
        }
        try {
            URL location = new URL(device.location);
            setAVTransport(location.getHost(), location.getPort(), url);
        } catch (Exception e) {

        }
    }

    //获取服务信息
    public void setAVTransport(String ip, int port, String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "setAVTransport");
                String response;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    Log.i(TAG, "setAVTransport mySocket:" + mySocket);
                    if (mySocket == null) {
                        mySocket = new MyStreamSocket(ip, port);
                    }
                    mySocket.sendMessage(CmdUtils.getAVTransportPost(ip, port, url));
                    response = mySocket.receiveMessage();
//                    Log.i(TAG, "setAVTransport response1:" + response);
                    while (response != null) {
                        stringBuilder.append(response);
                        response = mySocket.receiveMessage();
                    }
                    Log.i(TAG, "setAVTransport response:" + stringBuilder);
                } catch (Exception e) {
                }
            }
        }).start();
    }

}
