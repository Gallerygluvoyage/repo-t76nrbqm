package com.campus.jobs.controller;

import com.campus.jobs.common.PageResult;
import com.campus.jobs.common.Result;
import com.campus.jobs.entity.Job;
import com.campus.jobs.service.JobService;
import org.springframework.web.bind.annotation.*;

/**
 * 兼职信息管理控制器。
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public Result<PageResult<Job>> page(@RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) String category,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return Result.success(jobService.page(keyword, category, status, page, size));
    }

    @GetMapping("/{id}")
    public Result<Job> get(@PathVariable Long id) {
        return Result.success(jobService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Job job) {
        jobService.add(job);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Job job) {
        job.setId(id);
        jobService.update(job);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return Result.success();
    }
}
