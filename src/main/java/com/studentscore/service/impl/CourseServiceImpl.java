package com.studentscore.service.impl;

import com.studentscore.common.BusinessException;
import com.studentscore.entity.Course;
import com.studentscore.mapper.CourseMapper;
import com.studentscore.mapper.ScoreMapper;
import com.studentscore.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public List<Course> list() {
        return courseMapper.selectAll();
    }

    @Override
    public List<Course> listByTeacher(Long teacherId) {
        if (teacherId == null) {
            return list();
        }
        return courseMapper.selectByTeacher(teacherId);
    }

    @Override
    public Course get(Long id) {
        return courseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Course course) {
        if (!StringUtils.hasText(course.getCourseNo()) || !StringUtils.hasText(course.getCourseName())) {
            throw new BusinessException("课程编号和课程名称不能为空");
        }
        course.setCourseNo(course.getCourseNo().trim());
        course.setCourseName(course.getCourseName().trim());
        if (courseMapper.countByCourseNo(course.getCourseNo(), course.getId()) > 0) {
            throw new BusinessException("课程编号 " + course.getCourseNo() + " 已存在");
        }
        if (course.getCredit() == null) {
            course.setCredit(new BigDecimal("3.0"));
        }
        if (!StringUtils.hasText(course.getSemester())) {
            throw new BusinessException("请填写开课学期");
        }
        if (course.getId() == null) {
            courseMapper.insert(course);
        } else {
            courseMapper.update(course);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (scoreMapper.countByCourse(id) > 0) {
            throw new BusinessException("该课程已录入成绩，不能删除");
        }
        courseMapper.deleteById(id);
    }

    @Override
    public int countAll() {
        return courseMapper.countAll();
    }
}
