package com.show.specialshow.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * 格式 MM-dd HH:mm
	 */
	public static final SimpleDateFormat SHORT = new SimpleDateFormat(
			"MM-dd HH:mm");
	/**
	 * 格式 HH:mm
	 */
	public static final SimpleDateFormat HOUR_AND_MINUTE = new SimpleDateFormat(
			"HH:mm");

	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat format3 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 格式 yyyy年MM月dd日 HH:mm
	 */
	public static final SimpleDateFormat format7 = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm");

	/**
	 * 格式 yyyy-MM-dd
	 */
	public static final SimpleDateFormat format6 = new SimpleDateFormat(
			"yyyy-MM-dd");
	/**
	 * 格式 yyyy-MM-dd HH:mm
	 */
	public static final SimpleDateFormat format5 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String getLocalDate(String date) {
		if (!TextUtils.isEmpty(date))
			try {
				Date parse = format3.parse(date);
				return format7.format(parse);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static String getShortDate(String date) {
		if (!TextUtils.isEmpty(date))
			try {
				Date parse = format3.parse(date);
				return SHORT.format(parse);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static String getShortDateByMilli(long date) {
		try {
			Date parse = new Date(date);
			return SHORT.format(parse);
		} catch (Exception e) {
		}
		return null;
	}
	public static String getToMinuteByMilli(long date) {
		try {
			Date parse = new Date(date);
			return format5.format(parse);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getOnlytoDayByMilli(long date) {
		try {
			Date parse = new Date(date);
			return format6.format(parse);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getAlltoDateByMilli(long date) {
		try {
			Date parse = new Date(date);
			return format3.format(parse);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getHourAndMinute(String date) {
		if (!TextUtils.isEmpty(date))
			try {
				Date parse = format3.parse(date);
				return HOUR_AND_MINUTE.format(parse);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}

	public static Date getDate(String date) {
		if (!TextUtils.isEmpty(date))
			try {
				Date parse = format3.parse(date);
				return parse;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}
	/**
	 *       1 返回yyyy-MM-dd 23:59:59毫秒数
	 * @return
	 */
	public static long weeHours() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		//时分秒（毫秒数）
		long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
		//凌晨00:00:00
		cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);
		long toStart = cal.getTimeInMillis();
			//凌晨23:59:59
		long toBlack =cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000;
		return toStart/1000;
	}

	/**
	 * String类型转换为long类型
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
     */
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	/**
	 * string类型转换为date类型
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
     */
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	/**
	 * date类型转换为long类型
	 * @param date
	 * @return
     */
	// date要转换的date类型的时间
	public static long dateToLong(Date date) {
		return date.getTime();
	}
/**
 *     date类型转换为String类型

 */
	// formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	// data Date类型的时间
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}
	/**
	 *long类型转换为String类型
	 */
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	public static String longToString(long currentTime, String formatType)
			throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}
	/**
	 *     long转换为Date类型
	 */
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	public static Date longToDate(long currentTime, String formatType)
			throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}
}
