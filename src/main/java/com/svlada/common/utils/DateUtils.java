package com.svlada.common.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 */
public class DateUtils {

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
        xFormat = !StringUtils.isEmpty(xFormat) == true ? xFormat : FULL_DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(xFormat);
        return sdf.format(date);
    }
}
