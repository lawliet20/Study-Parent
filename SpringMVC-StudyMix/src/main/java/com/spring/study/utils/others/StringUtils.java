package com.spring.study.utils.others;

import java.util.Arrays;


public class StringUtils extends org.apache.commons.lang3.StringUtils{
	public static final int ACTION_EMPTY_AS_NULL = 0x001;
	public static final int ACTION_TRIM = 0x002;
	
	/**
	 * StringUtils constructor comment.
	 */
	public StringUtils() {
		super();
	}
	
	public static String createVAR(String val, int flag) {
		String result = "";
		if (2 == flag) {
			if (val.getBytes().length < 10) {
				result = "0" + val.getBytes().length + val;
			} else {
				result = val.getBytes().length + val;
			}
		} else {
			if (val.getBytes().length < 10) {
				result = "00" + val.getBytes().length + val;
			} else if (val.getBytes().length < 100) {
				result = "0" + val.getBytes().length + val;
			} else {
				result = val.getBytes().length + val;
			}
		}
		return result;
	}
	public static String createVAR(String val, int flag, int db) {
		String result = "";
		if (2 == flag) {
			if (val.getBytes().length < 10) {
				result = "0" + val.getBytes().length + val;
			} else {
				result = val.getBytes().length + val;
			}
		} else {
			if (val.getBytes().length < 10) {
				result = "00" + val.getBytes().length + val;
			} else if (val.length() * db < 100) {
				result = "0" + val.getBytes().length + val;
			} else {
				result = val.getBytes().length + val;
			}
		}
		return result;
	}

	public static String createVAR(String val, int flag, String type) {
		String result = "";
		int len = 0;
		if ("hex".equals(type)) {
			len = CryptoUtils.hex2byte(val).length;
		} else {
			len = val.getBytes().length;
		}
		if (2 == flag) {
			if (len < 10) {
				result = "0" + len + val;
			} else {
				result = len + val;
			}
		} else {
			if (len < 10) {
				result = "00" + len + val;
			} else if (len < 100) {
				result = "0" + len + val;
			} else {
				result = len + val;
			}
		}
		return result;
	}

