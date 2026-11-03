package com.studentscore.common;

import com.studentscore.vo.ScoreSummary;
import com.studentscore.vo.ScoreVO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 成绩汇总统计工具，供 SpringMVC Controller 与 Struts2 Action 共用。
 */
public final class ScoreStatUtil {

    private ScoreStatUtil() {
    }

    public static ScoreSummary summary(List<ScoreVO> scores) {
        ScoreSummary summary = new ScoreSummary();
        if (scores == null || scores.isEmpty()) {
            return summary;
        }

        BigDecimal avg = null;
        BigDecimal max = null;
        BigDecimal min = null;
        int counted = 0;
        int pass = 0;

        for (ScoreVO vo : scores) {
            BigDecimal total = vo.getTotalScore();
            if (total == null) {
                continue;
            }
            counted++;
            avg = avg == null ? total : avg.add(total);
            max = max == null || total.compareTo(max) > 0 ? total : max;
            min = min == null || total.compareTo(min) < 0 ? total : min;
            if (total.compareTo(BigDecimal.valueOf(Constants.PASS_LINE)) >= 0) {
                pass++;
            }
        }
        if (avg != null) {
            avg = avg.divide(BigDecimal.valueOf(counted), 1, RoundingMode.HALF_UP);
        }

        summary.setAvgScore(avg);
        summary.setMaxScore(max);
        summary.setMinScore(min);
        summary.setPassCount(pass);
        summary.setTotalCount(counted);
        return summary;
    }
}
