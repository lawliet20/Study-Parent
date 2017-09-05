package org.webservice;

import org.webservice.model.Mpc;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(endpointInterface = "org.webservice.MpcService", serviceName = "MpcService")
public class MpcServiceImpl implements MpcService {  
  
    public String createMpc(String name) {
        System.out.println("创建人：" + name);
        Mpc mpc = new Mpc();
        mpc.setAdd("香格里拉");
        return mpc.getAdd();
    }

    public static void main(String args[]) throws Exception {
        //实例化一个MpcServiceImpl的对象，并在http://localhost:9090/mpc的地址中发布webservice
        Endpoint.publish("http://localhost:9090/mpc", new MpcServiceImpl());
        System.out.println("服务启动...");
        Thread.sleep(100 * 60 * 1000);//随意设个时间，不要立马退出程序，最好长一点
        System.exit(0);
    }
} 