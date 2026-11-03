package com.studentscore.service;

import com.github.pagehelper.PageInfo;
import com.studentscore.dto.StudentQuery;
import com.studentscore.entity.Student;

import java.util.List;

public interface StudentService {

    PageInfo<Student> page(StudentQuery query);

    List<Student> listByClass(Long classId);

    /** 全部学生（不分页，用于批量生成账号等场景） */
    List<Student> listAll();

    Student get(Long id);

    void saveOrUpdate(Student student);

    void delete(Long id);

    int countAll();
}
