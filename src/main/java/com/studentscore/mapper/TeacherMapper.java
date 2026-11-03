package com.studentscore.mapper;

import com.studentscore.entity.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper {

    List<Teacher> selectAll();

    Teacher selectById(@Param("id") Long id);
}
