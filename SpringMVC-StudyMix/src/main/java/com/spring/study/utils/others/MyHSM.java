/**
 * 
 */
package com.spring.study.utils.others;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maikong.utils.exceptions.PreposeException;






public class MyHSM {

	private static Logger logger = LoggerFactory.getLogger(MyHSM.class);
	
	public static String getCardPwd(String cardNo,String pinBlock,String workKey) throws Exception{
		HSMDataPackage hsm = new HSMDataPackage();
		String localZPK = "X71D174CC1DF8FBFE92FA5FA3DD17E06A";
		 String pinString = MyHSM.doPin(hsm, workKey, localZPK,
				 pinBlock, cardNo);
		String pinBlockM = CryptoUtils.decrypt3DES(pinString, "FDE9106DA216D5DA3D677A01C126B391");
		String ming = CryptoUtils.pwdFromPinBlock(CryptoUtils.hex2byte(pinBlockM), cardNo);
		System.out.println("passwd:"+ming);
		return MD5Util.MD5(ming);
	}
	
	public static String doA6(HSMDataPackage hsm ,String chType,String termZMK,String orgZMK) throws IOException{
			return hsm.doA6000(chType,termZMK,orgZMK);
	}
	
	public static String doA6(HSMDataPackage hsm ,String chType,String termZMK) throws IOException{
		return hsm.doA6000(chType,termZMK,null);
}
	
