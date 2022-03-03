package com.xxx.test.dlna;

import android.util.Log;

import com.xxx.test.upnp.upnp.ControlPoint;
import com.xxx.test.upnp.upnp.Device;
import com.xxx.test.upnp.upnp.device.DeviceChangeListener;
import com.xxx.test.upnp.upnp.device.SearchResponseListener;
import com.xxx.test.upnp.upnp.ssdp.SSDPPacket;

import java.util.ArrayList;
import java.util.List;

public class DMC {
    private static final String TAG = "test_DMC";
    private ControlPoint mControlPoint = null;
    private List<Device> deviceList = new ArrayList<>();

    private SearchResponseListener mSearchResponseListener = new SearchResponseListener(){

        @Override
        public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
            Log.i(TAG,"deviceSearchResponseReceived ssdpPacket:" + ssdpPacket.toString());
            ssdpPacket.getLocation();
        }
    };

    private DeviceChangeListener mDeviceChangeListener = new DeviceChangeListener(){

        @Override
        public void deviceAdded(Device device) {
            if ("urn:schemas-upnp-org:device:MediaRenderer:1".equals(device.getDeviceType())) {
                deviceList.remove(device);
                Log.i(TAG,"deviceAdded name:" + device.getFriendlyName() + ", ip:" + device.toString());
            }
        }

        @Override
        public void deviceRemoved(Device device) {
            if ("urn:schemas-upnp-org:device:MediaRenderer:1".equals(device.getDeviceType())) {
                deviceList.remove(device);
                Log.i(TAG,"deviceRemoved name:" + device.getFriendlyName() + ", ip:" + device.toString());
            }
        }
    };

    public DMC(){
        mControlPoint = new ControlPoint();
        mControlPoint.addDeviceChangeListener(mDeviceChangeListener);
        mControlPoint.addSearchResponseListener(mSearchResponseListener);
    }

    public void start(){
        Log.i(TAG,"startDMC");
        if(mControlPoint == null){
            return;
        }
        mControlPoint.stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mControlPoint.start();
                mControlPoint.search();
            }
        }).start();
    }
}

