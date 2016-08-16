package com.spring.study.utils.others;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

/**
* CryptoUtils 提供了一个安全算法类,其中包括对称密码算法和散列算法
*/
public final class CryptoUtils
{
	private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
/**
 * Mac校验	
 * @param macKey key
 * @param data 校验数据
 * @return
 * @throws Exception 
 */
public static String getMac(byte[] makKey,byte[] data) throws Exception{
    //进行分组
    int group = (data.length + (8 - 1)) / 8;
    //偏移量
    int offset = 0 ;
    //输入计算数据
    byte[] edata = null;
    byte[] temp = null;
    for(int i = 0 ; i < group; i++){
        temp = new byte[8];
        if(i != group - 1){ 
            System.arraycopy(data, offset, temp, 0, 8);
            offset += 8;
        }else{//只有最后一组数据才进行填充0x00
            System.arraycopy(data, offset, temp, 0, data.length - offset);
        }


        if(i != 0){//只有第一次不做异或
            temp = XOR(edata,temp);
        }
        edata = temp;
    }
    String xorResult = byte2hex(edata);
    
    edata = symmetricEncrypto(makKey,xorResult.substring(0, 8).getBytes());
    temp = XOR(edata,xorResult.substring(8, 16).getBytes());
    edata = symmetricEncrypto(makKey,temp);
    String result = byte2hex(edata);
	return result.substring(0,8);
}



/**
 * 银联磁道加密
 * @param makKey
 * @param data
 * @return
 * @throws Exception
 */
public static String getEncCData(String encKey,byte[] cData) throws Exception{
	byte[] destData = new byte[8];
	System.arraycopy(cData, cData.length-9, destData, 0, 8);
	byte[] result = hex2byte(encrypt3DES(byte2hex(destData), encKey));
	System.arraycopy(result, 0, cData, cData.length-9, 8);
	return byte2hex(cData);
}

/**
 * 江苏银商磁道加密
 * @param makKey
 * @param data
 * @return
 * @throws Exception
 */
public static String getEncCDataForJsYs(String encKey,byte[] cData) throws Exception{
	if (cData.length % 8 != 0) {
		int group = (cData.length + (8 - 1)) / 8;
		byte[] edata = new byte[group * 8];
		System.out.println(cData.length);
		System.out.println(edata.length);
		System.arraycopy(cData, 0, edata, 0, cData.length);
		byte[] bt = new byte[group * 8 - cData.length];
		for (int i = 0; i < group * 8 - cData.length; i++) {
			bt[i] = 0x00;
		}
		System.arraycopy(bt, 0, edata,cData.length, group * 8 - cData.length);
		return encrypt3DES(byte2hex(edata), encKey);
	}else {
		return encrypt3DES(byte2hex(cData), encKey);
	}
}

/**
 * ANSIX9.8格式  pinBlock 还原密码
 * @param strPassword
 * @param strCardNo
 * @return
 */
public static String pwdFromPinBlock(byte[] bytesPinBlock, String strCardNo)
{
	//PIN - 8位
	byte[] bytesPin = new byte[8];
	//PAN  - 这里没算了前面的0，是6位
	int nLength = strCardNo.length();
	String strCardNo12 = strCardNo.substring(nLength-13, nLength-1);
	byte[] bcdPAN = CryptoUtils.str2bcd(strCardNo12);
	//异或
	bytesPin[0] = bytesPinBlock[0];
	bytesPin[1] = bytesPinBlock[1];
	for(int i=2;i<8;i++)
	{
		bytesPin[i] = (byte)(bytesPinBlock[i]^bcdPAN[i-2]);
	}

	String strPwd = CryptoUtils.byte2hex(bytesPin).substring(2, 8);
	return strPwd;
}

/**
 * 江苏银商Mac校验	
 * @param macKey key
 * @param data 校验数据
 * @return
 * @throws Exception 
 */
public static String getMacForJsYs(String makKey,byte[] data) throws Exception{
    //进行分组
    int group = (data.length + (8 - 1)) / 8;
    //偏移量
    int offset = 0 ;
    //输入计算数据
    byte[] edata = null;
    byte[] temp = null;
    for(int i = 0 ; i < group; i++){
        temp = new byte[8];
        if(i != group - 1){ 
            System.arraycopy(data, offset, temp, 0, 8);
            offset += 8;
        }else{//只有最后一组数据才进行填充0x00
            System.arraycopy(data, offset, temp, 0, data.length - offset);
        }


        if(i != 0){//只有第一次不做异或
            temp = XOR(edata,temp);
        }
        edata = temp;
    }
    String xorResult = byte2hex(edata);
    System.out.println(xorResult);
    String result ="" ;
    result = encrypt3DES(byte2hex(xorResult.substring(0, 8).getBytes()), makKey);
    temp = XOR(hex2byte(result),xorResult.substring(8, 16).getBytes());
    result = encrypt3DES(byte2hex(temp),makKey);
    System.out.println(result);
	return result.substring(0,8);
}

/**
 * 快钱磁道加密
 * @param makKey
 * @param data
 * @return
 * @throws Exception
 */
public static String getEncCDataForKq(String encKey,String cData) throws Exception{
	if (cData.length() >= 100) {
		cData = "0"+cData.length() + cData;
	}else {
		cData = cData.length() + cData;
	}
	System.out.println("cdata:"+cData);
    int group = (cData.length() + (16 - 1)) / 16;
    int offset = 0 ;
    StringBuilder result = new StringBuilder();
    String temp = null;
    for(int i = 0 ; i < group; i++){
        if(i != group - 1){ 
        	temp = cData.substring(offset, offset+16);
            offset += 16;
        }else{//只有最后一组数据才进行填充0x00
        	temp = cData.substring(offset, cData.length());
        	int busize = 16 - temp.length();
            for (int j = 0; j < busize; j++) {
            	temp += "0";
			}
        }
        result.append(encrypt3DES(temp, encKey));
    }
   
	return result.toString();
}

public static String getEncCDataForYj(String encKey,String cData) throws Exception{
	if (cData.length() >= 100) {
		cData = cData.length() + cData;
	}else {
		cData = cData.length() + cData;
	}
	
	int leng = cData.length() % 8;
	if (leng!=0) {
		for (int i = 0; i < 8-leng ;i++) {
			cData = cData+"0";
		}
	}
	System.out.println("cdata:"+cData);
	
    String result = encrypt3DES(byte2hex(cData.getBytes()), encKey);
   
	return result;
}

public static String getMacBy919(String makKey,byte[] data) throws Exception{
	String part1= makKey.substring(0,16);
	String part2= makKey.substring(16,32);
    //进行分组
    int group = (data.length + (8 - 1)) / 8;
    //偏移量
    int offset = 0 ;
    //输入计算数据
    byte[] edata = null;
    byte[] temp = null;
    for(int i = 0 ; i < group; i++){
        temp = new byte[8];
        if(i != group - 1){ 
            System.arraycopy(data, offset, temp, 0, 8);
            offset += 8;
        }else{//只有最后一组数据才进行填充0x00
            System.arraycopy(data, offset, temp, 0, data.length - offset);
        }

        if(i != 0){//只有第一次不做异或
            temp = XOR(edata,temp);
        }
        edata = hex2byte(encryptDES(byte2hex(temp),part1));
    }
    
    String r1 = decryptDES(byte2hex(edata), part2);
    String r2 = encryptDES(r1, part1);
	return r2;
}

public static String getMacBy919With001(String makKey,byte[] data) throws Exception{
    //进行分组
    int group = (data.length + (8 - 1)) / 8;
    //偏移量
    int offset = 0 ;
    //输入计算数据
    byte[] edata = null;
    byte[] temp = null;
    for(int i = 0 ; i < group; i++){
        temp = new byte[8];
        if(i != group - 1){ 
            System.arraycopy(data, offset, temp, 0, 8);
            offset += 8;
        }else{//只有最后一组数据才进行填充0x00
            System.arraycopy(data, offset, temp, 0, data.length - offset);
        }

        if(i != 0){//只有第一次不做异或
            temp = XOR(edata,temp);
        }
        edata = hex2byte(encryptDES(byte2hex(temp),makKey));
    }
    
    String result = encryptDES(byte2hex(edata),makKey);
	return byte2hex(edata);
}

public static byte[] XOR(byte[] edata, byte[] temp) {
    byte [] result = new byte[8];
    for (int i = 0 , j = result.length ; i < j; i++) {
        result [i] = (byte) (edata[i] ^ temp[i]);
    }
    return result;
}

public static byte[] desedeEn(byte[] key,byte[] data){
    byte[] result = null;
    try {
        SecretKey secretKey = getSecretKeySpec(key);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);//初始化向量为0,即异或不改变原始数据
        result = cipher.doFinal(data);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}

private static SecretKey getSecretKeySpec(byte[] keyB) throws NoSuchAlgorithmException, InvalidKeySpecException {
//    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("Des");
//    SecretKeySpec secretKeySpec = new SecretKeySpec(keyB,"Des");
//    return secretKeyFactory.generateSecret(secretKeySpec);
	return new SecretKeySpec(keyB, "DES");
}
	
/**
  * DES对称加密方法
  * @param byteSource 需要加密的数据
  * @return 经过加密的数据
  * @throws Exception
  */
public static byte[] symmetricEncrypto(byte[] keyData, byte[] byteSource) throws Exception
{
	//检测系统是否已加载此Provider的方法
	if(null==Security.getProvider("BC"))
	{
		//加载Provider
		Security.addProvider(new BouncyCastleProvider());
	}

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	int mode = Cipher.ENCRYPT_MODE;
	try
	{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES", "BC");
		DESKeySpec keySpec = new DESKeySpec(keyData);
		Key key = keyFactory.generateSecret(keySpec);
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding", "BC");
		cipher.init(mode, key);

		//必须用下面Stream实现，用原cipher.update和doFinal方法加密还是会在后面Padding内容。
		CipherOutputStream cOut = new CipherOutputStream(baos, cipher);
		cOut.write(byteSource);
		cOut.close();
		return baos.toByteArray();
	}
	catch(Exception e)
	{
		throw e;
	}
	finally
	{
		baos.close();
	}
}

/**
  * DES对称解密方法
  * @param byteSource 需要解密的数据
  * @return 经过解密的数据
  * @throws Exception
  */
public static byte[] symmetricDecrypto(byte[] keyData, byte[] byteSource) throws Exception
{
	//检测系统是否已加载此Provider的方法
	if(null==Security.getProvider("BC"))
	{
		//加载Provider
		Security.addProvider(new BouncyCastleProvider());
	}

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	int mode = Cipher.DECRYPT_MODE;
	try
	{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES", "BC");
		DESKeySpec keySpec = new DESKeySpec(keyData);
		Key key = keyFactory.generateSecret(keySpec);
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding", "BC");
		cipher.init(mode, key);

		CipherOutputStream cOut = new CipherOutputStream(baos, cipher);
		cOut.write(byteSource);
		cOut.close();
		return baos.toByteArray();
	}
	catch(Exception e)
	{
		throw e;
	}
	finally
	{
		baos.close();
	}
}

/**
  * 散列算法
  * @param strAlgorithm 算法，"SHA-1"或"MD5"
  * @param byteSource 需要散列计算的数据
  * @return 经过散列计算的数据
 * @throws NoSuchAlgorithmException 
  * @throws Exception
  */
public static byte[] doHash(String strAlgorithm, byte[] byteSource) throws NoSuchAlgorithmException
{
   MessageDigest currentAlgorithm = MessageDigest.getInstance(strAlgorithm);
   //currentAlgorithm.reset();
   currentAlgorithm.update(byteSource);
   return currentAlgorithm.digest();
}

/**
 * 散列算法
 * @param strAlgorithm 算法，"SHA-1"或"MD5"
 * @param byteSource 需要散列计算的数据
 * @return 经过散列计算的数据
* @throws NoSuchAlgorithmException 
 * @throws Exception
 */
public static String doHash(String strAlgorithm, String strSource) throws NoSuchAlgorithmException
{
  MessageDigest currentAlgorithm = MessageDigest.getInstance(strAlgorithm);
  //currentAlgorithm.reset();
  currentAlgorithm.update(strSource.getBytes());
  return byte2hex(currentAlgorithm.digest());
}

/**
  * 校验散列值
  * @param strAlgorithm 算法，"SHA-1"或"MD5"
  * @param byteSource 需要散列计算的数据
  * @param byteHash 需要验证的散列数据
  * @return 校验结果true或false
 * @throws NoSuchAlgorithmException 
  * @throws Exception
  */
public static boolean verifyHash(String strAlgorithm, byte[] byteSource, byte[] byteHash) throws NoSuchAlgorithmException 
{
	java.security.MessageDigest alga=java.security.MessageDigest.getInstance(strAlgorithm); 
	alga.update(byteSource); 
	byte[] digesta=alga.digest(); 
	if( MessageDigest.isEqual(digesta, byteHash) ) 
	{
		return true;
	}
	else
	{
		return false;
	}
}

/**
 * 校验散列值
 * @param strAlgorithm 算法，"SHA-1"或"MD5"
 * @param strSource 需要散列计算的文本数据
 * @param strHexHash 需要验证的散列数据，16进制字串表示
 * @return 校验结果true或false
 * @throws NoSuchAlgorithmException 
 * @throws Exception
 */
public static boolean verifyHash(String strAlgorithm, String strSource, String strHexHash) throws NoSuchAlgorithmException 
{
	java.security.MessageDigest alga=java.security.MessageDigest.getInstance(strAlgorithm); 
	alga.update(strSource.getBytes()); 
	byte[] digesta=alga.digest(); 
	if( MessageDigest.isEqual(digesta, hex2byte(strHexHash)) ) 
	{
		return true;
	}
	else
	{
		return false;
	}
}

/**
 * 签名
 * @param strKeyStorePath keystore文件路径
 * @param strKeyStorePass keystore密码
 * @param strPKIAlias 密钥对的别名
 * @param strPKIPass 提取私钥的密码
 * @param byteSource 需要被签名的数据
 * @param algorithm 签名算法
 * @return 经过签名计算的数据
 * @throws Exception
 */
public static byte[] signByKeyStore(String strKeyStorePath, String strKeyStorePass, String strPKIAlias, String strPKIPass, byte[] byteSource, String algorithm) throws Exception
{
	//检测系统是否已加载此Provider的方法
	if(null==Security.getProvider("BC"))
	{
		//加载Provider
		Security.addProvider(new BouncyCastleProvider());
	}

	try
	{
		//从密钥库中直接读取证书
		FileInputStream in=new FileInputStream(strKeyStorePath);
		KeyStore ks=KeyStore.getInstance("JKS");
		ks.load(in,strKeyStorePass.toCharArray());
		//从密钥库中读取CA的私钥
		PrivateKey myprikey=(PrivateKey)ks.getKey(strPKIAlias,strPKIPass.toCharArray());
		//用他私人密钥(prikey)对他所确认的信息(info)进行数字签名产生一个签名数组  
		//初始一个Signature对象,并用私钥对信息签名 
		java.security.Signature signet=java.security.Signature.getInstance(algorithm,"BC"); 
		signet.initSign(myprikey); 
		signet.update(byteSource); 
		return signet.sign(); 
	}
	catch(Exception e)
	{
		throw e;
	}
}

/**
 * 校验签名
 * @param strKeyStorePath keystore文件路径
 * @param strKeyStorePass keystore密码
 * @param strPKIAlias 密钥对的别名
 * @param byteSource 被签名的数据
 * @param byteSigned 需要被校验的签名
 * @param algorithm 签名算法
 * @return 验证签名的结果
 * @throws Exception
 */
public static boolean verifyByKeyStore(String strKeyStorePath, String strKeyStorePass, String strPKIAlias, byte[] byteSource, byte[] byteSigned, String algorithm) throws Exception
{
	//检测系统是否已加载此Provider的方法
	if(null==Security.getProvider("BC"))
	{
		//加载Provider
		Security.addProvider(new BouncyCastleProvider());
	}

	try
	{
		//从密钥库中直接读取证书
		FileInputStream in=new FileInputStream(strKeyStorePath);
		KeyStore ks=KeyStore.getInstance("JKS");
		ks.load(in,strKeyStorePass.toCharArray());
		//从密钥库中读取证书
		java.security.cert.Certificate c1=ks.getCertificate(strPKIAlias);
		//初始一个Signature对象,并用证书对签名作校验 
		java.security.Signature signet=java.security.Signature.getInstance(algorithm,"BC"); 
		signet.initVerify(c1); 
		signet.update(byteSource); 
		return signet.verify(byteSigned); 
	}
	catch(Exception e)
	{
		throw e;
	}
}

/**
 * 校验签名
 * @param byteSrc asc码数组
 * @return bcd码编码到的byte数组
 */
public static byte[] asc2bcd(byte[] byteSrc, int nLen) 
{
	return null;
}

/**
 * 校验签名
 * @param byteSrc bcd码数组
 * @return 解码出来的asc码byte数组
 */
public static byte[] bcd2asc(byte[] byteSrc, int nLen) 
{
	return null;
}

/**
 * 功能：byte数值数组转化成16进制字符字串
 * @param b byte数组数据
 * @return byte数值数组转化成16进制字符的字串
 */
public static String byte2hex(byte[] b) 
{ 
	String hs = ""; 
	String stmp = ""; 
	for (int i = 0; i < b.length; i++) 
	{ 
		stmp = Integer.toHexString(b[i] & 0xFF); 
		if (stmp.length() == 1) 
		{ 
			hs += "0" + stmp; 
		}
		else 
		{
			hs += stmp; 
		} 
	} 
	return hs.toUpperCase(); 
} 

/**
 * 功能：16进制字符字串转化成byte数值数组
 * @param hex 16进制字符字串
 * @return 16进制字符字串转化成byte数值的数组
 */
public static byte[] hex2byte(String hex) throws IllegalArgumentException 
{ 
	if (hex.length() % 2 != 0) 
	{ 
		throw new IllegalArgumentException(); 
	} 
	char[] arr = hex.toCharArray(); 
	byte[] b = new byte[hex.length() / 2]; 
	for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) 
	{ 
		String swap = "" + arr[i++] + arr[i]; 
		int byteint = Integer.parseInt(swap, 16) & 0xFF; 
		b[j] = new Integer(byteint).byteValue(); 
	} 
	return b; 
} 

