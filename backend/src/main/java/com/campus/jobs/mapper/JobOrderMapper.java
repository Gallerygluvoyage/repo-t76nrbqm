package com.campus.jobs.mapper;

import com.campus.jobs.entity.JobOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接单/订单数据访问层。
 */
public interface JobOrderMapper {

    List<JobOrder> selectList(@Param("keyword") String keyword,
                              @Param("status") String status,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    long count(@Param("keyword") String keyword, @Param("status") String status);

    JobOrder selectById(@Param("id") Long id);

    int insert(JobOrder order);

    int update(JobOrder order);

    int deleteById(@Param("id") Long id);
}
