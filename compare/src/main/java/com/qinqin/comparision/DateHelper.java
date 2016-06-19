package com.qinqin.comparision;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


public class DateHelper {
	 
	/**
     * 日志
     *
     */
    private static Logger _logger = Logger.getLogger(DateHelper.class.getName());
 
    /**
     * 将指定格式的字符串转化成日期
     *
     * @param day
     *            日期字符串
     * @param format
     *            格式化字符串
     * @return 日期
     */
    public static Date getDate(String date, String format) {
        Date d = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            d = df.parse(date);
        } catch (Exception e) {
            _logger.info("Parse string " + date + " to Date failed :"
                    + e.getMessage());
            return null;
        }
        return d;
    }
 
    /**
     * 将日期转换成指定格式的字符串
     *
     * @param date
     *            日期
     * @param format
     *            格式化字符串
     * @return 格式化后的日子字符串
     */
    public static String format(Date date, String format) {
        String d = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            d = df.format(date);
        } catch (Exception e) {
            _logger.info("Parse Date " + date + " to String failed :"
                    + e.getMessage());
            return null;
        }
        return d;
    }
}
