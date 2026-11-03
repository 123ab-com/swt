package com.studentscore.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 首页工作台数据。
 */
public class DashboardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer studentCount;
    private Integer classCount;
    private Integer courseCount;
    private BigDecimal avgScore;
    private BigDecimal passRate;
    private List<StudentStatVO> topStudents;

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Integer getClassCount() {
        return classCount;
    }

    public void setClassCount(Integer classCount) {
        this.classCount = classCount;
    }

    public Integer getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(Integer courseCount) {
        this.courseCount = courseCount;
    }

    public BigDecimal getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(BigDecimal avgScore) {
        this.avgScore = avgScore;
    }

    public BigDecimal getPassRate() {
        return passRate;
    }

    public void setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
    }

    public List<StudentStatVO> getTopStudents() {
        return topStudents;
    }

    public void setTopStudents(List<StudentStatVO> topStudents) {
        this.topStudents = topStudents;
    }
}
