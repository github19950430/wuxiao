package com.pldk.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
public class DateUtil {
	 private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

		private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		
		private final static SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");

		private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

		private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

		private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		private final static SimpleDateFormat sdfmsTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		

		private final static SimpleDateFormat allmsTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		
		private final static SimpleDateFormat allTime = new SimpleDateFormat("yyyyMMddHHmmss");
		
		public static final String YYYYMM = "yyyy-MM";
		
		public static final String YYYYMMDD = "yyyy-MM-dd";
		
		public static final String YYYYMMDD_NOTLINE = "yyyyMMdd";
		
		public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
		
		public static final String YYYYMMDDHHMMSS_NOT_LINE = "yyyyMMddHHmmss";
		
		
		public static final String YYYY_TIME = "yyyy/MM/dd HH:mm:ss";


		
		
		public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

		public static synchronized Date parseDateDayFormat3(String strDate) throws ParseException {
	        return parseDateFormat(strDate, YYYYMM);
	    }
		
		public static synchronized Date parseDateDayFormat(String strDate) throws ParseException {
	        return parseDateFormat(strDate, YYYYMMDD);
	    }
		
		public static synchronized String getDateDayFormat(Date date) {
	        return parseDateFormat(date, YYYYMMDD);
	    }
		
		public static synchronized Date parseDateSecondFormat(String strDate) throws ParseException {
	        return parseDateFormat(strDate, YYYYMMDDHHMMSS);
	    }
		
		public static Date getStrDateBegin(String date) throws ParseException {
	        Date re = parseDateSecondFormat(getDateDayFormat(parseDateDayFormat(date)) + " 00:00:00");

	        return re;
	    }
		
		/**
		 * 获取YYYY格式
		 * 
		 * @return
		 */
		public static String getYear() {
			return sdfYear.format(new Date());
		}

		/**
		 * 获取YYYY格式
		 * 
		 * @return
		 */
		public static String getYear(Date date) {
			return sdfYear.format(date);
		}
		
		/**
	     * 获取YYYY-MM格式
	     * 
	     * @return
	     */
	    public static String getMonth(Date date) {
	        return sdfMonth.format(date);
	    }

		/**
		 * 获取YYYY-MM-DD格式
		 * 
		 * @return
		 */
		public static String getDay() {
			return sdfDay.format(new Date());
		}
		
		
		/**
		 * 获取YYYY-MM-DD格式
		 * 
		 * @return
		 */
		public static String getDate(Date date) {
			return sdfDay.format(date);
		}

		/**
		 * 获取YYYY-MM-DD格式
		 * 
		 * @return
		 */
		public static String getDay(Date date) {
			return sdfDay.format(date);
		}

		/**
		 * 获取YYYYMMDD格式
		 * 
		 * @return
		 */
		public static String getDays() {
			return sdfDays.format(new Date());
		}

		/**
		 * 获取YYYYMMDD格式
		 * 
		 * @return
		 */
		public static String getDays(Date date) {
			return sdfDays.format(date);
		}
		
	    /**
	     * 将字符串格式YYYYMMDDHHMMSS的字符串转为日期，格式"yyyy-MM-dd HH:mm:ss"
	     *
	     * @param date 日期字符串
	     * @return 返回格式化的日期
	     * @throws ParseException 分析时意外地出现了错误异常
	     */
	    public static Date strToDateFormat(String date) throws ParseException {
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");  
	        LocalDateTime ldt = LocalDateTime.parse(date, dtf);  
	  
	        DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	        return parseTime(ldt.format(fa));
	    }

		/**
		 * 获取YYYY-MM-DD HH:mm:ss格式
		 * 
		 * @return
		 */
		public static String getTime() {
			return sdfTime.format(new Date());
		}
		
		/**
		 * 获取YYYY-MM-DD HH:mm:ss.SSS格式
		 * 
		 * @return
		 */
		public static String getMsTime() {
			return sdfmsTime.format(new Date());
		}
		
