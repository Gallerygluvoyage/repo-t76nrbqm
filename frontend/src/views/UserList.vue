<template>
  <div class="page-card">
    <h3 class="page-title">用户管理</h3>
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="搜索账号/姓名/电话" clearable style="width:220px" @keyup.enter="search" />
      <el-select v-model="query.role" placeholder="全部角色" clearable style="width:130px">
        <el-option label="管理员" value="ADMIN" />
        <el-option label="雇主" value="EMPLOYER" />
        <el-option label="学生" value="STUDENT" />
      </el-select>
      <el-button type="primary" :icon="Search" @click="search">查询</el-button>
      <el-button :icon="Refresh" @click="reset">重置</el-button>
      <div class="spacer"></div>
      <el-button type="primary" :icon="Plus" @click="openAdd">新增用户</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column type="index" label="#" width="55" />
      <el-table-column prop="username" label="账号" min-width="120" />
      <el-table-column prop="realName" label="真实姓名" min-width="120" />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="roleType(row.role)" effect="plain">{{ roleText(row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" width="140" />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === '正常' ? 'success' : 'danger'" effect="plain">{{ row.status }}</el-tag>
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

    <el-dialog v-model="dialog" :title="form.id ? '编辑用户' : '新增用户'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="登录账号" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="留空默认为123456" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" style="width:100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="雇主" value="EMPLOYER" />
            <el-option label="学生" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="正常">正常</el-radio>
            <el-radio value="禁用">禁用</el-radio>
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

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const dialog = ref(false)
const formRef = ref()
const query = reactive({ keyword: '', role: '', page: 1, size: 8 })

const emptyForm = () => ({
  id: null, username: '', password: '', realName: '', role: 'STUDENT', phone: '', status: '正常'
})
const form = reactive(emptyForm())

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const roleMap = { ADMIN: '管理员', EMPLOYER: '雇主', STUDENT: '学生' }
function roleText(r) { return roleMap[r] || r }
function roleType(r) { return { ADMIN: 'danger', EMPLOYER: 'primary', STUDENT: 'success' }[r] || 'info' }

async function load() {
  loading.value = true
  try {
    const res = await request.get('/users', { params: query })
    rows.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function search() { query.page = 1; load() }
function reset() { query.keyword = ''; query.role = ''; query.page = 1; load() }

function openAdd() {
  Object.assign(form, emptyForm())
  dialog.value = true
}
function openEdit(row) {
  Object.assign(form, { ...row, password: '' })
  dialog.value = true
}

function save() {
  formRef.value.validate(async valid => {
    if (!valid) return
    saving.value = true
    try {
      if (form.id) {
        await request.put(`/users/${form.id}`, form)
        ElMessage.success('修改成功')
      } else {
        await request.post('/users', form)
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
  if (row.role === 'ADMIN') {
    ElMessage.warning('管理员账号不可删除')
    return
  }
  ElMessageBox.confirm(`确认删除用户「${row.username}」吗？`, '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/users/${row.id}`)
    ElMessage.success('删除成功')
    load()
  }).catch(() => {})
}

onMounted(load)
</script>
