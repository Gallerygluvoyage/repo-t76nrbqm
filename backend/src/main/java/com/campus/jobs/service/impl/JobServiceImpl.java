package com.campus.jobs.service.impl;

import com.campus.jobs.common.BusinessException;
import com.campus.jobs.common.PageResult;
import com.campus.jobs.entity.Job;
import com.campus.jobs.mapper.JobMapper;
import com.campus.jobs.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    public JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    @Override
    public PageResult<Job> page(String keyword, String category, String status, int page, int size) {
        int offset = (page - 1) * size;
        List<Job> rows = jobMapper.selectList(keyword, category, status, offset, size);
        long total = jobMapper.count(keyword, category, status);
        return new PageResult<>(total, rows);
    }

    @Override
    public Job getById(Long id) {
        return jobMapper.selectById(id);
    }

    @Override
    public void add(Job job) {
        if (job.getTitle() == null || job.getTitle().isBlank()) {
            throw new BusinessException("兼职标题不能为空");
        }
        if (job.getStatus() == null || job.getStatus().isBlank()) {
            job.setStatus("招聘中");
        }
        if (job.getSalaryUnit() == null || job.getSalaryUnit().isBlank()) {
            job.setSalaryUnit("元/时");
        }
        jobMapper.insert(job);
    }

    @Override
    public void update(Job job) {
        if (job.getId() == null) {
            throw new BusinessException("缺少兼职ID");
        }
        jobMapper.update(job);
    }

    @Override
    public void delete(Long id) {
        jobMapper.deleteById(id);
    }
}
