package com.xxx.test.upnp.demo.dlna;

import android.util.Log;

import com.xxx.test.upnp.demo.dlna.search.SearchThread;
import com.xxx.test.upnp.demo.dlna.socket.UdpThread;

import java.net.URL;

public class Controller {
    public static final String TAG = "Controller";

    //String mUrl = "http://192.168.0.12:49152/description.xml";
//    String mUrl = "http://192.168.31.201:49152/description.xml";
    String mUrl = "http://192.168.31.16:49152/description.xml";
    public static final String CRLF = "\r\n";
    MyStreamSocket mySocket = null;
    SearchThread searchThread = null;

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
        UdpThread mUdnThread = new UdpThread();
        mUdnThread.start();
    }

    public void sartBrowse(){
        if(searchThread == null){
            searchThread = new SearchThread();
            searchThread.start();
        }
        searchThread.startSearch();
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


//    private String AVTransportBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//            "    <s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
//            "       <s:Body>\n" +
//            "          <u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
//            "             <InstanceID>0</InstanceID>\n" +
//            "             <CurrentURI>http://cdn.hpplay.com.cn/demo/lbtp.mp4</CurrentURI>\n" +
//            "             <CurrentURIMetaData>&lt;DIDL-Lite  xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:sec=&quot;http://www.sec.co.kr/&quot; &gt;&lt;item id=&quot;0&quot; parentID=&quot;0&quot; restricted=&quot;0&quot;&gt;&lt;dc:title&gt;DLNA-Video&lt;/dc:title&gt;&lt;dc:creator&gt;unknown&lt;/dc:creator&gt;&lt;upnp:class&gt;object.item.videoItem&lt;/upnp:class&gt;&lt;res  protocolInfo=&quot;http-get:*:video/mp4:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01500000000000000000000000000000&quot; &gt;http://cdn.hpplay.com.cn/demo/lbtp.mp4&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;</CurrentURIMetaData>\n" +
//            "          </u:SetAVTransportURI>\n" +
//            "       </s:Body>\n" +
//            "    </s:Envelope>";
//    private String getAVTransportPost(String host, int port){
//        StringBuffer str = new StringBuffer();
//        str.append(" POST /_urn:schemas-upnp-org:service:AVTransport_control HTTP/1.1" + CRLF);
//        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
//        str.append("HOST: " + host + ":" + port + CRLF);
//        str.append("CONTENT-LENGTH: " + AVTransportBody.getBytes().length + CRLF);
//        str.append("Content-Type: text/xml; charset=\"utf-8\"" + CRLF);
//        str.append("SOAPAction: \"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\"" + CRLF);
//        str.append(CRLF);
//        str.append(AVTransportBody);
//        String content = str.toString();
//        return content;
//    }

/**
 *
 * POST /_urn:schemas-upnp-org:service:AVTransport_control HTTP/1.1
 * HOST: 192.168.31.201:49152
 * CONTENT-LENGTH: 2587
 * CONTENT-TYPE: text/xml; charset="utf-8"
 * SOAPACTION: "urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI"
 * USER-AGENT: Linux/4.19.113-perf+, UPnP/1.0, Portable SDK for UPnP devices/1.6.20
 *
 * <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
 * <s:Body><u:SetAVTransportURI xmlns:u="urn:schemas-upnp-org:service:AVTransport:1">
 * <InstanceID>0</InstanceID>
 * <CurrentURI>http://pl-ali.youku.com/playlist/m3u8?vid=XNTIwNTM1NzI4MA%3D%3D&amp;type=mp4hdv3&amp;ups_client_netip=778bc505&amp;utid=YEsbeXDpDMEDAKBM%2FA5L0qxw&amp;ccode=01010301&amp;psid=a1f44efd9d605bf41bba84268c4d375043346&amp;app_ver=10.2.5&amp;duration=2598&amp;expire=18000&amp;drm_type=1&amp;drm_device=11&amp;drm_default=1&amp;play_ability=0&amp;player_source=1&amp;p_device=microsoftcorporation&amp;hotvt=1&amp;dyt=0&amp;btf=&amp;rid=200000007092EA5B5F368B5374275880B5B29B7602000000&amp;device_type=PEGM00&amp;ups_ts=1647958408&amp;onOff=0&amp;encr=0&amp;ups_key=f069adb656f284443edc728bcf5b3fcc&amp;fids=%5B%7B%22dt%22%3A1%2C%22p%22%3A130%2C%22v%22%3A%221462477435%22%7D%5D&amp;spd=1&amp;dlnaopt=2&amp;tpseq=4</CurrentURI>
 * <CurrentURIMetaData>&lt;DIDL-Lite xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot; xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot;&gt;&lt;item id=&quot;123&quot; parentID=&quot;-1&quot; restricted=&quot;1&quot;&gt;&lt;upnp:storageMedium&gt;UNKNOWN&lt;/upnp:storageMedium&gt;&lt;upnp:writeStatus&gt;UNKNOWN&lt;/upnp:writeStatus&gt;&lt;upnp:class&gt;object.item.videoItem&lt;/upnp:class&gt;&lt;dc:title&gt;............ 01 &lt;/dc:title&gt;&lt;dc:creator&gt;youku&lt;/dc:creator&gt;&lt;res protocolInfo=&quot;http-get:*:video/mp4:*;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000&quot;&gt;http://pl-ali.youku.com/playlist/m3u8?vid=XNTIwNTM1NzI4MA%3D%3D&amp;amp;type=mp4hdv3&amp;amp;ups_client_netip=778bc505&amp;amp;utid=YEsbeXDpDMEDAKBM%2FA5L0qxw&amp;amp;ccode=01010301&amp;amp;psid=a1f44efd9d605bf41bba84268c4d375043346&amp;amp;app_ver=10.2.5&amp;amp;duration=2598&amp;amp;expire=18000&amp;amp;drm_type=1&amp;amp;drm_device=11&amp;amp;drm_default=1&amp;amp;play_ability=0&amp;amp;player_source=1&amp;amp;p_device=microsoftcorporation&amp;amp;hotvt=1&amp;amp;dyt=0&amp;amp;btf=&amp;amp;rid=200000007092EA5B5F368B5374275880B5B29B7602000000&amp;amp;device_type=PEGM00&amp;amp;ups_ts=1647958408&amp;amp;onOff=0&amp;amp;encr=0&amp;amp;ups_key=f069adb656f284443edc728bcf5b3fcc&amp;amp;fids=%5B%7B%22dt%22%3A1%2C%22p%22%3A130%2C%22v%22%3A%221462477435%22%7D%5D&amp;amp;spd=1&amp;amp;dlnaopt=2&amp;amp;tpseq=4&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;</CurrentURIMetaData>
 * </u:SetAVTransportURI>
 * </s:Body>
 * </s:Envelope>
 * **/


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
