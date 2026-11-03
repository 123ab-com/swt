package com.studentscore.service;

import com.github.pagehelper.PageInfo;
import com.studentscore.dto.LogQuery;
import com.studentscore.entity.SysLog;

public interface LogService {

    /** 记录一条操作日志 */
    void record(Long userId, String username, String module, String operation, String ip);

    PageInfo<SysLog> page(LogQuery query);
}
