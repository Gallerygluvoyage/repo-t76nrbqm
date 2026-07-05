<template>
  <div class="auth-wrap">
    <div class="auth-card">
      <h2>注册账号</h2>
      <div class="subtitle">支持学生与雇主注册</div>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="账号" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="密码" :prefix-icon="Lock" size="large" />
        </el-form-item>
        <el-form-item prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" :prefix-icon="Avatar" size="large" />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="联系电话" :prefix-icon="Phone" size="large" />
        </el-form-item>
        <el-form-item prop="role">
          <el-radio-group v-model="form.role">
            <el-radio value="STUDENT">我是学生</el-radio>
            <el-radio value="EMPLOYER">我是雇主</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="submit">注 册</el-button>
      </el-form>
      <div class="switch-link">
        已有账号？<el-link type="primary" @click="$router.push('/login')">返回登录</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Avatar, Phone } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = ref({ username: '', password: '', realName: '', phone: '', role: 'STUDENT' })
const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

function submit() {
  formRef.value.validate(async valid => {
    if (!valid) return
    loading.value = true
    try {
      await request.post('/auth/register', form.value)
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } finally {
      loading.value = false
    }
  })
}
</script>
