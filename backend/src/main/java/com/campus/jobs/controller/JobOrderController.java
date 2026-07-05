package com.campus.jobs.controller;

import com.campus.jobs.common.PageResult;
import com.campus.jobs.common.Result;
import com.campus.jobs.entity.JobOrder;
import com.campus.jobs.service.JobOrderService;
import org.springframework.web.bind.annotation.*;

/**
 * 接单/订单管理控制器。
 */
@RestController
@RequestMapping("/api/orders")
public class JobOrderController {

    private final JobOrderService orderService;

    public JobOrderController(JobOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<PageResult<JobOrder>> page(@RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) String status,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return Result.success(orderService.page(keyword, status, page, size));
    }

    @GetMapping("/{id}")
    public Result<JobOrder> get(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody JobOrder order) {
        orderService.add(order);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody JobOrder order) {
        order.setId(id);
        orderService.update(order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return Result.success();
    }
}
