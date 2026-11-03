package com.studentscore.service.impl;

import com.studentscore.entity.Teacher;
import com.studentscore.mapper.TeacherMapper;
import com.studentscore.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Teacher> list() {
        return teacherMapper.selectAll();
    }
}