	public static String doHmSignIn(HSMDataPackage hsm ,String termZMK,String ylPk){
		try {
			return hsm.doHMSignIn(termZMK,ylPk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doHmSignInForDoubleX(HSMDataPackage hsm ,String termZMK,String ylPk){
		try {
			return hsm.doHMSignInForDoubleX(termZMK,ylPk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static String doHMSignInWithEncKeyAndZForZak(HSMDataPackage hsm ,String termZMK,String ylPk){
		try {
			return hsm.doHMSignInWithEncKeyAndZForZak(termZMK,ylPk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doHmSignInWithEncKey(HSMDataPackage hsm ,String termZMK,String ylPk){
		try {
			return hsm.doHMSignInWithEncKey(termZMK,ylPk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doCdataEnc(HSMDataPackage hsm ,String strZEK,String cData){
		try {
			if (cData.length() >= 40) {
				if (cData.length() >= 100) {
					cData = cData.length() + cData;
				}else {
					cData = "0"+cData.length() + cData;
				}
			}else {
				cData = cData.length() + cData;
			}
			
			int leng = cData.length() % 8;
			if (leng!=0) {
				for (int i = 0; i < 8-leng ;i++) {
					cData = cData+"0";
				}
			}
			
		    String result = hsm.doEncCardDataInfo(false, strZEK, "0", CryptoUtils.byte2hex(cData.getBytes()));
		   
		   
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getEncCDataForUP(HSMDataPackage hsm ,String encKey,byte[] cData) throws Exception{
		byte[] destData = new byte[8];
		System.arraycopy(cData, cData.length-9, destData, 0, 8);
		String r1 = hsm.doEncCardDataInfo(false, encKey, "0", CryptoUtils.byte2hex(destData));
		byte[] result = CryptoUtils.hex2byte(r1);
		System.arraycopy(result, 0, cData, cData.length-9, 8);
		return CryptoUtils.byte2hex(cData);
	}
	
	
	public static String getEncCDataForJsYs(HSMDataPackage hsm ,String encKey,byte[] cData) throws Exception{
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
			String result = hsm.doEncCardDataInfo(false, encKey, "0", CryptoUtils.byte2hex(edata));
			return result;
		}else {
			String result = hsm.doEncCardDataInfo(false, encKey, "0", CryptoUtils.byte2hex(cData));
			return result;
		}
	}
	
	
	public static String doMac(HSMDataPackage hsm ,String termZAK,byte[] macData){
		try {
			return hsm.getMac(hsm,termZAK,macData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getMacBy919With001(HSMDataPackage hsm ,String termZAK,byte[] macData) throws Exception{
		try {
			return hsm.getMacBy919With001(hsm,termZAK,macData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doMacForYinsheng(HSMDataPackage hsm ,String termZAK,byte[] macData){
		try {
			return hsm.getMacForYinsheng(hsm,termZAK,macData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doMacBy919(HSMDataPackage hsm ,String termZAK,byte[] macData){
		try {
			return hsm.getMacBy919(hsm,termZAK,macData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String doPin(HSMDataPackage hsm ,String workKey,String tmpZPK,String cardPwd,String cardNo) throws IOException, PreposeException{
			return hsm.doTransferPinZPK2ZPK(false, workKey,tmpZPK, cardPwd, cardNo, false);
	}
	
	public static String doPin(HSMDataPackage hsm ,String tmpZPK,byte[] block){
		try {
			return hsm.doPin(hsm, tmpZPK, block);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ANSIX9.8��ʽ
	 * @param strPassword
	 * @param strCardNo
	 * @return
	 */
	public static byte[] pinBlock(String strPassword, String strCardNo)
	{
		//PIN BLOCK - 8λ
		byte[] bytesPin = new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
		bytesPin[0] = (byte)strPassword.length();
		byte[] bcdPwd = CryptoUtils.str2bcd(strPassword);
		System.arraycopy(bcdPwd, 0, bytesPin, 1, bcdPwd.length);
		//PAN  - ����û����ǰ���0����6λ
		int nLength = strCardNo.length();
//		String strCardNo12 = strCardNo.substring(nLength-13, nLength-1);
		String strCardNo12="127901000037";
		byte[] bcdPAN = CryptoUtils.str2bcd(strCardNo12);
		//���
		byte[] bytesPinBlock = new byte[8];
		bytesPinBlock[0] = bytesPin[0];
		bytesPinBlock[1] = bytesPin[1];
		for(int i=2;i<8;i++)
		{
			bytesPinBlock[i] = (byte)(bytesPin[i]^bcdPAN[i-2]);
		}
		return bytesPinBlock;
	}

	/**
	 * ANSIX9.8��ʽ  pinBlock ��ԭ����
	 * @param strPassword
	 * @param strCardNo
	 * @return
	 */
	public static String pwdFromPinBlock(byte[] bytesPinBlock, String strCardNo)
	{
		//PIN - 8λ
		byte[] bytesPin = new byte[8];
		//PAN  - ����û����ǰ���0����6λ
		int nLength = strCardNo.length();
		String strCardNo12 = strCardNo.substring(nLength-13, nLength-1);
		byte[] bcdPAN = CryptoUtils.str2bcd(strCardNo12);
		//���
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
	 * ANSIX9.9��ʽ�ĵ�����MAC
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static byte[] ansi99MAC(byte[] strData, byte[] bytesKey) throws Exception
	{
//		byte[] bytesData = strData.getBytes();
		byte[] bytesData = strData;
		int nLength = bytesData.length%8;

		ByteArrayOutputStream byteArrayData = new ByteArrayOutputStream();
		byteArrayData.write(bytesData);

		if(0!=nLength)
		{
			byteArrayData.write(new byte[8-nLength]);
			nLength = bytesData.length+8-nLength;
		}
		else
		{
			nLength = bytesData.length;
		}
		bytesData = byteArrayData.toByteArray();

		//byte[] bytesMAC = new byte[8];
		byte[] bytesE = new byte[8];
		for(int i=0;i<nLength;i+=8)
		{
			for(int j=0;j<8;j++)
			{
				bytesE[j] ^= bytesData[i+j];
			}
//			System.out.println("key:"+new String(bytesKey));
//			System.out.println("key hex:"+byte2hex(bytesKey));
//			System.out.println("data:"+new String(bytesE));
//			System.out.println("data hex:"+byte2hex(bytesE));
			bytesE = CryptoUtils.symmetricEncrypto(bytesKey,bytesE);
			//System.out.println(byte2hex(bytesE));
		}

		return bytesE;
	}

	public static String randomKey(byte[] bytesTMK, int nKeyLen)
	{
		String strNewKey = StringUtils.genRandomStr(2*nKeyLen, 3);
		//System.out.println("strNewKey:"+strNewKey);
		byte[] byteData = CryptoUtils.hex2byte(strNewKey);
		try {
			byteData = CryptoUtils.symmetricEncrypto(bytesTMK, byteData);
			return CryptoUtils.byte2hex(byteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String randomKey(byte[] bytesTMK, int nKeyLen, int nFSData)
	{
		String strNewKey = StringUtils.genRandomStr(2*nKeyLen, 3, nFSData);
		//System.out.println("strNewKey:"+strNewKey);
		byte[] byteData = CryptoUtils.hex2byte(strNewKey);
		try {
			byteData = CryptoUtils.symmetricEncrypto(bytesTMK, byteData);
			return CryptoUtils.byte2hex(byteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String randomKey(byte[] bytesTMK, int nKeyLen, String strFSData)
	{
		String strNewKey = StringUtils.genRandomStr(2*nKeyLen, 3, strFSData);
		//System.out.println("strNewKey:"+strNewKey);
		byte[] byteData = CryptoUtils.hex2byte(strNewKey);
		try {
			byteData = CryptoUtils.symmetricEncrypto(bytesTMK, byteData);
			return CryptoUtils.byte2hex(byteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String encodeKey(byte[] bytesTMK, byte[] bytesKey)
	{
		try {
			byte[] byteData = CryptoUtils.symmetricEncrypto(bytesTMK, bytesKey);
			return CryptoUtils.byte2hex(byteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String decodeKey(byte[] bytesTMK, byte[] bytesKey)
	{
		try {
			byte[] byteData = CryptoUtils.symmetricDecrypto(bytesTMK, bytesKey);
			return CryptoUtils.byte2hex(byteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �������ն�Key������TPK��TAK�Կ�
	 * @param hsm     - Ӳ�����ܻ�ӿ���
	 * @param strZEK  - ��������Key���ĵļ��ܻ�ZEK����
	 * @param nKeyLen - Ҫ�����Key����λ��
	 * @param intFSData - ����Keyʱ�ĸ��ŷ���
	 * @return          - ��ZEK���ܹ���ն�Key���ģ�ͨ���洢����ݿ���
	 * @throws IOException
	 */
	public static String genRandTPTAKey(int nKeyLen, Integer intFSData, HSMDataPackage hsm, String strZEK) throws IOException
	{
		//����������������ִ�
		String strKey = null;
		if(null==intFSData)
		{
			strKey = StringUtils.genRandomStr(nKeyLen*2, 3);
		}
		else
		{
			strKey = StringUtils.genRandomStr(nKeyLen*2, 3, intFSData.intValue());
		}
		logger.debug("�ն�Key����:"+strKey);

		//��Կ�ִ���������ZEK�����ܻ���ܼ���õ�����
		strKey = hsm.doEncCardDataInfo(true, strZEK, "0", strKey);
		logger.debug("�ն�Key�����ܻ�ZEK���ܵ�����:"+strKey);

		return strKey;
	}

	/**
	 * �������ն�Key������TPK��TAK�Կ�
	 * @param hsm     - Ӳ�����ܻ�ӿ���
	 * @param strZEK  - ��������Key���ĵļ��ܻ�ZEK����
	 * @param nKeyLen - Ҫ�����Key����λ��
	 * @param intFSData - ����Keyʱ�ĸ��ŷ���
	 * @return          - ��ZEK���ܹ���ն�Key���ģ�ͨ���洢����ݿ���
	 * @throws Exception 
	 * @throws IllegalArgumentException 
	 */
	public static String transTPTAKey2TMN(HSMDataPackage hsm, String strZEK, String strKey, byte[] bytesTMK) throws IllegalArgumentException, Exception
	{
		logger.debug("���ܻ���ܵ�Key����:"+strKey);
		//��ZEK�����ܻ���ܼ����Key��Կ������
		String strRealKey = hsm.doEncCardDataInfo(true, strZEK, "1", strKey);
		logger.debug("���ܻ���ܵ�����:"+strRealKey);
		//��KeyתΪTMK����
		byte[] bytesEncodeKey = CryptoUtils.symmetricEncrypto(bytesTMK, CryptoUtils.hex2byte(strRealKey));
		String strEncodeKey = CryptoUtils.byte2hex(bytesEncodeKey);
		logger.debug("Key��TMK���ܵ�����:"+strEncodeKey);

		return strEncodeKey;
	}

	/**
	 * @param hsm     - Ӳ�����ܻ�ӿ���
	 * @param strZEK  - ��������Key���ĵļ��ܻ�ZEK����
	 * @param strTAK  - ������MACУ���MAC��Կ(������ZEK�ӹ��ܵ�����)
	 * @param strMACInfo - �����MAC����Ϣ
	 * @return           - MACУ��ֵ
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	public static String genPOSMAC(HSMDataPackage hsm, String strZEK, String strTAK, String strMACInfo) throws IllegalArgumentException, Exception
	{
		logger.debug("����MAC�����ݣ�"+strMACInfo);
		logger.debug("����MACУ���TAK���ģ�"+strTAK);
		//��ZEK�����ܻ���ܼ����TAK��Կ������
		String strRealTAK = hsm.doEncCardDataInfo(true, strZEK, "1", strTAK);
		logger.debug("����MACУ���TAK���ģ�"+strRealTAK);
		byte[] bytesMAC = MyHSM.ansi99MAC(strMACInfo.getBytes(), CryptoUtils.hex2byte(strRealTAK));
		String strMyMAC = CryptoUtils.byte2hex(bytesMAC);
		logger.debug("����˼����MAC��"+strMyMAC);

		return strMyMAC;
	}

	/**
	 * @param hsm     - Ӳ�����ܻ�ӿ���
	 * @param strZEK  - ��������Key���ĵļ��ܻ�ZEK����
	 * @param strTAK  - ������MACУ���MAC��Կ(������ZEK�ӹ��ܵ�����)
	 * @param strMACInfo - �����MAC����Ϣ
	 * @return           - MACУ��ֵ
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	public static String decodePOSPIN(HSMDataPackage hsm, String strZEK, String strTPK, byte[] bytesPIN, String strCardNo) throws IllegalArgumentException, Exception
	{
		//��ZEK�����ܻ���ܼ����TPK��Կ������
		logger.debug("��������PIN��TPK���ģ�"+strTPK);
		String strRealTPK = hsm.doEncCardDataInfo(true, strZEK, "1", strTPK);
		logger.debug("��������PIN��TPK���ģ�"+strRealTPK);
		byte[] bytesPinBlock = CryptoUtils.symmetricDecrypto(CryptoUtils.hex2byte(strRealTPK), bytesPIN);
		String strPwd = pwdFromPinBlock(bytesPinBlock, strCardNo);
		logger.debug("����˽��ܳ������룺"+strPwd);

		return strPwd;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/***pinBlock***/
//		System.out.println(CryptoUtils.hex2byte(6212790100003752D120152061636204505910));
//		String strCardNo="6229930100200000034=49121200001820000";
//		System.out.println(strCardNo.indexOf("=")-2);
		//61A2A4D9FE5BFE98
//		000000200103
//		127901000037
		
//		int nLength = strCardNo.length();
//		String strCardNo12 = strCardNo.substring(nLength-13, nLength-1);
//		System.out.println(strCardNo12);
//		MyHSM.pinBlock("111111", "127901000037");
//		try {
//			System.out.println(CryptoUtils.symmetricEncrypto(CryptoUtils.hex2byte("61A2A4D9FE5BFE98"), MyHSM.pinBlock("111111", "12334")));
//			System.out.println(CryptoUtils.byte2hex(CryptoUtils.symmetricEncrypto(CryptoUtils.hex2byte("61A2A4D9FE5BFE98"), MyHSM.pinBlock("111111", "12334"))));
//			
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		byte[] mk = null;
//		try {
//			mk = DesUtil.encrypt(byteData, keyData);
//			System.out.println(mk);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		byte[] result;
//		try {
//			result = DesUtil.decrypt("00000000".getBytes(), mk);
//			System.out.println(result);
//			System.out.println("B03C0184");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//		//String strData = "0612341FAD9FFEF7";

//		/***��pinBlock�������***/
//		String strPwd = pwdFromPinBlock(CryptoUtils.hex2byte("0612341FAD9FFEF7"), "9800880049526001088");
//		logger.info("����˽��ܳ������룺"+strPwd);//123456

//		/***��֪��Key���ļӽ���PINBlockֵ(01��ʽ)***/
//		String strKey = "AD5C93173ED83549";//"38722C3009F6C02C";//"FEE5AE61C79D30FA";//"38722C3009F6C02C";
//		String strData = "0612341FAD9FFEF7";
//		byte[] bytesKey = CryptoUtils.hex2byte(strKey);//com.lmx.util.ByteUtils.asc2num(strKey);
//		byte[] byteData = CryptoUtils.hex2byte(strData);
//
//		byte[] byteRet = null;//new byte[byteData.length];
//		byte[] byteRet1 = null;
//		try
//		{
//			System.out.println(CryptoUtils.byte2hex(byteData));
//			byteRet = CryptoUtils.symmetricEncrypto(bytesKey,byteData);//AAED130B2F1531C0
//			System.out.println(CryptoUtils.byte2hex(byteRet));
//			byteRet1 = CryptoUtils.symmetricDecrypto(bytesKey,byteRet);
//			System.out.println(CryptoUtils.byte2hex(byteRet1));
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace(System.err);
//		}
//		System.out.println(byteRet.length);
//		System.out.println(byteRet1.length);

		//�ֹ�MACֵ
//		System.out.println(CryptoUtils.byte2hex("88042959159B9DB0".getBytes()));

//		System.out.println(MyHSM.randomKey(CryptoUtils.hex2byte("38722C3009F6C02C"), 8, "38722C3009F6C02C"));
//		System.out.println(DateUtils.currentDate("yyyy-MM-dd hh:mm:ss.SSS"));
//		for(int i=0;i<10000;i++)
//		{
//			MyHSM.randomKey(CryptoUtils.hex2byte("38722C3009F6C02C"), 8, "38722C3009F6C02C");
//		}
//		System.out.println(DateUtils.currentDate("yyyy-MM-dd hh:mm:ss.SSS"));

	}

}
