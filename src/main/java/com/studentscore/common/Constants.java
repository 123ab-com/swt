package com.studentscore.common;

import java.time.LocalDate;

/**
 * 全局常量。
 */
public final class Constants {

    /** 平时成绩占总评比例 */
    public static final double USUAL_WEIGHT = 0.3;
    /** 期末成绩占总评比例 */
    public static final double EXAM_WEIGHT = 0.7;
    /** 及格线 */
    public static final double PASS_LINE = 60;
    /** 优秀线 */
    public static final double EXCELLENT_LINE = 90;
    /** 新建账号 / 重置密码后的默认口令 */
    public static final String DEFAULT_PASSWORD = "123456";

    private Constants() {
    }

    /**
     * 按当前时间推算学期，用于页面默认值兜底。
     * 8 月—次年 1 月为第 1 学期，2 月—7 月为第 2 学期。
     */
    public static String currentSemester() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        if (month >= 8) {
            return year + "-" + (year + 1) + "-1";
        }
        if (month >= 2) {
            return (year - 1) + "-" + year + "-2";
        }
        return (year - 1) + "-" + year + "-1";
    }
}
