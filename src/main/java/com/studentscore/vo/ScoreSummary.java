package com.studentscore.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 成绩列表的汇总统计（平均分/最高/最低/及格数）。
 */
public class ScoreSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal avgScore;
    private BigDecimal maxScore;
    private BigDecimal minScore;
    private int passCount;
    private int totalCount;

    public BigDecimal getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(BigDecimal avgScore) {
        this.avgScore = avgScore;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    public BigDecimal getMinScore() {
        return minScore;
    }

    public void setMinScore(BigDecimal minScore) {
        this.minScore = minScore;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
