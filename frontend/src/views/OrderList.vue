<template>
  <div class="page-card">
    <h3 class="page-title">接单订单管理</h3>
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="搜索接单人/电话/兼职" clearable style="width:220px" @keyup.enter="search" />
      <el-select v-model="query.status" placeholder="全部状态" clearable style="width:130px">
        <el-option v-for="s in statuses" :key="s" :label="s" :value="s" />
      </el-select>
      <el-button type="primary" :icon="Search" @click="search">查询</el-button>
      <el-button :icon="Refresh" @click="reset">重置</el-button>
      <div class="spacer"></div>
      <el-button type="primary" :icon="Plus" @click="openAdd">新增接单</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column type="index" label="#" width="55" />
      <el-table-column prop="jobTitle" label="兼职岗位" min-width="160" />
      <el-table-column prop="company" label="发布单位" min-width="120" />
      <el-table-column prop="applicantName" label="接单人" width="100" />
      <el-table-column prop="applicantPhone" label="联系电话" width="130" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" effect="plain">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="140" show-overflow-tooltip />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="Edit" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="margin-top:16px;justify-content:flex-end"
      background layout="total, prev, pager, next"
      :total="total" :page-size="query.size" :current-page="query.page"
      @current-change="p => { query.page = p; load() }" />

    <el-dialog v-model="dialog" :title="form.id ? '编辑接单' : '新增接单'" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="兼职岗位" prop="jobId">
          <el-select v-model="form.jobId" placeholder="请选择兼职岗位" filterable style="width:100%">
            <el-option v-for="j in jobOptions" :key="j.id" :label="`${j.title}（${j.company}）`" :value="j.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="接单人" prop="applicantName">
          <el-input v-model="form.applicantName" placeholder="接单人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="applicantPhone">
          <el-input v-model="form.applicantPhone" placeholder="联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" style="width:100%">
            <el-option v-for="s in statuses" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'

const statuses = ['待处理', '已接受', '已完成', '已取消']
const rows = ref([])
const jobOptions = ref([])
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const dialog = ref(false)
const formRef = ref()
const query = reactive({ keyword: '', status: '', page: 1, size: 8 })

const emptyForm = () => ({
  id: null, jobId: null, applicantName: '', applicantPhone: '', status: '待处理', remark: ''
})
const form = reactive(emptyForm())

const rules = {
  jobId: [{ required: true, message: '请选择兼职岗位', trigger: 'change' }],
  applicantName: [{ required: true, message: '请输入接单人姓名', trigger: 'blur' }]
}

function statusType(s) {
  return { '待处理': 'warning', '已接受': 'primary', '已完成': 'success', '已取消': 'info' }[s] || 'info'
}

async function load() {
  loading.value = true
  try {
    const res = await request.get('/orders', { params: query })
    rows.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

async function loadJobs() {
  const res = await request.get('/jobs', { params: { page: 1, size: 1000 } })
  jobOptions.value = res.data.rows
}

function search() { query.page = 1; load() }
function reset() { query.keyword = ''; query.status = ''; query.page = 1; load() }

function openAdd() {
  Object.assign(form, emptyForm())
  dialog.value = true
}
function openEdit(row) {
  Object.assign(form, {
    id: row.id, jobId: row.jobId, applicantName: row.applicantName,
    applicantPhone: row.applicantPhone, status: row.status, remark: row.remark
  })
  dialog.value = true
}

function save() {
  formRef.value.validate(async valid => {
    if (!valid) return
    saving.value = true
    try {
      if (form.id) {
        await request.put(`/orders/${form.id}`, form)
        ElMessage.success('修改成功')
      } else {
        await request.post('/orders', form)
        ElMessage.success('新增成功')
      }
      dialog.value = false
      load()
    } finally {
      saving.value = false
    }
  })
}

function remove(row) {
  ElMessageBox.confirm(`确认删除该接单记录吗？`, '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/orders/${row.id}`)
    ElMessage.success('删除成功')
    load()
  }).catch(() => {})
}

onMounted(() => { loadJobs(); load() })
</script>
