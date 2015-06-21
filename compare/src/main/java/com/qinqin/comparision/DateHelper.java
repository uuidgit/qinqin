package com.qinqin.comparision;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


public class DateHelper {
	 
    /**
     * ��־
     *
     */
    private static Logger _logger = Logger.getLogger(DateHelper.class.getName());
 
    /**
     * ��ָ����ʽ���ַ���ת��������
     *
     * @param day
     *            �����ַ���
     * @param format
     *            ��ʽ���ַ���
     * @return ����
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
     * ������ת����ָ����ʽ���ַ���
     *
     * @param date
     *            ����
     * @param format
     *            ��ʽ���ַ���
     * @return ��ʽ����������ַ���
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
