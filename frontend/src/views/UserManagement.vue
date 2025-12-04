<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>账号管理</span>
        </div>
      </template>
      
      <el-table :data="userList" border style="width: 100%">
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="idCard" label="身份证号" width="180" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="birthDate" label="出生日期" width="120" />
        <el-table-column prop="isApproved" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isApproved ? 'success' : 'warning'">
              {{ scope.row.isApproved ? '已审核' : '未审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleId" label="角色" width="150">
          <template #default="scope">
            <el-select
              v-model="scope.row.roleId"
              placeholder="选择角色"
              @change="handleRoleChange(scope.row)"
              style="width: 100%"
            >
              <el-option
                v-for="role in roleList"
                :key="role.id"
                :label="role.roleName"
                :value="role.id"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              v-if="!scope.row.isApproved"
              type="success"
              size="small"
              @click="handleApprove(scope.row)"
            >
              审核通过
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleResetPassword(scope.row)"
            >
              重置密码
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, approveUser, resetPassword, assignRole } from '../api/user'
import { getRoleList } from '../api/role'

const userList = ref([])
const roleList = ref([])

const loadData = async () => {
  try {
    const [userRes, roleRes] = await Promise.all([
      getUserList(),
      getRoleList()
    ])
    userList.value = userRes.data || []
    roleList.value = roleRes.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const handleApprove = async (row) => {
  try {
    await approveUser(row.id)
    ElMessage.success('审核通过')
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleResetPassword = async (row) => {
  ElMessageBox.confirm('确定要重置密码为123456吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await resetPassword(row.id)
      ElMessage.success('密码已重置为123456')
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    }
  })
}

const handleRoleChange = async (row) => {
  try {
    await assignRole(row.id, row.roleId)
    ElMessage.success('角色分配成功')
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
    loadData()
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-management {
  height: 100%;
}
</style>

