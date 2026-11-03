package com.studentscore.service;

import com.github.pagehelper.PageInfo;
import com.studentscore.dto.UserQuery;
import com.studentscore.entity.LoginUser;
import com.studentscore.entity.SysUser;

public interface UserService {

    /**
     * 账号密码登录，失败抛出 BusinessException。
     */
    LoginUser login(String username, String password);

    PageInfo<SysUser> page(UserQuery query);

    SysUser get(Long id);

    void saveOrUpdate(SysUser user);

    void delete(Long id);

    /** 重置为默认口令 */
    void resetPassword(Long id);

    /** 修改本人密码 */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /** 为尚无账号的在读学生批量生成账号（账号=学号，默认口令） */
    int generateStudentAccounts();
}
