package com.xxx.test.upnp.demo.dlna;

public class CmdUtils {
    public static final String TAG = "CmdUtils";
    public static final String CRLF = "\r\n";

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
    public static String getAVTransportPost(String host, int port, String url) {
        StringBuffer str = new StringBuffer();
        str.append(" POST /_urn:schemas-upnp-org:service:AVTransport_control HTTP/1.1" + CRLF);
        str.append("User-Agent: DMP/2.5.8, UPnP/1.0," + CRLF);
        str.append("HOST: " + host + ":" + port + CRLF);
        str.append("CONTENT-LENGTH: " + getAVTransportBody(url).getBytes().length + CRLF);
        str.append("Content-Type: text/xml; charset=\"utf-8\"" + CRLF);
        str.append("SOAPAction: \"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI\"" + CRLF);
        str.append(CRLF);
        str.append(getAVTransportBody(url));
        String content = str.toString();
        return content;
    }

    public static String getAVTransportBody(String uri) {
//        StringBuffer str = new StringBuffer();
//        str.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n");
//        str.append("  <s:Body>\n");
//        str.append("    <u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n");
//        str.append("      <InstanceID>0</InstanceID>\n");
//        str.append("      <CurrentURI>" + uri + "</CurrentURI>\n");
//        str.append("    </u:SetAVTransportURI>\n");
//        str.append("  </s:Body>\n");
//        str.append("</s:Envelope>");
//        return str.toString();
        String body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "  <s:Body>\n" +
                "    <u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\">\n" +
                "      <InstanceID>0</InstanceID>\n" +
                "      <CurrentURI>" + uri + "</CurrentURI>\n" +
                "    </u:SetAVTransportURI>\n" +
                "  </s:Body>\n" +
                "</s:Envelope>";
        return body;
    }


}
