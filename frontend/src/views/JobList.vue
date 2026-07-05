<template>
  <div class="page-card">
    <h3 class="page-title">兼职信息管理</h3>
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="搜索标题/单位/地点" clearable style="width:220px" @keyup.enter="search" />
      <el-select v-model="query.category" placeholder="全部分类" clearable style="width:130px">
        <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
      </el-select>
      <el-select v-model="query.status" placeholder="全部状态" clearable style="width:120px">
        <el-option label="招聘中" value="招聘中" />
        <el-option label="已结束" value="已结束" />
      </el-select>
      <el-button type="primary" :icon="Search" @click="search">查询</el-button>
      <el-button :icon="Refresh" @click="reset">重置</el-button>
      <div class="spacer"></div>
      <el-button type="primary" :icon="Plus" @click="openAdd">新增兼职</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column type="index" label="#" width="55" />
      <el-table-column prop="title" label="兼职标题" min-width="160" />
      <el-table-column prop="category" label="分类" width="90" />
      <el-table-column prop="company" label="发布单位" min-width="120" />
      <el-table-column prop="location" label="工作地点" min-width="120" />
      <el-table-column label="薪资" width="120">
        <template #default="{ row }">{{ row.salary }} {{ row.salaryUnit }}</template>
      </el-table-column>
      <el-table-column prop="headcount" label="人数" width="70" />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === '招聘中' ? 'success' : 'info'" effect="plain">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
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

    <el-dialog v-model="dialog" :title="form.id ? '编辑兼职' : '新增兼职'" width="560px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="兼职标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入兼职标题" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类" style="width:100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布单位" prop="company">
          <el-input v-model="form.company" placeholder="发布单位/商家" />
        </el-form-item>
        <el-form-item label="工作地点" prop="location">
          <el-input v-model="form.location" placeholder="工作地点" />
        </el-form-item>
        <el-form-item label="薪资" prop="salary">
          <el-input v-model.number="form.salary" style="width:150px" />
          <el-select v-model="form.salaryUnit" style="width:120px;margin-left:10px">
            <el-option label="元/时" value="元/时" />
            <el-option label="元/天" value="元/天" />
            <el-option label="元/月" value="元/月" />
            <el-option label="元/次" value="元/次" />
          </el-select>
        </el-form-item>
        <el-form-item label="招聘人数" prop="headcount">
          <el-input-number v-model="form.headcount" :min="1" :max="999" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="联系电话/微信" />
        </el-form-item>
        <el-form-item label="岗位描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="岗位描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="招聘中">招聘中</el-radio>
            <el-radio value="已结束">已结束</el-radio>
          </el-radio-group>
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

const categories = ['家教', '餐饮', '体力', '活动', '文职', '技术', '其他']
const rows = ref([])
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const dialog = ref(false)
const formRef = ref()
const query = reactive({ keyword: '', category: '', status: '', page: 1, size: 8 })

const emptyForm = () => ({
  id: null, title: '', category: '家教', company: '', location: '',
  salary: 20, salaryUnit: '元/时', headcount: 1, contact: '', description: '', status: '招聘中'
})
const form = reactive(emptyForm())

const rules = {
  title: [{ required: true, message: '请输入兼职标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

async function load() {
  loading.value = true
  try {
    const res = await request.get('/jobs', { params: query })
    rows.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function search() { query.page = 1; load() }
function reset() { query.keyword = ''; query.category = ''; query.status = ''; query.page = 1; load() }

function openAdd() {
  Object.assign(form, emptyForm())
  dialog.value = true
}
function openEdit(row) {
  Object.assign(form, row)
  dialog.value = true
}

function save() {
  formRef.value.validate(async valid => {
    if (!valid) return
    saving.value = true
    try {
      if (form.id) {
        await request.put(`/jobs/${form.id}`, form)
        ElMessage.success('修改成功')
      } else {
        await request.post('/jobs', form)
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
  ElMessageBox.confirm(`确认删除兼职「${row.title}」吗？`, '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/jobs/${row.id}`)
    ElMessage.success('删除成功')
    load()
  }).catch(() => {})
}

onMounted(load)
</script>
