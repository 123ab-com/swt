package com.studentscore.mapper;

import com.studentscore.dto.UserQuery;
import com.studentscore.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {

    SysUser selectByUsername(@Param("username") String username);

    SysUser selectById(@Param("id") Long id);

    List<SysUser> selectPage(UserQuery query);

    /** 已存在的账号名，用于批量生成学生账号时去重 */
    List<String> selectAllUsernames();

    int countByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);

    int insert(SysUser user);

    int update(SysUser user);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int deleteById(@Param("id") Long id);
}
