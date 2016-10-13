package com.spring.study.utils.others;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

//import org.aspectj.apache.bcel.generic.NEW;

/**
 * Insert the type's description here. Creation date: (2004-12-20 2:40:11)
 *
 * @author: ��Ϊ��(xun119@21cn.com)
 */
public class ByteUtils {
    /**
     * ByteUtils constructor comment.
     */
    public ByteUtils() {
        super();
    }

    // �����--�Ӻ������𣬺�nIndexToλ�ã�nCount����ԭ�ֽ����Ĳ�����ǰ������ƶ��ַ�
    public static void copyArrayBack(byte[] bTo, int nIndexTo, int nCountTo,
                                     String strFrom, byte bInsert) {
        byte[] bFrom = strFrom.getBytes();
        int nIndexFrom = 0;
        int nCountFrom = bFrom.length;

        copyArrayBack(bTo, nIndexTo, nCountTo, bFrom, nIndexFrom, nCountFrom,
                bInsert);
    }

    // �����--�Ӻ������𣬺�nIndexToλ�ã�nCount����ԭ�ֽ����Ĳ�����ǰ������ƶ��ַ�
    public static void copyArrayBack(byte[] bTo, int nIndexTo, int nCountTo,
                                     byte[] bFrom, int nIndexFrom, int nCountFrom, byte bInsert) {
        if (0 > (nIndexTo - nCountTo + 1)) {
            nCountTo = nIndexTo + 1;
        }

        int i = 0;
        for (i = 0; i < nCountTo - nCountFrom; ++i) {
            bTo[nIndexTo - nCountTo + 1 + i] = bInsert;
        }
        for (int n = 0; i < nCountTo; ++i, ++n) {
            bTo[nIndexTo - nCountTo + 1 + i] = bFrom[nIndexFrom + n];
        }
    }

    // �Ӻ������𣬰���nIndexTo
    public static void copyArrayBack(byte[] bTo, int nIndexTo, byte[] bFrom,
                                     int nIndexFrom, int nCount) {
        if (0 > (nIndexTo - nCount + 1)) {
            nCount = nIndexTo + 1;
        }

        for (int i = 0; i < nCount; ++i) {
            bTo[nIndexTo - nCount + 1 + i] = bFrom[nIndexFrom + i];
        }
    }

    // �Ӻ������𣬰���nIndexTo
    public static void copyArrayBack(byte[] bTo, int nIndexTo, String strFrom) {
        byte[] bFrom = strFrom.getBytes();
        int nIndexFrom = 0;
        int nCount = bFrom.length;// ������String���ַ����������Ļ�ضϣ���һ��

        copyArrayBack(bTo, nIndexTo, bFrom, nIndexFrom, nCount);
    }

    // �����--�Ӻ������𣬺�nIndexToλ�ã�nCount����ԭ�ֽ����Ĳ�����ǰ������ƶ��ַ�
    public static void copyArrayFore(byte[] bTo, int nIndexTo, int nCountTo,
                                     String strFrom, byte bInsert) {
        byte[] bFrom = strFrom.getBytes();
        int nIndexFrom = 0;
        int nCountFrom = bFrom.length;

        copyArrayFore(bTo, nIndexTo, nCountTo, bFrom, nIndexFrom, nCountFrom,
                bInsert);
    }

    // ǰ����--��ǰ�����𣬺�nIndexToλ�ã�nCount����ԭ�ֽ����Ĳ����ں�������ƶ��ַ�
    public static void copyArrayFore(byte[] bTo, int nIndexTo, int nCountTo,
                                     byte[] bFrom, int nIndexFrom, int nCountFrom, byte bInsert) {
        if (bTo.length < (nIndexTo + nCountTo)) {
            nCountTo = bTo.length - nIndexTo;
        }

        int i = 0;
        for (; i < nCountFrom; ++i) {
            bTo[nIndexTo + i] = bFrom[nIndexFrom + i];
        }
        for (; i < nCountTo; ++i) {
            bTo[nIndexTo + i] = bInsert;
        }
    }

    // ��ǰ�����𣬰���nIndexTo
    public static void copyArrayFore(byte[] bTo, int nIndexTo, byte[] bFrom,
                                     int nIndexFrom, int nCount) {
        if (bTo.length < (nIndexTo + nCount)) {
            nCount = bTo.length - nIndexTo;
        }

        for (int i = 0; i < nCount; ++i) {
            bTo[nIndexTo + i] = bFrom[nIndexFrom + i];
        }
    }

    // ��ǰ�����𣬰���nIndexTo
    public static void copyArrayFore(byte[] bTo, int nIndexTo, String strFrom) {
        byte[] bFrom = strFrom.getBytes();
        int nIndexFrom = 0;
        int nCount = bFrom.length;// ������String���ַ����������Ļ�ضϣ���һ��

        copyArrayFore(bTo, nIndexTo, bFrom, nIndexFrom, nCount);
    }