public static byte[] int2byte(int value, int count) {
	count = (count > 4 || count < 1) ? 4 : count;
	byte[] b = new byte[count];
	for (int i = 0; i < count; i++) {
		b[count - 1 - i] = (byte) Integer.rotateRight(value, i * 8);
	}
	return b;
}

/**
 * 把字符形式的数字转化为二进制形式
 * 如字符形式的 '0' (0x31)  将转化为  0 (0x01)     <br/>
 * 如已经是二进制形式，则不进行转化
 * @param digit_c 数字串
 * @return 二进制形式的数字串
 */
public static byte[] digit_c2b(byte[] digit_c){
	byte [] digit_b = new byte[digit_c.length];
	for(int i = 0; i < digit_c.length; i++ ){
		if( ((digit_c[i] > 0x09) && (digit_c[i] < 0x30))    
			|| digit_c[i] > 0x39 || digit_b[i] < 0x00)
			System.out.println("方法digit_c2b只能传入数字（字符或二进制形式）参数，方法调用失败! ");
		
		if(digit_c[i] <= 0x09)
			digit_b[i] = digit_c[i];
		else
			digit_b[i] = (byte) (digit_c[i] - 0x30);
	}
	return digit_b;
}
/**
 * 把二进制形式的数字转化为字符形式
 * 如字符形式的  0 (0x01) 将转化为 '0' (0x31) <br/>
 * 如已经是字符形式，则不进行转化
 * @param digit_b 数字串
 * @return 字符形式的数字串
 */
