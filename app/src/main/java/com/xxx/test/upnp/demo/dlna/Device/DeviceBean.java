package com.xxx.test.upnp.demo.dlna.Device;

public class DeviceBean {

    public String name;
    public String ip;
    public int port;
    public String uid;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceBean ");
        sb.append("name:" + name + "\n");
        sb.append("ip:" + ip + "\n");
        sb.append("port:" + port + "\n");
        sb.append("uid:" + uid);
        return sb.toString();
    }
}
