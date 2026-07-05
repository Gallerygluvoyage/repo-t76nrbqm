package com.campus.jobs.service;

import com.campus.jobs.common.PageResult;
import com.campus.jobs.entity.User;

public interface UserService {

    PageResult<User> page(String keyword, String role, int page, int size);

    User getById(Long id);

    void add(User user);

    void update(User user);

    void delete(Long id);

    User login(String username, String password);
}
