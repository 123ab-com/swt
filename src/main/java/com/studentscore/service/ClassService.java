package com.studentscore.service;

import com.studentscore.entity.ClassInfo;

import java.util.List;

public interface ClassService {

    List<ClassInfo> list();

    ClassInfo get(Long id);

    void saveOrUpdate(ClassInfo classInfo);

    void delete(Long id);

    int countAll();
}
