package com.campus.jobs.controller;

import com.campus.jobs.common.PageResult;
import com.campus.jobs.common.Result;
import com.campus.jobs.entity.User;
import com.campus.jobs.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<PageResult<User>> page(@RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) String role,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return Result.success(userService.page(keyword, role, page, size));
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
