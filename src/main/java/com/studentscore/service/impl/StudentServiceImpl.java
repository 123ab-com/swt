package com.studentscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.studentscore.common.BusinessException;
import com.studentscore.dto.StudentQuery;
import com.studentscore.entity.Student;
import com.studentscore.mapper.ScoreMapper;
import com.studentscore.mapper.StudentMapper;
import com.studentscore.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public PageInfo<Student> page(StudentQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        return new PageInfo<>(studentMapper.selectPage(query));
    }

    @Override
    public List<Student> listByClass(Long classId) {
        return studentMapper.selectByClassId(classId);
    }

    @Override
    public List<Student> listAll() {
        return studentMapper.selectAll();
    }

    @Override
    public Student get(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Student student) {
        if (!StringUtils.hasText(student.getStudentNo()) || !StringUtils.hasText(student.getName())) {
            throw new BusinessException("学号和姓名不能为空");
        }
        if (student.getClassId() == null) {
            throw new BusinessException("请选择所属班级");
        }
        if (studentMapper.countByStudentNo(student.getStudentNo().trim(), student.getId()) > 0) {
            throw new BusinessException("学号 " + student.getStudentNo() + " 已存在");
        }
        student.setStudentNo(student.getStudentNo().trim());
        student.setName(student.getName().trim());
        if (student.getId() == null) {
            if (student.getStatus() == null) {
                student.setStatus(1);
            }
            studentMapper.insert(student);
        } else {
            studentMapper.update(student);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        scoreMapper.deleteByStudentId(id);
        studentMapper.deleteById(id);
    }

    @Override
    public int countAll() {
        return studentMapper.countAll();
    }
}
