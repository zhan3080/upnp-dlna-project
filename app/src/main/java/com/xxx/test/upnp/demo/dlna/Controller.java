package com.xxx.test.upnp.demo.dlna;

import android.util.Log;

import com.xxx.test.upnp.demo.dlna.search.SearchService;
import com.xxx.test.upnp.demo.dlna.search.SearchUtil;

import java.net.URL;

public class Controller {
    public static final String TAG = "Controller";

    //String mUrl = "http://192.168.0.12:49152/description.xml";
//    String mUrl = "http://192.168.31.201:49152/description.xml";
    String mUrl = "http://192.168.31.16:49152/description.xml";
    public static final String CRLF = "\r\n";
    MyStreamSocket mySocket = null;
    private SearchUtil mSearchUtil = null;
    private SearchService mSearchService = null;

    // http://192.168.0.12:49152/description.xml
    private String getRequest(String host, int port){
        StringBuffer str = new StringBuffer();
        str.append("GET /description.xml HTTP/1.1" + CRLF);
        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
        str.append("HOST: " + host + ":" + port + CRLF);
        str.append(CRLF);
        String content = str.toString();
        return content;
    }

//    POST / _urn:schemas-upnp-org:service:AVTransport_control HTTP/1.1
//    HOST: 192.168.31.201:49152
//    USER-AGENT: Linux/4.19.113-perf+, UPnP/1.0, Portable SDK for UPnP devices/1.6.20
//    CONTENT-LENGTH: 2587
//    Content-Type: text/xml; charset="utf-8"
//    SOAPAction: "urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI"
//
//
//    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
//    <s:Body>
//    <u:SetAVTransportURI xmlns:u="urn:schemas-upnp-org:service:AVTransport:1">
//    <InstanceID>0</InstanceID>
//    <CurrentURI>http://video.hpplay.cn/demo/aom.mp4</CurrentURI>
//    </u:SetAVTransportURI>
//    </s:Body>
//    </s:Envelope>

    public void startService(){
        if(mSearchService == null){
            mSearchService = new SearchService();
        }
        mSearchService.start();
    }

    public void stopService(){
        if(mSearchService != null){
            mSearchService.stop();
            mSearchService = null;
        }

    }

    public void sartBrowse(){
        if(mSearchUtil == null){
            mSearchUtil = new SearchUtil();
        }
        mSearchUtil.start();
    }

    public void stopBrowse(){
        if(mSearchUtil != null){
            mSearchUtil.stop();
            mSearchUtil = null;
        }
    }

    //获取服务信息
    public void getdevice(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response;
                StringBuilder stringBuilder = new StringBuilder();
                try{
                    URL aa = new URL(mUrl);
                    if(mySocket == null) {
                        mySocket = new MyStreamSocket(aa.getHost(), aa.getPort());
                    }
                    Log.i(TAG,"getdevice mySocket:" + mySocket);
                    mySocket.sendMessage(getRequest(aa.getHost(),aa.getPort()));
                    response = mySocket.receiveMessage();
                    Log.i(TAG,"receive1:" + response);
                    while (response != null) {
                        stringBuilder.append(response);
                        response = mySocket.receiveMessage();
                        Log.i(TAG,"receive:" + response);//打印服务器响应信息。包括状态行。头部内容。空行。web对象内容
                    }
                    Log.i(TAG,"receive stringBuilder:" + stringBuilder);
                } catch (Exception e) {
                }
            }
        }).start();
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
    public void setAVTransport(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"setAVTransport");
                String response;
                StringBuilder stringBuilder = new StringBuilder();
                try{
                    Log.i(TAG,"setAVTransport mySocket:" + mySocket);
                    URL aa = new URL(mUrl);
                    if(mySocket == null) {
                        mySocket = new MyStreamSocket(aa.getHost(), aa.getPort());
                    }
                    Log.i(TAG,"setAVTransport mySocket11:" + mySocket);
                    mySocket.sendMessage(getAVTransportPost(aa.getHost(),aa.getPort()));
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
