package com.xxx.test.upnp.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xxx.test.dlna.DMC;
import com.xxx.test.upnp.demo.dlna.Controller;
import com.xxx.test.upnp.demo.dlna.Device.DeviceBean;
import com.xxx.test.upnp.demo.dlna.Device.IBrowserListener;
import com.xxx.test.upnp.demo.dlna.Device.IDeviceListener;
import com.xxx.test.upnp.demo.dlna.DeviceManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button searchBtn, listenerBtn,serviceShow,setUrlBtn;
    private ListView mListView;
    private List<DeviceBean> mLastList = new ArrayList();
    private DeviceAdapter mDeviceAdapter = null;
    private DeviceBean mSelectDevice;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Controller controller = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.search_id);
        searchBtn.setOnClickListener(listener);
        listenerBtn = findViewById(R.id.listen_id);
        listenerBtn.setOnClickListener(listener);
        serviceShow = findViewById(R.id.service_show);
        serviceShow.setOnClickListener(listener);
        setUrlBtn = findViewById(R.id.play);
        setUrlBtn.setOnClickListener(listener);
        checkPermission();
        initDeviceList();
        controller.setDeviceListener(mDeviceListener);
        // 被动收到设备广播
//        controller.startService();
    }

    private void initDeviceList(){
        mListView = findViewById(R.id.deviceList);
        if(mDeviceAdapter == null) {
            mDeviceAdapter = new DeviceAdapter(this.getApplicationContext(), mLastList);
        }
        TextView title = new TextView(this);
        title.setText("test");
        mListView.addHeaderView(title);
        mListView.setAdapter(mDeviceAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search_id:
                    // 主动搜索
                    controller.sartBrowse();
                    break;
                case R.id.listen_id:
                    // 监听广播
                    controller.startService();
                    break;
                case R.id.service_show:
                    // 获取服务信息
                    DeviceManager.getInstance().getdevice("");
                    break;
                case R.id.play:
                    // 播放url
                    if(controller != null){
                        controller.setAVTransport(mSelectDevice,PlayConfig.DEFAULT_URL_VIDEO2);
                    }
//                    try {
//                        URL location = new URL("http://192.168.31.16:49152/description.xml");
//                        controller.setAVTransport(location.getHost(), location.getPort(), "");
//                    }catch (Exception e){
//
//                    }
                    break;
            }
        }
    };



    private void checkPermission() {
        String[] permissionsCheck = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};

        List<String> permissionList = new ArrayList<>();

        for (String permissionStr : permissionsCheck) {
            if (ContextCompat.checkSelfPermission(this,
                    permissionStr) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissionStr);
            }
        }
        String[] permissionArr = new String[]{};
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(permissionArr), 100);
            return;
        }
    }

    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) {
            Log.w(TAG, "onRequestPermissionsResult failed requestCode: " + requestCode);
            finish();
            return;
        }
        if (grantResults.length <= 0) {
            Log.w(TAG, "onRequestPermissionsResult grantResults.length: " + grantResults.length);
            finish();
            return;
        }
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "onRequestPermissionsResult grantResults[0]: " + grantResults[0]);
            finish();
            return;
        }
        Log.i(TAG, "onRequestPermissionsResult 11grantResults[0]: " + grantResults[0]);
    }

    private IDeviceListener mDeviceListener = new IDeviceListener() {

        @Override
        public void onUpdateDevices(List<DeviceBean> infos) {
            Log.w(TAG,"onUpdateDevices size:" + infos.size());
            MainActivity.this.mHandler.post(new Runnable() {
                public void run() {
                    try {
                        if (mLastList != null) {
                            mLastList.clear();
                        }
                        if (infos != null) {
                            mLastList.addAll(infos);
                        }
                    } catch (Exception e) {
                        Log.w("mDeviceListener ", e);
                    }

                    if (mDeviceAdapter != null) {
                        Log.i(TAG,"notifyDataSetChanged ");
                        mDeviceAdapter.notifyDataSetChanged();
                    }

                }
            });
        }
    };

    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG,"onItemClick i:" + i);
            if(i == 0){
                return;
            }
            if(mListView!=null && mDeviceAdapter != null){
                mSelectDevice = mLastList.get(i-1);
                mDeviceAdapter.setSelectInfo(mSelectDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }
        }
    };

    // CyberGarage 源码测试
    private void testDmcBrower(){
        DMC mDMC = new DMC();
        mDMC.start();
    }
}