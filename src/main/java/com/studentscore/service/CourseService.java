package com.studentscore.service;

import com.studentscore.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> list();

    /**
     * 按授课教师过滤课程；teacherId 为空表示不过滤（管理员）。
     */
    List<Course> listByTeacher(Long teacherId);

    Course get(Long id);

    void saveOrUpdate(Course course);

    void delete(Long id);

    int countAll();
}
