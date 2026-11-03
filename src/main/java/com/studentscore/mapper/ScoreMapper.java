package com.studentscore.mapper;

import com.studentscore.dto.ScoreQuery;
import com.studentscore.entity.Score;
import com.studentscore.vo.CourseStatVO;
import com.studentscore.vo.ScoreSummary;
import com.studentscore.vo.ScoreVO;
import com.studentscore.vo.StudentStatVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ScoreMapper {

    List<ScoreVO> selectScoreList(ScoreQuery query);

    /** 全部匹配记录的汇总统计（不受分页影响） */
    ScoreSummary summary(ScoreQuery query);

    /** 算术平均分 */
    BigDecimal avgByStudent(@Param("studentId") Long studentId, @Param("semester") String semester);

    /** 学分加权平均分 */
    BigDecimal weightedAvgByStudent(@Param("studentId") Long studentId, @Param("semester") String semester);

    List<ScoreVO> selectByStudentId(@Param("studentId") Long studentId,
                                    @Param("semester") String semester);

    /** 存在则更新、不存在则插入 */
    int save(Score score);

    int deleteById(@Param("id") Long id);

    int deleteByStudentId(@Param("studentId") Long studentId);

    int deleteByCourseId(@Param("courseId") Long courseId);

    int countByCourse(@Param("courseId") Long courseId);

    int countByStudent(@Param("studentId") Long studentId);

    List<StudentStatVO> statByStudent(@Param("classId") Long classId,
                                      @Param("semester") String semester,
                                      @Param("courseId") Long courseId);

    List<CourseStatVO> statByCourse(@Param("classId") Long classId,
                                    @Param("semester") String semester);

    List<Map<String, Object>> distribution(@Param("classId") Long classId,
                                          @Param("courseId") Long courseId,
                                          @Param("semester") String semester);

    BigDecimal avgTotal();

    BigDecimal passRate();
}
