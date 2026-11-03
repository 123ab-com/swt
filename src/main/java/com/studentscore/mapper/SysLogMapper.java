package com.studentscore.mapper;

import com.studentscore.dto.LogQuery;
import com.studentscore.entity.SysLog;

import java.util.List;

public interface SysLogMapper {

    int insert(SysLog log);

    List<SysLog> selectPage(LogQuery query);
}
