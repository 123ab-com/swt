package com.studentscore.mapper;

import com.studentscore.entity.ClassInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassInfoMapper {

    List<ClassInfo> selectAll();

    ClassInfo selectById(@Param("id") Long id);

    int insert(ClassInfo classInfo);

    int update(ClassInfo classInfo);

    int deleteById(@Param("id") Long id);

    int countAll();

    int countByClassName(@Param("className") String className, @Param("excludeId") Long excludeId);
}
