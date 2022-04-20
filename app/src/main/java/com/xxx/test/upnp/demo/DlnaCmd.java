package com.xxx.test.upnp.demo;

public class DlnaCmd {

    // 没一行的结束符
    public static final String CRLF = "\r\n";
    /**
     * 组播端口
     * Default destination port for SSDP multicast messages
     */
    public static final int SSDP_PORT = 1900;

    /**
     * upnp协议指定的组播ip
     * Default IPv4 multicast address for SSDP messages
     */
    public static final String SSDP_ADDRESS = "239.255.255.250";

    // 服务类型：DMR
    public static final String DMR_ST = "urn:schemas-upnp-org:device:MediaRenderer:1";
    // 服务类型：跟设备
    public static final String ROOTDEVICE_ST = "upnp:rootdevice";

    // 搜索服务命令
    public static String getSearchString(String st){
        StringBuffer str = new StringBuffer();
        str.append("M-SEARCH * HTTP/1.1" + CRLF);
        // ST:service_type服务类型(all,rootdevice....)
        str.append("ST: " + st + CRLF);
        // 设备响应最大等待时间
        str.append("MX: 3" + CRLF);
        // ssdp命令：搜索服务
        str.append("MAN: \"ssdp:discover\"" + CRLF);
        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
        str.append("HOST: " + SSDP_ADDRESS + ":" + SSDP_PORT + CRLF);
        // 命令最后还有一个结尾符，不能漏
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
    // 获取设备描述信息
    public static String getRequest(String host, int port){
        StringBuffer str = new StringBuffer();
        str.append("GET /description.xml HTTP/1.1" + CRLF);
        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
        str.append("HOST: " + host + ":" + port + CRLF);
        str.append(CRLF);
        String content = str.toString();
        return content;
    }
}
