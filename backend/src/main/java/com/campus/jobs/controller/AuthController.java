package com.campus.jobs.controller;

import com.campus.jobs.common.BusinessException;
import com.campus.jobs.common.Result;
import com.campus.jobs.entity.User;
import com.campus.jobs.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证控制器：登录 / 注册。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User body) {
        User user = userService.login(body.getUsername(), body.getPassword());
        Map<String, Object> data = new HashMap<>();
        data.put("token", UUID.randomUUID().toString().replace("-", ""));
        user.setPassword(null);
        data.put("user", user);
        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User body) {
        String role = body.getRole();
        if (role == null || !(role.equals("STUDENT") || role.equals("EMPLOYER"))) {
            throw new BusinessException("注册角色只能是学生或雇主");
        }
        userService.add(body);
        return Result.success();
    }
}