public static byte[] digit_b2c(byte[] digit_b) throws  IllegalArgumentException{
	byte [] digit_c = new byte[digit_b.length];
	for(int i = 0; i < digit_c.length; i++ ){
		if( ((digit_b[i] > 0x09) && (digit_b[i] < 0x30))    
				|| digit_b[i] > 0x39 || digit_b[i] < 0x00)
			System.out.println("方法digit_b2c只能传入数字（字符或二进制形式）参数，方法调用失败! ");
		
		if(digit_b[i] >= 0x30)
			digit_c[i] = digit_b[i];
		else
			digit_c[i] = (byte) (digit_b[i] + 0x30);	
	}
	return digit_c;
}

/**
 * 判断传来的参数中全是都是数字（都在 0x00 ~ 0x09, 0x30 ~ 0x3F的范围）
 * @param b 
 * @return 如全是数字，则true，否则false。
 */
public static boolean isdigitAll(byte[] b) {
	for(int i = 0; i < b.length; i++) {
		if(b[i] < 0x00 || b[i] > 0x3F 
		|| (b[i] > 0x09 && b[i] < 0x30))
			return false;
	}
	return true;
}

public static String bcd2Str(byte[] bytes){
	char temp[] =  new char[bytes.length*2],val;
	for (int i = 0; i < bytes.length; i++) {
		val =(char)(((bytes[i] & 0xf0) >> 4) & 0x0f);
		temp[i*2] = (char)(val > 9 ? val + 'A' - 10: val+'0');
		val =(char)(bytes[i] & 0x0f);
		temp[i*2+1] = (char)(val > 9 ? val + 'A' - 10: val+'0');
	}
	return new String(temp);
}
//===============RSA密钥对生成和签名校验==================
/*
 * 功能：生成证书私钥der编码数据，此2数据可以导出被存为文件
 * */
