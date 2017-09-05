package org.webservice;
  
import javax.jws.WebMethod;  
import javax.jws.WebParam;  
import javax.jws.WebResult;  
import javax.jws.WebService;  
  
@WebService  
public interface MpcService {  
  
    @WebMethod  
    @WebResult(name = "address")//返回值的名称为address  
    String createMpc(@WebParam(name = "name") String name);//定义了一个名称为name的String类型的参数  
}  