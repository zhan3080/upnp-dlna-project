package com.xxx.test.upnp.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xxx.test.dlna.DMC;
import com.xxx.test.upnp.demo.dlna.search.SearchThread;
import com.xxx.test.upnp.demo.dlna.socket.DataThread;
import com.xxx.test.upnp.demo.dlna.socket.UdpThread;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button searchBtn;
    private UdpThread mUdnThread;
    private DataThread mdataThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.search_id);
        searchBtn.setOnClickListener(listener);
        checkPermission();
        // 被动收到设备广播
        mUdnThread = new UdpThread();
        mUdnThread.start();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search_id:
//                    sendContent(SEARCH_CONTENT);
//                    getdevice();
//                    testDmcBrower();
                    // 主动搜索
                    SearchThread searchThread = new SearchThread();
                    searchThread.start();
                    searchThread.startSearch();
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

    private void sendContent(String content){
        if(mUdnThread == null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUdnThread.post(content);
            }
        }).start();
    }
    private void getdevice(){
        if(mdataThread == null) {
            mdataThread = new DataThread();
            mdataThread.open("192.168.31.120", 12344);
            mdataThread.start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mdataThread.requestDescription("192.168.31.201",49154,"GET_DESCRIPTION");
            }
        }).start();

    }

    // CyberGarage 源码测试
    private void testDmcBrower(){
        DMC mDMC = new DMC();
        mDMC.start();
    }
}