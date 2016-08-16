package com.spring.study.utils.others;
import java.net.URLEncoder;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * @author 刘宗安
 * @version 1.0 功能 ：釆用3DES标准以模式为ECB、填充方式为PKCS7加密数据
 */
public class DESecbpkcs7Impl 
{
	private Cipher cipher = null;
	// base64编码
	private BASE64Encoder base64Encode = new BASE64Encoder();
	private BASE64Decoder base64Decode = new BASE64Decoder();
	// 密钥
	private String key = "";
	// 过滤换行
	private boolean filter = true;

	public String getKey()
	{
		return key;
	}

	public boolean getFilter()
	{
		return filter;
	}
	
	/**
	 * 设置密钥
	 * @param key
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	public void setFilter(boolean filter)
	{
		this.filter = filter;
	}

	private final Cipher initCipher(int mode)
	{
		try
		{
			// 添加新安全算法:PKCS7
			Security.addProvider(new BouncyCastleProvider());
			String algorithm = "DESede/ECB/PKCS7Padding";
                         		SecretKey desKey = new SecretKeySpec((new BASE64Decoder()).decodeBuffer(key), algorithm);
			//SecretKey desKey = new SecretKeySpec(key.getBytes("ASCII"), algorithm);
			Cipher tcipher = Cipher.getInstance(algorithm);
			tcipher.init(mode, desKey);
			return tcipher;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密以charset编码做为密文
	 * 
	 * @param src
	 *            明文
	 * @param charset
	 *            编码,例：UTF8、BASE64
	 * @return
	 */
	public String encrypt(String src, String charset)
	{
		try
		{
			return URLEncoder.encode(encrypt(src), charset);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * @param src 二进制数组
	 * @return
	 * @throws Exception
	 */
	private byte[] decrypt(byte[] src) throws Exception
	{
		cipher = initCipher(Cipher.DECRYPT_MODE);
		return cipher.doFinal(src);
	}
	/**
	 * 解密
	 * @param src 密文
	 * @return
	 * @throws Exception
	 */
	public  String decrypt(String src) throws Exception
	{
		byte[] bt=base64Decode.decodeBuffer(src);
		byte[] sbt=decrypt(bt);
		return new String(sbt,"ASCII");
	}
	/**
	 * 加密以base64做为密文
	 * 
	 * @param src
	 *            明文
	 * @return 密文
	 */
	public String encrypt(String src)
	{
		cipher = initCipher(Cipher.ENCRYPT_MODE);
		byte[] dd = encrypt(src.getBytes());
		System.out.println("3des:"+CryptoUtils.byte2hex(dd));
		String str = base64Encode.encode(dd);
		str = str.replaceAll("\r", "");
		str = str.replaceAll("\n", "");
		return str;
	}

	/**
	 * 
	 * @param src
	 * @return
	 */
	public byte[] encrypt(byte[] src)
	{
		try
		{
			return cipher.doFinal(src);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception
	{
		BASE64Encoder base64Encode = new BASE64Encoder();
		String str = base64Encode.encode(CryptoUtils.hex2byte("F07ACD1CEE6B5F2FF04DEA143EF40C258CB17681566B6812E6B5FE899E87DE64A54AE7AF84ADF819997A9C62347B06AEFDC67EBA6C49875E048C7D3BFB9C11D31C6EA5F0532A43E4F5A53BBED57B49DBF049AB64C1B044A1C1E03A338DE02401534F48F4A7E64FA0B13FB142A7DA267F"));
		str = str.replaceAll("\\+", "-");
		str = str.replaceAll("\\/", "_");
		System.out.println("result:"+str+"AAAA");
//		DESecbpkcs7Impl cWebService3DES = new DESecbpkcs7Impl();
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		cWebService3DES.key = "D328FBEE0E85D095C4A15F8A3DDCF676";
//		String s = cWebService3DES
//				.encrypt("800002628|2015012300000001|000000000001|6212790200001797|6212790200001797=12015208084198109646|||CFBCD9C8A0F4776D|000000");
//		System.out.println("s:"+s);
//		s = s.replaceAll("\\+", "-");
//		s = s.replaceAll("\\/", "_");
//		System.out.println(s);
//		System.out.println(cWebService3DES.decrypt(s));
	}

}