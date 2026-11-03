package com.studentscore.service;

import com.github.pagehelper.PageInfo;
import com.studentscore.dto.ScoreQuery;
import com.studentscore.entity.Score;
import com.studentscore.vo.CourseStatVO;
import com.studentscore.vo.DashboardVO;
import com.studentscore.vo.ScoreSummary;
import com.studentscore.vo.ScoreVO;
import com.studentscore.vo.StudentStatVO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ScoreService {

    /** 成绩录入页的行数据（班级学生 + 已录成绩） */
    List<ScoreVO> entryRows(Long classId, Long courseId, String semester);

    /**
     * 批量保存成绩。
     *
     * @param teacherId 当前操作人对应的教师ID，为空表示管理员（不校验归属）
     */
    void saveBatch(List<Score> scores, Long teacherId);

    List<ScoreVO> list(ScoreQuery query);

    /** 分页查询 */
    PageInfo<ScoreVO> listPage(ScoreQuery query);

    /** 全部匹配记录的汇总统计（不受分页影响） */
    ScoreSummary summary(ScoreQuery query);

    /** 导入 Excel 成绩，返回成功条数 */
    int importScores(InputStream inputStream, Long teacherId);

    List<ScoreVO> listByStudent(Long studentId, String semester);

    /** 算术平均分 */
    BigDecimal avgByStudent(Long studentId, String semester);

    /** 学分加权平均分（GPA 口径） */
    BigDecimal weightedAvgByStudent(Long studentId, String semester);

    List<StudentStatVO> statByStudent(Long classId, String semester, Long courseId);

    List<CourseStatVO> statByCourse(Long classId, String semester);

    List<Map<String, Object>> distribution(Long classId, Long courseId, String semester);

    DashboardVO dashboard();
}