public static KeyPair generateKeyPair(String strAlgorithm, Provider provider, int nKeySize) throws NoSuchAlgorithmException
{
	/****生成证书，导出文件****/
	KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(strAlgorithm, provider);
	//final int KEY_SIZE = 1024;//没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
	keyPairGen.initialize(nKeySize, new SecureRandom());
	return keyPairGen.genKeyPair();
}

/*
 * 功能：从文件中取得公钥der编码数据，生成公钥对象
 * */
public static PublicKey getFilePublicKey(String strAlgorithm, String strFilePath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException
{
	KeyFactory kf = KeyFactory.getInstance(strAlgorithm);
	File file = new File(strFilePath);
	FileInputStream fis = new FileInputStream(file);
	byte bPublicKey[] = new byte[(int)file.length()];
	BufferedInputStream bis = new BufferedInputStream(fis);
	if (bis.available() > 0) {
		bis.read(bPublicKey);
	}
	bis.close();
	X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(bPublicKey);
	return kf.generatePublic(keySpecPublic);
}

/*
 * 功能：从文件中取得私钥der编码数据，生成私钥对象
 * */
public static PrivateKey getFilePrivateKey(String strAlgorithm, String strFilePath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException
{
	KeyFactory kf = KeyFactory.getInstance(strAlgorithm);
	File file = new File(strFilePath);
	FileInputStream fis = new FileInputStream(file);
	byte bPrivateKey[] = new byte[(int)file.length()];
	BufferedInputStream bis = new BufferedInputStream(fis);
	if (bis.available() > 0) {
		bis.read(bPrivateKey);
	}
	bis.close();
	PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(bPrivateKey);
	return kf.generatePrivate(keySpecPrivate);
}

/*
 * 功能：用私钥对信息签名
 * */
public static byte[] sign(String strAlgorithm, Provider provider, PrivateKey privateKey, byte[] btSignedData) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
{
    //初始一个Signature对象,并用私钥对信息签名
    java.security.Signature signet=java.security.Signature.getInstance(strAlgorithm, provider); 

    signet.initSign(privateKey); 
    signet.update(btSignedData); 
    return signet.sign(); 
}

/*
 * 功能：用私钥对信息签名
 * */
public static String sign(String strAlgorithm, Provider provider, PrivateKey privateKey, String strSignedData) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
{
    //初始一个Signature对象,并用私钥对信息签名
    java.security.Signature signet=java.security.Signature.getInstance(strAlgorithm, provider); 

    signet.initSign(privateKey); 
    signet.update(strSignedData.getBytes()); 
    return byte2hex(signet.sign()); 
}

/*
 * 功能：用公钥对签名信息进行校验
 * */
public static boolean verifySign(String strAlgorithm, Provider provider, PublicKey publicKey, byte[] btSignedData, byte[] btDigest) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
{
    //初始一个Signature对象,并用私钥对信息签名
    java.security.Signature signet=java.security.Signature.getInstance(strAlgorithm, provider); 

	signet.initVerify(publicKey);
	signet.update(btSignedData); 
    return signet.verify(btDigest); 
}

/*
 * 功能：用公钥对签名信息进行校验
 * */
public static boolean verifySign(String strAlgorithm, Provider provider, PublicKey publicKey, String strSignedData, String strDigest) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
{
    //初始一个Signature对象,并用私钥对信息签名
    java.security.Signature signet=java.security.Signature.getInstance(strAlgorithm, provider); 

	signet.initVerify(publicKey);
	signet.update(strSignedData.getBytes()); 
    return signet.verify(hex2byte(strDigest)); 
}

/**
 * 功能：10进制串转为BCD码
 * 
 * @param str 待转的字符串
 * @return bcd码编码到的byte数组
 */
public static byte[] str2bcd(String str) 
{
	if(str.length()%2!=0) str="0"+str;

	StringBuffer sb = new StringBuffer(str);   
	ByteBuffer   bb = ByteBuffer.allocate(str.length()/2);

	int i=0;
	while(i<str.length())
	{   
		bb.put((byte)((Integer.parseInt(sb.substring(i,i+1)))<<4|Integer.parseInt(sb.substring(i+1,i+2))));   
		i=i+2;
	}
	return bb.array();   
}

/**
 * ANSIX9.8格式
 * @param strPassword
 * @param strCardNo
 * @return
 */
public static byte[] pinBlock(String strPassword, String strCardNo)
{
	//PIN BLOCK - 8位
	byte[] bytesPin = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
	System.out.println("strPassword:"+strPassword);
	System.out.println("strPassword.length():"+strPassword.length());
	bytesPin[0] = (byte)strPassword.length();
	byte[] bcdPwd = str2bcd(strPassword);
	System.arraycopy(bcdPwd, 0, bytesPin, 1, bcdPwd.length);
	//PAN  - 这里没算了前面的0，是6位
	int nLength = strCardNo.length();
	if (nLength==20) {
		nLength=19;
	}
	String strCardNo12 = strCardNo.substring(nLength-13, nLength-1);
	System.out.println("pan:"+strCardNo12);
	byte[] bcdPAN = str2bcd(strCardNo12);
	//异或
	byte[] bytesPinBlock = new byte[8];
	bytesPinBlock[0] = bytesPin[0];
	bytesPinBlock[1] = bytesPin[1];
	for(int i=2;i<8;i++)
	{
		bytesPinBlock[i] = (byte)(bytesPin[i]^bcdPAN[i-2]);
	}
	return bytesPinBlock;
}

/*******
 * 功能：商赢系统编码
 * @throws NoSuchAlgorithmException 
 * */
public static String sanwingEncode(long lngID, String strPassword) throws NoSuchAlgorithmException 
{
	StringBuilder sb = new StringBuilder();
	//1)添加[ID值*2+119]
	//2)添加密码
	//3)添加[金莲花开]
	//4)进行MD5算法，得到签名串
	sb.append(lngID*2+119).append(strPassword).append("金莲花开");
	String strContent = sb.toString();
	String strMD5 = CryptoUtils.doHash("MD5", strContent);
	//System.out.println("第1次MD5的内容："+strContent);
	//System.out.println("第1次MD5的结果："+strMD5);
	//5)反转签名串
	//6)添加[万里江山万里晴，一缕尘心一缕烟。]
	//7)再做MD5算法
	sb.setLength(0);
	sb.append(strMD5);
	sb.reverse();
	sb.append("万里江山万里晴，一缕尘心一缕烟。");
	strContent = sb.toString();
	strMD5 = CryptoUtils.doHash("MD5", strContent);
	//System.out.println("第2次MD5的内容："+strContent);
	//System.out.println("第2次MD5的结果："+strMD5);
	//8)将上次的MD5值分成两份
	//9)再分别做MD5算法
	//10)合并两份的值
	String strContent1 = strMD5.substring(0, 5);
	String strContent2 = strMD5.substring(5);
	strContent = strContent1;
	strMD5 = CryptoUtils.doHash("MD5", strContent);
	sb.setLength(0);
	sb.append(strMD5);
	//System.out.println("第3次MD5的内容："+strContent);
	//System.out.println("第3次MD5的结果："+strMD5);
	strContent = strContent2;
	strMD5 = CryptoUtils.doHash("MD5", strContent);
	sb.append(strMD5);
	//System.out.println("第4次MD5的内容："+strContent);
	//System.out.println("第4次MD5的结果："+strMD5);
	//11)对所得的值再做一次MD5算法
	strContent = sb.toString();
	strMD5 = CryptoUtils.doHash("MD5", strContent);
	//System.out.println("第5次MD5的内容："+strContent);
	//System.out.println("第5次MD5的结果："+strMD5);

    return strMD5; 
}

public static String decryptDES(String data, String key) throws Exception {
	SecretKeySpec secretKey = new SecretKeySpec(hex2byte(key), "DES");
	Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
	cipher.init(Cipher.DECRYPT_MODE, secretKey);
	byte decryptedData[] = cipher.doFinal(hex2byte(data));
	return byte2hex(decryptedData);
}

public static String encryptDES(String data, String key) throws Exception {
	SecretKeySpec secretKey = new SecretKeySpec(hex2byte(key), "DES");
	Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	byte[] encryptedData = cipher.doFinal(hex2byte(data));
	return byte2hex(encryptedData);
}


/**
 * 3DES Decrypt
 */
public static String decrypt3DES(String data, String key) throws Exception{
	if(key.length() != 32){
		throw new InvalidKeyException(); 
	}
	
	String firstKey = key.substring(0, 16);
	String secondKey = key.substring(16, 32);
	String result = null;
	result = decryptDES(data, firstKey);
	result = encryptDES(result, secondKey);
	result = decryptDES(result, firstKey);
	
	return result;
}
/**
 * 3DES Encrypt
 */
public static String encrypt3DES(String data, String key) throws Exception{
	if(key.length() != 32){
		throw new InvalidKeyException(); 
	}
	
	String firstKey = key.substring(0, 16);
	String secondKey = key.substring(16, 32);
	String result = null;
	result = encryptDES(data, firstKey);
	result = decryptDES(result, secondKey);
	result = encryptDES(result, firstKey);
	
	return result;
}

public static byte[] str2Bcd(String asc) {
    int len = asc.length();
    int mod = len % 2;

    if (mod != 0) {
     asc = "0" + asc;
     len = asc.length();
    }

    byte abt[] = new byte[len];
    if (len >= 2) {
     len = len / 2;
    }

    byte bbt[] = new byte[len];
    abt = asc.getBytes();
    int j, k;

    for (int p = 0; p < asc.length()/2; p++) {
     if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
      j = abt[2 * p] - '0';
     } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
      j = abt[2 * p] - 'a' + 0x0a;
     } else {
      j = abt[2 * p] - 'A' + 0x0a;
     }

     if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
      k = abt[2 * p + 1] - '0';
     } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
      k = abt[2 * p + 1] - 'a' + 0x0a;
     }else {
      k = abt[2 * p + 1] - 'A' + 0x0a;
     }

     int a = (j << 4) + k;
     byte b = (byte) a;
     bbt[p] = b;
    }
    return bbt;
}

