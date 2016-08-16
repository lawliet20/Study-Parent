package com.spring.study.utils.others;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.StringTokenizer;

public class DateUtils {
	// 用来全局控制 上一周，本周，下一周的周数变化
	private int weeks = 0;
	private int MaxDate;// 一月最大天数
	private int MaxYear;// 一年最大天数
	
	public static void main(String[] args) {
		String now = getDateToString(new Date(), "yyyyMMddHHmmss");
		System.out.println(now);
	}

	/**
	 * DateUtil constructor comment.
	 */
	public DateUtils() {
		super();
	}

	/**
	 * Insert the method's description here. Creation date: (2002-12-23 14:12:23)
	 * 
	 * @return java.lang.String
	 */
	public static String currentDayOfMonthStr() {
		Calendar cal = Calendar.getInstance();
		int currDay = cal.get(Calendar.DAY_OF_MONTH);
		String strTmp = "0" + String.valueOf(currDay);
		return strTmp.substring(strTmp.length() - 2, strTmp.length());
	}

	public static String currentMonthStr() {
		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(Calendar.MONTH) + 1;
		String strTmp = "0" + String.valueOf(currMonth);
		return strTmp.substring(strTmp.length() - 2, strTmp.length());
	}

	public static String currentWeekDayCNStr() {
		Calendar cal = Calendar.getInstance();
		int currWeekDay = cal.get(Calendar.DAY_OF_WEEK);
		String sWeekDay = null;
		switch (currWeekDay) {
		case 1:
			sWeekDay = "星期日";
			break;
		case 2:
			sWeekDay = "星期一";
			break;
		case 3:
			sWeekDay = "星期二";
			break;
		case 4:
			sWeekDay = "星期三";
			break;
		case 5:
			sWeekDay = "星期四";
			break;
		case 6:
			sWeekDay = "星期五";
			break;
		case 7:
			sWeekDay = "星期六";
			break;
		}

		return sWeekDay;
	}

	/**
	 * 获得当今年4四位字符串形式
	 * 
	 * @return java.lang.String
	 */
	public static String currentYearStr() {
		Calendar cal = Calendar.getInstance();
		int currYear = cal.get(Calendar.YEAR);
		return String.valueOf(currYear);
	}

	/**
	 * 获得当今年4四位字符串形式后2位
	 * 
	 * @return java.lang.String
	 */
	public static String currentYearStrLast2() {
		Calendar cal = Calendar.getInstance();
		int currYear = cal.get(Calendar.YEAR);
		String year = String.valueOf(currYear);
		return year.substring(year.length() - 2, year.length());
	}

	public static String parseDateCN(String strDate) {
		StringTokenizer ken = new StringTokenizer(strDate, "-");
		StringBuffer sb = new StringBuffer();
		if (ken.hasMoreElements()) {
			sb.append((String) ken.nextElement()).append("年");
			if (ken.hasMoreElements()) {
				sb.append((String) ken.nextElement()).append("月");
				if (ken.hasMoreElements()) {
					sb.append((String) ken.nextElement()).append("日");
					strDate = sb.toString();
				}
			}
		}

		return strDate;
	}

	public static String parseDateEN(String strDate) {
		StringTokenizer ken = new StringTokenizer(strDate, "-");
		StringBuffer sb = new StringBuffer();
		if (ken.hasMoreElements()) {
			sb.append((String) ken.nextElement()).append("y");
			if (ken.hasMoreElements()) {
				sb.append((String) ken.nextElement()).append("m");
				if (ken.hasMoreElements()) {
					sb.append((String) ken.nextElement()).append("d");
					strDate = sb.toString();
				}
			}
		}

		return strDate;
	}

	public static String beforeDate(String strFormat) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(strFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		return format.format(cal.getTime());
	}

	/**
	 * 根据某个日期得到前一天日期
	 * 
	 * @param d
	 * @return
	 */
	public static Date getBeforeDate(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		d = calendar.getTime();
		return d;
	}

