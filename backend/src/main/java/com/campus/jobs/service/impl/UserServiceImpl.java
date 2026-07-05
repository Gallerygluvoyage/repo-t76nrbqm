package com.campus.jobs.service.impl;

import com.campus.jobs.common.BusinessException;
import com.campus.jobs.common.PageResult;
import com.campus.jobs.entity.User;
import com.campus.jobs.mapper.UserMapper;
import com.campus.jobs.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PageResult<User> page(String keyword, String role, int page, int size) {
        int offset = (page - 1) * size;
        List<User> rows = userMapper.selectList(keyword, role, offset, size);
        long total = userMapper.count(keyword, role);
        return new PageResult<>(total, rows);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void add(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BusinessException("用户名不能为空");
        }
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword("123456");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("STUDENT");
        }
        if (user.getStatus() == null || user.getStatus().isBlank()) {
            user.setStatus("正常");
        }
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            throw new BusinessException("缺少用户ID");
        }
        userMapper.update(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!"正常".equals(user.getStatus())) {
            throw new BusinessException("账号已被禁用");
        }
        return user;
    }
}
