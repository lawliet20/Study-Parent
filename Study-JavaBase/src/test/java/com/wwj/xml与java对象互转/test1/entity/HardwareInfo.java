package com.wwj.xml与java对象互转.test1.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

@XStreamAlias("HardwareInfo")
public class HardwareInfo {
    private String cpuInfo;
    private String networkInfo;
    private List<DiskInfo> diskInfos;

    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public String getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(String networkInfo) {
        this.networkInfo = networkInfo;
    }

    public List<DiskInfo> getDiskInfos() {
        return diskInfos;
    }

    public void setDiskInfos(List<DiskInfo> diskInfos) {
        this.diskInfos = diskInfos;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(cpuInfo);
        sb.append("\r\n");
        sb.append(networkInfo);
        sb.append("\r\n");
        for (DiskInfo disk : diskInfos) {
            sb.append(disk.toString());
            sb.append("\r\n");
        }
        return sb.toString();
    }
}  