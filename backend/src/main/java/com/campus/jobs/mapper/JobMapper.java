package com.campus.jobs.mapper;

import com.campus.jobs.entity.Job;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 兼职信息数据访问层。
 */
public interface JobMapper {

    List<Job> selectList(@Param("keyword") String keyword,
                         @Param("category") String category,
                         @Param("status") String status,
                         @Param("offset") int offset,
                         @Param("limit") int limit);

    long count(@Param("keyword") String keyword,
               @Param("category") String category,
               @Param("status") String status);

    Job selectById(@Param("id") Long id);

    int insert(Job job);

    int update(Job job);

    int deleteById(@Param("id") Long id);
}
