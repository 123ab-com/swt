package com.studentscore.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期字符串与 Date 的互转（用于 Struts2 表单的字符串入参）。
 */
public final class DateUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    private DateUtil() {
    }

    public static Date parseDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(text.trim());
        } catch (ParseException e) {
            throw new BusinessException("日期格式不正确，应为 " + DATE_PATTERN);
        }
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }
}
