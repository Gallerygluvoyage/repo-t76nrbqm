package com.campus.jobs.mapper;

import com.campus.jobs.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层。
 */
public interface UserMapper {

    List<User> selectList(@Param("keyword") String keyword,
                          @Param("role") String role,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    long count(@Param("keyword") String keyword, @Param("role") String role);

    User selectById(@Param("id") Long id);

    User selectByUsername(@Param("username") String username);

    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);
}
