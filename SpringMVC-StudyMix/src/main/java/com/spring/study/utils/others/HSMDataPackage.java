package com.spring.study.utils.others;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maikong.utils.exceptions.PreposeException;

/**
 * 
 * 加密机数据操作类别
 */
public class HSMDataPackage {

	private static Logger logger = LoggerFactory
			.getLogger(HSMDataPackage.class);

	/* 版本号 */
	private byte _Version = 1;
	private ByteArrayOutputStream _ByteArrayBuffer = new ByteArrayOutputStream();
	// 为了保持长连接的通讯，使用static
	private DataInputStream _InputFromHSM = null;
	private DataOutputStream _OutToHSM = null;
	// HSM服务器地址和端口
	// private static String _HSMHost = "10.200.250.150";
	private static String _HSMHost = "";
	// private static int _nPort = 5555;
	private static int _nPort = 0;
	// From PIK
	// private String _FromEncPIK =
	// "X3C717BA764EC211DC40E826AB7405C74 X504F06B957160D280427AE34693C1D8B";

	private static String _ZMK = "";
	private static String BJ_ZMK = "";
	private static String AXL_ZMK = "";
	private static String YC_ZMK = "";
	private static String _ZPKbyLMK = "";
	private static String _ZAKbyLMK = "";

	private static String _FromEncPIK = "";

	public void setFromEncPIK(String fromEncPIK) {
		_FromEncPIK = fromEncPIK;
	}

	private static String _FromTMNKey01 = "";
	private static String _FromTMNKey02 = "";

	public static String getFromTMNKey(String strFromCode) {
		switch (NumberUtils.parseAsInt(strFromCode, 0)) {
		case 1:
			return _FromTMNKey01;
		case 2:
			return _FromTMNKey02;
		default:
			return null;
		}
	}

	private static String _FromEncPIK01 = "";
	private static String _FromEncPIK02 = "";

	public static String getFromEncPIK(String strFromCode) {
		switch (NumberUtils.parseAsInt(strFromCode, 0)) {
		case 1:
			return _FromEncPIK01;
		case 2:
			return _FromEncPIK02;
		default:
			return null;
		}
	}

	private static String _FromMACKey01 = "";
	private static String _FromMACKey02 = "";

	public static String getFromMACKey(String strFromCode) {
		switch (NumberUtils.parseAsInt(strFromCode, 0)) {
		case 1:
			return _FromMACKey01;
		case 2:
			return _FromMACKey02;
		default:
			return null;
		}
	}

	// Local PIK
	// private String _LocalEncPIK = "X8B4ECCAE01B4B17A8B4ECCAE01B4B17A";
	private static String _LocalEncPIK = "";

	public void setLocalEncPIK(String localEncPIK) {
		_LocalEncPIK = localEncPIK;
	}

	// To PIK
	private static String _ToEncPIK = "";

	// public static String _FixCardDataZEK =
	// "X1031CAFD652E389DB2C0636A4578460C";//为加密卡磁道信息所专用，不能用每次产生的那个ZEK，因为那个是每次变动的
	// public static String _FixCardDataZAK =
	// "X6BD0404B2683C660CCB63483635814B3";//为加密卡磁道信息所专用，不能用每次产生的那个ZEK，因为那个是每次变动的
	public static String _FixCardDataZAK = "";

	public static String _MakeCardZMK = "";
	public static String _TMNZEK = "";

	/**
	 * @return the _TMNZEK
	 */
	public static String getTMNZEK() {
		return _TMNZEK;
	}

	/**
	 * @param _TMNZEK
	 *            the _TMNZEK to set
	 */
	public static void setTMNZEK(String _TMNZEK) {
		HSMDataPackage._TMNZEK = _TMNZEK;
	}

