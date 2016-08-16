package com.spring.study.utils.others;

/**
 * 访问远程请求工具类(异步请求、同步请求)
 * @date 2015年6月29日 15:25:36
 * @author wwj
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class ReqRemoteUrl {
	public static void main(String[] args) {
		try {
			URL url = new URL("http://dev3.miigaa.com/ed-weixin/member/getMemberAccount");
			reqRemoteUrl(url, "POST");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static final String ADD_URL = "https://test.pay.popcircle.cn/servlets/binserv/cn.syman.pay.PayServer";

	/**************************** 访问URL，异步请求 **********************************/
	public static String reqRemoteUrl(URL url, String method) {
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// GET or POST Request Define:
			urlConnection.setRequestMethod(method);
			urlConnection.connect();

			// Connection Response From Test Servlet
			System.out.println("Connection Response From Test Servlet");
			InputStream inputStream = urlConnection.getInputStream();

			// Convert Stream to String
			String responseStr = StreamToString.ConvertToString(inputStream);
			System.out.println(responseStr);
			return responseStr;
		} catch (IOException e) {
			Logger.getLogger(ReqRemoteUrl.class.getName()).log(Level.SEVERE, null, e);
		}
		return "";
	}

	/**************************** 访问URL，同步请求 **********************************/
	public static String httpPost(String url, Map<String, Object> params) throws Exception {
		HttpClient client = SingletonClient.getHttpClient();
		HttpPost httpost = new HttpPost(url);
		if (params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, (String) params.get(key)));
			}
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		}
		try {
			HttpResponse response = client.execute(httpost);
			HttpEntity entity = response.getEntity();
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
			return sendHttps(ADD_URL, jsonObject.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONObject sendHttps(String requsetString, String requestData) throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
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
		reqEntity = new StringEntity(requestData, "UTF-8");
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

class StreamToString {
	public static String ConvertToString(InputStream inputStream) {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStreamReader.close();
				inputStream.close();
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String ConvertToString(FileInputStream inputStream) {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStreamReader.close();
				inputStream.close();
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
}
