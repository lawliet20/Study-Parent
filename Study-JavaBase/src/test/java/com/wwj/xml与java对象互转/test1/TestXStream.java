package com.wwj.xml与java对象互转.test1;

import com.thoughtworks.xstream.XStream;
import com.wwj.xml与java对象互转.test1.entity.DiskInfo;
import com.wwj.xml与java对象互转.test1.entity.HardwareInfo;

import java.util.ArrayList;

public class TestXStream {
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        DiskInfo disk1 = new DiskInfo();
        disk1.setName("D:\\");
        disk1.setTotleSize(100);
        disk1.setUserSize(20);
        disk1.setAvilableSize(80);
        DiskInfo disk2 = new DiskInfo();
        disk2.setName("E:\\");
        disk2.setTotleSize(200);
        disk2.setUserSize(40);
        disk2.setAvilableSize(160);

        ArrayList<DiskInfo> diskInfos = new ArrayList<DiskInfo>();
        diskInfos.add(disk1);
        diskInfos.add(disk2);

        HardwareInfo hwInfo = new HardwareInfo();
        hwInfo.setCpuInfo("cpu info is ...");
        hwInfo.setNetworkInfo("network info is ...");
        hwInfo.setDiskInfos(diskInfos);

        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
//        String xml = xStream.toXML(hwInfo);
//        System.out.println(xml);

        String xmlStr = "" +
                "<HardwareInfo>\n" +
                "  <cpuInfo>cpu info is ...</cpuInfo>\n" +
                "  <networkInfo>network info is ...</networkInfo>\n" +
                "  <diskInfos>\n" +
                "    <DiskInfo diskName=\"D:\\\">\n" +
                "      <totalSize>100</totalSize>\n" +
                "      <avilableSize>80</avilableSize>\n" +
                "      <usedSize>20</usedSize>\n" +
                "    </DiskInfo>\n" +
                "    <DiskInfo diskName=\"E:\\\">\n" +
                "      <totalSize>200</totalSize>\n" +
                "      <avilableSize>160</avilableSize>\n" +
                "      <usedSize>40</usedSize>\n" +
                "    </DiskInfo>\n" +
                "  </diskInfos>\n" +
                "</HardwareInfo>";
        xStream.alias("HardwareInfo",HardwareInfo.class);
        xStream.alias("DiskInfo",DiskInfo.class);
        HardwareInfo info2 = (HardwareInfo) xStream.fromXML(xmlStr);
        System.out.println(info2);  
    }  
  
}  