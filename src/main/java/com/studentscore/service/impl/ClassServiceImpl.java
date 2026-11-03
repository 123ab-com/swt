package com.studentscore.service.impl;

import com.studentscore.common.BusinessException;
import com.studentscore.entity.ClassInfo;
import com.studentscore.mapper.ClassInfoMapper;
import com.studentscore.mapper.StudentMapper;
import com.studentscore.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassInfoMapper classInfoMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<ClassInfo> list() {
        return classInfoMapper.selectAll();
    }

    @Override
    public ClassInfo get(Long id) {
        return classInfoMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(ClassInfo classInfo) {
        if (!StringUtils.hasText(classInfo.getClassName())) {
            throw new BusinessException("班级名称不能为空");
        }
        classInfo.setClassName(classInfo.getClassName().trim());
        if (classInfoMapper.countByClassName(classInfo.getClassName(), classInfo.getId()) > 0) {
            throw new BusinessException("班级 " + classInfo.getClassName() + " 已存在");
        }
        if (classInfo.getId() == null) {
            classInfoMapper.insert(classInfo);
        } else {
            classInfoMapper.update(classInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (studentMapper.countByClassId(id) > 0) {
            throw new BusinessException("该班级下仍有学生，不能删除");
        }
        classInfoMapper.deleteById(id);
    }

    @Override
    public int countAll() {
        return classInfoMapper.countAll();
    }
}