	public static String createByteVAR(String val, int flag) {
		String result = "";
		int valLen = CryptoUtils.hex2byte(val).length;
		if (2 == flag) {
			if (valLen < 10) {
				result = "0" + valLen + val;
			} else {
				result = valLen + val;
			}
		} else {
			if (valLen < 10) {
				result = "00" + valLen + val;
			} else if (valLen < 100) {
				result = "0" + valLen + val;
			} else {
				result = valLen + val;
			}
		}
		return result;
	}
	public static String perform(String strObj, int intMethod) {
		if (strObj == null)
			return null;

		if ((intMethod & StringUtils.ACTION_EMPTY_AS_NULL) > 0) {
			if (null != strObj && strObj.trim().equals(""))
				return null;
		}

		if ((intMethod & StringUtils.ACTION_TRIM) > 0) {
			if (null != strObj)
				strObj = strObj.trim();
		}

		if ((intMethod & StringUtils.ACTION_REPLACE_RETURN_AS_BLANK) > 0) {
			if (null != strObj) {
				StringBuffer sb = new StringBuffer(strObj.trim());
				for (int i = 0; i < sb.length(); ++i) {
					if ('\n' == sb.charAt(i) || '\r' == sb.charAt(i)) {
						sb.setCharAt(i, ' ');
					}
				}
				strObj = sb.toString();
			}
		}

		return strObj;
	}
	public static final int ACTION_REPLACE_RETURN_AS_BLANK = 0x003;
	public static String Int2String(int nValue, int nBit) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nBit; ++i) {
			sb.append('0');
		}
		sb.append(nValue);

		return sb.substring(sb.length() - nBit);
	}
	public static boolean isCharInASCII(String str) {
		int length = str.length();
		char[] charArray = new char[length];
		str.getChars(0, length, charArray, 0);

		for (int i = 0; i < length; i++) {
			if (charArray[i] < '0' || charArray[i] > 'z')
				return false;
		}

		return true;
	}
	public static String Long2String(long lnValue, int nBit) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nBit; ++i) {
			sb.append('0');
		}
		sb.append(lnValue);

		return sb.substring(sb.length() - nBit);
	}
	public static String getParameter(String strParam) {
		String strRet = strParam;
		if (strRet == null)
			return null;

		try {
			strRet = new String(strRet.getBytes("8859_1"));
		} catch (java.io.UnsupportedEncodingException ex) {
			return null;
		}

		return strRet;
	}
	public static String genRandomStr(int bit) {
		StringBuffer sbResult = new StringBuffer();
		java.util.Random rand =
			new java.util.Random(
				java.util.Calendar.getInstance().getTime().getTime());
		byte byteArray[] = new byte[bit];
		rand.nextBytes(byteArray);

		for (int i = 0; i < bit; i++) {
			byte sect = (byte) Math.abs(rand.nextInt() % 3);
			int temp;

			switch (sect) {
				//add a char of 0-9
				case 0 :
					temp = Math.abs(byteArray[i]) % 10 + 48;
					sbResult.append((char) temp);
					break;
					//add a char of A-Z
				case 1 :
					temp = Math.abs(byteArray[i]) % 26 + 65;
					sbResult.append((char) temp);
					break;
					//add a char of a-z
				case 2 :
					temp = Math.abs(byteArray[i]) % 26 + 97;
					sbResult.append((char) temp);
			}
		}

		return sbResult.toString();
	}
	/**
	 * ������һ��ָ�����Ⱥ����͵��ַ�
	 * @param bit
	 * @param type 0-0��9������ 1-A��Z����ĸ 2-a��z����ĸ 3-0��9�����ֺ�A��F����ĸ
	 * @return
	 */
	public static String genRandomStr(int bit, int type) {
		StringBuffer sbResult = new StringBuffer();
		java.util.Random rand =
			new java.util.Random(
				java.util.Calendar.getInstance().getTime().getTime());
		byte byteArray[] = new byte[bit];
		rand.nextBytes(byteArray);

		int temp;
		switch (type) 
		{
			//add a char of 0-9
			case 0 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]) % 10 + 48;
					sbResult.append((char) temp);
				}
				break;
			//add a char of A-Z
			case 1 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]) % 26 + 65;
					sbResult.append((char) temp);
				}
				break;
			//add a char of a-z
			case 2 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]) % 26 + 97;
					sbResult.append((char) temp);
				}
				//add a char of 0-9 A-F
			case 3 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]) % 16;
					if(temp<10) temp += 48;
					else temp += 65-10;
					sbResult.append((char) temp);
				}
				break;
		}

		return sbResult.toString();
	}
	/**
	 * ������һ��ָ�����Ⱥ����͵��ַ�
	 * @param bit
	 * @param type 0-0��9������ 1-A��Z����ĸ 2-a��z����ĸ 3-0��9�����ֺ�A��F����ĸ
	 * @return
	 */
	public static String genRandomStr(int bit, int type, int nFSData) {
		StringBuffer sbResult = new StringBuffer();

		java.util.Random rand =
			new java.util.Random(
				java.util.Calendar.getInstance().getTime().getTime());
		byte byteArray[] = new byte[bit];
		rand.nextBytes(byteArray);

		int temp;
		switch (type) 
		{
			//add a char of 0-9
			case 0 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^nFSData) % 10 + 48;
					sbResult.append((char) temp);
				}
				break;
			//add a char of A-Z
			case 1 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^nFSData) % 26 + 65;
					sbResult.append((char) temp);
				}
				break;
			//add a char of a-z
			case 2 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^nFSData) % 26 + 97;
					sbResult.append((char) temp);
				}
				//add a char of 0-9 A-F
			case 3 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^nFSData) % 16;
					if(temp<10) temp += 48;
					else temp += 65-10;
					sbResult.append((char) temp);
				}
				break;
		}

		return sbResult.toString();
	}
	/**
	 * ������һ��ָ�����Ⱥ����͵��ַ�
	 * @param bit
	 * @param type 0-0��9������ 1-A��Z����ĸ 2-a��z����ĸ 3-0��9�����ֺ�A��F����ĸ
	 * @return
	 */
	public static String genRandomStr(int bit, int type, String strFSData) {
		StringBuffer sbResult = new StringBuffer();
		sbResult.append(strFSData);
		while(sbResult.length()<bit)
		{
			sbResult.append(strFSData);
		}
		byte[] bytesFSData = sbResult.toString().getBytes();
		sbResult.setLength(0);

		java.util.Random rand =
			new java.util.Random(
				java.util.Calendar.getInstance().getTime().getTime());
		byte byteArray[] = new byte[bit];
		rand.nextBytes(byteArray);

		int temp;
		switch (type) 
		{
			//add a char of 0-9
			case 0 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^bytesFSData[i]) % 10 + 48;
					sbResult.append((char) temp);
				}
				break;
			//add a char of A-Z
			case 1 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^bytesFSData[i]) % 26 + 65;
					sbResult.append((char) temp);
				}
				break;
			//add a char of a-z
			case 2 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^bytesFSData[i]) % 26 + 97;
					sbResult.append((char) temp);
				}
				//add a char of 0-9 A-F
			case 3 :
				for (int i = 0; i < bit; i++) {
					temp = Math.abs(byteArray[i]^bytesFSData[i]) % 16;
					if(temp<10) temp += 48;
					else temp += 65-10;
					sbResult.append((char) temp);
				}
				break;
		}

		return sbResult.toString();
	}
	public static String substring(String strSub, int start, int end, String strDefault)
	{
		if(null==strSub || start<0 || end<start)
		{
			return strDefault;
		}

		if(end>strSub.length())
		{
			end = strSub.length();
		}

		return strSub.substring(start, end);
	}
	/* ���ܣ���ָ�����ַ������ֵ���ֵ��ԭ��ʽ���
	 * ����
	 * strValue - �ַ������ֵ
	 * lngAdd - Ҫ��ӵ�ֵ����
	 * nBit - Ҫ������ַ���ĳ���
	 * */
	public static String formatLongStr(String strValue, long lngAdd, int nBit) throws NumberFormatException
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nBit; ++i) {
			sb.append('0');
		}
		long lnNo = Long.parseLong(strValue)+lngAdd;
		sb.append(lnNo);
		int length = sb.length();
		return sb.substring(length - nBit, length);
	}

	/* ���ܣ������ַ��������ȵĲ�ָ�����
	 * ����
	 * strValue - �ַ������ֵ
	 * isAlignLeft - ָ������ķ���
	 * nBit - Ҫ������ַ���ĳ���
	 * */
	public static String align(String strValue, boolean isAlignLeft, char cInsert, int nBit) throws NumberFormatException
	{
		StringBuffer sb = new StringBuffer();
		if(isAlignLeft)
		{
			sb.append(strValue);
			for (int i = 0; i < nBit; ++i) {
				sb.append(cInsert);
			}
		}
		else
		{
			for (int i = 0; i < nBit; ++i) {
				sb.append(cInsert);
			}
			sb.append(strValue);
		}

		int length = sb.length();
		return sb.substring(length - nBit, length);
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
	public  static String hexToStr(String hex) {
		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);
			temp.append(decimal);
		}
		return sb.toString();
	}
	public static String strToHex(String str) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString().toUpperCase();
	}
	public static String intToHex(int str) {

		String chars = Integer.toHexString(str);

		StringBuffer hex = new StringBuffer();
		for (int i = chars.length(); i < 4; i++) {
			hex.append("0");
		}
		hex.append(chars.toUpperCase());
		return hex.toString();
	}
	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}

	public static String[] getArraysSorty(String[] s){
		Arrays.sort(s);
	   for (int i = 0;i<s.length;i++){
	   	System.out.println(s[i]);
	}
	return s;
}
	
}