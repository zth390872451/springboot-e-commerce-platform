package com.svlada.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FULL_DATE_FORMAT_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String PART_DATE_FORMAT = "yyyy-MM-dd";
    public static final String PART_DATE_FORMAT_CN = "yyyy年MM月dd日";
    public static final String YEAR_DATE_FORMAT = "yyyy";
    public static final String MONTH_DATE_FORMAT = "MM";
    public static final String DAY_DATE_FORMAT = "dd";
    public static final String WEEK_DATE_FORMAT = "week";
    public static final String FULL_DATE_FORMAT_ALL = "yyyyMMddHHmmss";

    /**
     * 将日期类型转换为字符串
     *
     * @param date    日期
     * @param xFormat 格式
     * @return
     */
    public static String getFormatDate(Date date, String xFormat) {
        date = date == null ? new Date() : date;
        xFormat = StringUtils.isNotEmpty(xFormat) == true ? xFormat : FULL_DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
        return sdf.format(date);
    }

    /**
     * 比较日期大小
     *
     * @param dateX
     * @param dateY
     * @return x < y return [-1]; x = y return [0] ; x > y return [1] ;
     */
    public static int compareDate(Date dateX, Date dateY) {
        return dateX.compareTo(dateY);
    }

    /**
     * 将日期字符串转换为日期格式类型
     *
     * @param xDate
     * @param xFormat 为NULL则转换如：2012-06-25
     * @return
     */
    public static Date parseString2Date(String xDate, String xFormat) {
        while (!isNotDate(xDate)) {
            xFormat = StringUtils.isNotEmpty(xFormat) == true ? xFormat : PART_DATE_FORMAT;
            SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
            Date date = null;
            try {
                date = sdf.parse(xDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            return date;
        }
        return null;
    }

    /**
     * 将日期字符串转换为日期格式类型
     *
     * @param xDate
     * @param xFormat 为NULL则转换如：2012-06-25
     * @return
     */
    public static Date parseString2Date2(String xDate, String xFormat) {
        xFormat = StringUtils.isNotEmpty(xFormat) == true ? xFormat : PART_DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
        Date date = null;
        try {
            date = sdf.parse(xDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 判断需要转换类型的日期字符串是否符合格式要求
     *
     * @param xDate 可以为NULL
     * @return
     */
    public static boolean isNotDate(String xDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PART_DATE_FORMAT);
        try {
            if (StringUtils.isEmpty(xDate)) {
                return true;
            }
            sdf.parse(xDate);
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean isDate(String xDate) {
        return !isDate(xDate);
    }

    /**
     * 获取俩个日期之间相差天数
     *
     * @param dateX
     * @param dateY
     * @return
     */
    public static int getDiffDays(Date dateX, Date dateY) {
        if ((dateX == null) || (dateY == null)) {
            return 0;
        }

        int dayX = (int) (dateX.getTime() / (60 * 60 * 1000 * 24));
        int dayY = (int) (dateY.getTime() / (60 * 60 * 1000 * 24));

        return dayX > dayY ? dayX - dayY : dayY - dayX;
    }

    /**
     * 获取传值日期之后几天的日期并转换为字符串类型
     *
     * @param date    需要转换的日期 date 可以为NULL 此条件下则获取当前日期
     * @param after   天数
     * @param xFormat 转换字符串类型 (可以为NULL)
     * @return
     */
    public static String getAfterCountDate(Date date, int after, String xFormat) {
        date = date == null ? new Date() : date;
        xFormat = StringUtils.isNotEmpty(xFormat) == true ? xFormat : PART_DATE_FORMAT;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, after);
        return getFormatDate(calendar.getTime(), xFormat);
    }

    /**
     * 获取日期的参数 如：年 , 月 , 日 , 星期几
     *
     * @param xDate   日期 可以为日期格式,可以是字符串格式; 为NULL或者其他格式时都判定为当前日期
     * @param xFormat 年 yyyy 月 MM 日 dd 星期 week ;其他条件下都返回0
     */
    public static int getDateTimeParam(Object xDate, String xFormat) {
        xDate = xDate == null ? new Date() : xDate;
        Date date = null;
        if (xDate instanceof String) {
            date = parseString2Date(xDate.toString(), null);
        } else if (xDate instanceof Date) {
            date = (Date) xDate;
        } else {
            date = new Date();
        }
        date = date == null ? new Date() : date;
        if (StringUtils.isNotEmpty(xFormat) && (xFormat.equals(YEAR_DATE_FORMAT) || xFormat.equals(MONTH_DATE_FORMAT)
                || xFormat.equals(DAY_DATE_FORMAT))) {
            return Integer.parseInt(getFormatDate(date, xFormat));
        } else if (StringUtils.isNotEmpty(xFormat) && (WEEK_DATE_FORMAT.equals(xFormat))) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int week = cal.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1;
            return week;
        } else {
            return 0;
        }
    }

    /**
     * 获取星期字符串
     *
     * @param xDate
     * @return
     */
    public static String getWeekString(Object xDate) {
        int week = getDateTimeParam(xDate, WEEK_DATE_FORMAT);
        switch (week) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    public static long getMinTime(long time) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        Calendar c = Calendar.getInstance();
        long time2 = c.getTimeInMillis();
        long diff;
        if (time < time2) {
            diff = time2 - time;
        } else {
            diff = time - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        // sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        return min;
    }

    public static long getMinTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = (diff / (60 * 1000));
        // sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        return min;
    }

    public static long getSecTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = (diff / (60 * 1000));
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return sec;
    }

    public static String beforeDate(Integer number, String aFormat) {
        if (aFormat == null) {
            aFormat = "yyyy-MM-dd HH:mm:ss";
        } else if ("".equals(aFormat)) {
            aFormat = "yyyy-MM-dd HH:mm:ss";
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, number);
        SimpleDateFormat formatter = new SimpleDateFormat(aFormat);
        String mDateTime = formatter.format(c.getTime());
        return mDateTime;
    }

    // 判断是同一天
    public static boolean isOneDay(Date fromDate, Date toDate) {
        boolean flag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromStr = sdf.format(fromDate);
        String toStr = sdf.format(toDate);
        if (fromStr.equals(toStr)) {
            flag = true;
        }
        return flag;
    }

    // 与当前时间相差几分钟
    public static Long minutesFormCurrent(Long currentDate, Long beginTime) {
        return (currentDate - beginTime) / (60 * 1000);
    }
}