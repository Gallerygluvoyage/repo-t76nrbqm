package com.campus.jobs.service;

import com.campus.jobs.common.PageResult;
import com.campus.jobs.entity.Job;

public interface JobService {

    PageResult<Job> page(String keyword, String category, String status, int page, int size);

    Job getById(Long id);

    void add(Job job);

    void update(Job job);

    void delete(Long id);
}
