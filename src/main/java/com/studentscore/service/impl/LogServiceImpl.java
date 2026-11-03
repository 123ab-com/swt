package com.studentscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.studentscore.dto.LogQuery;
import com.studentscore.entity.SysLog;
import com.studentscore.mapper.SysLogMapper;
import com.studentscore.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void record(Long userId, String username, String module, String operation, String ip) {
        SysLog log = new SysLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setModule(module);
        log.setOperation(operation);
        log.setIp(ip);
        sysLogMapper.insert(log);
    }

    @Override
    public PageInfo<SysLog> page(LogQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        return new PageInfo<>(sysLogMapper.selectPage(query));
    }
}
