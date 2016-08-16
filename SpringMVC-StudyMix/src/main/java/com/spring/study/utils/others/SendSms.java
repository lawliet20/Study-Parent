package com.spring.study.utils.others;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.maikong.model.SmsModel;
import redis.clients.jedis.JedisCluster;

/**
 * 发送短信
 * @author wwj
 * 2015年7月8日15:16:05
 */
public class SendSms {
	private static Logger log = LoggerFactory.getLogger(SendSms.class);
	public static void main(String[] args) {
//		try {
//			System.out.println(sendSms("13155002656", "4333"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		try {
//			String resp = sendMssms("13155002656", "@1@=2735");
//			log.info(resp);
//			if(!MyStringUtil.isEmpty(resp)){
//				resp = resp.trim();
//				if (resp.indexOf("#") > -1 && "0".equals(resp.substring(0,resp.indexOf("#")))) {
//					log.info("发送成功！");
//				}else{
//					log.info("发送失败！");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		try {
//			boolean resp = sendMssmsNew("13155002656", "@1@=8485");
//			if(resp){
//				System.out.println("发送成功！");
//			}else{
//				System.out.println("发送失败！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		testSmsBat();
		
//		try {
//			System.out.println(sendDuanXinWangsms("13155002656", "尊敬的用户，您的验证码为6343，此验证码5分钟内有效。【e豆支付】"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private static String smsUrl = "https://openapi.1card1.cn/OpenApi/SendSms";
	private static String openId = "3C4D750012294F6EBFE68523930604A0";
	private static String secret = "FTDNLL";
	
	private static String mssmsUrl = "http://222.185.228.25:8000/msm/sdk/http/sendsmsutf8.jsp";
	private static String mssmsUsername = "JSMB260023";
	private static String mssmsScode = "njpyzs2011";
	
	/**
	 * 异步发送短信
	 */
	public static void sendSmsAsy(String smsContent,String mobileNo,JedisCluster jedisCluster){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mobileNo", mobileNo);
		jsonObject.put("smsbody", smsContent);
		try{
			jedisCluster.lpush("smskey",jsonObject.toJSONString());
		}catch(Exception e){
			log.error("redis sms queue error:",e);
		}
	}

	/**
	 * 发送短信
	 */
	public static String sendSms(String phoneNum, String content){
		SmsModel sms = new SmsModel();
		sms.setContent(content);
		sms.setMobile(phoneNum);
		sms.setUserAccount("10074");
		String data = JsonUtils.java2JsonStr(sms);
		Long timestamp = DateUtils.getCurrentDate();
		String md5str= openId + secret + timestamp +data;
		String signature = (MD5Util.MD5(md5str).toUpperCase());
		
		String url = smsUrl+"?openId=" + openId + "&signature=" + signature + "&timestamp=" + timestamp;
		log.info("url:" + url);
		Map params = new HashMap();
		params.put("data", data);
		log.info("data:" + data);
		//发送短信
		String resp = null;
		try {
			resp = ReqRemoteUrl.httpPost(url, params);
		} catch (Exception e) {
			log.error("sms error", e);
		}
		return resp;
	}

	/**
	 * 发送短信,美圣接口
	 */
	public static String sendMssms(String phoneNum, String content){
		log.info("url:" + mssmsUrl);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", mssmsUsername);
		params.put("scode", mssmsScode);
		params.put("mobile", phoneNum);
		params.put("content", content);
		params.put("tempid", "MB-2015110610");
		//发送短信
		String resp = null;
		try {
			resp = ReqRemoteUrl.httpPost(mssmsUrl, params);
		} catch (Exception e) {
			log.error("sms error", e);
		}
		return resp;
	}
	
	/**
	 * 发送短信,美圣接口(新接口)
	 */
	public static boolean sendMssmsNew(String phoneNum, String content){
		String url = "http://112.74.76.186:8030/service/httpService/httpInterface.do?method=sendMsg";
		log.info("url:" + url);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", "JSM40421");
		params.put("password", "i67n02to");
		params.put("veryCode", "rwwglsldl7bj");
		params.put("msgtype", "2");
		params.put("code", "utf-8");
		params.put("mobile", phoneNum);
		params.put("content", content);
		params.put("tempid", "JSM40421-0002");
		//发送短信
		String resp = null;
		try {
			resp = ReqRemoteUrl.httpPost(url, params);
			log.info("resp:" + resp);
			if(!MyStringUtil.isEmpty(resp)){
				Map<String,String> map = getMapFromXML(resp);
				if ("0".equals(map.get("status"))) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error("sms error", e);
		}
		return false;
	}
	
	/**
	 * 短信网
	 */
	public static String sendDuanXinWangsms(String phoneNum, String content) throws IOException {
		//发送内容
		String sign="";
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://web.duanxinwang.cc/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name=test91");

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=9FA6EB4D075A98F84F896CF0D451");

		// 向StringBuffer追加手机号码
		sb.append("&mobile=").append(phoneNum);

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content="+URLEncoder.encode(content,"UTF-8"));
		
		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");
		
		//加签名
		sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
		
		//type为固定值pt  extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		// 创建url对象
		//String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
		log.info("sb:"+sb.toString());
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		InputStream is =url.openStream();

		//转换返回值
		String returnStr = convertStreamToString(is);
		
		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
		return returnStr;
		// 返回发送结果
	}
	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                is.close();    
            } catch (IOException e) {    
               e.printStackTrace();    
            }    
        }    
        return sb1.toString();    
    }
	
	public static Map<String,String> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = null;
        Map<String, String> map = new HashMap<String, String>();
        if (xmlString != null && !xmlString.trim().equals("")) {
        	is = new ByteArrayInputStream(xmlString.getBytes());
        	Document document = builder.parse(is);
        	//获取到document里面的全部结点
            NodeList allNodes = document.getFirstChild().getFirstChild().getChildNodes();
            Node node;
            int i=0;
            while (i < allNodes.getLength()) {
                node = allNodes.item(i);
                if(node instanceof Element){
                    map.put(node.getNodeName(),node.getTextContent());
                }
                i++;
            }
        }
        return map;
    }
	
	private static List<String> phoneList = Arrays.asList("", "");
	
	public static void testSmsBat(){
		for (String phone : phoneList) {
			try {
				String time = DateUtils.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
				boolean resp = sendMssmsNew(phone, "@1@=" + time);
				if(resp){
					System.out.println(phone + "发送成功！");
				}else{
					System.out.println(phone + "发送失败！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
