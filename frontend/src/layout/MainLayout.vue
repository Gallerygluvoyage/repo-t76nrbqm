<template>
  <el-container style="height: 100%">
    <div class="app-header">
      <div class="logo">校园兼职信息发布与接单管理系统</div>
      <div class="user-box">
        <el-icon><UserFilled /></el-icon>
        <span>{{ user.realName || user.username }}（{{ roleText }}）</span>
        <el-button link style="color:#cbd1d7" @click="logout">退出登录</el-button>
      </div>
    </div>
    <el-container>
      <el-aside width="200px" class="app-aside">
        <el-menu :default-active="activePath" router>
          <el-menu-item index="/dashboard">
            <el-icon><DataBoard /></el-icon><span>首页概览</span>
          </el-menu-item>
          <el-menu-item index="/jobs">
            <el-icon><Briefcase /></el-icon><span>兼职信息管理</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <el-icon><Tickets /></el-icon><span>接单订单管理</span>
          </el-menu-item>
          <el-menu-item v-if="user.role === 'ADMIN'" index="/users">
            <el-icon><User /></el-icon><span>用户管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="app-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const activePath = computed(() => route.path)
const user = JSON.parse(localStorage.getItem('user') || '{}')

const roleMap = { ADMIN: '管理员', EMPLOYER: '雇主', STUDENT: '学生' }
const roleText = computed(() => roleMap[user.role] || '用户')

function logout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' }).then(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  }).catch(() => {})
}
</script>
