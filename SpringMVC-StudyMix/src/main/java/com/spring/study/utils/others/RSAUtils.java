package com.spring.study.utils.others;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

public class RSAUtils {

    /**
     * 生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus, 16);
            BigInteger b2 = new BigInteger(exponent, 16);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus, 16);
            BigInteger b2 = new BigInteger(exponent, 16);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长  
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11  
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密  
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长  
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密  
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static void main(String[] args) throws Exception {

//		HashMap<String, Object> map = RSAUtils.getKeys();  
//        //生成公钥和私钥  
//        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");  
//        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");  

//        //模  
//        String modulus = publicKey.getModulus().toString();  
//        //公钥指数  
//        String public_exponent = publicKey.getPublicExponent().toString();  
//        //私钥指数  
//        String private_exponent = privateKey.getPrivateExponent().toString();  

        String modulus = "FDED6B3AAE91E986826588A7DBE92CE1CDE8D6B837E2B52032C91B6ADBE1F5B0550A04211AC7A58B2C9BF162C217604C00B044B086986872B04757239AEC7DBDBA4AA511F9177B6614CBED8D55A2B8266A75F953D1D2DC27AE8E37B121CDF316E30ABFE0A885BFC65E9D72B66A63469AB7646A73D4B886CC2066F3CC401C35A3";
        String public_exponent = "010001";
        String private_exponent = "B681EE903EE8A0F00CC8ED6FB89FDFA26C5CEDF77A3377FBAC84DF2FEEDA79BC4362EF94DBAAA35164BEB0C04AC082761E0893564D673DAB3108159981C0789E76C4DA58BF374FC8F5639C0CBCFE4736C9C92E82A4BCDBFCD2DC99F65FFF4FE332AAE5EE65269DEFCDD6578F63DFAC18B3A2331466CF5A235E721CACDD39C001";
//        System.out.println(CryptoUtils.byte2hex(publicKey.getModulus().toByteArray()));
//        System.out.println(CryptoUtils.byte2hex(privateKey.getPrivateExponent().toByteArray()));
//        System.out.println(CryptoUtils.byte2hex(publicKey.getPublicExponent().toByteArray()));
//        System.out.println("modulus:"+modulus);
//        System.out.println("public_exponent:"+public_exponent);
//        System.out.println("private_exponent:"+private_exponent);
        System.out.println("*****************************");
        //明文  
        String ming = "A63C95E17B661A921237C397236094AA";
//        //使用模和指数生成公钥和私钥  
//        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);  
//        System.out.println("len:"+pubKey.toString());
        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
        //加密后的密文  
//        String mi = RSAUtils.encryptByPublicKey(ming, pubKey);  
        String mi = "8C525E30D7ACD47AD51C4ED5D2AA749790CD1B424C9E44EFAD4776AA415C6A877EE5AD1D808635380E213FFFA75D4D5DD788ED741A801AC3ACCF4C0BFB36BD8DE9308961FE1CD42094B81384EF59BAE08A61A3BCFCC28BFDEB2CBCC9F40A378B3166E15B448E0458DFDBE99AF0A7B35BEF1820F0BEC00FD67C32B390DD843AA9";
        System.err.println(mi);
//        //解密后的明文  
        ming = RSAUtils.decryptByPrivateKey(mi, priKey);
        System.err.println(ming);


//		// TODO Auto-generated method stub
//		// String[] elements =
//		// {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
//		// Random random = new Random();
//		String randomKey = "";
//		// for (int i = 0; i < 32; i++) {
//		// randomKey += elements[random.nextInt(16)];
//		// }
//		// System.out.println(randomKey);
//		// String checkValue = CryptoUtils.encrypt3DES("0000000000000000",
//		// randomKey);
//		// System.out.println(checkValue);
//
//		 HashMap<String, Object> map = RSAUtils.getKeys();
//		 //生成公钥和私钥
//		 RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
//		 RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
//		 //
//		 // System.out.println(getKeyString(publicKey));
//		 // //模
//		 String modulus = publicKey.getModulus().toString();
//		 // //公钥指数
//		 String public_exponent = publicKey.getPublicExponent().toString();
//		 //私钥指数
//		String private_exponent = privateKey.getPrivateExponent().toString();
//		
//		 System.out.println("modulus:"+modulus);
//		 System.out.println("exponent:"+public_exponent);
//		 System.out.println("priKey_exponent:"+private_exponent);
//		
//		// //明文
//		String ming = "123456";
////		randomKey = "A63C95E17B661A921237C397236094AA";
//		// //使用模和指数生成公钥和私钥
////		String modulus = "782DEFBAED38F82216E706CC14C387412007C943A1FDC0413126F1531E1093E06351762BF1D4082C775D869BB11BC9E24E365F36183A1EA67C3C0DBBD47258252F1F437CFA4CB97B09B900AC495802C2ED8AECE773F7511C22A8A38C9F66B0AD7A4566037409A647925E53056E0DF61D44D6EEE611888D5DEDD7FA8D295106E1";
//		// modulus =
//		// "10103166745709600780215616551837697832816413714471062522342538060943596036859967333870827790358555455232243383580565187280643159050869924436081447583051139";
////		String public_exponent = "010001";
//		RSAPublicKey pubKey = RSAUtils
//				.getPublicKey(
//						modulus,
//						public_exponent);
//		RSAPrivateKey priKey = RSAUtils
//				.getPrivateKey(
//						modulus,
//						private_exponent);
//		// // //加密后的密文
//		String mi = RSAUtils.encryptByPublicKey(ming, pubKey);
//		System.err.println(mi);
//		// //解密后的明文
//		 ming = RSAUtils.decryptByPrivateKey(mi, priKey);
//		 System.err.println(ming);
    }
}