	public HSMDataPackage() {
		super();
		// TODO Auto-generated constructor stub
		if (_HSMHost.equals("")) {
			// _HSMHost = "192.168.18.44";
			// _nPort = 7600;
			java.util.Properties properties = new java.util.Properties();
			FileInputStream input;
			try {
				InputStream is = HSMDataPackage.class.getClassLoader()
						.getResourceAsStream("hsm_sw.property");
				// input = is;
				properties.load(is);
				_HSMHost = properties.getProperty("host");
				_nPort = Integer.parseInt(properties.getProperty("port"));
				// _FromTMNKey01 = properties.getProperty("from_TMK01");
				// _FromEncPIK01 = properties.getProperty("from_PIK01");
				// _FromMACKey01 = properties.getProperty("from_MAK01");
				// _FromEncPIK = properties.getProperty("from_PIK");
				// _LocalEncPIK = properties.getProperty("local_PIK");
				// //_ToEncPIK
				// BJ_ZMK = properties.getProperty("bj_tmn_ZMK");
				// AXL_ZMK = properties.getProperty("axl_tmn_ZMK");
				// YC_ZMK = properties.getProperty("yc_tmn_ZMK");
				// // _ZMK =
				// properties.getProperty("bj_"+MessageTrans.termId+"_ZMK");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* 功能：接收和解析数据 */
	public byte[] receive(DataInputStream input) throws IOException {
		int nLen = 0;
		byte[] bBufferHeader = new byte[2];
		nLen = input.read(bBufferHeader);
		if (nLen <= 0)
			return null;
		// System.out.println("bBufferHeader[0]:"+bBufferHeader[0]);
		// System.out.println("bBufferHeader[1]:"+bBufferHeader[1]);

		_ByteArrayBuffer.reset();
		// nLen = bBufferHeader[0]*0x100+bBufferHeader[1];//error
		nLen = (bBufferHeader[0] << 8) | (0xFF & bBufferHeader[1]);// 必须用位运算0xFF来过滤掉字节前面附加的符号位，否则二进制表达时前面有1的就当作负数来处理了，+或|操作的时候都会出错
		// System.out.println("receive Data nLen:"+nLen);

		byte[] bBufferData = new byte[nLen];
		int nReadTotal = 0;
		int nReadLen = 0;
		while (nReadTotal < nLen && 0 < (nReadLen = input.read(bBufferData))) {
			_ByteArrayBuffer.write(bBufferData, 0, nLen);
			nReadTotal += nReadLen;
		}

		return _ByteArrayBuffer.toByteArray();
	}

	/* 功能：不打包直接输出数据 */
	public void send(DataOutputStream output, byte[] bBuffer, int nLength)
			throws IOException {
		output.flush();
		output.write(bBuffer, 0, nLength);
		output.flush();
	}

	/* 功能：制作请求数据包，把各域的字符串拼接起来 */
	public static byte[] makeRequestData(Vector vecData) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		Iterator ite = vecData.iterator();
		while (ite.hasNext()) {
			String strFieldValue = (String) ite.next();
			byteArrayRetData.write(strFieldValue.getBytes());
		}

		return byteArrayRetData.toByteArray();
	}

	/* 功能：添加空域到vecData */
	public static void appendEmptyTradeField(Vector vecData, int nStart,
			int nEnd) {
		for (int i = nStart; i <= nEnd; ++i) {
			vecData.add("");
		}
	}

	/* 功能：制作数据请求的包头--包头长度请求 */
	/**
	 * @param byteData
	 *            待发送的数据包体
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestHeader(byte[] byteData) throws IOException {
		int nSize = byteData.length;
		byte[] bRet = new byte[2];
		bRet[0] = (byte) (0xFF & (nSize >> 8));
		bRet[1] = (byte) (0xFF & nSize);

		return bRet;
	}

	/* 功能：制作数据请求的包头--包头长度请求 */
	/**
	 * @param byteData
	 *            待发送的数据包体
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeHexRequestHeader(byte[] byteData)
			throws IOException {
		int nSize = byteData.length / 2;
		byte[] bRet = new byte[2];
		bRet[0] = (byte) (0xFF & (nSize >> 8));
		bRet[1] = (byte) (0xFF & nSize);

		return bRet;
	}

	/* 功能：输入一个密钥 */
	/**
	 * @param zpk
	 *            zmk加密的zpk
	 * @param zak
	 *            银联返回的pik和mak
	 * @param pType
	 *            001-zpk ;008-zak
	 * @param flag
	 *            X-双倍长 ;Z-单倍长
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestA6(String zmk, String zak, String pType,
			String flag) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "A6";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(pType.getBytes());
		byteArrayRetData.write(zmk.getBytes());
		byteArrayRetData.write(zak.getBytes());
		byteArrayRetData.write(flag.getBytes());

		return byteArrayRetData.toByteArray();
	}

	public static byte[] makeRequestEI() throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "EI";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("11024010101".getBytes());
		byteArrayRetData.write("65537".getBytes());
		return byteArrayRetData.toByteArray();
	}

	public static Vector<String> parseResponseEJ(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		logger.info("RSA Response:" + CryptoUtils.byte2hex(bBuffer));
		Vector<String> vecData = new Vector<String>(3);
		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-返回密钥
		// vecData.addElement(CryptoUtils.byte2hex(b));

		return vecData;
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseA7(byte[] bBuffer, String type)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(3);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-返回密钥
		if ("2".equals(type)) {
			vecData.addElement(new String(bBuffer, 4, 33));
		} else {
			vecData.addElement(new String(bBuffer, 4, 16));
		}

		return vecData;
	}

	public static Vector<String> parseResponseA7(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(3);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-返回密钥
		vecData.addElement(new String(bBuffer, 4, 16));

		return vecData;
	}

	/* 功能：制作请求数据包--BA--加密明文pin请求 */
	/**
	 * @param strPassword
	 *            待加密的密码明文
	 * @param strCard12
	 *            卡号后面除了校验位的12位
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestBA(String strPassword, String strCard12)
			throws IOException {
		String strCommand = "BA";
		Vector<String> vecData = new Vector<String>(3);
		vecData.add(strCommand);
		StringBuilder sb = new StringBuilder();
		sb.append(strPassword).append(
				"FFFFFFF".substring(0, 7 - strPassword.length()));
		vecData.add(sb.toString());
		vecData.add(strCard12);

		return makeRequestData(vecData);
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseBB(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(3);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-PIN
		vecData.addElement(new String(bBuffer, 4, 7));

		return vecData;
	}

	/* 功能：制作请求数据包--IA--2.4.1 生成一个ZPK（IA/IB） */
	/**
	 * @param strZMK
	 *            - 终端主密钥
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestIA(String strZMK, String strZMKStrategy,
			String strLMKStrategy) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "IA";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strZMK.getBytes());
		byteArrayRetData.write(";".getBytes());
		byteArrayRetData.write(strZMKStrategy.getBytes());
		byteArrayRetData.write(strLMKStrategy.getBytes());
		byteArrayRetData.write("0".getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseIB(byte[] bBuffer, int strKeyLenType) {
		Vector vecData = new Vector(5);
		logger.debug("tpkByLmk:"+new String(bBuffer));
		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		if ("00".equals(vecData.get(1))) {
			// 3-当前ZMK密钥下加密的新密钥
			vecData.addElement(new String(bBuffer, (strKeyLenType == 1 ? 4 : 5),
					(strKeyLenType == 1 ? 16 : 32)));
			// 4-LMK下加密的新密钥
			vecData.addElement(new String(bBuffer, (strKeyLenType == 1 ? 20 : 37),
					(strKeyLenType == 1 ? 16 : 33)));
			// 5-密钥校验值
			vecData.addElement(new String(bBuffer, (strKeyLenType == 1 ? 36 : 70),
					(strKeyLenType == 1 ? 16 : 16)));
		}
		

		return vecData;
	}

	/* 功能：制作请求数据包--JG--将PIN从LMK翻译到ZPK(LMK)请求 */
	/**
	 * @param strPassword
	 *            待加密的密码明文
	 * @param strCard12
	 *            卡号后面除了校验位的12位
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestJG(String strEncPIK, String strEncPassword,
			String strCard12) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "JG";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strEncPIK.getBytes());
		byteArrayRetData.write("01".getBytes());
		byteArrayRetData.write(strCard12.getBytes());
		byteArrayRetData.write(strEncPassword.getBytes());

		return byteArrayRetData.toByteArray();// makeRequestData(vecData);
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseJH(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(3);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-PIN
		vecData.addElement(new String(bBuffer, 4, 16));

		return vecData;
	}

	/* 功能：制作请求数据包--CC--pin转加密：终端ZPK->后台ZPK(LMK)请求 */
	/**
	 * @param strPassword
	 *            待加密的密码明文
	 * @param strCard12
	 *            卡号后面除了校验位的12位
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestCC(String strInEncPIK, String strOutEncPIK,
			String strEncPassword, String strCard12) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "CC";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strInEncPIK.getBytes());
		byteArrayRetData.write(strOutEncPIK.getBytes());
		byteArrayRetData.write("12".getBytes());
		byteArrayRetData.write(strEncPassword.getBytes());
		byteArrayRetData.write("01".getBytes());
		byteArrayRetData.write("01".getBytes());
		byteArrayRetData.write(strCard12.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* 功能：制作请求数据包--CC--pin转加密：终端ZPK->后台ZPK(LMK)请求 */
	/**
	 * @param strPassword
	 *            待加密的密码明文
	 * @param strCard12
	 *            卡号后面除了校验位的12位
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestCC(String strInEncPIK, String strOutEncPIK,
			String strEncPassword, String strCard12, String strSrcFormat,
			String strDstFormat) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "CC";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strInEncPIK.getBytes());
		byteArrayRetData.write(strOutEncPIK.getBytes());
		byteArrayRetData.write("12".getBytes());
		byteArrayRetData.write(strEncPassword.getBytes());
		byteArrayRetData.write(strSrcFormat.getBytes());
		byteArrayRetData.write(strDstFormat.getBytes());
		byteArrayRetData.write(strCard12.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseCD(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(4);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));

		if ("00".equals(new String(bBuffer, 2, 2))) {
			// 3-密码长度06
			vecData.addElement(new String(bBuffer, 4, 2));
			// 4-PIN
			vecData.addElement(new String(bBuffer, 6, 16));
			// 5-
			vecData.addElement(new String(bBuffer, 22, 2));
		}

		return vecData;
	}

	/* 功能：制作请求数据包--CA--pin转加密：终端TPK->后台ZPK(LMK)请求 */
	/**
	 * @param strPassword
	 *            待加密的密码明文
	 * @param strCard12
	 *            卡号后面除了校验位的12位
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestCA(String strInEncTPK, String strOutEncZPK,
			String strEncPassword, String strCard12, String strSrcFormat,
			String strDstFormat) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "CA";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strInEncTPK.getBytes());
		byteArrayRetData.write(strOutEncZPK.getBytes());
		byteArrayRetData.write("12".getBytes());
		byteArrayRetData.write(strEncPassword.getBytes());
		byteArrayRetData.write(strSrcFormat.getBytes());
		byteArrayRetData.write(strDstFormat.getBytes());
		byteArrayRetData.write(strCard12.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseCB(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(4);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-密码长度
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-错误代码(要注意：这个指令里密码长度和错误代码的位置和CC/CD指令里的位置是反的)
		vecData.addElement(new String(bBuffer, 4, 2));
		// 4-PIN
		vecData.addElement(new String(bBuffer, 6, 16));
		// 5-
		vecData.addElement(new String(bBuffer, 22, 2));

		return vecData;
	}

	/* 功能：制作请求数据包--34--产生一对公私钥密钥对 */
	/**
	 * @param nKeyLen
	 * @param nKeyIndex
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequest34(int nKeyLen, int nKeyIndex)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "34";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(StringUtils.Int2String(nKeyLen, 4).getBytes());
		byteArrayRetData.write(StringUtils.Int2String(nKeyIndex, 2).getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponse35(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-私钥长度
		String strPrivateKeyLen = new String(bBuffer, 4, 4);
		vecData.addElement(strPrivateKeyLen);
		System.out.println("strPrivateKeyLen:" + strPrivateKeyLen);
		// 4-私钥密文
		int nPrivateKeyLen = Integer.parseInt(strPrivateKeyLen);
		byte[] bBufferKey = new byte[nPrivateKeyLen];
		System.arraycopy(bBuffer, 8, bBufferKey, 0, nPrivateKeyLen);
		vecData.addElement(bBufferKey);
		// 5-公　钥：ANS.1 DER编码方式
		// 长度看测试的例子，长度为270字节，一般经过16进制编码后成字符的长度是512，实际表达的长度是256，差了14字节，是因为前有9位后有5位加密机附加的特殊字符串
		int nPublicKeyLen = 140;// 270;
		byte[] bBufferPublicKey = new byte[nPublicKeyLen];
		System.out.println("bBufferPublicKey index:" + (8 + nPrivateKeyLen));
		System.out.println("bBufferPublicKey end:"
				+ (8 + nPrivateKeyLen + nPublicKeyLen));
		System.arraycopy(bBuffer, 8 + nPrivateKeyLen, bBufferPublicKey, 0,
				nPublicKeyLen);
		vecData.addElement(bBufferPublicKey);

		return vecData;
	}

	/*功能：制作请求数据包--FI--生成ZEK/ZAK请求*/
	/**
	 * @param strZEKZAKFlag
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestFI(String strZEKZAKFlag, String strZMK, String strZMKStrategy, String strLMKStrategy) throws IOException
	{
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "FI";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strZEKZAKFlag.getBytes());
		//String strZMK = "5C6FF58D938060B4288A45E9D4E74BEB";
		byteArrayRetData.write(strZMK.getBytes());
		//byteArrayRetData.write(";XX0".getBytes());
		byteArrayRetData.write(";".getBytes());
		byteArrayRetData.write(strZMKStrategy.getBytes());
		byteArrayRetData.write(strLMKStrategy.getBytes());
		byteArrayRetData.write("0".getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包，指定产生的密钥长度和校验码长度
	 * 参数：
	 * bBuffer   - 要解析的数据缓冲区
	 * */
	public static Vector<String> parseResponseFJ(byte[] bBuffer, int nKeyLen, int nVerifyCodeLen) throws java.util.NoSuchElementException
	{
		Vector<String> vecData = new Vector<String>(5);

		//1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		//2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		if ("00".equals(vecData.get(1))) {
			//3-1A+32H的ZEK传输
			vecData.addElement(new String(bBuffer, 4, nKeyLen));
			//4-1A+32H的ZEK存储
			int nPos = 4+nKeyLen;
			vecData.addElement(new String(bBuffer, nPos, nKeyLen));
			//5-16H的ZEK密码校验
			nPos += nKeyLen;
			vecData.addElement(new String(bBuffer, nPos, nVerifyCodeLen));
		}
		

		return vecData;
	}

	
	/* 功能：制作请求数据包--FI--生成ZEK/ZAK请求 */
	/**
	 * @param strZEKZAKFlag
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestFI(String strZEKZAKFlag) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "FI";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strZEKZAKFlag.getBytes());
		// String strZMK = "5C6FF58D938060B4288A45E9D4E74BEB";
		byteArrayRetData.write(_MakeCardZMK.getBytes());
		byteArrayRetData.write(";XX0".getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseFJ(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-1A+32H的ZEK传输
		vecData.addElement(new String(bBuffer, 4, 33));
		// 4-1A+32H的ZEK存储
		vecData.addElement(new String(bBuffer, 37, 33));
		// 5-16H的ZEK密码校验
		vecData.addElement(new String(bBuffer, 70, 16));

		return vecData;
	}

	/* 功能：制作请求数据包--E0--使用带入的密钥进行数据加解密计算 */
	/**
	 * @param strEncFlag
	 *            加解密类型：0-DES加密 1-DES解密
	 * @param strZEK
	 * @param strData
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestE0(String strZEK, String strEncFlag,
			String strData) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "E0";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("0".getBytes());
		byteArrayRetData.write(strEncFlag.getBytes());
		byteArrayRetData.write("10".getBytes());
		byteArrayRetData.write(strZEK.getBytes());
		byteArrayRetData.write("11000000".getBytes());
		// byteArrayRetData.write(StringUtils.Int2String(strData.length(),
		// 3).getBytes());
		StringBuilder sb = new StringBuilder();
		sb.append("000").append(Integer.toHexString(strData.length() / 2));
		byteArrayRetData.write(sb.substring(sb.length() - 3).getBytes());
		byteArrayRetData.write(strData.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseE1(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-输出模式
		vecData.addElement(new String(bBuffer, 4, 1));
		// 4-消息数据长度
		// byte[] bytesDataLen = new byte[3];
		// System.arraycopy(bBuffer, 5, bytesDataLen, 0, 3);
		// vecData.addElement(bytesDataLen);
		// System.out.println("bytesDataLen[0]:"+bytesDataLen[0]);
		// System.out.println("bytesDataLen[1]:"+bytesDataLen[1]);
		// System.out.println("bytesDataLen[2]:"+bytesDataLen[2]);
		String strDatalen = new String(bBuffer, 5, 3);
		vecData.addElement(strDatalen);
		// 5-消息数据
		int nDataLen = Integer.valueOf(strDatalen, 16);// 16进制字符表达的长度
		logger.debug("nDataLen:" + nDataLen);
		vecData.addElement(new String(bBuffer, 8, nDataLen * 2));

		return vecData;
	}

	/* 功能：制作请求数据包--98--密钥离散功能（98/99） */
	/**
	 * @param strKey
	 *            -
	 * @param strKeyType
	 * @param strLSData1
	 * @param strLSData2
	 * @param strLSData3
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequest98(String strKey, String strKeyType,
			String strLSData1, String strLSData2, String strLSData3)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "98";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strKey.getBytes());
		byteArrayRetData.write(strKeyType.getBytes());
		byteArrayRetData.write(strLSData1.getBytes());
		byteArrayRetData.write(strLSData2.getBytes());
		byteArrayRetData.write(strLSData3.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponse99(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-密钥密文
		vecData.addElement(new String(bBuffer, 4, 32));

		return vecData;
	}

	/* 功能：制作请求数据包--MQ--对大消息生成MAC（MAB）（MQ/MR） */
	/**
	 * @param strZAK
	 * @param strData
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestMQ(String strZAK, String strData)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "MQ";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("0".getBytes());
		byteArrayRetData.write(strZAK.getBytes());
		StringBuilder sb = new StringBuilder();
		sb.append("000").append(Integer.toHexString(strData.length() / 2));
		byteArrayRetData.write(sb.substring(sb.length() - 3).getBytes());
		byteArrayRetData.write(strData.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseMR(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-MAB
		vecData.addElement(new String(bBuffer, 4, 16));

		return vecData;
	}

	/* 功能：制作请求数据包--MS--用ANSI X9.19方式对大消息生成MAC（MS/MT） */
	/**
	 * @param strZAK
	 * @param strData
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestMS(String strKeyType, String strKeyLenType,
			String strKey, String strData) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "MS";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("0".getBytes());
		byteArrayRetData.write(strKeyType.getBytes());
		byteArrayRetData.write(strKeyLenType.getBytes());
		byteArrayRetData.write("0".getBytes());
		byteArrayRetData.write(strKey.getBytes());
		StringBuilder sb = new StringBuilder();
		System.out.println("strData.length();" + strData.length());
		sb.append("000").append(Integer.toHexString(strData.length()));
		System.out.println("a:" + sb);
		byteArrayRetData.write(sb.substring(sb.length() - 4).getBytes());
		byteArrayRetData.write(strData.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* 功能：制作请求数据包--MS--用ANSI X9.19方式对大消息生成MAC（MS/MT） */
	/**
	 * @param strBlockNum
	 *            - 消息块号 0：仅一块。/1：第一块。/2：中间块。/3：最后块。
	 * @param strKeyType
	 *            - 密钥类型 0－TAK（终端认证密钥）/1－ZAK（区域认证密钥）
	 * @param strKeyLenType
	 *            - 密钥长度类型 0－单倍长度DES密钥/1－双倍长度DES密钥
	 * @param strKey
	 *            - 密钥
	 * @param strDataBlock
	 *            - 消息块
	 * @param strIV
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestMS(String strBlockNum, String strKeyType,
			String strKeyLenType, String strKey, String strDataBlock,
			String strIV) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "MS";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strBlockNum.getBytes());
		byteArrayRetData.write(strKeyType.getBytes());
		byteArrayRetData.write(strKeyLenType.getBytes());
		byteArrayRetData.write("0".getBytes());
		byteArrayRetData.write(strKey.getBytes());
		byteArrayRetData.write(strIV.getBytes());
		StringBuilder sb = new StringBuilder();
		sb.append("000").append(Integer.toHexString(strDataBlock.length()));
		byteArrayRetData.write(sb.substring(sb.length() - 4).getBytes());
		byteArrayRetData.write(strDataBlock.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* 功能：制作请求数据包--MS--用ANSI X9.19方式对大消息生成MAC（MS/MT） */
	/**
	 * @param strBlockNum
	 *            - 消息块号 0：仅一块。/1：第一块。/2：中间块。/3：最后块。
	 * @param strKeyType
	 *            - 密钥类型 0－TAK（终端认证密钥）/1－ZAK（区域认证密钥）
	 * @param strKeyLenType
	 *            - 密钥长度类型 0－单倍长度DES密钥/1－双倍长度DES密钥
	 * @param strKey
	 *            - 密钥
	 * @param strDataBlock
	 *            - 消息块
	 * @param strIV
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestMS(String strBlockNum, String strKeyType,
			String strKeyLenType, String type, String strKey,
			byte[] bytesDataBlock, String strIV) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "MS";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strBlockNum.getBytes());
		byteArrayRetData.write(strKeyType.getBytes());
		byteArrayRetData.write(strKeyLenType.getBytes());
		byteArrayRetData.write(type.getBytes());
		byteArrayRetData.write(strKey.getBytes());
		byteArrayRetData.write(strIV.getBytes());
		StringBuilder sb = new StringBuilder();
		logger.debug("fuck:"
				+ Integer.toHexString(bytesDataBlock.length).toUpperCase());
		sb.append("000").append(
				Integer.toHexString(bytesDataBlock.length).toUpperCase());
		byteArrayRetData.write(sb.substring(sb.length() - 4).getBytes());
		if ("0".equals(type)) {
			byteArrayRetData.write(bytesDataBlock);
		} else {
			byteArrayRetData.write(CryptoUtils.byte2hex(bytesDataBlock)
					.getBytes());
		}

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseMT(byte[] bBuffer) {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-MAB
		vecData.addElement(new String(bBuffer, 4, 16));

		return vecData;
	}

	/* 功能：制作请求数据包--HC--2.6.2 生成一个TMK、TPK或PVK（HC/HD） */
	/**
	 * @param strTMK
	 *            - 终端密钥
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestHC(String strTMK) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "HC";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strTMK.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseHD(byte[] bBuffer) {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-当前TMK密钥下加密的新密钥
		vecData.addElement(new String(bBuffer, 4, 16));
		// 4-LMK下加密的新密钥
		vecData.addElement(new String(bBuffer, 20, 16));
		// 5-密钥校验值

		return vecData;
	}

	/* 功能：制作请求数据包--AE--2.6.3 将TMK、TPK或PVK从LMK转为另一TMK、TPK或PVK加密（AE/AF） */
	/**
	 * @param strTMK
	 *            - 终端密钥
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestAE(String strKey, String strTMK)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "AE";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strKey.getBytes());
		byteArrayRetData.write(strTMK.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseAF(byte[] bBuffer) {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-当前TMK密钥下加密的密钥
		vecData.addElement(new String(bBuffer, 4, 16));
		// 4-密钥校验值

		return vecData;
	}

	/* 功能：制作请求数据包--HA--2.7.1 生成一个TAK（HA/HB） */
	/**
	 * @param strTMK
	 *            - 终端密钥
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestHA(String strTMK) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "HA";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strTMK.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseHB(byte[] bBuffer) {
		Vector vecData = new Vector(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-当前TMK密钥下加密的新密钥
		vecData.addElement(new String(bBuffer, 4, 16));
		// 4-LMK下加密的新密钥
		vecData.addElement(new String(bBuffer, 20, 16));
		// 5-密钥校验值

		return vecData;
	}

	/* 功能：制作请求数据包--AG--2.7.4 将TAK从LMK转为TMK加密（AG/AH） */
	/**
	 * @param strTMK
	 *            - 终端密钥
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestAG(String strTAK, String strTMK)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "AG";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strTMK.getBytes());
		byteArrayRetData.write(strTAK.getBytes());

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseAH(byte[] bBuffer) {
		Vector vecData = new Vector(4);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-当前TMK密钥下加密的密钥
		vecData.addElement(new String(bBuffer, 4, 16));
		// 4-密钥校验值

		return vecData;
	}

	/* 功能：制作请求数据包--EO--由公钥生成一个MAC */
	/**
	 * @param strPublicKey
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestEO(String strPublicKey) throws IOException {
		// ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "EO";
		// byteArrayRetData.write(CryptoUtils.byte2hex(strCommand.getBytes()).getBytes());
		// byteArrayRetData.write(CryptoUtils.byte2hex("01".getBytes()).getBytes());
		// byteArrayRetData.write("308201".getBytes());
		// byteArrayRetData.write("0A0282010100".getBytes());
		// byteArrayRetData.write(strPublicKey.getBytes());
		// byteArrayRetData.write("0203010001".getBytes());

		StringBuilder sb = new StringBuilder();
		sb.append(CryptoUtils.byte2hex(strCommand.getBytes()));
		sb.append(CryptoUtils.byte2hex("01".getBytes()));
		// sb.append("308201").append("0A0282010100");
		sb.append(strPublicKey);

		return CryptoUtils.hex2byte(sb.toString());
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseEP(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector vecData = new Vector(4);

		// //1-响应代码
		// vecData.addElement(new String(bBuffer, 0, 4));
		// //2-错误代码
		// vecData.addElement(new String(bBuffer, 4, 4));
		// //3-MAC
		// vecData.addElement(new String(bBuffer, 8, 8));
		// System.out.println("bytesMAC:"+new String(bBuffer, 8, 8));

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-MAC
		byte[] bytesMAC = new byte[4];
		System.arraycopy(bBuffer, 4, bytesMAC, 0, 4);
		vecData.addElement(bytesMAC);
		System.out.println("bytesMAC:" + new String(bytesMAC));
		System.out.println("bytesMAC:" + CryptoUtils.byte2hex(bytesMAC));
		// 4-公钥 - ASN.1格式编码的DER
		// vecData.addElement(new String(bBuffer, 8, 512));//报错了，此项值暂时用不到，忽略

		return vecData;
	}

	/* 功能：制作请求数据包--GK--使用带入的密钥进行数据加解密计算 */
	/**
	 * @param strDESKeyType
	 * @param strDESKey
	 * @param strDESMAC
	 * @param bytesMAC
	 * @param strPublicKey
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestGK(String strDESKeyType, String strDESKey,
			String strDESMAC, byte[] bytesMAC, String strPublicKey)
			throws IOException {
		// ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "GK";
		// byteArrayRetData.write(strCommand.getBytes());
		// byteArrayRetData.write("0101".getBytes());
		// byteArrayRetData.write(strDESKeyType.getBytes());
		// byteArrayRetData.write("1".getBytes());
		// byteArrayRetData.write(strDESKey.getBytes());
		// byteArrayRetData.write(strDESMAC.getBytes());
		// byteArrayRetData.write(bytesMAC);
		// byteArrayRetData.write(strPublicKey.getBytes());

		// StringBuffer sb = new StringBuffer();
		// sb.append(strCommand).append("0101").append(strDESKeyType).append("1").append(strDESKey).append(strDESMAC);
		// byteArrayRetData.write(CryptoUtils.byte2hex(sb.toString().getBytes()).getBytes());
		// byteArrayRetData.write(strMAC.getBytes());
		// byteArrayRetData.write("308201".getBytes());
		// byteArrayRetData.write("0A0282010100".getBytes());
		// byteArrayRetData.write(strPublicKey.getBytes());
		// byteArrayRetData.write("0203010001".getBytes());
		//
		// return byteArrayRetData.toByteArray();

		StringBuffer sb = new StringBuffer();
		sb.append(strCommand).append("0101").append(strDESKeyType).append("1")
				.append(strDESKey).append(strDESMAC);
		String strTemp = sb.toString();
		sb.setLength(0);
		sb.append(CryptoUtils.byte2hex(strTemp.getBytes()));
		// sb.append(CryptoUtils.byte2hex(bytesMAC));
		// sb.append("308201").append("0A0282010100");
		sb.append(strPublicKey).append(strPublicKey);
		System.out.println("GK:" + CryptoUtils.hex2byte(sb.toString()));

		return CryptoUtils.hex2byte(sb.toString());

	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector parseResponseGL(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector vecData = new Vector(6);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-对DES密钥的初始化值
		vecData.addElement(new String(bBuffer, 4, 16));
		// 4-DES密钥长度
		String strDataLen = new String(bBuffer, 20, 4);
		vecData.addElement(strDataLen);
		System.out.println("strDataLen:" + strDataLen);
		// 5-DES密钥
		int nDataLen = Integer.parseInt(strDataLen);
		byte[] bytesDESKeyData = new byte[nDataLen];
		System.arraycopy(bBuffer, 24, bytesDESKeyData, 0, nDataLen);
		vecData.addElement(bytesDESKeyData);

		return vecData;
	}

	/* 功能：制作请求数据包--GI--将DES密钥从公钥下加密转换为LMK下加密 */
	/**
	 * @param strDESKeyType
	 * @param strDESKey
	 * @param strPrivateKeyIndex
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestGI(String strDESKeyType, String strDESKey,
			String strPrivateKeyIndex) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "GI";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("0101".getBytes());
		byteArrayRetData.write(strDESKeyType.getBytes());
		byte[] bytesDESKey = CryptoUtils.hex2byte(strDESKey);
		byteArrayRetData.write(StringUtils.Int2String(bytesDESKey.length, 4)
				.getBytes());
		byteArrayRetData.write(bytesDESKey);
		byteArrayRetData.write(";".getBytes());
		byteArrayRetData.write(strPrivateKeyIndex.getBytes());
		byteArrayRetData.write(";".getBytes());
		byteArrayRetData.write(CryptoUtils.hex2byte("585830"));

		// System.out.println("GI Hex:"+CryptoUtils.byte2hex(byteArrayRetData.toByteArray()));

		return byteArrayRetData.toByteArray();
	}

	/* 功能：制作请求数据包--GI--将DES密钥从公钥下加密转换为LMK下加密 */
	/**
	 * @param strDESKeyType
	 * @param strDESKey
	 * @param strPrivateKeyIndex
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestGIByLynx(String strDESKeyType,
			String strDESKey, String strPrivateKeyIndex) throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "GI";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("0100".getBytes());
		byteArrayRetData.write(strDESKeyType.getBytes());
		byte[] bytesDESKey = CryptoUtils.hex2byte(strDESKey);
		byteArrayRetData.write(StringUtils.Int2String(bytesDESKey.length, 4)
				.getBytes());
		byteArrayRetData.write(bytesDESKey);
		byteArrayRetData.write(";".getBytes());
		byteArrayRetData.write(strPrivateKeyIndex.getBytes());

		// System.out.println("GI Hex:"+CryptoUtils.byte2hex(byteArrayRetData.toByteArray()));

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseGJ(byte[] bBuffer, int nDESKeyLen)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(6);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-对DES密钥的初始化值
		vecData.addElement(new String(bBuffer, 4, 16));
		// 4-DES密钥（LMK）
		vecData.addElement(new String(bBuffer, 20, 32));// nDESKeyLen的长度应该和GK指令导出指令时的DES密钥标记相对应，当初的GK指令中DES密钥标记是1这里就是32，产生的checkvalue就是16
		// 5-DES密钥校验值 16H还是6H取决于KCV的类型选项。
		vecData.addElement(new String(bBuffer, 52, 16));

		return vecData;
	}

	/* 功能：制作请求数据包--A0--生成一个密钥，同时可选的为交易在LMK下加密密钥 */
	/**
	 * @param strKeyType
	 * @param strKeyFlag
	 * @param strZMK
	 * @param strZMKFlag
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeRequestA0(String strActionType, String strKeyType,
			String strKeyFlag, String strZMK, String strZMKFlag)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "A0";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write(strActionType.getBytes());
		byteArrayRetData.write(strKeyType.getBytes());
		byteArrayRetData.write(strKeyFlag.getBytes());
		byteArrayRetData.write(strZMK.getBytes());
		byteArrayRetData.write(strZMKFlag.getBytes());

		// System.out.println("A0 Hex:"+CryptoUtils.byte2hex(byteArrayRetData.toByteArray()));

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseA1(byte[] bBuffer, int nKeyLen)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-密钥（LMK）
		vecData.addElement(new String(bBuffer, 4, nKeyLen));
		// 4-密钥（ZMK）
		vecData.addElement(new String(bBuffer, 4 + nKeyLen, nKeyLen));
		// 5-密钥校验值 6H
		vecData.addElement(new String(bBuffer, 4 + 2 * nKeyLen, 16));

		return vecData;
	}

	public static byte[] makeRequestA5(String strZMK1, String strZMK2,
			String strZMKFlag) throws IOException {
		// 发送数据：
		// A5 2 000 X A63C95E17B661A921237C397236094AA
		// 00000000000000000000000000000000
		// 接收数据：
		// A6 00 U X 55C6A560C6BBBD281EF384D3D57BFD83 AA3A2330EA08C879
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "A5";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("2".getBytes());
		byteArrayRetData.write("000".getBytes());
		byteArrayRetData.write(strZMKFlag.getBytes());
		byteArrayRetData.write(strZMK1.getBytes());
		byteArrayRetData.write(strZMK2.getBytes());

		return byteArrayRetData.toByteArray();
	}

	public static Vector<String> parseResponseA6(byte[] bBuffer, int nKeyLen)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-密钥（ZMK）
		vecData.addElement(new String(bBuffer, 5, 32));
		// 4-密钥校验值 6H
		vecData.addElement(new String(bBuffer, 37, 16));

		return vecData;
	}

	/*
	 * 功能：MAC生成 /**
	 * 
	 * @param strZMK
	 * 
	 * @param strZMKFlag
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public static byte[] makeRequestMU(String strZMK, byte[] macData)
			throws IOException {
		ByteArrayOutputStream byteArrayRetData = new ByteArrayOutputStream();
		String strCommand = "MU";
		byteArrayRetData.write(strCommand.getBytes());
		byteArrayRetData.write("0".getBytes());
		byteArrayRetData.write("1".getBytes());
		// 密钥长度 0-单倍长 1-双倍长
		byteArrayRetData.write("0".getBytes());
		// 消息类型 0-二进制 1-十六进制
		byteArrayRetData.write("1".getBytes());
		byteArrayRetData.write(strZMK.getBytes());
		StringBuilder sb = new StringBuilder();
		// sb.append("000").append(macData.length);
		int mlth = CryptoUtils.byte2hex(macData).length() / 2;
		sb.append("000").append(Integer.toHexString(mlth));
		byteArrayRetData.write(sb.substring(sb.length() - 4).getBytes());
		// byteArrayRetData.write(macData);
		byteArrayRetData.write(CryptoUtils.byte2hex(macData).getBytes());

		// System.out.println("A0 Hex:"+CryptoUtils.byte2hex(byteArrayRetData.toByteArray()));

		return byteArrayRetData.toByteArray();
	}

	/* * * *
	 * 功能：解析交易数据包 参数： bBuffer - 要解析的数据缓冲区
	 */
	public static Vector<String> parseResponseMY(byte[] bBuffer)
			throws java.util.NoSuchElementException {
		Vector<String> vecData = new Vector<String>(5);

		// 1-响应代码
		vecData.addElement(new String(bBuffer, 0, 2));
		// 2-错误代码
		vecData.addElement(new String(bBuffer, 2, 2));
		// 3-MAB
		vecData.addElement(new String(bBuffer, 4, 16));

		return vecData;
	}

	public void connectionHSM() throws UnknownHostException, IOException {
		// 建立通讯
		Socket socket = new Socket(_HSMHost, _nPort);
		_OutToHSM = new DataOutputStream(socket.getOutputStream());
		_InputFromHSM = new DataInputStream(socket.getInputStream());
	}

	public void closeHSM() throws UnknownHostException, IOException {
		// 断开通讯
		if (null != _OutToHSM)
			_OutToHSM.close();
		if (null != _InputFromHSM)
			_InputFromHSM.close();
	}

	public synchronized String doEncodePassword2(String pik,
			String strPassword, String strCardNo) throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		// System.out.println("strCardNo12:"+strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// A6-A7
			byte[] bRequest = makeRequestA6(_LocalEncPIK, pik, "001", "X");
			// String strSend =
			// "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseA7(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String LMK = (String) vecRet.get(2);

			// BA-BB
			bRequest = makeRequestBA(strPassword, strCardNo12);
			// String strSend =
			// "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseBB(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String strEncPassword = (String) vecRet.get(2);

			// JG-JH
			bRequest = makeRequestJG(LMK, strEncPassword, strCardNo12);
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseJH(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command JG/JH return error code "
						+ strRetCode);
			}
			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			// connectionHSM();
			// if(bErrorTry)
			// {
			return null;
			// }
			// else
			// {
			// throw e;
			// }
		}

	}

	public synchronized String doA6000(String chType, String termZMK,
			String orgZMK) throws IOException {
		// 建立通讯
		if (null == _OutToHSM || null == _InputFromHSM)
			connectionHSM();
		// A6-A7
		// byte[] bRequest = makeRequestA6(termZMK,ylpk.substring(0, 16),"001");
		// if ("403".equals(chType)) {
		// logger.info("*********** BJ_ZMK ***********");
		// _ZMK = BJ_ZMK;
		// }else if ("100".equals(chType)) {
		// logger.info("*********** YC_ZMK ***********");
		// _ZMK = YC_ZMK;
		// }else {
		// //530 , 102, 307
		// logger.info("*********** AXL_ZMK ***********");
		// _ZMK = AXL_ZMK;
		// }
		if (orgZMK != null) {
			_ZMK = orgZMK;
		}
		byte[] bRequest = makeRequestA6(_ZMK, "X" + termZMK, "000", "X");
		logger.debug("bRequest:" + new String(bRequest));
		// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
		_OutToHSM.write(makeRequestHeader(bRequest));
		_OutToHSM.write(bRequest);
		byte[] bResponse = receive(_InputFromHSM);
		logger.debug("bResponse:" + new String(bResponse));
		Vector<String> vecRet = parseResponseA7(bResponse, "2");
		String strRetCode = (String) vecRet.get(1);
		if (!strRetCode.equals("00")) {
			throw new IOException("HSM command BA/BB return error code "
					+ strRetCode);
		}
		return (String) vecRet.get(2);

	}

	public synchronized String doHMSignIn(String termZMK, String ylpk)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();
			// A6-A7
			// byte[] bRequest = makeRequestA6(termZMK,ylpk.substring(0,
			// 16),"001");
			byte[] bRequest = makeRequestA6(termZMK,
					"X" + ylpk.substring(0, 32), "001", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			Vector<String> vecRet = parseResponseA7(bResponse, "2");
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZPKbyLMK = (String) vecRet.get(2);

			// A6-A7
			// bRequest = makeRequestA6(termZMK,ylpk.substring(24, 40),"008");
			bRequest = makeRequestA6(termZMK, ylpk.substring(40, 56), "008",
					"Z");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			vecRet = parseResponseA7(bResponse, "1");
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZAKbyLMK = (String) vecRet.get(2);

			System.out
					.println("sign in success:" + _ZPKbyLMK + "|" + _ZAKbyLMK);
			return _ZPKbyLMK + "," + _ZAKbyLMK;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			return null;
		}

	}
	
	public synchronized String doHMSignInForDoubleX(String termZMK, String ylpk)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();
			// A6-A7
			// byte[] bRequest = makeRequestA6(termZMK,ylpk.substring(0,
			// 16),"001");
			byte[] bRequest = makeRequestA6(termZMK,
					"X" + ylpk.substring(0, 32), "001", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			Vector<String> vecRet = parseResponseA7(bResponse, "2");
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZPKbyLMK = (String) vecRet.get(2);

			// A6-A7
			// bRequest = makeRequestA6(termZMK,ylpk.substring(24, 40),"008");
			bRequest = makeRequestA6(termZMK, "X" + ylpk.substring(40, 72), "008","X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			vecRet = parseResponseA7(bResponse, "2");
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZAKbyLMK = (String) vecRet.get(2);

			System.out
					.println("sign in success:" + _ZPKbyLMK + "|" + _ZAKbyLMK);
			return _ZPKbyLMK + "," + _ZAKbyLMK;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			return null;
		}

	}

	public synchronized String doHMSignInWithEncKey(String termZMK, String ylpk)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();
			// A6-A7
			// byte[] bRequest = makeRequestA6(termZMK,ylpk.substring(0,
			// 16),"001");
			byte[] bRequest = makeRequestA6(termZMK,
					"X" + ylpk.substring(0, 32), "001", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			Vector<String> vecRet = parseResponseA7(bResponse, "2");
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZPKbyLMK = (String) vecRet.get(2);

			// A6-A7
			// bRequest = makeRequestA6(termZMK,ylpk.substring(24, 40),"008");
			bRequest = makeRequestA6(termZMK, "X" + ylpk.substring(40, 72),
					"008", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			vecRet = parseResponseA7(bResponse, "2");
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZAKbyLMK = (String) vecRet.get(2);

			bRequest = makeRequestA6(termZMK, "X" + ylpk.substring(80, 112),
					"00A", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			vecRet = parseResponseA7(bResponse, "2");
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String encKey = (String) vecRet.get(2);

			System.out
					.println("sign in success:" + _ZPKbyLMK + "|" + _ZAKbyLMK);
			return _ZPKbyLMK + "," + _ZAKbyLMK + "," + encKey;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			return null;
		}

	}
	
	public synchronized String doHMSignInWithEncKeyAndZForZak(String termZMK, String ylpk)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();
			// A6-A7
			// byte[] bRequest = makeRequestA6(termZMK,ylpk.substring(0,
			// 16),"001");
			byte[] bRequest = makeRequestA6(termZMK,
					"X" + ylpk.substring(0, 32), "001", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			Vector<String> vecRet = parseResponseA7(bResponse, "2");
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZPKbyLMK = (String) vecRet.get(2);

			// A6-A7
			// bRequest = makeRequestA6(termZMK,ylpk.substring(24, 40),"008");
			bRequest = makeRequestA6(termZMK, ylpk.substring(40, 56), "008",
					"Z");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			vecRet = parseResponseA7(bResponse, "1");
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			_ZAKbyLMK = (String) vecRet.get(2);

			bRequest = makeRequestA6(termZMK, "X" + ylpk.substring(80, 112),
					"00A", "X");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			vecRet = parseResponseA7(bResponse, "2");
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String encKey = (String) vecRet.get(2);

			System.out
					.println("sign in success:" + _ZPKbyLMK + "|" + _ZAKbyLMK);
			return _ZPKbyLMK + "," + _ZAKbyLMK + "," + encKey;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			return null;
		}

	}

	public synchronized String getMac(HSMDataPackage hsm, String termZAK,
			byte[] data) throws IOException {
		// 进行分组
		int group = (data.length + (8 - 1)) / 8;
		// 偏移量
		int offset = 0;
		// 输入计算数据
		byte[] edata = null;
		byte[] temp = null;
		for (int i = 0; i < group; i++) {
			temp = new byte[8];
			if (i != group - 1) {
				System.arraycopy(data, offset, temp, 0, 8);
				offset += 8;
			} else {// 只有最后一组数据才进行填充0x00
				System.arraycopy(data, offset, temp, 0, data.length - offset);
			}

			if (i != 0) {// 只有第一次不做异或
				temp = CryptoUtils.XOR(edata, temp);
			}
			edata = temp;
		}
		String xorResult = CryptoUtils.byte2hex(edata);

		// logger.info("************** mac ****************");
		// logger.info("termZAK:"+termZAK);
		// logger.info("macdata:"+CryptoUtils.byte2hex(data));
		//
		// logger.info("xorResult:"+xorResult);
		// edata = symmetricEncrypto(makKey,xorResult.substring(0,
		// 8).getBytes());
		// temp = XOR(edata,xorResult.substring(8, 16).getBytes());
		// edata = symmetricEncrypto(makKey,temp);
		// String result = byte2hex(edata);
		// return result.substring(0,8);

		String strEData = hsm.doPOSMAC(false, "0", "1", "0", "0", termZAK,
				xorResult.substring(0, 8).getBytes());
		// String strEData = hsm.doMakeMac(xorResult.substring(0,
		// 8).getBytes());
		edata = CryptoUtils.hex2byte(strEData);
		// logger.info("edata:"+strEData);
		temp = CryptoUtils.XOR(edata, xorResult.substring(8, 16).getBytes());
		// logger.info("temp:"+CryptoUtils.byte2hex(temp));
		// edata = CryptoUtils.symmetricEncrypto(makKey, temp);
		strEData = hsm.doPOSMAC(false, "0", "1", "0", "1", termZAK, temp);
		// strEData = hsm.doMakeMac(temp);
		// System.out.println("加密机仅1块加密的edata:"+strEData);

		// String result = CryptoUtils.byte2hex(edata);
		String result = strEData;
		// logger.info("result"+result);
		return result.substring(0, 8);
	}

	public synchronized String getMacForYinsheng(HSMDataPackage hsm, String termZAK,
			byte[] data) throws IOException {
		// 进行分组
		int group = (data.length + (8 - 1)) / 8;
		// 偏移量
		int offset = 0;
		// 输入计算数据
		byte[] edata = null;
		byte[] temp = null;
		for (int i = 0; i < group; i++) {
			temp = new byte[8];
			if (i != group - 1) {
				System.arraycopy(data, offset, temp, 0, 8);
				offset += 8;
			} else {// 只有最后一组数据才进行填充0x00
				System.arraycopy(data, offset, temp, 0, data.length - offset);
			}

			if (i != 0) {// 只有第一次不做异或
				temp = CryptoUtils.XOR(edata, temp);
			}
			edata = temp;
		}
		String xorResult = CryptoUtils.byte2hex(edata);

		// logger.info("************** mac ****************");
		// logger.info("termZAK:"+termZAK);
		// logger.info("macdata:"+CryptoUtils.byte2hex(data));
		//
		// logger.info("xorResult:"+xorResult);
		// edata = symmetricEncrypto(makKey,xorResult.substring(0,
		// 8).getBytes());
		// temp = XOR(edata,xorResult.substring(8, 16).getBytes());
		// edata = symmetricEncrypto(makKey,temp);
		// String result = byte2hex(edata);
		// return result.substring(0,8);

		String strEData = hsm.doPOSMAC(false, "0", "1", "1", "0", termZAK,
				xorResult.substring(0, 8).getBytes());
		// String strEData = hsm.doMakeMac(xorResult.substring(0,
		// 8).getBytes());
		edata = CryptoUtils.hex2byte(strEData);
		// logger.info("edata:"+strEData);
		temp = CryptoUtils.XOR(edata, xorResult.substring(8, 16).getBytes());
		// logger.info("temp:"+CryptoUtils.byte2hex(temp));
		// edata = CryptoUtils.symmetricEncrypto(makKey, temp);
		strEData = hsm.doPOSMAC(false, "0", "1", "1", "1", termZAK, temp);
		// strEData = hsm.doMakeMac(temp);
		// System.out.println("加密机仅1块加密的edata:"+strEData);

		// String result = CryptoUtils.byte2hex(edata);
		String result = strEData;
		// logger.info("result"+result);
		return result.substring(0, 8);
	}
	
	public synchronized String getMacBy919With001(HSMDataPackage hsm, String makKey,byte[] data) throws Exception{
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
	            temp = CryptoUtils.XOR(edata,temp);
	        }