    public static byte[] asc2bcdforNum(String strData) {
        if (1 == strData.length() % 2)
            strData = "0" + strData;
        int len = strData.length() / 2;
        byte byteData[] = new byte[len * 2];
        System.arraycopy(strData.getBytes(), 0, byteData, 0, strData.length());
        byte byteRet[] = new byte[len];
        int m = 0;
        int n = 0;
        for (int i = 0; i < byteData.length; i += 2) {
            m = byteData[i];
            if (m >= 48 && m <= 57)
                m -= 48;
            else if (m == 61)
                m = 13;
            else if (m == 63)
                m = 15;
            n = byteData[i + 1];
            if (n >= 48 && n <= 57)
                n -= 48;
            else if (n == 61)
                n = 13;
            else if (n == 63)
                n = 15;
            byteRet[i / 2] = (byte) ((m << 4) + n);
        }

        return byteRet;
    }

    // asc���ַ�תΪbcd����
    public static byte[] asc2bcd(String strData) {
        int len = (strData.length() + 1) / 2;
        byte[] byteData = new byte[len * 2];
        System.arraycopy(strData.getBytes(), 0, byteData, 0, strData.length());
        byte[] byteRet = new byte[len];
        int m = 0;
        int n = 0;
        for (int i = 0; i < byteData.length; i += 2) {
            m = byteData[i];
            if (m >= '0' && m <= '9') {
                m = m - '0';
            } else if (m == '=') {
                m = 13;
            } else if (m == '?') {
                m = 15;
            }

            n = byteData[i + 1];
            if (n >= '0' && n <= '9') {
                n = n - '0';
            } else if (n == '=') {
                n = 13;
            } else if (n == '?') {
                n = 15;
            }
            byteRet[i / 2] = (byte) ((m << 4) + n);// <<����������ȼ��Ƚϵ�
        }

        return byteRet;
    }

    // bcd����תΪasc���ַ�
    public static String bcd2asc(byte[] byteData) {
        byte[] byteRet = new byte[2 * byteData.length];
        int j = 0;
        int m = 0;
        int n = 0;
        for (int i = 0; i < byteData.length; ++i) {
            j = byteData[i];

            m = (j >>> 4) & 0x0f;
            n = (j - (m << 4)) & 0x0f;
            if (m >= 0 && m <= 9) {
                m = m + '0';
            } else if (m == 13) {
                m = '=';
            } else if (m == 15) {
                m = '?';
            }

            if (n >= 0 && n <= 9) {
                n = n + '0';
            } else if (n == 13) {
                n = '=';
            } else if (n == 15) {
                n = '?';
            }
            byteRet[2 * i] = (byte) m;
            byteRet[2 * i + 1] = (byte) n;
        }

        return new String(byteRet);
    }

    // asc���ַ�תΪ��������
    public static byte[] asc2num(String strData) {
        byte[] byteData = strData.getBytes();
        byte[] byteRet = new byte[byteData.length];
        int m = 0;
        for (int i = 0; i < byteData.length; ++i) {
            m = byteData[i] - '0';
            byteRet[i] = (byte) m;
        }

        return byteRet;
    }

    // ��������תΪasc���ַ�
    public static String num2asc(byte[] byteData) {
        byte[] byteRet = new byte[byteData.length];
        int m = 0;
        for (int i = 0; i < byteData.length; ++i) {
            m = byteData[i] + '0';
            byteRet[i] = (byte) m;
        }

        return new String(byteRet);
    }

    /**
     * Converting Between a BitSet and a Byte Array - start **
     */

    // Returns a bitset containing the values in bytes.
    // The byte-ordering of bytes must be big-endian which means the most
    // significant bit is in element 0.
    public static BitSet ByteArray2BitSet(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }

