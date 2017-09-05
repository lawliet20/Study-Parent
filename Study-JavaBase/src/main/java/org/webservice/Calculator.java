package org.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface Calculator {
    @WebMethod
    @WebResult(name = "num3")
	public int plus(@WebParam(name = "num1") int num1,
                    @WebParam(name = "num2") int num2);
}