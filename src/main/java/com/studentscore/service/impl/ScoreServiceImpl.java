package com.studentscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Constants;
import com.studentscore.common.ScoreExcelUtil;
import com.studentscore.dto.ScoreImportRow;
import com.studentscore.dto.ScoreQuery;
import com.studentscore.entity.Course;
import com.studentscore.entity.Score;
import com.studentscore.entity.Student;
import com.studentscore.mapper.ClassInfoMapper;
import com.studentscore.mapper.CourseMapper;
import com.studentscore.mapper.ScoreMapper;
import com.studentscore.mapper.StudentMapper;
import com.studentscore.service.ScoreService;
import com.studentscore.vo.CourseStatVO;
import com.studentscore.vo.DashboardVO;
import com.studentscore.vo.ScoreSummary;
import com.studentscore.vo.ScoreVO;
import com.studentscore.vo.StudentStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Override
    public List<ScoreVO> entryRows(Long classId, Long courseId, String semester) {
        if (classId == null || courseId == null) {
            return Collections.emptyList();
        }
        List<Student> students = studentMapper.selectByClassId(classId);
        List<ScoreVO> exists = scoreMapper.selectScoreList(new ScoreQuery(classId, courseId, semester, null));
        Map<Long, ScoreVO> existMap = new HashMap<>();
        for (ScoreVO vo : exists) {
            existMap.put(vo.getStudentId(), vo);
        }
        List<ScoreVO> rows = new ArrayList<>(students.size());
        for (Student student : students) {
            ScoreVO vo = existMap.get(student.getId());
            if (vo == null) {
                vo = new ScoreVO();
                vo.setStudentId(student.getId());
                vo.setStudentNo(student.getStudentNo());
                vo.setStudentName(student.getName());
                vo.setClassName(student.getClassName());
                vo.setCourseId(courseId);
                vo.setSemester(semester);
            }
            rows.add(vo);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<Score> scores, Long teacherId) {
        if (scores == null || scores.isEmpty()) {
            return;
        }
        for (Score score : scores) {
            if (score.getStudentId() == null || score.getCourseId() == null) {
                continue;
            }
            if (score.getUsualScore() == null || score.getExamScore() == null) {
                continue;
            }
            // 教师只能录入自己所授课程的成绩，管理员（teacherId 为空）不受限制
            if (teacherId != null) {
                Course course = courseMapper.selectById(score.getCourseId());
                if (course == null || !teacherId.equals(course.getTeacherId())) {
                    throw new BusinessException("只能录入自己所授课程的成绩");
                }
            }
            checkRange(score.getUsualScore(), "平时成绩");
            checkRange(score.getExamScore(), "期末成绩");
            if (!StringUtils.hasText(score.getSemester())) {
                score.setSemester(Constants.currentSemester());
            }
            score.setTotalScore(calcTotal(score));
            scoreMapper.save(score);
        }
    }

    private void checkRange(BigDecimal value, String label) {
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException(label + "必须在 0 ~ 100 之间");
        }
    }

    private BigDecimal calcTotal(Score score) {
        BigDecimal total = score.getUsualScore()
                .multiply(BigDecimal.valueOf(Constants.USUAL_WEIGHT))
                .add(score.getExamScore().multiply(BigDecimal.valueOf(Constants.EXAM_WEIGHT)));
        return total.setScale(1, RoundingMode.HALF_UP);
    }

    @Override
    public List<ScoreVO> list(ScoreQuery query) {
        return scoreMapper.selectScoreList(query);
    }

    @Override
    public PageInfo<ScoreVO> listPage(ScoreQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        return new PageInfo<>(scoreMapper.selectScoreList(query));
    }

    @Override
    public ScoreSummary summary(ScoreQuery query) {
        return scoreMapper.summary(query);
    }

    @Override
    public BigDecimal avgByStudent(Long studentId, String semester) {
        return scoreMapper.avgByStudent(studentId, semester);
    }

    @Override
    public BigDecimal weightedAvgByStudent(Long studentId, String semester) {
        return scoreMapper.weightedAvgByStudent(studentId, semester);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importScores(InputStream inputStream, Long teacherId) {
        List<ScoreImportRow> rows = ScoreExcelUtil.read(inputStream);
        if (rows.isEmpty()) {
            throw new BusinessException("Excel 中没有可导入的数据");
        }

        List<Score> scores = new ArrayList<>(rows.size());
        int line = 1;
        for (ScoreImportRow row : rows) {
            line++;
            if (!StringUtils.hasText(row.getStudentNo())) {
                throw new BusinessException("第 " + line + " 行：学号不能为空");
            }
            Student student = studentMapper.selectByStudentNo(row.getStudentNo().trim());
            if (student == null) {
                throw new BusinessException("第 " + line + " 行：学号 " + row.getStudentNo() + " 不存在");
            }
            if (!StringUtils.hasText(row.getCourseNo())) {
                throw new BusinessException("第 " + line + " 行：课程编号不能为空");
            }
            Course course = courseMapper.selectByCourseNo(row.getCourseNo().trim());
            if (course == null) {
                throw new BusinessException("第 " + line + " 行：课程编号 " + row.getCourseNo() + " 不存在");
            }
            if (teacherId != null && !teacherId.equals(course.getTeacherId())) {
                throw new BusinessException("第 " + line + " 行：只能导入自己所授课程的成绩（" + row.getCourseNo() + "）");
            }
            if (row.getUsualScore() == null || row.getExamScore() == null) {
                throw new BusinessException("第 " + line + " 行：平时成绩与期末成绩均不能为空");
            }

            Score score = new Score();
            score.setStudentId(student.getId());
            score.setCourseId(course.getId());
            score.setUsualScore(row.getUsualScore());
            score.setExamScore(row.getExamScore());
            score.setSemester(StringUtils.hasText(row.getSemester())
                    ? row.getSemester().trim() : Constants.currentSemester());
            scores.add(score);
        }

        for (Score score : scores) {
            checkRange(score.getUsualScore(), "平时成绩");
            checkRange(score.getExamScore(), "期末成绩");
            score.setTotalScore(calcTotal(score));
            scoreMapper.save(score);
        }
        return scores.size();
    }

    @Override
    public List<ScoreVO> listByStudent(Long studentId, String semester) {
        return scoreMapper.selectByStudentId(studentId, semester);
    }

    @Override
    public List<StudentStatVO> statByStudent(Long classId, String semester, Long courseId) {
        return scoreMapper.statByStudent(classId, semester, courseId);
    }

    @Override
    public List<CourseStatVO> statByCourse(Long classId, String semester) {
        return scoreMapper.statByCourse(classId, semester);
    }

    @Override
    public List<Map<String, Object>> distribution(Long classId, Long courseId, String semester) {
        return scoreMapper.distribution(classId, courseId, semester);
    }

    @Override
    public DashboardVO dashboard() {
        DashboardVO vo = new DashboardVO();
        vo.setStudentCount(studentMapper.countAll());
        vo.setClassCount(classInfoMapper.countAll());
        vo.setCourseCount(courseMapper.countAll());
        vo.setAvgScore(scoreMapper.avgTotal());
        vo.setPassRate(scoreMapper.passRate());
        List<StudentStatVO> ranking = scoreMapper.statByStudent(null, null, null);
        vo.setTopStudents(ranking.size() > 5 ? new ArrayList<>(ranking.subList(0, 5)) : ranking);
        return vo;
    }
}
