package com.spring.study.utils.others;

import java.io.*;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (2003-11-29 22:06:51)
 *
 * @author: ������(xun119@21cn.com)
 */
public class FileUtils {
    /**
     * FileUtils constructor comment.
     */
    public FileUtils() {
        super();
    }

    /**
     * �ļ�����
     */
    public static boolean copy(File fileSrc, File fileDst) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileDst);
            FileInputStream input = new FileInputStream(fileSrc);
            byte[] buffer = new byte[2048];
            int length = -1;
            while (-1 != (length = input.read(buffer))) {
                out.write(buffer, 0, length);
            }
            out.close();
        } catch (IOException ex) {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ioEx) {
            }

            fileDst.delete();

            throw ex;
        }

        return true;
    }

    /**
     * �ļ�����
     */
    public static boolean copy(File fileSrc, File fileDst, boolean append) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileDst, append);
            FileInputStream input = new FileInputStream(fileSrc);
            byte[] buffer = new byte[2048];
            int length = -1;
            while (-1 != (length = input.read(buffer))) {
                out.write(buffer, 0, length);
            }
            out.close();
        } catch (IOException ex) {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ioEx) {
            }

            fileDst.delete();

            throw ex;
        }

        return true;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2003-12-1 16:43:34)
     *
     * @param src java.lang.String
     * @param dst java.lang.String
     * @return boolean
     */
    public static boolean copy(String src, String dst) throws IOException {
        File file = new File(dst);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) return false;
            }

            out = new FileOutputStream(file);
            FileInputStream input = new FileInputStream(src);
            byte[] buffer = new byte[2048];
            int length = -1;
            while (-1 != (length = input.read(buffer))) {
                out.write(buffer, 0, length);
            }
            out.close();
        } catch (IOException ex) {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ioEx) {
            }

            file.delete();

            throw ex;
        }

        return true;
    }

    /**
     * Insert the method's description here.
     * Creation date: (2003-12-1 16:43:34)
     *
     * @param src java.lang.String
     * @param dst java.lang.String
     * @return boolean
     */
    public static boolean copy(String src, String dst, boolean append) throws IOException {
        File file = new File(dst);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) return false;
            }

            out = new FileOutputStream(file, append);
            FileInputStream input = new FileInputStream(src);
            byte[] buffer = new byte[2048];
            int length = -1;
            while (-1 != (length = input.read(buffer))) {
                out.write(buffer, 0, length);
            }
            out.close();
        } catch (IOException ex) {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ioEx) {
            }

            file.delete();

            throw ex;
        }

        return true;
    }

    public static boolean forceRemove(File file) {
        if (!file.exists()) return true;

        if (file.isDirectory()) {
            File[] lstFile = file.listFiles();
            for (int i = 0; i < lstFile.length; i++) {
                if (!forceRemove(lstFile[i]))
                    return false;
            }

            if (!file.delete()) return false;
        } else {
            if (!file.delete()) return false;
        }

        return true;
    }

    public static boolean forceRemove(String strPath) {
        File file = new File(strPath);
        if (!file.exists()) return true;

        if (file.isDirectory()) {
            File[] lstFile = file.listFiles();
            for (int i = 0; i < lstFile.length; i++) {
                if (!forceRemove(lstFile[i]))
                    return false;
            }

            if (!file.delete()) return false;
        } else {
            if (!file.delete()) return false;
        }

        return true;
    }

    public static String getFileDir(String strFilePath) {
        int index1 = strFilePath.lastIndexOf('\\');
        int index2 = strFilePath.lastIndexOf('/');
        int index = index1 > index2 ? index1 : index2;
        if (index == -1) return null;

        return strFilePath.substring(0, index);
    }

    public static String getFileExt(String strFilePath) {
        int index1 = strFilePath.lastIndexOf('.');
        if (index1 == -1) return "";

        return strFilePath.substring(index1 + 1).toLowerCase();
    }

    public static String getFileName(String strFilePath) {
        int index1 = strFilePath.lastIndexOf('\\');
        int index2 = strFilePath.lastIndexOf('/');
        if (index1 == index2 && index1 == -1) return null;

        int index = (index1 > index2) ? index1 : index2;

        return strFilePath.substring(index + 1);
    }

    public static long getFileSize(String strFile) {
        File file = new File(strFile);
        return file.length();
    }

    public synchronized int log(String strFile, boolean bAppend, byte[] byteOut) {
        File file = new File(strFile);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) return -1;
            }

            out = new FileOutputStream(strFile, bAppend);
            out.write(byteOut);
        } catch (Exception ex) {
            System.out.println("log error:" + ex.toString());
        }

        try {
            if (out != null)
                out.close();
        } catch (IOException ioEx) {
        }

        return 0;
    }

    public synchronized int log(String strFile, boolean bAppend, String strInfo) {
        File file = new File(strFile);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) return -1;
            }

            out = new FileOutputStream(strFile, bAppend);
            out.write(strInfo.getBytes());
        } catch (Exception ex) {
            System.out.println("log error:" + ex.toString());
        }

        try {
            if (out != null)
                out.close();
        } catch (IOException ioEx) {
        }

        return 0;
    }

    public synchronized int logLn(String strFile, byte[] byteOut) {
        File file = new File(strFile);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) return -1;
            }

            out = new FileOutputStream(strFile, true);
            out.write(byteOut);
            out.write("\r\n".getBytes());
        } catch (Exception ex) {
            System.out.println("log error:" + ex.toString());
        }

        try {
            if (out != null)
                out.close();
        } catch (IOException ioEx) {
        }

        return 0;
    }

    public synchronized int logLn(String strFile, String strInfo) {
        File file = new File(strFile);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) return -1;
            }

            out = new FileOutputStream(strFile, true);
            strInfo += "\r\n";
            out.write(strInfo.getBytes());
        } catch (Exception ex) {
            System.out.println("log error:" + ex.toString());
        }

        try {
            if (out != null)
                out.close();
        } catch (IOException ioEx) {
        }

        return 0;
    }

    public static int logLnOnce(String strFile, byte[] byteOut) {
        FileUtils util = new FileUtils();
        return util.logLn(strFile, byteOut);
    }

    public static int logLnOnce(String strFile, String strInfo) {
        FileUtils util = new FileUtils();
        return util.logLn(strFile, strInfo);
    }

    public static int logOnce(String strFile, byte[] byteOut) {
        FileUtils util = new FileUtils();
        return util.log(strFile, true, byteOut);
    }

    public static int logOnce(String strFile, String strInfo) {
        FileUtils util = new FileUtils();
        return util.log(strFile, true, strInfo);
    }

    public static int overwrite(String strFile, byte[] byteOut) {
        FileUtils util = new FileUtils();
        return util.log(strFile, false, byteOut);
    }

    public static int overwrite(String strFile, String strInfo) {
        FileUtils util = new FileUtils();
        return util.log(strFile, false, strInfo);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2003-11-30 3:26:04)
     *
     * @param args java.lang.String[]
     */
    public static void main(String[] args) {
        String currentDt = DateUtils.currentDate("yyyyMMdd");
        String outputfile = "d:\\NACFront\\" + currentDt + ".xls";
        File xlsReport = new File(outputfile);
        if (!xlsReport.exists()) {
            try {
                xlsReport.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            xlsReport.delete();
            try {
                xlsReport.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //String strFile = "C:\\a/b\\\\/a������c.txt";
//	String strFile = "C:\\Tomcat 4.1\\webapps\\ROOT\\WEBINF/service-manager.xml";
//	int index = strFile.lastIndexOf('\\');
//	System.out.println(index);
//	index = strFile.lastIndexOf('/');
//	System.out.println(index);
//	//System.out.println(com.lmx.util.FileUtils.getFileName(strFile));
//	System.out.println("getFileDir:"+FileUtils.getFileDir(strFile));
//	System.out.println("getFileName:"+FileUtils.getFileName("C:/Tomcat 4.1/webapps/ROOT/SendMailAddress/zgyh.rar"));
//	try
//	{
//		System.out.println(FileUtils.doFile2Str("C:/Test/webapps/ROOT/sms/sendtest.sms"));
//	}
//	catch(Exception ex)
//	{
//		System.out.println(ex);
//	}

        //FileUtils.copy("C:\\upload\\��Ա��Ƭ\\lmx\\1.jpg","C:\\upload\\��ǰ����\\Hlmx0021-03120100001\\1\\1.jpg",true);
    }

    public static boolean removeFile(String strFile) {
        File file = new File(strFile);
        return file.delete();
    }

    public static String doFile2Str(String strFile) throws FileNotFoundException, IOException {
        StringBuffer sbRet = new StringBuffer();
        FileReader reader = new FileReader(strFile);
        int nLength = -1;
        char cBuffer[] = new char[2048];
        nLength = reader.read(cBuffer);
        while (nLength != -1) {
            sbRet.append(cBuffer, 0, nLength);
            nLength = reader.read(cBuffer);
        }
        return sbRet.toString();
    }

    public static String doInput2Str(InputStream input) throws FileNotFoundException, IOException {
        StringBuffer sbRet = new StringBuffer();
        int nLength = -1;
        byte bBuffer[] = new byte[2048];
        nLength = input.read(bBuffer);
        while (nLength != -1) {
            sbRet.append(new String(bBuffer, 0, nLength));
            nLength = input.read(bBuffer);
        }
        return sbRet.toString();
    }

    public static String getJarDir(String strJarResPath) {
        if (strJarResPath == null) return null;
        int nPos = strJarResPath.indexOf('!');
        if (-1 != nPos) {/*�᲻��windows��linux���ֲ�ͬ����Ҫע��*/
            return FileUtils.getFileDir(strJarResPath.substring(10, nPos));
        }

        return null;
    }

    /**
     * ���ָ���ļ���byte����
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if (file != null) {
                int length = (int) file.length();
                if (length > Integer.MAX_VALUE) {
                    System.out.println("this file is max");
                    return null;
                }
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[length];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();

                byte[] test = new byte[100];
                byte[] testEnd = new byte[100];
                System.arraycopy(buffer, 0, test, 0, 100);
                System.arraycopy(buffer, length - 100, testEnd, 0, 100);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return buffer;
    }

}