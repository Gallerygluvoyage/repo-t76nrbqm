package com.campus.jobs.service;

import com.campus.jobs.common.PageResult;
import com.campus.jobs.entity.JobOrder;

public interface JobOrderService {

    PageResult<JobOrder> page(String keyword, String status, int page, int size);

    JobOrder getById(Long id);

    void add(JobOrder order);

    void update(JobOrder order);

    void delete(Long id);
}
