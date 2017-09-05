package org.webservice;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import javax.jws.WebService;


@WebService(endpointInterface = "org.webservice.Calculator",
			serviceName = "calculator")
public class CalculatorImpl implements Calculator{

	@Override
	public int plus(int num1, int num2) {
		return num1+num2;
	}

/*
* 简单启动一个webservice服务
* */
public static void main(String[] args) {
		Calculator calculator;
		Server server;
		calculator = new CalculatorImpl();
	        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
	        svrFactory.setServiceClass(Calculator.class);
	        svrFactory.setAddress("http://localhost:63081/calculator");
	        svrFactory.setServiceBean(calculator);
	        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
	        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
	        server = svrFactory.create();
	        server.start();
}
}