//	        edata = CryptoUtils.hex2byte(CryptoUtils.encryptDES(CryptoUtils.byte2hex(temp),makKey));
	        String strEData = hsm.doPOSMAC(false, "0", "1", "0", "0", makKey,
	        		temp);
	        edata = CryptoUtils.hex2byte(strEData);
	    }
	    
//	    String result = CryptoUtils.encryptDES(CryptoUtils.byte2hex(edata),makKey);
		return CryptoUtils.byte2hex(edata);
	}
	
	public synchronized String getMacBy919(HSMDataPackage hsm, String termZAK,
			byte[] data) throws IOException {
		return hsm.doPOSMAC(false, "0", "1", "1", "0", termZAK, data);
	}

	public synchronized String doPin(HSMDataPackage hsm, String termZPK,
			byte[] data) throws IOException {

		String result = hsm.doPOSMAC(false, "0", "1", "0", "1", termZPK, data);
		logger.info("result" + result);
		return result;
	}

	public synchronized String doMakeMac(byte[] macData) throws IOException {

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// //A6-A7
			// byte[] bRequest = makeRequestA6(_LocalEncPIK,mak,"008");
			// //String strSend =
			// "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			// logger.debug("bRequest:"+new String(bRequest));
			// //System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			// _OutToHSM.write(makeRequestHeader(bRequest));
			// _OutToHSM.write(bRequest);
			// byte[] bResponse = receive(_InputFromHSM);
			// logger.debug("bResponse:"+new String(bResponse));
			// //System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			// Vector<String> vecRet = parseResponseA7(bResponse);
			// String strRetCode = (String)vecRet.get(1);
			// if(!strRetCode.equals("00"))
			// {
			// throw new
			// IOException("HSM command BA/BB return error code "+strRetCode);
			// }
			// String LMK = (String)vecRet.get(2);

			// MU-MY
			// _ZAKbyLMK="CD0F9D3F8550A01B";
			// macData=CryptoUtils.hex2byte("0200202004C020C0981131000000006702100012376200100000803786D49121201651700010000030303336313030313430333331303037333932303036333135364B64FB4DCFA228532000000000000000000801000002");
			byte[] bRequest = makeRequestMU(_ZAKbyLMK, macData);
			logger.debug("bRequest:" + new String(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			Vector<String> vecRet = parseResponseMY(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			return (String) vecRet.get(2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			// connectionHSM();
			// if(bErrorTry)
			// {
			return null;
			// }
			// else
			// {
			// throw e;
			// }
		}
	}

	public synchronized String doEncodePassword(boolean bErrorTry,
			String strPassword, String strCardNo) throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		// System.out.println("strCardNo12:"+strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// BA-BB
			byte[] bRequest = makeRequestBA(strPassword, strCardNo12);
			// String strSend =
			// "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseBB(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String strEncPassword = (String) vecRet.get(2);

			// JG-JH
			bRequest = makeRequestJG(_LocalEncPIK, strEncPassword, strCardNo12);
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseJH(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command JG/JH return error code "
						+ strRetCode);
			}
			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doEncodePassword(false, strPassword, strCardNo);
			} else {
				throw e;
			}
		}
	}

	public synchronized String doFromEncodePassword(boolean bErrorTry,
			String strPassword, String strCardNo) throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		// System.out.println("strCardNo12:"+strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// BA-BB
			byte[] bRequest = makeRequestBA(strPassword, strCardNo12);
			// String strSend =
			// "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseBB(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String strEncPassword = (String) vecRet.get(2);

			// JG-JH
			bRequest = makeRequestJG(_FromEncPIK, strEncPassword, strCardNo12);
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseJH(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command JG/JH return error code "
						+ strRetCode);
			}
			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doFromEncodePassword(false, strPassword, strCardNo);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 用对方密钥加密
	 * 
	 * @param bErrorTry
	 * @param strFromEncPIK
	 *            - 指定的对方密钥值
	 * @param strPassword
	 * @param strCardNo
	 * @return
	 * @throws IOException
	 */
	public synchronized String doFromEncodePassword(boolean bErrorTry,
			String strFromEncPIK, String strPassword, String strCardNo)
			throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		// System.out.println("strCardNo12:"+strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// BA-BB
			byte[] bRequest = makeRequestBA(strPassword, strCardNo12);
			// String strSend =
			// "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseBB(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command BA/BB return error code "
						+ strRetCode);
			}
			String strEncPassword = (String) vecRet.get(2);

			// JG-JH
			bRequest = makeRequestJG(strFromEncPIK, strEncPassword, strCardNo12);
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseJH(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command JG/JH return error code "
						+ strRetCode);
			}
			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doFromEncodePassword(false, strFromEncPIK, strPassword,
						strCardNo);
			} else {
				throw e;
			}
		}
	}

	public synchronized String doTransferPinZPK2ZPK(boolean bErrorTry,
			String strEncPassword, String strCardNo) throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		System.out.println("strCardNo12:" + strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// BA-BB
			byte[] bRequest = makeRequestCC(_FromEncPIK, _LocalEncPIK,
					strEncPassword, strCardNo12);
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseCD(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTransferPinZPK2ZPK(false, strEncPassword, strCardNo);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 带卡号转PIN
	 * 
	 * @param bErrorTry
	 * @param strFromEncPIK
	 *            - 指定的对方密钥值
	 * @param strEncPassword
	 * @param strCardNo
	 * @return
	 * @throws IOException
	 */
	public synchronized String doTransferPinZPK2ZPK(boolean bErrorTry,
			String strFromEncPIK, String strEncPassword, String strCardNo)
			throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		System.out.println("strCardNo12:" + strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// BA-BB
			byte[] bRequest = makeRequestCC(strFromEncPIK, _LocalEncPIK,
					strEncPassword, strCardNo12);
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseCD(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTransferPinZPK2ZPK(false, strFromEncPIK,
						strEncPassword, strCardNo);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 功能：转PIN，支持PIN是不带卡号的加密的情况。
	 * 
	 * @param bErrorTry
	 * @param strEncPassword
	 * @param strCardNo
	 * @param bNotCardNoPin
	 * @return
	 * @throws IOException
	 */
	public synchronized String doTransferPinZPK2ZPK(boolean bErrorTry,
			String strEncPassword, String strCardNo, boolean bNotCardNoPin)
			throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		System.out.println("strCardNo12:" + strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// CC-CD
			String strTemp = bNotCardNoPin ? "000000000000" : strCardNo12;
			byte[] bRequest = makeRequestCC(_FromEncPIK, _LocalEncPIK,
					strEncPassword, strTemp, "01", "03");
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseCD(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}
			strEncPassword = (String) vecRet.get(2);

			// CC-CD
			bRequest = makeRequestCC(_LocalEncPIK, _LocalEncPIK,
					strEncPassword, strCardNo12, "03", "01");
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseCD(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTransferPinZPK2ZPK(false, strEncPassword, strCardNo,
						bNotCardNoPin);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 功能：转PIN，支持PIN是不带卡号的加密的情况。
	 * 
	 * @param bErrorTry
	 * @param strFromEncPIK
	 *            - 指定的对方密钥值
	 * @param strEncPassword
	 * @param strCardNo
	 * @param bNotCardNoPin
	 * @return
	 * @throws IOException
	 */
	public synchronized String doTransferPinZPK2ZPK(boolean bErrorTry,
			String strFromEncPIK, String strEncPassword, String strCardNo,
			boolean bNotCardNoPin) throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		logger.info("strCardNo12:" + strCardNo12);
		logger.info("strCardNo12:" + _LocalEncPIK);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// CC-CD
			String strTemp = bNotCardNoPin ? "000000000000" : strCardNo12;
			byte[] bRequest = makeRequestCC(strFromEncPIK, _LocalEncPIK,
					strEncPassword, strTemp, "01", "03");
			logger.info("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.info("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseCD(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}
			strEncPassword = (String) vecRet.get(3);

			// CC-CD
			bRequest = makeRequestCC(_LocalEncPIK, _LocalEncPIK,
					strEncPassword, strCardNo12, "03", "01");
			logger.info("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.info("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseCD(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTransferPinZPK2ZPK(false, strFromEncPIK,
						strEncPassword, strCardNo, bNotCardNoPin);
			} else {
				throw e;
			}
		}
	}

	public synchronized String doTransferPinZPK2ZPK(boolean bErrorTry,
			String strFromEncPIK, String strToEncPIK, String strEncPassword,
			String strCardNo, boolean bNotCardNoPin) throws PreposeException,
			UnknownHostException, IOException {
		int nLength = strCardNo.length();
		if (nLength == 20) {
			nLength = 19;
		}
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// CC-CD
			String strTemp = bNotCardNoPin ? "000000000000" : strCardNo12;
			byte[] bRequest = makeRequestCC(strFromEncPIK, strToEncPIK,
					strEncPassword, strTemp, "01", "03");
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseCD(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new PreposeException(PreposeException.ERROR_PIN_DATA);
				// throw new
				// IOException("HSM command CC/CD return error code "+strRetCode);
			}
			strEncPassword = (String) vecRet.get(3);

			// CC-CD
			bRequest = makeRequestCC(strToEncPIK, strToEncPIK, strEncPassword,
					strCardNo12, "03", "01");
			logger.info("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			logger.info("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseCD(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new PreposeException(PreposeException.ERROR_PIN_DATA);
				// throw new
				// IOException("HSM command CC/CD return error code "+strRetCode);
			}

			return (String) vecRet.get(3);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTransferPinZPK2ZPK(false, strFromEncPIK,
						strEncPassword, strCardNo, bNotCardNoPin);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 功能：转PIN，支持PIN是不带卡号的加密的情况。
	 * 
	 * @param bErrorTry
	 * @param strFromEncPIK
	 *            - 指定的对方密钥值
	 * @param strEncPassword
	 * @param strCardNo
	 * @param bNotCardNoPin
	 * @return
	 * @throws IOException
	 */
	public synchronized String doTransferPinTPK2ZPK(boolean bErrorTry,
			String strFromEncPIK, String strEncPassword, String strCardNo,
			boolean bNotCardNoPin) throws IOException {
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength - 13, nLength - 1);
		System.out.println("strCardNo12:" + strCardNo12);

		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// CA-CB
			String strTemp = bNotCardNoPin ? "000000000000" : strCardNo12;
			byte[] bRequest = makeRequestCA(strFromEncPIK, _LocalEncPIK,
					strEncPassword, strTemp, "01", "03");
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseCB(bResponse);
			String strRetCode = (String) vecRet.get(2);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CA/CB return error code "
						+ strRetCode);
			}
			strEncPassword = (String) vecRet.get(2);

			// CC-CD
			bRequest = makeRequestCC(_LocalEncPIK, _LocalEncPIK,
					strEncPassword, strCardNo12, "03", "01");
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseCD(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command CC/CD return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTransferPinTPK2ZPK(false, strFromEncPIK,
						strEncPassword, strCardNo, bNotCardNoPin);
			} else {
				throw e;
			}
		}
	}

	/**
	 * @param bErrorTry
	 * @param strZEKZAKFlag
	 *            产生ZEK或是ZAK的标识，0-ZEK，1-ZAK
	 * @return
	 * @throws IOException
	 */
	public synchronized Vector<String> doCreateRSAKeyPair(boolean bErrorTry,
			int nKeySize, int nKeyIndex) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// 34-35
			byte[] bRequest = makeRequest34(nKeySize, nKeyIndex);
			// System.out.println("bRequest:"+new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse length:" + bResponse.length);
			// System.out.println("bResponse:"+new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponse35(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command 34/35 return error code "
						+ strRetCode);
			}

			return vecRet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doCreateRSAKeyPair(false, nKeySize, nKeyIndex);
			} else {
				throw e;
			}
		}
	}

	/**
	 * @param bErrorTry
	 * @param strZEKZAKFlag
	 *            产生ZEK或是ZAK的标识，0-ZEK，1-ZAK
	 * @return
	 * @throws IOException
	 */
	public synchronized Vector<String> doCreateZEKZAK(boolean bErrorTry,
			String strZEKZAKFlag) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// FI-FJ
			byte[] bRequest = makeRequestFI(strZEKZAKFlag);
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseFJ(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command FI/FJ return error code "
						+ strRetCode);
			}

			return vecRet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doCreateZEKZAK(false, strZEKZAKFlag);
			} else {
				throw e;
			}
		}
	}

	/**
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strMakerPublicKey
	 *            厂商公钥
	 * @param strZEK
	 *            ZEK
	 * @param strZEKMAC
	 *            ZEK校验值
	 * @return 加密过的DES密钥
	 * @throws IOException
	 */
	public synchronized String doExportEncDESKey(boolean bErrorTry,
			String strMakerPublicKey, String strDESKeyType, String strZEK,
			String strZEKMAC) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// EO-EP
			byte[] bRequest = makeRequestEO(strMakerPublicKey);
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			// System.out.println("bRequest Header:"+CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)));
			// _OutToHSM.write(CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)).getBytes());
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector vecRet = parseResponseEP(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command EO/EP return error code "
						+ strRetCode);
			}

			bRequest = makeRequestGK(strDESKeyType, strZEK, strZEKMAC,
					(byte[]) vecRet.get(2), strMakerPublicKey);
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest Header:"+CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			// _OutToHSM.write(CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)).getBytes());
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			vecRet = parseResponseGL(bResponse);
			strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command GK/GL return error code "
						+ strRetCode);
			}

			return CryptoUtils.byte2hex((byte[]) vecRet.get(4));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doExportEncDESKey(false, strMakerPublicKey,
						strDESKeyType, strZEK, strZEKMAC);
			} else {
				throw e;
			}
		}
	}

	public synchronized String doMakeGK() throws IOException {
		// EO-EP
		byte[] bRequest = makeRequestGK(
				"0400",
				"DBA88193145626DDD63F1BD6C8663839AA3A2330EA08C879",
				"3F22887F",
				null,
				"30818902818100E5AF4A1B383BBC49A0017FBCFA418488E01C07A5B59594CEFD053465CCA1BFE4D8F361468ABD521860D80D6E92352023C037C653C681CB428DB059C2FCFEDC90B5C753B072A1EFEA2A5F493093DC0DC8D129732C854913892C5E0BE934C695A8E1937F46D4D50C1778DAF5576B106C00EF4AD21F8730FC758A73C7FD975F685B0203010001");
		System.out.println("bRequest:" + new String(bRequest));
		// System.out.println("bRequest Header:"+CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)));
		System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
		// _OutToHSM.write(CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)).getBytes());
		_OutToHSM.write(makeRequestHeader(bRequest));
		_OutToHSM.write(bRequest);
		byte[] bResponse = receive(_InputFromHSM);
		System.out.println("bResponse:" + new String(bResponse));
		System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
		Vector<String> vecRet = parseResponseGL(bResponse);
		String strRetCode = (String) vecRet.get(1);
		if (!strRetCode.equals("00")) {
			throw new IOException("HSM command GK/GL return error code "
					+ strRetCode);
		}

		return "";
		// return CryptoUtils.byte2hex((byte[])vecRet.get(4));
	}

	public synchronized String doPkMakWithEO(boolean bErrorTry,
			String strMakerPublicKey) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// EO-EP
			byte[] bRequest = makeRequestEO(strMakerPublicKey);
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			// System.out.println("bRequest Header:"+CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)));
			// _OutToHSM.write(CryptoUtils.byte2hex(makeHexRequestHeader(bRequest)).getBytes());
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector vecRet = parseResponseEP(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command EO/EP return error code "
						+ strRetCode);
			}
			return CryptoUtils.byte2hex((byte[]) vecRet.get(2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			return "";
		}
	}

	/**
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strZEK
	 *            ZEK
	 * @param strEncFlag
	 *            加解密类型：0-DES加密 1-DES解密
	 * @param strCardDataInfo
	 * @return
	 * @throws IOException
	 */
	public synchronized String doEncCardDataInfo(boolean bErrorTry,
			String strZEK, String strEncFlag, String strCardDataInfo)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// E0-E1
			byte[] bRequest = makeRequestE0(strZEK, strEncFlag, strCardDataInfo);
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseE1(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command E0/E1 return error code "
						+ strRetCode);
			}

			return vecRet.get(4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doEncCardDataInfo(false, strZEK, strEncFlag,
						strCardDataInfo);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 1.要注意加锁，不能用一个通道同时并发写入数据，会导致加密机加密数据报错。
	 * 2.数据错误或连接错误之后，要注意断开连接进行重连，否则数据通道里的残留数据可能导致加密机加密错误。
	 * 3.要考虑添加加密机连接池功能，不断开连接而存储多个连接，便于快速交互，又可以保持连接加密机的处理能有好的并发处理特性。
	 * 
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strKeyType
	 *            密钥类型 0－TAK（终端认证密钥）1－ZAK（区域认证密钥）
	 * @param strKeyLenType
	 *            密钥长度 0－单倍长度DES密钥 1－双倍长度DES密钥
	 * @param strKey
	 *            TAK 或 ZAK
	 * @param strData
	 *            需要做MAC的数据
	 * @return
	 * @throws IOException
	 */
	public synchronized String doMACData(boolean bErrorTry, String strKeyType,
			String strKeyLenType, String strKey, String strData)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// MQ-MR
			byte[] bRequest = makeRequestMS(strKeyType, strKeyLenType, strKey,
					strData);
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseMT(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command MS/MT return error code "
						+ strRetCode);
			}

			return vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doMACData(false, strKeyType, strKeyLenType, strKey,
						strData);
			} else {
				throw e;
			}
		}
	}

	// /**
	// * 1.要注意加锁，不能用一个通道同时并发写入数据，会导致加密机加密数据报错。
	// * 2.数据错误或连接错误之后，要注意断开连接进行重连，否则数据通道里的残留数据可能导致加密机加密错误。
	// * 3.要考虑添加加密机连接池功能，不断开连接而存储多个连接，便于快速交互，又可以保持连接加密机的处理能有好的并发处理特性。
	// *
	// * @param bErrorTry 出错重试一次
	// * @param strKeyType 密钥类型 0－TAK（终端认证密钥）1－ZAK（区域认证密钥）
	// * @param strKeyLenType 密钥长度 0－单倍长度DES密钥 1－双倍长度DES密钥
	// * @param strKey TAK 或 ZAK
	// * @param strData 需要做MAC的数据
	// * @return
	// * @throws IOException
	// */
	// public synchronized String doPOSMAC(boolean bErrorTry, String strKeyType,
	// String strKeyLenType, String strKey, String strData) throws IOException
	// {
	// return doPOSMAC(bErrorTry, strKeyType, strKeyLenType, strKey,
	// strData.getBytes());
	// }

	/**
	 * add by 李沣霖, 20121106 1.要注意加锁，不能用一个通道同时并发写入数据，会导致加密机加密数据报错。
	 * 2.数据错误或连接错误之后，要注意断开连接进行重连，否则数据通道里的残留数据可能导致加密机加密错误。
	 * 3.要考虑添加加密机连接池功能，不断开连接而存储多个连接，便于快速交互，又可以保持连接加密机的处理能有好的并发处理特性。
	 * 
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strKeyType
	 *            密钥类型 0－TAK（终端认证密钥）1－ZAK（区域认证密钥）
	 * @param strKeyLenType
	 *            密钥长度 0－单倍长度DES密钥 1－双倍长度DES密钥
	 * @param strKey
	 *            TAK 或 ZAK
	 * @param bytesData
	 *            需要做MAC的数据
	 * @return
	 * @throws IOException
	 */
	public synchronized String doPOSMAC(boolean bErrorTry, String strBlockNo,
			String strKeyType, String strKeyLenType, String type,
			String strKey, byte[] bytesData) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			int nLength = bytesData.length;

			String strIV = "";

			// MS-MT
			byte[] bRequest = makeRequestMS(strBlockNo, strKeyType,
					strKeyLenType, type, strKey, bytesData, strIV);
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseMT(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command MS/MT return error code "
						+ strRetCode);
			}
			strIV = vecRet.get(2);
			// System.out.println("strIV:"+strIV);

			return strIV;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doPOSMAC(bErrorTry, strBlockNo, strKeyType,
						strKeyLenType, type, strKey, bytesData);
			} else {
				throw e;
			}
		}
	}

	// /**
	// * 1.要注意加锁，不能用一个通道同时并发写入数据，会导致加密机加密数据报错。
	// * 2.数据错误或连接错误之后，要注意断开连接进行重连，否则数据通道里的残留数据可能导致加密机加密错误。
	// * 3.要考虑添加加密机连接池功能，不断开连接而存储多个连接，便于快速交互，又可以保持连接加密机的处理能有好的并发处理特性。
	// *
	// * @param bErrorTry 出错重试一次
	// * @param strKeyType 密钥类型 0－TAK（终端认证密钥）1－ZAK（区域认证密钥）
	// * @param strKeyLenType 密钥长度 0－单倍长度DES密钥 1－双倍长度DES密钥
	// * @param strKey TAK 或 ZAK
	// * @param bytesData 需要做MAC的数据
	// * @return
	// * @throws IOException
	// */
	// public synchronized String doPOSMAC(boolean bErrorTry, String strKeyType,
	// String strKeyLenType, String strKey, byte[] bytesData) throws IOException
	// {
	// //建立通讯
	// try {
	// if(null==_OutToHSM || null==_InputFromHSM) connectionHSM();
	//
	// int nLength = bytesData.length;
	//
	// byte[] bytesDataBlock = new byte[8];
	// String strBlockNum = "1";//第1块
	// if(nLength<9)
	// {
	// strBlockNum = "0"; //仅1块
	// System.arraycopy(bytesData, 0, bytesDataBlock, 0, nLength);
	// }
	// else
	// {
	// System.arraycopy(bytesData, 0, bytesDataBlock, 0, 8);
	// }
	// String strIV = "";
	//
	// //第1块
	// //MS-MT
	// byte[] bRequest = makeRequestMS(strBlockNum, strKeyType, strKeyLenType,
	// strKey, bytesDataBlock, strIV);
	// logger.debug("bRequest:"+new String(bRequest));
	// //System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
	// _OutToHSM.write(makeRequestHeader(bRequest));
	// _OutToHSM.write(bRequest);
	// byte[] bResponse = receive(_InputFromHSM);
	// logger.debug("bResponse:"+new String(bResponse));
	// //System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
	// Vector<String> vecRet = parseResponseMT(bResponse);
	// String strRetCode = (String)vecRet.get(1);
	// if(!strRetCode.equals("00"))
	// {
	// throw new IOException("HSM command MS/MT return error code "+strRetCode);
	// }
	// strIV = vecRet.get(2);
	// System.out.println("strIV:"+strIV);
	// if(strBlockNum.equals("0")) return strIV;
	//
	// strBlockNum = "2"; //中间块
	// //中间块
	// int i=8;
	// for(;i<nLength-8;i+=8)
	// {
	// System.arraycopy(bytesData, i, bytesDataBlock, 0, 8);
	// //MS-MT
	// bRequest = makeRequestMS(strBlockNum, strKeyType, strKeyLenType, strKey,
	// bytesDataBlock, strIV);
	// logger.debug("bRequest:"+new String(bRequest));
	// //System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
	// _OutToHSM.write(makeRequestHeader(bRequest));
	// _OutToHSM.write(bRequest);
	// bResponse = receive(_InputFromHSM);
	// logger.debug("bResponse:"+new String(bResponse));
	// //System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
	// vecRet = parseResponseMT(bResponse);
	// strRetCode = (String)vecRet.get(1);
	// if(!strRetCode.equals("00"))
	// {
	// throw new IOException("HSM command MS/MT return error code "+strRetCode);
	// }
	// strIV = vecRet.get(2);
	// System.out.println("strIV:"+strIV);
	// }
	//
	// //最后块
	// byte[] bytesZeroBlock = new byte[8];
	// strBlockNum = "3"; //最末块
	// System.arraycopy(bytesData, i, bytesZeroBlock, 0, nLength-i);
	// //MS-MT
	// bRequest = makeRequestMS(strBlockNum, strKeyType, strKeyLenType, strKey,
	// bytesZeroBlock, strIV);
	// logger.debug("bRequest:"+new String(bRequest));
	// //System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
	// _OutToHSM.write(makeRequestHeader(bRequest));
	// _OutToHSM.write(bRequest);
	// bResponse = receive(_InputFromHSM);
	// logger.debug("bResponse:"+new String(bResponse));
	// //System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
	// vecRet = parseResponseMT(bResponse);
	// strRetCode = (String)vecRet.get(1);
	// if(!strRetCode.equals("00"))
	// {
	// throw new IOException("HSM command MS/MT return error code "+strRetCode);
	// }
	// strIV = vecRet.get(2);
	// System.out.println("strIV:"+strIV);
	//
	// return strIV;
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// closeHSM();
	// connectionHSM();
	// if(bErrorTry)
	// {
	// return doPOSMAC(bErrorTry, strKeyType, strKeyLenType, strKey, bytesData);
	// }
	// else
	// {
	// throw e;
	// }
	// }
	// }

	/**
	 * 随机产生终端工作密钥
	 * 
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strTMK
	 *            TMK
	 * @return
	 * @throws IOException
	 */
	public synchronized Vector<String> doGenTPK(boolean bErrorTry,
			String strTMK, int strKeyLenType) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// HC/HD
			byte[] bRequest = makeRequestIA(strTMK, (strKeyLenType == 1 ? "Z"
					: "X"), (strKeyLenType == 1 ? "Z" : "X"));
			logger.debug("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseIB(bResponse, strKeyLenType);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command IA/IB return error code "
						+ strRetCode);
			}

			return vecRet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doGenTPK(false, strTMK, strKeyLenType);
			} else {
				throw e;
			}
		}
	}

	/**
	 * @param bErrorTry
	 * @param strZEKZAKFlag 产生ZEK或是ZAK的标识，0-ZEK，1-ZAK
	 * @return
	 * @throws IOException
	 */
	public synchronized Vector<String> doGenZEKZAK(boolean bErrorTry, String strZEKZAKFlag, String strZMK, String strZMKStrategy, String strLMKStrategy) throws IOException
	{
		//建立通讯
		try {
			if(null==_OutToHSM || null==_InputFromHSM) connectionHSM();

			//FI-FJ
			byte[] bRequest = makeRequestFI(strZEKZAKFlag, strZMK, strZMKStrategy, strLMKStrategy);
			logger.debug("bRequest:"+new String(bRequest));
			logger.debug("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:"+new String(bResponse));
			logger.debug("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseFJ(bResponse, 16 , 8);
			String strRetCode = (String)vecRet.get(1);
			if(!strRetCode.equals("00"))
			{
				throw new IOException("HSM command FI/FJ return error code "+strRetCode);
			}

			return vecRet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if(bErrorTry)
			{
				return doGenZEKZAK(false, strZEKZAKFlag, strZMK, strZMKStrategy, strLMKStrategy);
			}
			else
			{
				throw e;
			}
		}
	}
	
	/**
	 * 终端MAC密钥LMK转为TMK加密
	 * 
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strTMK
	 *            TMK
	 * @return
	 * @throws IOException
	 */
	public synchronized String doTPKOnLMK2TMK(boolean bErrorTry, String strTPK,
			String strTMK) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// HA/HB
			byte[] bRequest = makeRequestAE(strTPK, strTMK);
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseAF(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command AG/AH return error code "
						+ strRetCode);
			}

			return vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTPKOnLMK2TMK(false, strTPK, strTMK);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 随机产生终端MAC密钥
	 * 
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strTMK
	 *            TMK
	 * @return
	 * @throws IOException
	 */
	public synchronized String doGenTAK(boolean bErrorTry, String strTMK)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// HA/HB
			byte[] bRequest = makeRequestHA(strTMK);
			System.out.println("bRequest:" + new String(bRequest));
			// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			// System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseHB(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command HA/HB return error code "
						+ strRetCode);
			}

			return vecRet.get(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doGenTAK(false, strTMK);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 终端MAC密钥LMK转为TMK加密
	 * 
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strTMK
	 *            TMK
	 * @return
	 * @throws IOException
	 */
	public synchronized String doTAKOnLMK2TMK(boolean bErrorTry, String strTAK,
			String strTMK) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			// HA/HB
			byte[] bRequest = makeRequestAG(strTAK, strTMK);
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector<String> vecRet = parseResponseAH(bResponse);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command AG/AH return error code "
						+ strRetCode);
			}

			return vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doTAKOnLMK2TMK(false, strTAK, strTMK);
			} else {
				throw e;
			}
		}
	}

	/**
	 * @param bErrorTry
	 *            出错重试一次
	 * @param strDESKeyType
	 *            ZEK DES密钥类型
	 * @param strDESKey
	 *            被加密的ZEK密钥
	 * @param strPrivateKeyIndex
	 *            用来解密的私钥的索引值
	 * @return
	 * @throws IOException
	 */
	public synchronized String doImportEncDESKey(boolean bErrorTry,
			String strDESKeyType, String strDESKey, String strPrivateKeyIndex)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			byte[] bRequest = makeRequestGIByLynx(strDESKeyType, strDESKey,
					strPrivateKeyIndex);
			logger.debug("bRequest:" + new String(bRequest));
			logger.debug("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			logger.debug("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector vecRet = parseResponseGJ(bResponse, 33);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command GI/GJ return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(3) + (String) vecRet.get(4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doImportEncDESKey(false, strDESKeyType, strDESKey,
						strPrivateKeyIndex);
			} else {
				throw e;
			}
		}
	}

	/**
	 * @param bErrorTry
	 *            出错重试一次
	 * @return
	 * @throws IOException
	 */
	public synchronized String doCreateZMKKey(boolean bErrorTry,
			String strKeyFlag) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			byte[] bRequest = makeRequestA0("0", "000", strKeyFlag, "", "");
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector vecRet = parseResponseA1(bResponse, 33);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command GI/GJ return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doCreateZMKKey(false, strKeyFlag);
			} else {
				throw e;
			}
		}
	}

	public synchronized String doMakeNewZMK(boolean bErrorTry, String zmk1,
			String zmk2) throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			byte[] bRequest = makeRequestA5(zmk1, zmk2, "X");
			System.out.println("bRequest:" + new String(bRequest));
			System.out.println("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			System.out.println("bResponse:" + new String(bResponse));
			System.out.println("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector vecRet = parseResponseA6(bResponse, 33);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command GI/GJ return error code "
						+ strRetCode);
			}

			return (String) vecRet.get(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doMakeNewZMK(false, zmk1, zmk2);
			} else {
				throw e;
			}
		}
	}

	public synchronized Vector doCreateTMK(boolean bErrorTry, String ZMK)
			throws IOException {
		// 建立通讯
		try {
			if (null == _OutToHSM || null == _InputFromHSM)
				connectionHSM();

			byte[] bRequest = makeRequestA0("1", "000", "X", ZMK, "X");
			logger.debug("bRequest:" + new String(bRequest));
			logger.debug("bRequest:" + CryptoUtils.byte2hex(bRequest));
			_OutToHSM.write(makeRequestHeader(bRequest));
			_OutToHSM.write(bRequest);
			byte[] bResponse = receive(_InputFromHSM);
			logger.debug("bResponse:" + new String(bResponse));
			logger.debug("bResponse:" + CryptoUtils.byte2hex(bResponse));
			Vector vecRet = parseResponseA1(bResponse, 33);
			String strRetCode = (String) vecRet.get(1);
			if (!strRetCode.equals("00")) {
				throw new IOException("HSM command GI/GJ return error code "
						+ strRetCode);
			}

			return vecRet;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeHSM();
			connectionHSM();
			if (bErrorTry) {
				return doCreateTMK(false, ZMK);
			} else {
				throw e;
			}
		}
	}

	public synchronized String doRSA() throws IOException {
		// 建立通讯
		if (null == _OutToHSM || null == _InputFromHSM)
			connectionHSM();
		// A6-A7
		// byte[] bRequest = makeRequestA6(termZMK,ylpk.substring(0, 16),"001");
		byte[] bRequest = makeRequestEI();
		logger.debug("bRequest:" + new String(bRequest));
		// System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
		_OutToHSM.write(makeRequestHeader(bRequest));
		_OutToHSM.write(bRequest);
		byte[] bResponse = receive(_InputFromHSM);
		logger.debug("bResponse:" + CryptoUtils.byte2hex(bResponse));
		Vector<String> vecRet = parseResponseEJ(bResponse);
		String strRetCode = (String) vecRet.get(1);
		if (!strRetCode.equals("00")) {
			throw new IOException("HSM command BA/BB return error code "
					+ strRetCode);
		}
		return (String) vecRet.get(2);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String strCardNo = "6212790000000087";
		// String strPassword = "123456";
		// String strCardNo = "6212790000000049";
		// String strPassword = "135790";
		// String strPassword = "5057AA550E35FA39";

		// String strHost = "8.99.21.51";
		// int nPort = 5555;
		// DataInputStream input = null;
		// DataOutputStream out = null;

		// int nLength = strCardNo.length();
		// String strCardNo12 = strCardNo.substring(nLength-13, nLength-1);
		// System.out.println("strCardNo12:"+strCardNo12);

		// 建立通讯
		try {
			HSMDataPackage hsm = new HSMDataPackage();

			String result = hsm.doMakeNewZMK(false,
					"A63C95E17B661A921237C397236094AA",
					"00000000000000000000000000000000");
			System.out.println(result);
			Vector rv = hsm.doCreateTMK(false, result);
			// 生成RSA公私钥
			// Vector vector = hsm.doCreateRSAKeyPair(false, 1024, 16);
			// System.out.println("*****************");
			// System.out.println((String)vector.get(0));
			// System.out.println((String)vector.get(1));
			// System.out.println((String)vector.get(2));
			// System.out.println(CryptoUtils.byte2hex((byte[])vector.get(3)));
			// System.out.println(CryptoUtils.byte2hex((byte[])vector.get(4)));
			// 私钥解密转为LMK加密
			// String strEncDESKey =
			// "BD39412487DF40CC67DF0F621E0EEBC13BD54A4B28BF3467AC303D213BE3269023808F51F5F10CC5E9B01F36861F1E1999C2A5C89961CD7AA20D91A8DB7EE601A428C78FC0E501C0CEC08A10AC6CD8200130B975DFE0CBF3582AFD67CBE519A0B2099194FB5F38B54C3D33C941FFA929A88FA11887094FD41C965162CD1A74BF".toUpperCase();
			// String strEncDESKey =
			// "C11D93E09A0FAC0A7513C70A677A34AD1EC1422C0BE1B089F7B467EF2A96566941E4F8469B7E54913D7EF57F84BF001D6BA558A327580E2973F31390E2D59568AC7BAB8015811ADDCFE204BC564CF8BB81851C2339D417B96AE66283B445D15DAE961DB1522F100544DFE53C628DFB391D99651D07D0B121E42DE1274B2DE311".toUpperCase();
			// String strPrivateKeyIndex = "16";
			// String strZEK = hsm.doImportEncDESKey(true, "0400", strEncDESKey,
			// strPrivateKeyIndex);
			// System.out.println("*******:"+strZEK);

			// 公钥MAC
			// String pkMac =
			// hsm.doPkMakWithEO(false,"30818902818100E5AF4A1B383BBC49A0017FBCFA418488E01C07A5B59594CEFD053465CCA1BFE4D8F361468ABD521860D80D6E92352023C037C653C681CB428DB059C2FCFEDC90B5C753B072A1EFEA2A5F493093DC0DC8D129732C854913892C5E0BE934C695A8E1937F46D4D50C1778DAF5576B106C00EF4AD21F8730FC758A73C7FD975F685B0203010001");
			// System.out.println("pkMac --->"+pkMac);
			// Vector<String> reVector = hsm.doCreateTMK(false,
			// "E3654366739F2DCACAB2F5A585EF3AD6");
			// String strEData = hsm.doPOSMAC(false,"0","1", "0","0",
			// "2F6458BB32157949",
			// CryptoUtils.hex2byte("0200202004C020C0881131000019385002200012376201180000025001D161252050323747674420313230303032303134303333313030353331313534303131353626000000000000000017010000020005000000"));
			// System.out.println(strEData);
			// hsm.getMacBy919(hsm, "X6DDB0A4F18DA0465E57FBA4AB3E60F87",
			// CryptoUtils.hex2byte("30323030203139393535393937303033303030303030303231352033303030303020303831393134323135362030303133343020303231203038313931343231353633352031323030303230362034303333313030353431313534303120313536"));

			// hsm.doMakeGK();
			// String result = MyHSM.doCdataEnc(hsm,
			// "X2FC4EAE983931BC3A5FF2AA15E54550E",
			// "9559970030000000215=00002101815546");
			// System.out.println(result);
			// 5AEB38859C9CFC07570EFE2B8705EDAE
			// String strCardNo = "2051032000000001340";
			// String strPassword = "123456";
			// String strEncPassword = hsm.doFromEncodePassword(false,
			// strPassword, strCardNo);//hsm.doEncodePassword(false,
			// strPassword, strCardNo);
			// System.out.println("strEncPassword:"+strEncPassword);
			// strEncPassword = hsm.doTransferPinZPK2ZPK(false,
			// "2C482BC03908974C", strCardNo);
			// // String strEncPassword = hsm.doTransferPinZPK2ZPK(false,
			// "6144CC1E8E2D59F8", strCardNo, true);
			// System.out.println("strEncPassword:"+strEncPassword);
			// // strEncPassword =
			// DataFieldPasswordToSWEncode.genEncCardPassword(strCardNo,
			// strPassword);
			// // System.out.println("strEncPassword:"+strEncPassword);

			// HSMDataPackage hsm = new HSMDataPackage();
			// String strCardNo = "9800890020003001477";
			// String strPassword = "123456";
			// String strEncPassword = hsm.doEncodePassword(false, strPassword,
			// strCardNo);
			// System.out.println("strEncPassword:"+strEncPassword);
			// String strEncPassword = hsm.doTransferPinZPK2ZPK(false,
			// "6144CC1E8E2D59F8", strCardNo);
			// String strEncPassword = hsm.doTransferPinZPK2ZPK(false,
			// "6144CC1E8E2D59F8", strCardNo, true);

			// //不带卡号转PIN
			// boolean bNoCardPin = false;
			// if(args[2].equals("1")) bNoCardPin = true;
			// String strEncPassword = "";
			// try
			// {
			// strEncPassword = hsm.doTransferPinZPK2ZPK(false, args[0],
			// args[1], bNoCardPin);
			// }
			// catch(Exception ex)
			// {
			// ex.printStackTrace();
			// }
			// System.out.println("In EncPassword:"+args[0]);
			// System.out.println("strCardNo:"+args[1]);
			// System.out.println("doTransferPinZPK2ZPK 3 params Out strEncPassword:"+strEncPassword);//应该是：B1825C31D38C99FE
			//
			// //带卡号转PIN
			// strEncPassword = hsm.doTransferPinZPK2ZPK(false, args[0],
			// args[1]);
			// System.out.println("doTransferPinZPK2ZPK 2 params Out strEncPassword:"+strEncPassword);//应该是：B1825C31D38C99FE

			// String strCardData = ";6212790200000003=12125202004310456179?";
			// System.out.println("strCardData:"+CryptoUtils.byte2hex(strCardData.getBytes()));
			// System.out.println("strCardData Length:"+CryptoUtils.byte2hex(strCardData.getBytes()).length());
			// ----
			// Vector vecRet = hsm.doCreateRSAKeyPair(true, 1024, 99);//2048
			// error
			// System.out.println("vecRet.1:"+vecRet.get(0));
			// System.out.println("vecRet.2:"+vecRet.get(1));
			// System.out.println("vecRet.3:"+vecRet.get(2));
			// System.out.println("vecRet.4 Hex:"+CryptoUtils.byte2hex((byte[])vecRet.get(3)));
			// System.out.println("vecRet.5 Hex:"+CryptoUtils.byte2hex((byte[])vecRet.get(4)));

			// //产生ZAK
			// Vector<String> vecRet = hsm.doCreateZEKZAK(true, "1");//ok
			// System.out.println("vecRet.1:"+vecRet.get(0));
			// System.out.println("vecRet.2:"+vecRet.get(1));
			// System.out.println("vecRet.3:"+vecRet.get(2));
			// System.out.println("vecRet.4:"+vecRet.get(3));

			// byte[] bBufferHeader = {5,-54};
			// System.out.println("bBufferHeader[0]:"+bBufferHeader[0]);
			// System.out.println("bBufferHeader[1]:"+bBufferHeader[1]);
			// //nLen = bBufferHeader[0]*0x100+bBufferHeader[1];//error
			// System.out.println("nLen Hi:"+(bBufferHeader[0]<<8));
			// System.out.println("nLen Low:"+(0xFF&bBufferHeader[1]));
			// int nLen = (bBufferHeader[0]<<8)|(0xFF&bBufferHeader[1]);//ok
			// System.out.println("nLen:"+nLen);

			// String strMakerPublicKey =
			// "E81814D56F5B0A92A62D99F64312BD1F993AE6ED3720F7763A55CB7A932F218855BEC8CFE060458D3332446C063DF556D9D520869D3CAC4943C6107E94A0614D512104666344AED47B8C0A33C74088BBA8A3DBEEF20FA0CCFF33C24B4AAAB3F16F623DD42736FB9DC24BFC44776515120EFAAD497E5BE3ADDBD2FF7B22BAC50BC3BF01630048BF035F47F694BFA62FB776703D4A19731100FCC75B9FCEC2FFB3DE4FF606ED9309E2C771DE7F14A0B44F948F3181E1A8D89BE1F7D02E05045A06C78EC1F9D1B3DDEA1FFFC899EDCDF603F37EC9DF0BA6E55E9921C2AEAC5300AE2C345760D7923F1EE48EBA90071825AE496AA6FE7D9CEC41FB38EAD5742E1CA3";
			// String strZEK = "X27BD35B0D31ED290D26304E757AFD179";
			// String strZEKMAC = "47323DE01D0F5A8A";
			// String strEncDESKey = hsm.doExportEncDESKey(true,
			// strMakerPublicKey, "3000", strZEK, strZEKMAC);
			// System.out.println("strEncDESKey Hex:"+strEncDESKey);

			// String strZEK = "X8CE0B999294AB1AC8CE0B999294AB1AC";
			// String strEncFlag = "0";//加密
			// String strCardDataInfo =
			// ";6212790000000087=00005206569793874303?         ";//必须是16的倍数
			// //String strCardDataInfo = "6212790000000087";
			// String strEncCardDataInfo = hsm.doEncCardDataInfo(true, strZEK,
			// strEncFlag, CryptoUtils.byte2hex(strCardDataInfo.getBytes()));
			// System.out.println("strEncCardDataInfo:"+strEncCardDataInfo);
			// strEncFlag = "1";//解密
			// strCardDataInfo = hsm.doEncCardDataInfo(true, strZEK, strEncFlag,
			// strEncCardDataInfo);
			// System.out.println("strCardDataInfo:"+new
			// String(CryptoUtils.hex2byte(strCardDataInfo)));

			// String strDESKeyType = "3000";
			// String strEncDESKey =
			// "8D6CB453D59725E2854F49A8E608059A9AEAA46DD8FCF4F6E2AE5EAF5CA257A93483F693EDA1813CFCBDEB8C3F8FC93446FD932A21E7B857D08E80C07DD4914523BE0073F3FF88E97F2EF3FDBD76DA2BF0BB2925FE243909C0A4EAFB9F8FA47A939692A53324008D8D2DEFB0ADD0E7E093E7BCEB3BC77DE97005736014CFD3C762BBB0BC43406CFEC8A2D054C7CFF2B1A27202E646C1997C881635F5820AAA515712D69E76EF64CBD63EC3AE4FC6B325A8E594EF0FF3EE99A42BCFD26B49F3ED68DDC9AD014BDD0E691FE7841543605D8C21F4D640CFAA9342FFF26D8F036D451A5B227FC20E2C31B9C2D0B4F31A23B54BA841AA488AAA4A635CA4DC0F541ABF";
			// String strPrivateKeyIndex = "12";
			// String strZEK = hsm.doImportEncDESKey(true, strDESKeyType,
			// strEncDESKey, strPrivateKeyIndex);
			// String strEncFlag = "1";//解密
			// String strEncCardDataInfo =
			// "E7516CE7D3401C3EE7516CE7D3401C3EE7516CE7D3401C3EE7516CE7D3401C3EE7516CE7D3401C3EE7516CE7D3401C3E3BB753F5FFE6F453A96D9CAE350BB7CE4B4003599E3AD371FE0092D68B654A9531C055D2CA4E6A1F31C055D2CA4E6A1F7B02C9F1F51FE42A";
			// String strCardDataInfo = hsm.doEncCardDataInfo(true, strZEK,
			// strEncFlag, strEncCardDataInfo);
			// System.out.println("strCardDataInfo:"+strCardDataInfo);
			// System.out.println("strCardDataInfo:"+new
			// String(CryptoUtils.hex2byte(strCardDataInfo)));

			// String strKeyType = "1";
			// String strKeyLenType = "0";
			// String strKey = "CD0F9D3F8550A01B";
			// String strCardDataInfo = "1234567890ABCDEF";
			// String strMACCardDataInfo = hsm.doMACData(true, strKeyType,
			// strKeyLenType, strKey, strCardDataInfo);
			// System.out.println("strMACCardDataInfo:"+strMACCardDataInfo);
			//
			// System.out.println(CryptoUtils.byte2hex(CryptoUtils.symmetricEncrypto(CryptoUtils.hex2byte("1111111111111111"),
			// CryptoUtils.hex2byte(strCardDataInfo))));

			// String strKeyFlag = "X";
			// String strZMK = hsm.doCreateZMKKey(true, strKeyFlag);
			// System.out.println("strZMK:"+strZMK);

			/*
			 * Socket socket = new Socket(strHost, nPort);
			 * socket.setSoTimeout(0); socket.setSoLinger(true, 300); out = new
			 * DataOutputStream(socket.getOutputStream()); input = new
			 * DataInputStream(socket.getInputStream());
			 * 
			 * HSMDataPackage hsm = new HSMDataPackage(); byte[] bRequest =
			 * hsm.makeRequestBA(strPassword, strCardNo12); //String strSend =
			 * "JGX7B4D14A7FDEAC51429B7BB06D381E34D010222795975996450993";
			 * System.out.println("bRequest:"+new String(bRequest));
			 * System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			 * out.write(bRequest); byte[] bResponse = hsm.receive(input);
			 * System.out.println("bResponse:"+new String(bResponse));
			 * System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			 */

			/*
			 * SocketChannel sc = SocketChannel.open(new
			 * InetSocketAddress(strHost, nPort)); sc.configureBlocking(false);
			 * HSMDataPackage hsm = new HSMDataPackage(); byte[] bRequest =
			 * hsm.makeRequestBA(strPassword, strCardNo12);
			 * System.out.println("bRequest:"+new String(bRequest));
			 * System.out.println("bRequest:"+CryptoUtils.byte2hex(bRequest));
			 * ByteBuffer bBuffer = ByteBuffer.allocate(bRequest.length);
			 * bBuffer.put(bRequest); bBuffer.flip(); sc.write(bBuffer);
			 * ByteBuffer bBufferRet = ByteBuffer.allocate(1024); while (true) {
			 * int r = 0; r = sc.read(bBufferRet);
			 * 
			 * if (r<=0) { break; }
			 * 
			 * System.out.println( "recieve message"); bBufferRet.flip(); }
			 * //sc.read(bBufferRet); //bBufferRet.flip();
			 * //bBufferRet.get(bResponse); byte[] bResponse =
			 * bBufferRet.array(); System.out.println("bResponse:"+new
			 * String(bResponse));
			 * System.out.println("bResponse:"+CryptoUtils.byte2hex(bResponse));
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte getVersion() {
		return _Version;
	}

	public void setVersion(byte version) {
		_Version = version;
	}

}
