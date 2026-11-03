package com.studentscore.mapper;

import com.studentscore.dto.StudentQuery;
import com.studentscore.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {

    List<Student> selectPage(StudentQuery query);

    List<Student> selectByClassId(@Param("classId") Long classId);

    List<Student> selectAll();

    Student selectByStudentNo(@Param("studentNo") String studentNo);

    Student selectById(@Param("id") Long id);

    int insert(Student student);

    int update(Student student);

    int deleteById(@Param("id") Long id);

    int countAll();

    int countByClassId(@Param("classId") Long classId);

    int countByStudentNo(@Param("studentNo") String studentNo, @Param("excludeId") Long excludeId);
}
