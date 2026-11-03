package com.studentscore.dto;

import com.studentscore.common.PageQuery;

/**
 * 成绩查询条件（同时作为分页参数）。
 */
public class ScoreQuery extends PageQuery {

    private Long classId;
    private Long courseId;
    private String semester;
    private String keyword;

    public ScoreQuery() {
    }

    public ScoreQuery(Long classId, Long courseId, String semester, String keyword) {
        this.classId = classId;
        this.courseId = courseId;
        this.semester = semester;
        this.keyword = keyword;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
