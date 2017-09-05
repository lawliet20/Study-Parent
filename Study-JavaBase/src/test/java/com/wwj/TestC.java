package com.wwj;


import org.apache.shiro.crypto.hash.SimpleHash;
import org.safehaus.uuid.EthernetAddress;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * 统计任三出现的最多的几率的组合
 *
 * @author wangmingjie
 * @date 2009-1-1下午01:22:19
 */
public class TestC {
    public static void main(String[] args) {
        String passwd = new SimpleHash("SHA-1", "admin","123456").toString();
        System.out.println(passwd);

    }

    public void uuidTest(){

        UUIDGenerator generator= UUIDGenerator.getInstance();

        EthernetAddress address = generator.getDummyAddress();
        System.out.println(address.toString());
        UUID uuid=generator.generateRandomBasedUUID();
        System.out.println(uuid.toString());
        uuid=generator.generateTimeBasedUUID();
        System.out.println(uuid.toString());
    }
}

