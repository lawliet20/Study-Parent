package com.spring.study.utils.others;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** 
 * @author  Z
 * @date 2015-7-22 上午10:42:45 
 * 
 */
public class StringCompress {
	
	public static void main(String[] args) {
		String sign = "31f2127bba1d40d12e73ac4bf7c854195530af1728ab3adffe9c4a276fed5c665572d01ea75f984fe02a7e336543c0cb8e62475293fd4afd71047b8c15ef14dc90bc71d167543c93ec73081f16bd736976a8c24388b866db7b4d9dcaa23b7ee6fe520d7c269f0bdbbeecd8eee37cdad5a13e72cb6d0d29780c3dc565819c54f7";
		System.out.println(MD5Util.MD5(sign));
//		byte[] abc = compress(sign);
//		System.out.println(sign.length());
//		System.out.println(CryptoUtils.byte2hex(abc));
	}
	
	public static final byte[] compress(String paramString) { 
        if (paramString == null) 
            return null; 
        ByteArrayOutputStream byteArrayOutputStream = null; 
        ZipOutputStream zipOutputStream = null; 
        byte[] arrayOfByte; 
        try { 
            byteArrayOutputStream = new ByteArrayOutputStream(); 
            zipOutputStream = new ZipOutputStream(byteArrayOutputStream); 
            zipOutputStream.putNextEntry(new ZipEntry("0")); 
            zipOutputStream.write(paramString.getBytes()); 
            zipOutputStream.closeEntry(); 
            arrayOfByte = byteArrayOutputStream.toByteArray(); 
        } catch (IOException localIOException5) { 
            arrayOfByte = null; 
        } finally { 
            if (zipOutputStream != null) 
                try { 
                    zipOutputStream.close(); 
                } catch (IOException localIOException6) { 
            } 
            if (byteArrayOutputStream != null) 
                try { 
                    byteArrayOutputStream.close(); 
                } catch (IOException localIOException7) { 
            } 
        } 
        return arrayOfByte; 
    } 
 
    @SuppressWarnings("unused") 
    public static final String decompress(byte[] paramArrayOfByte) { 
        if (paramArrayOfByte == null) 
            return null; 
        ByteArrayOutputStream byteArrayOutputStream = null; 
        ByteArrayInputStream byteArrayInputStream = null; 
        ZipInputStream zipInputStream = null; 
        String str; 
        try { 
            byteArrayOutputStream = new ByteArrayOutputStream(); 
            byteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte); 
            zipInputStream = new ZipInputStream(byteArrayInputStream); 
            ZipEntry localZipEntry = zipInputStream.getNextEntry(); 
            byte[] arrayOfByte = new byte[1024]; 
            int i = -1; 
            while ((i = zipInputStream.read(arrayOfByte)) != -1) 
                byteArrayOutputStream.write(arrayOfByte, 0, i); 
            str = byteArrayOutputStream.toString(); 
        } catch (IOException localIOException7) { 
            str = null; 
        } finally { 
            if (zipInputStream != null) 
                try { 
                    zipInputStream.close(); 
                } catch (IOException localIOException8) { 
                } 
            if (byteArrayInputStream != null) 
                try { 
                    byteArrayInputStream.close(); 
                } catch (IOException localIOException9) { 
                } 
            if (byteArrayOutputStream != null) 
                try { 
                    byteArrayOutputStream.close(); 
                } catch (IOException localIOException10) { 
            } 
        } 
        return str; 
    } 
}
