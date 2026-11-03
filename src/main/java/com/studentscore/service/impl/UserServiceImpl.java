package com.studentscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.studentscore.common.BusinessException;
import com.studentscore.common.Constants;
import com.studentscore.common.PasswordUtil;
import com.studentscore.common.Role;
import com.studentscore.dto.UserQuery;
import com.studentscore.entity.LoginUser;
import com.studentscore.entity.Student;
import com.studentscore.entity.SysUser;
import com.studentscore.mapper.SysUserMapper;
import com.studentscore.service.StudentService;
import com.studentscore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private StudentService studentService;

    @Override
    public LoginUser login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new BusinessException("请输入账号和密码");
        }
        SysUser user = sysUserMapper.selectByUsername(username.trim());
        if (user == null || !PasswordUtil.matches(password, user.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("该账号已被禁用，请联系管理员");
        }
        return new LoginUser(user.getId(), user.getUsername(), user.getRealName(), user.getRole(), user.getRefId());
    }

    @Override
    public PageInfo<SysUser> page(UserQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        return new PageInfo<>(sysUserMapper.selectPage(query));
    }

    @Override
    public SysUser get(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysUser user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new BusinessException("登录账号不能为空");
        }
        user.setUsername(user.getUsername().trim());
        if (sysUserMapper.countByUsername(user.getUsername(), user.getId()) > 0) {
            throw new BusinessException("账号 " + user.getUsername() + " 已存在");
        }
        if (!Role.ADMIN.equals(user.getRole()) && !Role.TEACHER.equals(user.getRole())
                && !Role.STUDENT.equals(user.getRole())) {
            throw new BusinessException("请选择正确的角色");
        }
        if (StringUtils.hasText(user.getPassword())) {
            checkPassword(user.getPassword());
            user.setPassword(PasswordUtil.md5(user.getPassword()));
        } else {
            user.setPassword(null);
        }

        if (user.getId() == null) {
            if (user.getPassword() == null) {
                user.setPassword(PasswordUtil.md5(Constants.DEFAULT_PASSWORD));
            }
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            sysUserMapper.insert(user);
        } else {
            sysUserMapper.update(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("账号不存在");
        }
        sysUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id) {
        if (sysUserMapper.selectById(id) == null) {
            throw new BusinessException("账号不存在");
        }
        sysUserMapper.updatePassword(id, PasswordUtil.md5(Constants.DEFAULT_PASSWORD));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("账号不存在");
        }
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }
        checkPassword(newPassword);
        sysUserMapper.updatePassword(userId, PasswordUtil.md5(newPassword));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateStudentAccounts() {
        Set<String> exists = new HashSet<>(sysUserMapper.selectAllUsernames());
        List<Student> students = studentService.listAll();
        int created = 0;
        for (Student student : students) {
            if (student.getStatus() != null && student.getStatus() == 0) {
                continue;
            }
            if (!StringUtils.hasText(student.getStudentNo()) || exists.contains(student.getStudentNo())) {
                continue;
            }
            SysUser user = new SysUser();
            user.setUsername(student.getStudentNo());
            user.setPassword(PasswordUtil.md5(Constants.DEFAULT_PASSWORD));
            user.setRole(Role.STUDENT);
            user.setRealName(student.getName());
            user.setRefId(student.getId());
            user.setStatus(1);
            sysUserMapper.insert(user);
            exists.add(student.getStudentNo());
            created++;
        }
        return created;
    }

    private void checkPassword(String password) {
        if (password.length() < 6 || password.length() > 20) {
            throw new BusinessException("密码长度应为 6 ~ 20 位");
        }
    }
}