		/**
		 * 获取YYYYMMDDHHmmss格式
		 * 
		 * @return
		 */
		public static String getAllTime() {
			return allTime.format(new Date());
		}
		
		/**
	     * 获取YYYYMMDDHHmmss格式
	     * 
	     * @return
	     */
	    public static String getAllTime(Date date) {
	        return allTime.format(date);
	    }

		/**
		 * 获取YYYY-MM-DD HH:mm:ss格式
		 * 
		 * @return
		 */
		public static String getTime(Date date) {
			return sdfTime.format(date);
		}

		/**
		 * @Title: compareDate
		 * @Description:(日期比较，如果s>e 返回true 否则返回false)
		 * @param s
		 * @param e
		 * @return boolean
		 * @throws
		 * @author luguosui
		 */
		public static boolean compareDate(String s, String e) {
			if (parseDate(s) == null || parseDate(e) == null) {
				return false;
			}
			return parseDate(s).getTime() > parseDate(e).getTime();
		}
		
		public static boolean compareDate1(Date a, Date b) {
			String s = sdfDay.format(a);
			String e = sdfDay.format(b);
			if (parseDate(s) == null || parseDate(e) == null) {
				return false;
			}
			return parseDate(s).getTime() >= parseDate(e).getTime();
		}

		/**
		 * 格式化日期
		 * 
		 * @return
		 */
		public static Date parseDate(String date) {
			try {
				return sdfDay.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 格式化日期
		 * 
		 * @return
		 */
		public static Date parseTime(String date) {
			try {
				return sdfTime.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 格式化日期
		 * 
		 * @return
		 */
		public static Date parseAllTime(String date) {
			try {
				return allTime.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 格式化日期
		 * 
		 * @return
		 */
		public static Date parse(String date, String pattern) {
			DateFormat fmt = new SimpleDateFormat(pattern);
			try {
				return fmt.parse(date);
			} catch (ParseException e) {
				logger.error("",e);
				return null;
			}
		}

		/**
		 * 格式化日期
		 * 
		 * @return
		 */
		public static String format(Date date, String pattern) {
			DateFormat fmt = new SimpleDateFormat(pattern);
			return fmt.format(date);
		}

		/**
		 * 把日期转换为Timestamp
		 * 
		 * @param date
		 * @return
		 */
		public static Timestamp format(Date date) {
			return new java.sql.Timestamp(date.getTime());
		}

		/**
		 * 校验日期是否合法
		 * 
		 * @return
		 */
		public static boolean isValidDate(String s) {
			try {
				sdfTime.parse(s);
				return true;
			} catch (Exception e) {
				// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
				return false;
			}
		}
		
		/**
		 * 将时间戳转为YYYY-MM-DD HH:mm:ss格式
		 */
		public static Date timestampToTime(long timestamp) {
		    String time = sdfTime.format(timestamp);
		    Date date = parseTime(time);
		    return date;
		}

		/**
		 * 校验日期是否合法
		 * 
		 * @return
		 */
		public static boolean isValidDate(String s, String pattern) {
			DateFormat fmt = new SimpleDateFormat(pattern);
			try {
				fmt.parse(s);
				return true;
			} catch (Exception e) {
				// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
				return false;
			}
		}

		public static int getDiffYear(String startTime, String endTime) {
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			try {
				int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
						startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
				return years;
			} catch (Exception e) {
				// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
				return 0;
			}
		}

		/**
		 * <li>功能描述：时间相减得到天数
		 * 
		 * @param beginDateStr
		 * @param endDateStr
		 * @return long
		 * @author Administrator
		 */
		public static long getDaySub(String beginDateStr, String endDateStr) {
			long day = 0;
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date beginDate = null;
			java.util.Date endDate = null;

			try {
				beginDate = format.parse(beginDateStr);
				endDate = format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
			// System.out.println("相隔的天数="+day);

			return day;
		}

		/**
		 * 得到n天之后的日期
		 * 
		 * @param days
		 * @return
		 */
		public static Date getAfterDayDate(String days) {
			int daysInt = Integer.parseInt(days);

			Calendar canlendar = Calendar.getInstance(); // java.util包
			canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
			Date date = canlendar.getTime();

//			SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String dateStr = sdfd.format(date);

			return getFormatDate(date, YYYYMMDD);
		}

		/**
		 * 得到n天之后是周几
		 * 
		 * @param days
		 * @return
		 */
		public static String getAfterDayWeek(String days) {
			int daysInt = Integer.parseInt(days);

			Calendar canlendar = Calendar.getInstance(); // java.util包
			canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
			Date date = canlendar.getTime();

			SimpleDateFormat sdf = new SimpleDateFormat("E");
			String dateStr = sdf.format(date);

			return dateStr;
		}
		
		/**
		 * 得到当月第一天
		 * 
		 * @param days
		 * @return
		 */
		public static Date getFirstDay() {
			Calendar canlendar = Calendar.getInstance(); // java.util包
			// 获取前月的第一天
			canlendar.add(Calendar.MONTH, 0);
			canlendar.set(Calendar.DAY_OF_MONTH, 1);
			return getFormatDate(canlendar.getTime(), YYYYMMDD);
		}
		
		
		/**
		 * 获取日期最后一天
		 * @author liangcm
		 * @param date
		 * @return
		 */
		public static Date getLastDay(Date date) {
			Calendar canlendar = Calendar.getInstance() ; // java.util包
			canlendar.setTime(date);
			canlendar.add(Calendar.MONTH, 1);
			canlendar.set(Calendar.DAY_OF_MONTH, 1);
			canlendar.add(Calendar.DAY_OF_MONTH, -1);
			return getFormatDate(canlendar.getTime(), YYYYMMDD);
		}
		
		
		
		/**
		 * 获取日期最后一天
		 * @author liangcm
		 * @param date
		 * @return
		 */
		public static Integer getLastMonthDay(Date date) {
			Calendar canlendar = Calendar.getInstance() ; // java.util包
			canlendar.setTime(date);
			canlendar.add(Calendar.MONTH, 1);
			canlendar.set(Calendar.DAY_OF_MONTH, 1);
			canlendar.add(Calendar.DAY_OF_MONTH, -1);
			return canlendar.get(Calendar.DAY_OF_MONTH);
		}

		/**
		 * 格式化Oracle Date
		 * @param value
		 * @return
		 */
//		public static String buildDateValue(Object value){
//			if(Func.isOracle()){
//				return "to_date('"+ value +"','yyyy-mm-dd HH24:MI:SS')";
//			}else{
//				return Func.toStr(value);
//			}
//		}
		
		
		public static Date getFormatDate(Date date,String format) {
		    SimpleDateFormat formatter = new SimpleDateFormat(format);
		    String dateString = formatter.format(date);
			try {
				Date converDate = formatter.parse(dateString);
				return converDate;
			} catch (ParseException e) {
			}
		   return null;
		}
		
		public static synchronized Date parseDateFormat(String strDate, String pattern)
	            throws ParseException {
	        if (StringUtils.isEmpty(strDate)) {
	            return null;
	        }

	        SimpleDateFormat sdf = new SimpleDateFormat();
	        if (pattern != null) {
	            sdf.applyPattern(pattern);
	        }

	        return sdf.parse(strDate);
	    }
		
		public static synchronized String parseDateFormat(Date date, String pattern) {
	        if (date == null) {
	            return "";
	        }

	        SimpleDateFormat sdf = new SimpleDateFormat();
	        if (pattern != null) {
	            sdf.applyPattern(pattern);
	        }

	        return sdf.format(date);
	    }
		
		
		 /**
		     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
		     * 
		     * @param strDate
		     * @return
		     */
		  public static Date strToDateLong(String strDate) {
		     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		     ParsePosition pos = new ParsePosition(0);
		     Date strtodate = formatter.parse(strDate, pos);
		     return strtodate;
		  }
		public static void main(String[] args) {
			System.out.println(strToDateLong("2019-02-23 12:00:00"));
			System.out.println(getAfterDayWeek("3"));
		}
}
