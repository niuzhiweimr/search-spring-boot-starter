package com.elastic.search.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author niuzhiwei
 */
public class DateUtils {

    public static long getDiffDay(Calendar firstCal, Calendar secordCal) {
        long diffTime = firstCal.getTimeInMillis() - secordCal.getTimeInMillis();
        long diffDay = diffTime / (1000 * 60 * 60 * 24);
        return diffDay;
    }

    public static long getDiffDay(Date firstCal, Date secordCal) {
        long diffTime = firstCal.getTime() - secordCal.getTime();
        long diffDay = diffTime / (1000 * 60 * 60 * 24);
        return diffDay;
    }

    public static boolean isToday(Long timeStamp) {
        if (timeStamp != null) {
            Long now = System.currentTimeMillis();
            if (now / (1000 * 60 * 60 * 24) == timeStamp / (1000 * 60 * 60 * 24)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isYesTerday(Long timeStamp) {
        if (timeStamp != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -1);
            if (cal.getTimeInMillis() / (1000 * 60 * 60 * 24) == timeStamp / (1000 * 60 * 60 * 24)) {
                return true;
            }
        }
        return false;
    }

    public static String formatDateTime(Date date) {
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormat.format(date);
    }

    /***
     * @param date
     *        yyyy-MM-dd
     * @return
     */
    public static String formatDate(Date date) {
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateTimeFormat.format(date);
    }

    public static Date parserDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }

    public static Date parser(String date, String pattern) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }

    public static Date parserDateTime(String dateTime) throws ParseException {
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormat.setLenient(false);
        return dateTimeFormat.parse(dateTime);
    }

    public static String getCurrentYearMonth() {
        DateFormat yyyyMMFormat = new SimpleDateFormat("yyyyMM");
        return yyyyMMFormat.format(new Date());
    }

    public static String getYearMonth_YYYYMM(Date date) {
        DateFormat yyyyMMFormat = new SimpleDateFormat("yyyyMM");
        return yyyyMMFormat.format(date);
    }

    public static boolean isCurrentDay(Date date) {
        if (date != null) {
            return format(date, "yyyy-MM-dd").equals(getToday());
        }
        throw new IllegalArgumentException("date cannot be null ");
    }

    public static boolean isTomorrow(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        Date tommorowDate = addDay(1);
        return format(date, "yyyy-MM-dd").equals(format(tommorowDate, "yyyy-MM-dd"));
    }

    public static boolean isYesterday(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        Date tommorowDate = addDay(-1);
        return format(date, "yyyy-MM-dd").equals(format(tommorowDate, "yyyy-MM-dd"));
    }

    public static Date addYear(Date date, Integer year) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public static Date addYear(Integer year) {
        return addYear(new Date(), year);
    }

    /**
     * 添加指定月份
     */
    public static Date addMonth(Date date, Integer month) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static Date addMonth(Integer month) {
        return addMonth(new Date(), month);
    }

    /**
     * 获取当前系统时间加指定天数
     *
     * @param day
     * @return
     */
    public static Date addDay(Integer day) {
        return addDay(new Date(), day);
    }

    public static Date addDay(Date date, Integer day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 传入日期；为该日期添加指定小时数
     *
     * @return
     * @throws ParseException
     */

    public static Date addHour(Integer hour) {
        return addHour(new Date(), hour);
    }

    public static Date addHour(Date date, Integer hour) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * 添加指定分钟数
     */
    public static Date addMinute(Date date, Integer minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static Date addMinute(Integer minute) {
        return addMinute(new Date(), minute);
    }

    public static Date addSecond(Date date, Integer second) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date addSecond(Integer second) {
        return addSecond(new Date(), second);
    }

    /**
     * 获得当天的时间字符串，包含年月日<br />
     * 形如：2012-11-29
     *
     * @return 当天的时间字符串
     */
    public static String getToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String[] parserTime(long second) {
        String HH, MM, SS;
        long h = 0, m = 0, s = 0;
        if (second > 3600) {
            h = second / 3600;
            if (second % 3600 > 60) {
                m = (second % 3600) / 60;
                s = (second % 3600) % 60;
            } else {
                s = (second % 3600) % 60;
            }
        } else if (second > 60) {
            m = second / 60;
            s = second % 60;
        } else if (second > 0) {
            s = second;
        }
        if (h < 10) {
            HH = "0" + h;
        } else {
            HH = String.valueOf(h);
        }
        if (m < 10) {
            MM = "0" + m;
        } else {
            MM = String.valueOf(m);
        }
        if (s < 10) {
            SS = "0" + s;
        } else {
            SS = String.valueOf(s);
        }
        return new String[]{HH, MM, SS};
    }

    public static String[] parserDayTime(long second) {
        String DD;
        long d = 0;
        String hms[];
        if (second > 86400) {
            d = second / 86400;
            hms = parserTime(second % 86400);
        } else {
            hms = parserTime(second);
        }
        if (d > 9) {
            DD = String.valueOf(d);
        } else {
            DD = String.valueOf("0" + d);
        }
        String[] dayTime = new String[4];
        dayTime[0] = DD;
        System.arraycopy(hms, 0, dayTime, 1, hms.length);
        return dayTime;
    }

    /**
     * 获取明天的00:00:00
     *
     * @return
     */
    public static Date tomorrow() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        String datetime = format(calendar.getTime(), "yyyy-MM-dd") + " 00:00:00";
        try {
            return parserDate(datetime);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当天距离24点00分00秒剩余的毫秒数
     *
     * @return
     */
    public static long todayLastTime() {
        return tomorrow().getTime() - System.currentTimeMillis();
    }

    public static int getWeek() {
        int week = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (week - 1 == 0) {
            week = 7;
        } else {
            --week;
        }
        return week;
    }

    public static int getWeekByYear() {
        if (getWeek() == 7) {
            return GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1;
        } else {
            return GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        }
    }

    /**
     * 获取下一个星期五的间隔时间 单位（毫秒） 如果本周未到周五的话，返回距离本周五的时间 否则获取距离下周五的时间
     *
     * @return
     */
    public static long getNextWeekFiveIntervalTime() {
        int week = getWeek();
        Date date = null;
        if (week < 5) {
            date = addDay(5 - week);
        } else {
            date = addDay(7 - week + 5);
        }
        try {
            return parserDate(format(date, "yyyy-MM-dd")).getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            return -1;
        }
    }

    /**
     * <p>
     * 将传入日期抹去时分秒.毫秒的值并。
     * </p>
     * <p>
     * 如果传入null,则使用当前系统时间
     * </p>
     *
     * @param date
     * @return 传入日期抹去时分秒.毫秒
     */
    public static Date trimHMS(Date date) {
        Calendar calendar = new GregorianCalendar();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 是月初或月末
     *
     * @return
     */
    public static boolean isFirstOrLastDayOfMonth() {
        Calendar calendar = GregorianCalendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return day == 1 || getDaysOfMonth(year, month) == day;
    }

    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(DateUtils.parserDate(year + "-" + month + "-01"));
        } catch (ParseException e) {
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getRestSecondsOfToday() {
        return (int) (tomorrow().getTime() - System.currentTimeMillis()) / 1000;
    }
}
