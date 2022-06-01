package com.xxx.test.upnp.demo.dlna.Device;

import androidx.annotation.Nullable;

public class DeviceBean {
    public String name;
    public String ip;
    public int port;
    public String uid;
    public String location;

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    private boolean isSelect = false;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUid() {
        return uid;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof DeviceBean)) {
            return false;
        }
        DeviceBean b = (DeviceBean) obj;
        return this.uid.equals(b.uid);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceBean ");
        sb.append("name:" + name + "\n");
        sb.append("ip:" + ip + "\n");
        sb.append("port:" + port + "\n");
        sb.append("uid:" + uid);
        return sb.toString();
    }
}
