package com.studentscore.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Excel 导入的一行成绩数据：学号 + 课程编号 + 学期 + 平时 + 期末。
 */
public class ScoreImportRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentNo;
    private String courseNo;
    private String semester;
    private BigDecimal usualScore;
    private BigDecimal examScore;

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public BigDecimal getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(BigDecimal usualScore) {
        this.usualScore = usualScore;
    }

    public BigDecimal getExamScore() {
        return examScore;
    }

    public void setExamScore(BigDecimal examScore) {
        this.examScore = examScore;
    }
}
