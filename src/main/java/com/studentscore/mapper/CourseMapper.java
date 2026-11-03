package com.studentscore.mapper;

import com.studentscore.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {

    List<Course> selectAll();

    List<Course> selectByTeacher(@Param("teacherId") Long teacherId);

    Course selectById(@Param("id") Long id);

    Course selectByCourseNo(@Param("courseNo") String courseNo);

    int insert(Course course);

    int update(Course course);

    int deleteById(@Param("id") Long id);

    int countAll();

    int countByCourseNo(@Param("courseNo") String courseNo, @Param("excludeId") Long excludeId);
}
