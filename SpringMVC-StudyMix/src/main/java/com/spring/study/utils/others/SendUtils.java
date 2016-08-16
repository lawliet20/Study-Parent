package com.spring.study.utils.others;


import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.mk.utils.SingletonClient;

public class SendUtils {
	private static final String ADD_URL = "https://test.pay.popcircle.cn/servlets/binserv/cn.syman.pay.PayServer";
	
	
	public static void main(String[] args) {
		
	}
	
	
	public static String httpPost(String url, Map<String, Object> params)
			throws Exception {
		HttpClient client = SingletonClient.getHttpClient();
		HttpPost httpost = new HttpPost(url);
		if (params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, (String)params.get(key)));
			}
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			System.out.println("request:"+EntityUtils.toString(httpost.getEntity()));
		}
		try {
			HttpResponse response = client.execute(httpost);
			System.out.println(response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			for (int i = 0; i < response.getAllHeaders().length; i++) {
				System.out.println(response.getAllHeaders()[i].getName() + " --->" +response.getAllHeaders()[i].getValue());
			}
			// 判断响应实体是否为空
			if (entity != null) {
				return EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return "";
	}
	
	public static JSONObject JsonPost(JSONObject jsonObject) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {

		try {
			return sendHttps(ADD_URL,jsonObject.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return  null;
		} catch (IOException e) {
			e.printStackTrace();
			return  null;
		}

	}
	
	public static JSONObject JsonPostUrl(String url,JSONObject jsonObject) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {

		try {
			return sendHttps(url,jsonObject.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return  null;
		} catch (IOException e) {
			e.printStackTrace();
			return  null;
		}

	}
	

	public static JSONObject sendHttps(String requsetString, String requestData) throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException,
			IOException {
		// First create a trust manager that won't care.
		X509TrustManager trustManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Don't do anything.
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Don't do anything.
			}

			public X509Certificate[] getAcceptedIssuers() {
				// Don't do anything.
				return null;
			}
		};
		// Now put the trust manager into an SSLContext.
		SSLContext sslcontext = SSLContext.getInstance("SSL");
		sslcontext.init(null, new TrustManager[] { trustManager }, null);
		// Use the above SSLContext to create your socket factory
		// (I found trying to extend the factory a bit difficult due to a
		// call to createSocket with no arguments, a method which doesn't
		// exist anywhere I can find, but hey-ho).
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));
		HttpPost httpPost = new HttpPost(requsetString);
		String result = "";
		// Execute HTTP request
		httpPost.setHeader("Authorization", "basic " + "dGNsb3VkYWRtaW46dGNsb3VkMTIz");
		httpPost.setHeader("Content-type", "application/json");
		StringEntity reqEntity;
		// 将请求参数封装成HttpEntity
		reqEntity = new StringEntity(requestData,"UTF-8");
		BufferedHttpEntity bhe = new BufferedHttpEntity(reqEntity);
		httpPost.setEntity(bhe);

		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity resEntity = response.getEntity();
		InputStreamReader reader = new InputStreamReader(resEntity.getContent());
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			result += new String(buff, 0, length);
		}
		httpclient.getConnectionManager().shutdown();
		return JSONObject.parseObject(result);
	}
}
