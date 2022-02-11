package com.xxx.test.upnp.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xxx.test.upnp.demo.socket.UdpThread;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button searchBtn;
    private UdpThread mUdnThread;
    public static final String CRLF = "\r\n";
    private static final String SEARCH_CONTENT = "SEARCH * HTTP/1.1\n" +
            "HOST: 239.255.255.250:1900\n" +
            "MAN: \"ssdp:discover\"\n" +
            "MX: 6\n" +
            "ST: urn:schemas-upnp-org:device:MediaRenderer:1" +
            CRLF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.search_id);
        searchBtn.setOnClickListener(listener);
        mUdnThread = new UdpThread();
        mUdnThread.start();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search_id:
                    sendContent(SEARCH_CONTENT);
                    break;
            }
        }
    };

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
}