        return bits;
    }

    // Returns a byte array of at least length 1.
    // The most significant bit in the result is guaranteed not to be a 1
    // (since BitSet does not support sign extension).
    // The byte-ordering of the result is big-endian which means the most
    // significant bit is in element 0.
    // The bit at index 0 of the bit set is assumed to be the least significant
    // bit.
    public static byte[] BitSet2ByteArray(BitSet bits) {
        byte[] bytes = new byte[bits.length() / 8 + 1];

        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }

        return bytes;
    }

    /*** Converting Between a BitSet and a Byte Array - end ***/

    /**
     * *
     * ���ܣ�������ݻ���� ���� byteArrayOut - ��ݻ������ nBufferLen - Ҫ�������ݻ������ bAlignLeft
     * - ��ݶ��뷽��,true-�����/false-�Ҷ��� bFill - ���������������(1�ֽ�) byteArrayData - Ҫ��������
     */
    public static void settleDataBlock(ByteArrayOutputStream byteArrayOut,
                                       int nBufferLen, boolean bAlignLeft, byte bFill, byte[] byteArrayData) {
        // �������������Ҫ�����ֽڳ���
        int nEmptyLength = nBufferLen - byteArrayData.length;
        if (nEmptyLength < 0) {
            nEmptyLength = 0;
        }
        // д�����
        if (bAlignLeft)// �����
        {
            byteArrayOut.write(byteArrayData, 0, nBufferLen - nEmptyLength);
            for (int i = 0; i < nEmptyLength; ++i) {
                byteArrayOut.write(bFill);
            }
        } else// �Ҷ���
        {
            for (int i = 0; i < nEmptyLength; ++i) {
                byteArrayOut.write(bFill);
            }
            byteArrayOut.write(byteArrayData, 0, nBufferLen - nEmptyLength);
        }
    }

    /**
     * *
     * ���ܣ�������ݻ���� ���� byteArrayOut - ��ݻ������ nBufferLen - Ҫ�������ݻ������ bAlignLeft
     * - ��ݶ��뷽��,true-�����/false-�Ҷ��� bFill - ���������������(1�ֽ�) byteArrayData - Ҫ��������
     */
    public static void settleDataBlock(ByteArrayOutputStream byteArrayOut,
                                       int nBufferLen, boolean bAlignLeft, byte bFill, String strData) {
        // �������������Ҫ�����ֽڳ���
        byte[] byteArrayData = strData.getBytes();
        int nEmptyLength = nBufferLen - byteArrayData.length;
        if (nEmptyLength < 0) {
            nEmptyLength = 0;
        }
        // д�����
        if (bAlignLeft)// �����
        {
            byteArrayOut.write(byteArrayData, 0, nBufferLen - nEmptyLength);
            for (int i = 0; i < nEmptyLength; ++i) {
                byteArrayOut.write(bFill);
            }
        } else// �Ҷ���
        {
            for (int i = 0; i < nEmptyLength; ++i) {
                byteArrayOut.write(bFill);
            }
            byteArrayOut.write(byteArrayData, 0, nBufferLen - nEmptyLength);
        }
    }

    /**
     * Converting Between a int and a Byte Array - start **
     */

    public static byte[] Int2Byte(int send) throws IOException {
        byte[] bTemp = new byte[4];
        //System.out.println("send:   "+send+   "     Hex:"   +   Integer.toHexString(send));
        for (int i = 0; i < 4; i++) {
            bTemp[i] = (byte) ((send >> (i * 8)) & 0xff);
            //System.out.println("send   byte"   +((send   >>(i*16))&0xff));
        }
        return bTemp;
    }

    // ȡ��
    private int toInt(byte b) {
        if (b >= 0)
            return (int) b;
        else
            return (int) (b + 256);
    }

    // 4 byte Array to int
    public int byteArray4ToInt(byte[] byteValue) {
        if (byteValue.length != 4)
            return 0;

        int intValue = 0;
        try {
            intValue = toInt(byteValue[0]);
            intValue = (intValue << 8) + toInt(byteValue[1]);
            intValue = (intValue << 8) + toInt(byteValue[2]);
            intValue = (intValue << 8) + toInt(byteValue[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intValue;
    }

    /**
     * Converting Between a int and a Byte Array - end **
     */


    public static final void main(String[] s) {
        /*
		 * String str = "09AZaz"; byte[] bb = {'0','9',0x00,0x00,0x7F,0x7F};
		 * System.out.print(new String(bb)); byte[] bs = str.getBytes(); for(int
		 * i=0;i<str.length();i++) { System.out.println(bs[i]); }
		 */
        // String str = "02";
        // byte[] bs = ByteUtils.asc2bcd(str);
        // for(int i=0;i<str.length()/2;i++)
        // {
        // System.out.println(bs[i]);
        // }
        // System.out.println(ByteUtils.bcd2asc(bs));
//		String strTemp = "C466652C07A52F4CF7F9EDC4BD948FCD";
//		byte[] bytesOut = new byte[100];
//		ByteUtils.copyArrayFore(bytesOut, 29, 37, strTemp, (byte) 0);

        byte[] tmp = new byte[422];
        ByteUtils.copyArrayFore(tmp, 276, "000033");
        for (int i = 276; i < 282; i++) {
            tmp[i] = 0;
        }
        System.out.println(new String(tmp, 276, 6));
        ByteUtils.copyArrayFore(tmp, 276, "000023");
        System.out.println(new String(tmp, 276, 6));

        String test = "0,000033";
        System.out.println(test.split(",")[0]);
//		String key = "6000020000";
//		byte[] MACKEY = StringUtils.str2Bcd(key);
//		byte[] t = ByteUtils.asc2bcd(key);
//		System.out.println(new String(MACKEY));
//		System.out.println(new String(t));
    }
}