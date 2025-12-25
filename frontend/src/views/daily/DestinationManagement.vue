<template>
  <div class="destination-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>去向管理</span>
          <div>
            <el-button 
              v-permission="{ moduleId: 'm024', action: 'add' }"
              type="primary" 
              @click="handleAdd"
            >
              新增去向
            </el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="营销活动名称">
          <el-input v-model="queryForm.activityName" placeholder="请输入营销活动名称" clearable />
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="queryForm.location" placeholder="请输入地点" clearable />
        </el-form-item>
        <el-form-item>
          <el-button 
            v-permission="{ moduleId: 'm024', action: 'read' }"
            type="primary" 
            @click="handleQuery"
          >
            查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 去向列表 -->
      <el-table :data="filteredList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="destinationTime" label="时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.destinationTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="activityName" label="营销活动名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="location" label="地点" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm024', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm024', action: 'delete' }"
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 去向看板 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <span>去向看板</span>
      </template>
      <div class="dashboard-container">
        <div class="dashboard-item" v-for="item in dashboardData" :key="item.location">
          <div class="location-name">{{ item.location }}</div>
          <div class="person-count">{{ item.count }}人</div>
          <div class="person-list">
            <el-tag v-for="person in item.persons" :key="person.userId" style="margin: 2px">
              {{ person.activityName }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="时间" prop="destinationTime">
          <el-date-picker
            v-model="form.destinationTime"
            type="datetime"
            placeholder="选择日期时间"
            format="YYYY/MM/DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="营销活动名称" prop="activityName">
          <el-input v-model="form.activityName" placeholder="请输入营销活动名称" />
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入地点" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { canRead, loadPermissions } from '../../utils/permission'
import {
  getDailyDestinationList,
  saveDailyDestination,
  updateDailyDestination,
  deleteDailyDestination,
  exportDailyDestinations
} from '../../api/daily-destination'
import { getToken } from '../../utils/auth'

const destinationList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增去向')
const formRef = ref(null)

const queryForm = ref({
  activityName: '',
  location: ''
})

const form = ref({
  id: null,
  userId: '',
  destinationTime: null,
  activityName: '',
  location: ''
})

const rules = {
  destinationTime: [{ required: true, message: '请选择时间', trigger: 'change' }],
  activityName: [{ required: true, message: '请输入营销活动名称', trigger: 'blur' }],
  location: [{ required: true, message: '请输入地点', trigger: 'blur' }]
}

// 从token中解析用户ID（简化处理，实际应该从后端获取）
const getCurrentUserId = () => {
  // 后端会自动从token中获取当前用户ID，这里可以返回空字符串
  return ''
}

// 过滤列表
const filteredList = computed(() => {
  let list = destinationList.value
  if (queryForm.value.activityName) {
    list = list.filter(item => item.activityName.includes(queryForm.value.activityName))
  }
  if (queryForm.value.location) {
    list = list.filter(item => item.location.includes(queryForm.value.location))
  }
  return list
})

// 看板数据
const dashboardData = computed(() => {
  const map = new Map()
  destinationList.value.forEach(item => {
    if (!map.has(item.location)) {
      map.set(item.location, {
        location: item.location,
        count: 0,
        persons: []
      })
    }
    const data = map.get(item.location)
    data.count++
    data.persons.push({
      userId: item.userId,
      activityName: item.activityName
    })
  })
  return Array.from(map.values())
})

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDailyDestinationList()
    destinationList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增去向'
  dialogVisible.value = true
  resetForm()
  form.value.userId = getCurrentUserId()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑去向'
  dialogVisible.value = true
  form.value = {
    id: row.id,
    userId: row.userId,
    destinationTime: row.destinationTime ? row.destinationTime.replace('T', ' ') : null,
    activityName: row.activityName,
    location: row.location
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该去向记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDailyDestination(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.msg || '删除失败')
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData = {
          ...form.value
        }
        // 确保新增时id为null或undefined
        if (!submitData.id || submitData.id === '') {
          delete submitData.id
        }
        if (form.value.id && form.value.id !== '') {
          await updateDailyDestination(submitData)
          ElMessage.success('更新成功')
        } else {
          await saveDailyDestination(submitData)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.msg || error.message || '操作失败')
      }
    }
  })
}

const handleQuery = () => {
  // 查询逻辑已在computed中实现
}

const handleReset = () => {
  queryForm.value = {
    activityName: '',
    location: ''
  }
}

const handleExport = async () => {
  try {
    const res = await exportDailyDestinations()
    const blob = new Blob([res.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '去向管理列表.xlsx'
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const resetForm = () => {
  form.value = {
    id: null,
    userId: '',
    destinationTime: null,
    activityName: '',
    location: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m024')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看去向管理的权限')
  }
})
</script>

<style scoped>
.destination-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.query-form {
  margin-bottom: 20px;
}

.dashboard-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.dashboard-item {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  min-width: 200px;
  background-color: #f5f7fa;
}

.location-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #409eff;
}

.person-count {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.person-list {
  margin-top: 10px;
}
</style>

