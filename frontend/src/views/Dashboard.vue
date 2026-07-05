<template>
  <div>
    <h3 class="page-title">首页概览</h3>
    <div class="stat-grid">
      <div class="stat-card">
        <div class="num">{{ stat.jobs }}</div>
        <div class="label">兼职岗位总数</div>
      </div>
      <div class="stat-card">
        <div class="num">{{ stat.openJobs }}</div>
        <div class="label">招聘中岗位</div>
      </div>
      <div class="stat-card">
        <div class="num">{{ stat.orders }}</div>
        <div class="label">接单订单总数</div>
      </div>
      <div class="stat-card">
        <div class="num">{{ stat.pending }}</div>
        <div class="label">待处理订单</div>
      </div>
    </div>

    <div class="page-card">
      <h3 class="page-title">最新兼职</h3>
      <el-table :data="latestJobs" stripe>
        <el-table-column type="index" label="#" width="55" />
        <el-table-column prop="title" label="兼职标题" min-width="160" />
        <el-table-column prop="category" label="分类" width="90" />
        <el-table-column prop="company" label="发布单位" min-width="120" />
        <el-table-column prop="location" label="地点" min-width="120" />
        <el-table-column label="薪资" width="120">
          <template #default="{ row }">{{ row.salary }} {{ row.salaryUnit }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === '招聘中' ? 'success' : 'info'" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api/request'

const stat = ref({ jobs: 0, openJobs: 0, orders: 0, pending: 0 })
const latestJobs = ref([])

onMounted(async () => {
  const jobs = await request.get('/jobs', { params: { page: 1, size: 1000 } })
  const orders = await request.get('/orders', { params: { page: 1, size: 1000 } })
  const jobRows = jobs.data.rows
  const orderRows = orders.data.rows
  stat.value.jobs = jobs.data.total
  stat.value.openJobs = jobRows.filter(j => j.status === '招聘中').length
  stat.value.orders = orders.data.total
  stat.value.pending = orderRows.filter(o => o.status === '待处理').length
  latestJobs.value = jobRows.slice(0, 5)
})
</script>
