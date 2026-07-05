package com.campus.jobs.service.impl;

import com.campus.jobs.common.BusinessException;
import com.campus.jobs.common.PageResult;
import com.campus.jobs.entity.JobOrder;
import com.campus.jobs.mapper.JobOrderMapper;
import com.campus.jobs.service.JobOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobOrderServiceImpl implements JobOrderService {

    private final JobOrderMapper orderMapper;

    public JobOrderServiceImpl(JobOrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public PageResult<JobOrder> page(String keyword, String status, int page, int size) {
        int offset = (page - 1) * size;
        List<JobOrder> rows = orderMapper.selectList(keyword, status, offset, size);
        long total = orderMapper.count(keyword, status);
        return new PageResult<>(total, rows);
    }

    @Override
    public JobOrder getById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void add(JobOrder order) {
        if (order.getJobId() == null) {
            throw new BusinessException("请选择兼职岗位");
        }
        if (order.getApplicantName() == null || order.getApplicantName().isBlank()) {
            throw new BusinessException("接单人姓名不能为空");
        }
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("待处理");
        }
        orderMapper.insert(order);
    }

    @Override
    public void update(JobOrder order) {
        if (order.getId() == null) {
            throw new BusinessException("缺少订单ID");
        }
        orderMapper.update(order);
    }

    @Override
    public void delete(Long id) {
        orderMapper.deleteById(id);
    }
}