	public static Date afterDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		return cal.getTime();
	}

	public static String currentDate(String strFormat) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(strFormat);
		Calendar cal = Calendar.getInstance();

		return format.format(cal.getTime());
	}

	public static String thisDate(Calendar cal, String strFormat) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(strFormat);

		return format.format(cal.getTime());
	}

	public static String nextDate(String strFormat) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(strFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);

		return format.format(cal.getTime());
	}

	public static String nextDateWithDays(String strDate, int days) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
		Date dt = string2Date(strDate, "yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DATE, days);

		return format.format(cal.getTime());
	}

	public static int getDayOfWeek(String strDate) {
		Date dt = string2Date(strDate, "yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * String类型转成Date类型
	 * */
	public static Date string2Date(String strDate, String pattern) {
		if (strDate == null || strDate.equals("")) {
			throw new RuntimeException("str date null");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyyMMdd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;

		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

	public static final Date getStringToDate(String string, String format) {
		try {
			if (string != null && !string.trim().equals("")) {
				return new SimpleDateFormat(format).parse(string);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对日期格式的进行转换
	 * */
	public static final String getDateToString(Date date, String format) {
		if (date != null) {
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}

	/**
	 * 一天的开始点 begin
	 */
	public static Date getDayBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	/**
	 * 一天的结束点 end
	 */
	public static Date getDayEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date getNowWeekBegin() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
		// DateFormat df = DateFormat.getDateInstance();
		// return df.format(c.getTime());
		// c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
	}

	public static boolean isWeekend(String nowDate) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = df.parse(nowDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if ((dayOfWeek - 1) == 5) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMonthend(String nowDate) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = df.parse(nowDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int nowDay = cal.get(Calendar.DAY_OF_MONTH);
		if (nowDay == lastDayOfMonth) {
			return true;
		} else {
			return false;
		}
	}

	public static int dateDiff(Date dt1, Date dt2) {
		long cha = Math.abs(dt2.getTime() - dt1.getTime());
		long result = cha / (1000 * 60 * 60);
		System.out.println(result);
		return (int) result;
	}

	public static boolean isHoliday(String MMdd) {
		String isHoliday = "0906,0907,0908,1001,1002,1003,1004,1005,1006,1007";
		String overDays = "0928,1011";

		if (isHoliday.contains(MMdd)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getNextWorkDay(String days, Map<String, Boolean> holidayMap) {
		if (holidayMap.get(days.substring(4)) == null) {
			return days;
		}
		days = nextDateWithDays(days, 1);
		return getNextWorkDay(days, holidayMap);
	}

	public static String getPrevWorkDay(String days) {
		if (!isHoliday(days.substring(4))) {
			return days + ",true";
		}
		// 如果礼拜一仍然不是工作日，则返回空;
		if (2 == getDayOfWeek(days)) {
			return days + ",false";
		}
		days = nextDateWithDays(days, -1);
		return getPrevWorkDay(days) + ",true";
	}

	public static String getNextTransDay(String strDate, Map<String, Boolean> holidayMap, Map<String, Boolean> overDaysMap) throws ParseException {
		// Map<String, Boolean> holidayMap = new HashMap<String, Boolean>();
		// holidayMap.put("0906", true);
		// holidayMap.put("0907", true);
		// holidayMap.put("0908", true);
		// holidayMap.put("1001", true);
		// holidayMap.put("1002", true);
		// holidayMap.put("1003", true);
		// holidayMap.put("1004", true);
		// holidayMap.put("1005", true);
		// holidayMap.put("1006", true);
		// holidayMap.put("1007", true);
		// Map<String, Boolean> overDaysMap = new HashMap<String, Boolean>();
		// overDaysMap.put("0928", true);
		// overDaysMap.put("1011", true);

		// 判断今天是否假期
		if (holidayMap.get(strDate.substring(4)) != null) {
			return getNextWorkDay(strDate, holidayMap);
		}
		// 判断当前日期是否周末
		if (isWeekend(strDate)) {
			// 判断周六是否补班日
			if (overDaysMap.get(nextDateWithDays(strDate, 1).substring(4)) != null) {
				return nextDateWithDays(strDate, 1);
			}
			if (overDaysMap.get(nextDateWithDays(strDate, 2).substring(4)) != null) {
				return nextDateWithDays(strDate, 2);
			}
			// 根据下周一的日期获取最近一天的工作日；
			return getNextWorkDay(nextDateWithDays(strDate, 3), holidayMap);
		} else {
			// 判断今天是否礼拜六\日
			int currWeekDay = getDayOfWeek(strDate);
			if (currWeekDay == 7) {
				if (overDaysMap.get(nextDateWithDays(strDate, 1).substring(4)) != null) {
					return nextDateWithDays(strDate, 1);
				} else {
					return nextDateWithDays(strDate, 2);
				}
			}
			return getNextWorkDay(nextDateWithDays(strDate, 1), holidayMap);
		}
	}

	public static String getNextWeekTransDay(String strDate, Map<String, Boolean> holidayMap, Map<String, Boolean> overDaysMap) throws Exception {
		// Map<String, Boolean> holidayMap = new HashMap<String, Boolean>();
		// holidayMap.put("0906", true);
		// holidayMap.put("0907", true);
		// holidayMap.put("0908", true);
		// holidayMap.put("1001", true);
		// holidayMap.put("1002", true);
		// holidayMap.put("1003", true);
		// holidayMap.put("1004", true);
		// holidayMap.put("1005", true);
		// holidayMap.put("1006", true);
		// holidayMap.put("1007", true);
		// Map<String, Boolean> overDaysMap = new HashMap<String, Boolean>();
		// overDaysMap.put("0928", true);
		// overDaysMap.put("1011", true);
		// 获取下周日的日期
		String nextWeekTransDay = getNextSunday(strDate);
		// 判断下周日是否补班日
		if (overDaysMap.get(nextWeekTransDay.substring(4)) != null) {
			return nextWeekTransDay;
		}
		nextWeekTransDay = nextDateWithDays(nextWeekTransDay, -1);
		// 判断下周六是否补班日
		if (overDaysMap.get(nextWeekTransDay.substring(4)) != null) {
			return nextWeekTransDay;
		}
		nextWeekTransDay = nextDateWithDays(nextWeekTransDay, -1);
		String resultString = getPrevWorkDay(nextWeekTransDay);
		String[] prevWorkDay = resultString.split(",");
		if (prevWorkDay[1].equals("true")) {
			return prevWorkDay[0];
		} else {
			return getNextWeekTransDay(prevWorkDay[0], holidayMap, overDaysMap);
		}

	}

	public static String getNextSunday(String strDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = string2Date(strDate, "yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		if (week > 1) {
			cal.add(Calendar.DAY_OF_MONTH, -(week - 1) + 7 + 7);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, 1 - week + 7 + 7);
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 得到指定月后的日期
	 */

	public static String getAfterMonth(int month) {
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse("2009-11-04");// 初始日期
		} catch (Exception e) {

		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, month);// 在日历的月份上增加6个月
		String strDate = sdf.format(c.getTime());// 的到你想要得6个月后的日期
		return strDate;

	}

	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = DateUtils.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour 中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将时间 转换字符串
	 */
	public static String dateToStr(Date dt, String pattenStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattenStr);
		return formatter.format(dt);
	}

	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	// 计算当月最后一天,返回字符串
	public String getDefaultDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1 号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1 号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 上月第一天
	public String getPreviousMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1 号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1 号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取当月第一天
	public String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1 号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得本周星期日的日期
	public String getCurrentWeekday() {
		weeks = 0;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获取当天时间
	public static String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 获取昨日时间
	public static String getLastTime(String dateformat) {
		Date as = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
		SimpleDateFormat matter1 = new SimpleDateFormat(dateformat);
		String time = matter1.format(as);
		return time;
	}

	// 获得当前日期与本周日相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一
															// 天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得本周一的日期
	public String getMondayOFWeek() {
		weeks = 0;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得相应周的周六的日期
	public String getSaturday() {
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期日的日期
	public String getPreviousWeekSunday() {
		weeks = 0;
		weeks--;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期一的日期
	public String getPreviousWeekday() {
		weeks--;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期一的日期
	public String getNextMonday() {
		weeks++;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期日的日期
	public static String getNextSunday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(monday);
	}

	private int getMonthPlus() {
		Calendar cd = Calendar.getInstance();
		int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
		cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		MaxDate = cd.get(Calendar.DATE);
		if (monthOfNumber == 1) {
			return -MaxDate;
		} else {
			return 1 - monthOfNumber;
		}
	}

	// 获得上月最后一天的日期
	public String getPreviousMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, -1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得下个月第一天的日期
	public String getNextMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得下个月最后一天的日期
	public String getNextMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);// 加一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年最后一天的日期
	public String getNextYearEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.roll(Calendar.DAY_OF_YEAR, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年第一天的日期
	public String getNextYearFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得本年有多少天
	private int getMaxYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		return MaxYear;
	}

	private int getYearPlus() {
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}

	// 获得本年第一天的日期
	public String getCurrentYearFirst() {
		int yearPlus = this.getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus);
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;
	}

	// 获得本年最后一天的日期 *
	public String getCurrentYearEnd() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修
																	// 改日期格式
		String years = dateFormat.format(date);
		return years + "-12-31";
	}

	// 获得上年第一天的日期 *
	public String getPreviousYearFirst() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);
		years_value--;
		return years_value + "-1-1";
	}

	// 获得上年最后一天的日期
	public String getPreviousYearEnd() {
		weeks--;
		int yearPlus = this.getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		getThisSeasonTime(11);
		return preYearDay;
	}

	// 获得本季度
	public String getThisSeasonTime(int month) {
		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);
		int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
		int end_days = getLastDayOfMonth(years_value, end_month);
		String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
		return seasonDate;
	}

	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	private int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 * @return
	 */
	public boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * 获取系统当前时间 System.currentTimeMillis()返回系统当前时间，结果为1970年1月1日0时0分0秒开始，到程序执行取得系统时间为止所经过的秒数 1秒＝1000毫秒
	 */
	public static Long getSystemCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 通过Date类获取自1970年1月1日0时0分0秒开始至今所经历的秒数 date.toString()把日期转换为dow mon dd hh:mm:ss zzz yyyy
	 */
	public static Long getCurrentDate() {
		// 创建并初始化一个日期（初始值为当前日期）
		Date date = new Date();
		return date.getTime() / 1000;
	}

	/**
	 * 没有- 时间格式
	 * 
	 * @param beforeNum
	 * @return
	 */
	public static String[] getBeforeNumDate(int beforeNum) {
		String[] result = new String[beforeNum];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (int i = beforeNum; i > 0; i--) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -i);
			String res = sdf.format(cal.getTime());
			result[i - 1] = res;
		}
		return result;
	}

	/**
	 * 时间格式的日期
	 * 
	 * @param beforeNum
	 * @return
	 */
	public static String[] getNumDate(int beforeNum) {
		String[] result = new String[beforeNum];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = beforeNum; i > 0; i--) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -i);
			String res = sdf.format(cal.getTime());
			result[i - 1] = res;
		}
		return result;
	}

}