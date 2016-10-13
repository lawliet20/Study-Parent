package com.spring.study.utils.others;

import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {
    /**
     * NumberUtils constructor comment.
     */
    public NumberUtils() {
        super();
    }

    // 功能：做4舍5入
    // 参数：a - 要做4舍5入的数字
    // n - 做4舍5入保留的小数位数
    public static double do4Down5Up(double a, int n) {
        long lPower = Math.round(Math.pow(10, n));
        return (double) Math.round(a * lPower) / lPower;
    }

    // 功能：做4舍5入
    // 参数：a - 要做4舍5入的数字
    // n - 做4舍5入保留的小数位数
    public static float do4Down5Up(float a, int n) {
        long lPower = Math.round(Math.pow(10, n));
        return (float) Math.round(a * lPower) / lPower;
    }

    public static double parseAsDouble(String strData, double dDefault) {
        double dRet = dDefault;
        try {
            dRet = Double.parseDouble(strData.trim());
        } catch (Exception ex) {
            // ex.printStackTrace(System.err);
        }

        return dRet;
    }

    public static boolean isIn(String substring, String[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }
        return false;
    }

    public static float parseAsFloat(String strData, float fDefault) {
        float fRet = fDefault;
        try {
            fRet = Float.parseFloat(strData.trim());
        } catch (Exception ex) {
            // ex.printStackTrace(System.err);
        }

        return fRet;
    }

    public static int parseAsInt(String strData, int nDefault) {
        int nRet = nDefault;
        try {
            nRet = Integer.parseInt(strData.trim());
        } catch (Exception ex) {
            // ex.printStackTrace(System.err);
        }

        return nRet;
    }

    public static long parseAsLong(String strData, long lDefault) {
        long lRet = lDefault;
        try {
            lRet = Long.parseLong(strData.trim());
        } catch (Exception ex) {
            // ex.printStackTrace(System.err);
        }

        return lRet;
    }

    public static String addZero(String value, int length) {
        for (int i = value.length(); i < length; i++) {
            value = "0" + value;
        }
        return value;
    }

    public static double Long2Double(long lngValue, double dDiv) {
        return (double) (lngValue / dDiv);
    }

    public static float Long2Float(long lngValue, float fDiv) {
        return (float) (lngValue / fDiv);
    }

    public static int Long2Int(Long value) {
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public static String toHexString(byte[] btArray, String strDelimer) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < btArray.length; ++i) {
            sb.append(StringUtils.align(Integer.toHexString(btArray[i]), false, '0', 2)).append(strDelimer);
        }
        return sb.toString();
    }

    public static String getPosTxnSeq() {
        String dt = DateUtils.currentDate("MMddHHmmss");
        String s = "";
        while (s.length() < 2)
            s += (int) (Math.random() * 10);
        return dt + s;
    }

    public static void main(String[] params) {
        // System.out.println(getPosTxnSeq());
        // System.out.println(createBalanceStr(new
        // BigDecimal("3.23").movePointRight(2)));
        // System.out.println(luhnTest("6181000001998096"));
        // luhnTest("9800080100068007439");
        // System.out.println(createLuhnBit("902001000100000001"));
        // System.out.println(luhnTest("9800080100022001097"));
        // System.out.println(luhnTest("4408041234567893"));
        // System.out.println(NumberUtils.createBalanceStr(new
        // BigDecimal("10.00").movePointRight(2)));
        System.out.println(isMobileNO("18105195106"));
    }

    public static String createBalanceStr(Object balance) {
        String bal = "";
        if (balance instanceof BigDecimal) {
            bal = balance.toString();
        } else if (balance instanceof Integer) {
            bal = balance.toString();
        }
        while (bal.length() < 12)
            bal = "0" + bal;
        return bal;
    }

    public static String createLuhnBit(String cardNo) {
        int[] CI = new int[]{2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1};
        int i, d, result;
        int chk_dig = 0;
        int length = cardNo.length();
        for (i = 0; i < length; i++) {
            d = Integer.parseInt(cardNo.substring(length - i - 1, length - i));
            result = d * CI[i];
            chk_dig += result / 10 + result % 10;
        }
        chk_dig = 10 - chk_dig % 10;
        chk_dig = (chk_dig == 10) ? 0 : chk_dig;
        return cardNo + chk_dig;
    }

    /**
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 　　		联通：130、131、132、152、155、156、185、186
     * 　　		电信：133、153、180、189、（1349卫通）
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String genRandomNum(int size) {
        String[] array = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Random rand = new Random();
        String result = "";
        for (int i = 0; i < size; i++) {
            result = result + array[rand.nextInt(10)];
        }
        return result;
    }

    public static String genRandomPassword() {
        String[] array1 = {"1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999", "8888"};
        String[] array = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Random rand = new Random();
        String result = "";
        for (int i = 0; i < 2; i++) {
            result = result + array[rand.nextInt(10)];
        }
        return array1[rand.nextInt(10)] + result;
    }

    // 返回属于[p,q)的随机数
    public static Integer getRandNum(int p, int q) {
        Random random = new Random();
        int size = q - p + 1;
        return p + random.nextInt(size);
    }
}