public static Vector<String> genRandomKey(int keySize) throws Exception{
	Vector<String> vector = new Vector<String>(2);
	 String[] elements =
	 {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	 Random random = new Random();
	String randomKey = "";
	 for (int i = 0; i < keySize; i++) {
	 randomKey += elements[random.nextInt(16)];
	 }
	 System.out.println("randomKey:"+randomKey);
	 vector.addElement(randomKey);
	 String checkValue = "";
	 if (keySize == 32) {
		 checkValue = CryptoUtils.encrypt3DES("0000000000000000", randomKey);
	}else {
		checkValue = CryptoUtils.encryptDES("0000000000000000", randomKey);
	}
	 System.out.println("checkValue:"+checkValue);
	 vector.addElement(checkValue);
	 return vector;
}
public static void main(String[] args) throws IllegalArgumentException, Exception
{
	String pwdString =  CryptoUtils.byte2hex(CryptoUtils.pinBlock("158507", "0000015850767126"));
	System.out.println("pwdString:"+pwdString);
	System.out.println(MD5Util.MD5(pwdString));
	System.out.println(MD5.sign("158507", "hfejMDtMOr"));
	genRandomKey(16);
//	System.out.println("t:"+19/2);
//	
//	System.out.println("z:"+CryptoUtils.encrypt3DES("0A106226662001059403C2256226662001059403D49121200000000617623000", "4E63EDD1526316024B81ABAA8EFE4B13"));
////	String a="sdfafd000001";
////	System.out.println(a.substring(a.length()-6,a.length()));
////	String a = "0000000000000000";
//	String temp = "5802ECBA64D3D3C1";
////////	System.out.println(CryptoUtils.encrypt3DES(a, temp));
//	String mab= "04003020048002C08011000000000000000100000002021000393831303030313238333130323330313035373132303030313135360011220000010000";
//	System.out.println("1:"+getMac(hex2byte(temp), hex2byte(mab)));
//	System.out.println("2:"+getMacForJsYs(temp, hex2byte(mab)));
//	8B4C7A1B
	
//	System.out.println(MyHSM.getCardPwd("6217850800000345239", "06225003FFFCBADC"));
	
//	System.out.println(getEncCDataForYj("E6EA1B7CC8BC82FFAF23BCFD3DE82E5F","9559970030000000215=00002101815546"));
//	System.out.println(getEncCData("3B073701AD40CDE0866B3854DFF75134","6200485046794218D49121201337700319"));
//	System.out.println(getMacBy919("C1FEE5D3F49D8A61B50410200D0404C8", hex2byte("6200485046794218310000000027122557060301563230313430343233")));
	
//	String tmk= "11111111111111112222222222222222";
//	String pinkey = CryptoUtils.decrypt3DES("A4427A2756D19C79315551E74BB114A5", tmk);
//	System.out.println(CryptoUtils.encrypt3DES("0000000000000000", pinkey));
	
//	BigDecimal transAt = new BigDecimal("000000001000").movePointLeft(2);
//	System.out.println(transAt);
//	System.out.println(transAt.compareTo(new BigDecimal(11.00)));
	
	System.out.println("test:"+pwdFromPinBlock(hex2byte("06460804FFFFFFFF"), "0000000000000000000"));
	
//	String key = "88888888888888888888888888888888";
//	String data = "6201180000002091=1712520273939996927900000000000";
//	String data2 = CryptoUtils.byte2hex(data.getBytes());
//	System.out.println(data2);
//	
//	String result = CryptoUtils.encrypt3DES(data2, key);
//	System.out.println(result);
//	
//	String decodeString = CryptoUtils.decrypt3DES(result, key);
//	System.out.println(decodeString);
//	System.out.println(new String(hex2byte(decodeString)));
//	
//	System.out.println(CryptoUtils.getEncCDataForYj("9A39109E94D213A1B773752D38FE016F", "9559970030000000215=00002101815546"));
//	String macData = "{\"VER\":\"66\",\"PR\":\"2002\",\"PSEQ\":\"000008\",\"PAN\":\"621279******1797\",\"AMT\":\"0.01\",\"EXP\":\"\",\"SVR\":\"022\",\"ICSEQ\":\"\",\"SENS\":\"8HrNHO5rXy_wTeoUPvQMJYyxdoFWa2gS5rX-iZ6H3mSlSuevhK34GZl6nGI0ewau_cZ-umxJh14EjH07-5wR0xxupfBTKkPk9aU7vtV7SdvwSatkwbBEocHgOjON4CQBU09I9KfmT6CxP7FCp9omfw==\",\"REF\":\"005801003411\",\"POSID\":\"800002628\",\"ORDID\":\"2015012600000004\",\"IC\":\"\",\"ORG\":\"2015012600000003\"}";
	String macData = "04003020048102C08091000000000000000001000322021000031020303639303033393134393835373431303037333131363030313135360031373530383330313634313737373631355A30373030303030303134313836330011220000010000";
////	macData="08302038009002F000019100000000011545371205000030303130303036393538383232313030303539373030303133C8CECEC4D7D3B2E2CAD4323031343131313220202020202020202020202020202020202020202020245AB8026E961C7B6BC6AF09B354F9BEADC437F067078A2F3D";
////	byte[] mak = CryptoUtils.symmetricDecrypto(CryptoUtils.hex2byte("31313131313131313131313131313131"), CryptoUtils.hex2byte("5603C7B803D53832"));
////	System.out.println(CryptoUtils.byte2hex(mak));
	String mk ="0CA703225A00F4851CCF4C2CDEB22B10";
	mk="793E6D0D231A895E";
////	mk="70DFFDF4A2CD79F4";
	String mac = CryptoUtils.getMac(hex2byte(mk), hex2byte(macData));
	System.out.println("mac:"+mac);
	
//	mk="3833353638373246393646303545383446324538334533363135413439314335";
//	byte[] test = CryptoUtils.hex2byte(mk);
//	System.out.println(new String(test));
//	
//	System.out.println("pinBlock:"+byte2hex(pinBlock("777777", "6212790200001797")));
//	System.out.println("mima:"+pwdFromPinBlock(hex2byte("067750E7DFFFFE86"), "6212790200001797"));
////	System.out.println(encrypt3DES("800002628|2015012300000001|000000000001|6212790200001797|6212790200001797=12015208084198109646|||CFBCD9C8A0F4776D|000000", "D328FBEE0E85D095C4A15F8A3DDCF676"));
//	System.out.println("result:"+encrypt3DES(byte2hex("900005828|20150204900005828000023|0.01|5527422021018162|5527422021018162=18061010000025200000|||2DFCCD5F276DB5DF|0000000".getBytes()), "DC62F74A38B9F27C5208D9E670E534F7"));
//	System.out.println(new String(hex2byte("B7C7B1B1BEA9D2F8D0D0BFA8")));
	
	
//	String test = "2FB404500C5B4298B506763A4915DF98B2EB27B1D1CF49C1E27DC210ECF949675108EFEB4F08111E6E8F86414973F0C6B2EB27B1D1CF49C1A7255DDF9DA10E14B81375BAB2B039875108EFEB4F08111E5108EFEB4F08111EE0222EF7E00A9E6B8E0979A4F59BE1E5";
//	System.out.println(CryptoUtils.decrypt3DES(test, "7438718866BDAE778856DAAEE1360D50"));
//	System.out.println("pinBlock:"+byte2hex(pinBlock("777777", "6212790200001797")));
//	
//	System.out.println(byte2hex(CryptoUtils.symmetricEncrypto(CryptoUtils.hex2byte(mk), "AA84496E".getBytes())));
//	System.out.println(byte2hex(CryptoUtils.XOR(hex2byte("A79B20232273AD88"), ("A41077EB").getBytes())));
	
//	String pk = "11111111111111111111111111111111";
//	byte[] test = CryptoUtils.symmetricEncrypto(CryptoUtils.hex2byte("73575E29D00B19757ADC854C23D0EAE6"), CryptoUtils.pinBlock("111111", "6212790200001797"));
//	System.out.println(CryptoUtils.byte2hex(test));
//	
//	System.out.println(CryptoUtils.encrypt3DES("06113681DFFFFE86", "73575E29D00B19757ADC854C23D0EAE6"));
//	String test = "375289311350074334D160810100000396000000";
//	byte[] abc = CryptoUtils.hex2byte(test);
//	System.out.println(CryptoUtils.bcd2Str(abc));
//	System.out.println(byte2hex(CryptoUtils.pinBlock("042435", "9800080100990002615")));
	
//	byte[] edata = hex2byte("DA97CEA3AAF3A5DF");
//	byte[] temp = "9376054AFF194F00".substring(8, 16).getBytes();
//	for(int j=0;j<8;j++)
//	{
//		temp[j] ^= edata[j];
//	}
//	System.out.println("temp:"+byte2hex(temp));
	System.out.println("trk:"+getEncCDataForJsYs("1A186A9A5DDE1283AA574FF3418BE80C",hex2byte("6226662001059403D491212000000006176230")));
//	String teString = "3932344239393243";
//	System.out.println(Integer.toHexString(teString.length()/2));
	
//	byte[] pik = CryptoUtils.symmetricDecrypto(CryptoUtils.hex2byte("31313131313131313131313131313131"), CryptoUtils.hex2byte("AF249E2052D06B50"));
//	System.out.println(byte2hex(pik));
//	
//	byte[] r = CryptoUtils.symmetricEncrypto(pik, CryptoUtils.pinBlock("111111", "6229930100200000034"));
//	System.out.println(byte2hex(r));
//	String mab = "0200302004C020C0981100000000000000000100058102100012376212790100003752D120152061636204505910313930353730303131303232393030373031313030303631353684E85F9FE6FE36B92000000000000000001422000111000500";
//	String mab2 = "0200302004C020C0981000000000000000000100005002100006376229930100200000034D49121200001820000031393035373030313130323239303037303131303030363135365B424031343632372000000000000000000822000111";
//	String maString = getMac(mak, hex2byte(mab));
//	System.out.println("ma:"+maString);
//	String xorResult = "BC807B7C593C97C0";
//	String masterKey = "BAD9E7710DF78365";
//	String data = "BC807B7C";
	
//	84E85F9FE6FE36B9
//	String r="3933443141393731";
//	System.out.println(new String(hex2byte(r)));
	
//	String result = "9D75F7CE2904CD26C4CB540DC1E32970";
//	try {
//		System.out.println(byte2hex(symmetricEncrypto(hex2byte(masterKey), data.getBytes())));
////		System.out.println(byte2hex(symmetricEncrypto(hex2byte(result), "00000000")));
//	} catch (IllegalArgumentException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	} catch (Exception e1) {
		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
//	System.out.println(byte2hex(decryptMode(hex2byte(masterKey+masterKey.substring(0,16)), hex2byte(data))));
	
//	String tpdu = "6000020000";
//	String lt = "0066";
//	System.out.println(CryptoUtils.hex2byte(tpdu));
//	byte[] a =StringUtils.str2Bcd(tpdu);
//	byte[] b = StringUtils.str2Bcd(lt);
//	System.out.println(CryptoUtils.byte2hex(b));
//	System.out.println(StringUtils.str2Bcd(tpdu));
//	System.out.println(CryptoUtils.byte2hex(a));
//	
//	System.out.println(b[0]);
//	System.out.println(b[1]);
//	System.out.println(CryptoUtils.byte2hex(CryptoUtils.hex2byte(tpdu)));
//	System.out.println(a[0]);
//	System.out.println(a[1]);
	
	/*
	String strKey = "22222222";
	String strData = "3333333333333333";
	byte[] byteKey = com.lmx.util.ByteUtils.asc2num(strKey);
	byte[] byteData = com.lmx.util.ByteUtils.asc2num(strData);
	//System.out.println(strData);
	//System.out.println(com.lmx.util.ByteUtils.bcd2asc(byteData));
	for(int i=0;i<byteKey.length;++i)
	{
		//System.out.println(byteKey[i]);
	}

	byte[] byteRet = null;//new byte[byteData.length];
	byte[] byteRet1 = null;
	try
	{
		byteRet = CryptoUtils.symmetricEncrypto(byteKey,byteData);
		byteRet1 = CryptoUtils.symmetricDecrypto(byteKey,byteRet);
	}
	catch(Exception ex)
	{
		ex.printStackTrace(System.err);
	}
	System.out.println(byteRet.length);
	System.out.println(byteRet1.length);
	for(int i=0;i<byteRet.length;++i)
	{
		System.out.print(byteRet[i]);
		System.out.print(" " + Integer.toHexString(byteRet[i]) +" ");
		System.out.println(byteRet1[i]);
	}*/

	try {

//		/***测试sanwingEncode操作花费的时间长短***/
//		long lngID = 100;
//		System.out.println(DateUtils.currentDate("yyyy-MM-dd hh:mm:ss.SSS"));
//		for(int i=0;i<10000;++i)
//		{
//			String strPwd = StringUtils.genRandomStr(8*2, 3, i);
//			String strEncPwd = CryptoUtils.sanwingEncode(lngID+i, strPwd);
//			System.out.println("strPwd:" +strPwd+" encode:"+strEncPwd);
//		}
//		System.out.println(DateUtils.currentDate("yyyy-MM-dd hh:mm:ss.SSS"));

//		//MD5出来的结果是128bit的大整数，占用16个字节(每个字节8bit，所以是8*16=128bit)
//		byte[] byteRet = CryptoUtils.doHash("MD5", "11111111".getBytes());
//		System.out.println(byteRet.length);
//
//		String strRet = CryptoUtils.doHash("MD5", "11111111");
//		System.out.println(strRet);
//		System.out.println(strRet.length());
//
//		byte[] byteRet1 = CryptoUtils.symmetricEncrypto("11111111".getBytes(), "222222223333333344444444".getBytes());
//		System.out.println(new String(byteRet1));
//		System.out.println(byteRet1.length);

/*
		String strAlgorithm = "RSA";
		Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();		

		byte[] privateKeyBytes = null;
		byte[] publicKeyBytes = null;
		KeyPair keyPair = CryptoUtils.generateKeyPair(strAlgorithm, provider, 1024);
		privateKeyBytes = keyPair.getPrivate().getEncoded();
		publicKeyBytes = keyPair.getPublic().getEncoded();
		FileUtils.logOnce("C:/test.key",privateKeyBytes);
		FileUtils.logOnce("C:/test.der",publicKeyBytes);

		PublicKey publicKey = CryptoUtils.getFilePublicKey(strAlgorithm, "C:/test.der");
		PrivateKey privateKey = CryptoUtils.getFilePrivateKey(strAlgorithm, "C:/test.key");

		String myinfo = "12345678asdfasdfadsfasdvfasdvvcszxvvdsadfasfsdafa用他私人密钥(prikey)对他所确认的信息(info)进行数字签名产生一个签名数组12345678asdfasdfadsfasdvfasdvvcszxvvdsadfasfsdafa用他私人密钥(prikey)对他所确认的信息(info)进行数字签名产生一个签名数组";
		byte[] signed = CryptoUtils.sign("MD5withRSA", provider, privateKey, myinfo.getBytes());
		System.out.println( CryptoUtils.verifySign("MD5withRSA", provider, publicKey, myinfo.getBytes(), signed) );
*/

		/*
		String strAlgorithm = "RSA";
		Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();		

		byte[] privateKeyBytes = null;
		KeyFactory kf = KeyFactory.getInstance("RSA");
		//String strPrivateKeyPath = "D:/cert/CNCA_Server_UCINFO.der";
		//File file = new File(strPrivateKeyPath);
		//FileInputStream fis = new FileInputStream(file);
		//byte bPrivateKey[] = new byte[(int)file.length()];
		//BufferedInputStream bis = new BufferedInputStream(fis);
		//if (bis.available() > 0) {
			//bis.read(bPrivateKey);
		//}
		//bis.close();
		String strPrivateKey = "MIICWwIBAAKBgQDbPlPiZW5ztXih1mjEwyiJcRUZ2JcSro5I+khIXY9mqsHKtAMg0Y9EdOPaFMSX+FAfqXsSUjGvB4dhqM9nSATrWokHix3McVoRiWbat76PFC4/0dLt5+4GSdGVPxvlofe8IBu/76+MfABnco+hIrUKhvbd6w0e7QqkRXfGLEv9qwIDAQABAoGATCL18I428DnZ/aG+2m3HmwtJP6OGuzchNFiuV2seTJIKIIGpAHGl5ikNo4NIzlmuQYCSu8cNd/IOYqzD8eMBybEoYQIlVeetCyMHf3qkqz7n3I7jiqQqhH9OnwGmRUY9N01BeUon5IW6DykexryNtZnzwMqpL7vew+iiQFUW/jkCQQDtLGA7qoCgGoKcSV6nNo6Wd23i/2cDNRv/4QQoM5uHwiCPoq/5ZKW5TavBfco7ujSnV8t8Ah/E7ugofGz/nWkNAkEA7KWYzi5a0o28b589QKN4tNdBQpdalBTc0LoK8HJDDiJsWZVviCLOCoMTTXpcOYDp1PFYTbQIHBQLIMVM919jlwJAXKMem2+HegGgzmah//IBU6gmVamolMoju5gqRYGS2D3s9e13ipPTX0OFOLhnS1j5w/s40aEKw6MFLvsan+Td6QJAZOJ30TYby/VWpvNo5nyKQfYeQShLGiQGuwkFDtp3UbhFHq/BO3UzB3I3sWj/lhgqF3jIH2AnsOiukcrq8+57gwJAImfX9VNNyTFgleKTJjvHq2n9bYo8vPmI43Dxqi/saAF9a5qAvmskLmnOQ28RQEurFpNF6QG2ezYEzLZWKVWCeQ==";
		byte bPrivateKey[] = Base64.decode(strPrivateKey);//CryptoUtils.hex2byte(strPrivateKey);
		PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(bPrivateKey);
		PrivateKey privateKey = kf.generatePrivate(keySpecPrivate); 
		privateKeyBytes = privateKey.getEncoded();
		FileUtils.logOnce("C:/CNCA_Server_UCINFO.key",privateKeyBytes);

		privateKey = CryptoUtils.getFilePrivateKey(strAlgorithm, "C:/CNCA_Server_UCINFO.key");

		String myinfo = "12345678asdfasdfadsfasdvfasdvvcszxvvdsadfasfsdafa用他私人密钥(prikey)对他所确认的信息(info)进行数字签名产生一个签名数组12345678asdfasdfadsfasdvfasdvvcszxvvdsadfasfsdafa用他私人密钥(prikey)对他所确认的信息(info)进行数字签名产生一个签名数组";
		byte[] signed = CryptoUtils.sign("MD5withRSA", provider, privateKey, myinfo.getBytes());
				*/
	} catch (Exception e) {
		e.printStackTrace(System.out);
	}

}

} 

