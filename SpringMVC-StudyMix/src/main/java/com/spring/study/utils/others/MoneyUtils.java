package com.spring.study.utils.others;
import java.math.BigDecimal;
import java.util.StringTokenizer;
/**
 * 闁叉垿顤傛径鍕倞閸忣剛鏁ょ猾锟� * @author wangwch
 * @date 2006-5-12
 * @version 1.0
 */
public class MoneyUtils {

	/**
	 * 鏉烆剙褰夐柌鎴︻杺鐎涙顑佹稉韫礋閸掑棗宕熸担锟�	 * @param money
	 * @return
	 */
	public static String toCent(String money) {
		String moneyValue = "";
		if (money == null || money.equals("")) {
			return moneyValue;
		}
		money = money.trim();
		BigDecimal bigDecimal = new BigDecimal(money);
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		money = bigDecimal.toString();
		StringTokenizer st = new StringTokenizer(money, ".");
		return st.nextToken() + st.nextToken();
	}
	/**
	 * 鏉烆剙褰夐柌鎴︻杺鐎涙顑佹稉韫礋閸掑棗宕熸担锟�	 * @param money Log
	 * @return
	 */
	public static String toCent(Long money){
		return toCent(money.toString());
	}

	/**
	 * @param money String
	 * 閹跺﹤鍨庢潪顒�綁娑撳搫鍘�
	 */
	public static String toDollar(String money) {
		BigDecimal bigDecimal = new BigDecimal(money);
		bigDecimal = bigDecimal.movePointLeft(2);
		return bigDecimal.toString();
	}
	
	public static double toDollar(double money) {
		BigDecimal bigDecimal = new BigDecimal(money);
		bigDecimal = bigDecimal.movePointLeft(2);
		return bigDecimal.doubleValue();
	}
	/**
	 * 鏉烆剙褰夐柌鎴︻杺鐎涙顑佹稉韫礋閸掑棗宕熸担锟�	 * @param money Long
	 * @return
	 */
	public static String toDollar(Long money){
		return toDollar(money.toString());
	}
	public static String toDollar(Integer txnFee) {
		return toDollar(txnFee.toString());
	}
	
	/**
     * 鐏忓敊缁墽鈥橀崚鏉跨毈閺佹壆鍋ｉ崥搴ｎ儑scale娴ｅ秵鏆�
     * 楠炶泛娲撻懜宥勭安閸忥拷
     */
    public static double round(double v,int scale)
    {
        if (Double.isInfinite(v) || Double.isNaN(v)) {
            v = 0;
        }
        if(scale<0) {
            scale = 0;
        }
        String vStr =  Double.toString(v);
        BigDecimal b = new BigDecimal(vStr);
        BigDecimal one = new BigDecimal("1");
        
        double result = b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }
    
    public static void main(String[] args){
    	String test="000000000011";
    	System.out.println(toDollar(test));
    }


}
