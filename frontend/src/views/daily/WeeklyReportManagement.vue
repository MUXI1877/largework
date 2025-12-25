<template>
  <div class="weekly-report-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>周报管理</span>
          <div>
            <el-button 
              v-permission="{ moduleId: 'm025', action: 'add' }"
              type="primary" 
              @click="handleAdd" 
              v-if="!isLeader"
            >
              新建周报
            </el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="周报名称">
          <el-input v-model="queryForm.reportName" placeholder="请输入周报名称" clearable />
        </el-form-item>
        <el-form-item label="状态" v-if="isLeader">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft" />
            <el-option label="已提交" value="submitted" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button 
            v-permission="{ moduleId: 'm025', action: 'read' }"
            type="primary" 
            @click="handleQuery"
          >
            查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 周报列表 -->
      <el-table :data="filteredList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="reportTime" label="周报时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.reportTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="reportName" label="周报名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="reportContent" label="周报内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="reportRemark" label="周报备注" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'submitted' ? 'success' : 'info'">
              {{ scope.row.status === 'submitted' ? '已提交' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button
              v-permission="{ moduleId: 'm025', action: 'update' }"
              v-if="!isLeader && scope.row.status === 'draft'"
              type="warning"
              size="small"
              @click="handleEdit(scope.row)"
            >
              修改
            </el-button>
            <el-button
              v-permission="{ moduleId: 'm025', action: 'update' }"
              v-if="!isLeader && scope.row.status === 'draft'"
              type="success"
              size="small"
              @click="handleSubmit(scope.row)"
            >
              提交
            </el-button>
            <el-button type="info" size="small" @click="handlePrint(scope.row)">打印</el-button>
            <el-button
              v-permission="{ moduleId: 'm025', action: 'delete' }"
              v-if="!isLeader && scope.row.status === 'draft'"
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @closed="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="周报名称" prop="reportName">
          <el-input v-model="form.reportName" placeholder="请输入周报名称" clearable />
        </el-form-item>
        <el-form-item label="周报时间" prop="reportTime">
          <el-date-picker
            v-model="form.reportTime"
            type="datetime"
            placeholder="选择日期时间"
            format="YYYY/MM/DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="周报内容" prop="reportContent">
          <el-input
            v-model="form.reportContent"
            type="textarea"
            :rows="6"
            placeholder="请输入周报内容"
            clearable
          />
        </el-form-item>
        <el-form-item label="周报备注">
          <el-input
            v-model="form.reportRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入周报备注"
            clearable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">返回</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="周报详情"
      width="700px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="周报时间">
          {{ formatDateTime(viewData.reportTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="周报名称">
          {{ viewData.reportName }}
        </el-descriptions-item>
        <el-descriptions-item label="周报内容">
          {{ viewData.reportContent }}
        </el-descriptions-item>
        <el-descriptions-item label="周报备注">
          {{ viewData.reportRemark || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.status === 'submitted' ? 'success' : 'info'">
            {{ viewData.status === 'submitted' ? '已提交' : '草稿' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handlePrint(viewData)">打印</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { canRead, loadPermissions } from '../../utils/permission'
import {
  getWeeklyReportList,
  saveWeeklyReport,
  updateWeeklyReport,
  submitWeeklyReport,
  deleteWeeklyReport,
  exportWeeklyReports
} from '../../api/weekly-report'
import { getToken } from '../../utils/auth'

const reportList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('新建周报')
const formRef = ref(null)

// 判断是否为领导（简化处理，实际应该从权限系统获取）
const isLeader = ref(false)

const queryForm = ref({
  reportName: '',
  status: ''
})

const form = ref({
  id: null,
  userId: '',
  reportName: '',
  reportTime: null,
  reportContent: '',
  reportRemark: '',
  status: 'draft'
})

const viewData = ref({})

const rules = {
  reportName: [{ required: true, message: '请输入周报名称', trigger: 'blur' }],
  reportTime: [{ required: true, message: '请选择周报时间', trigger: 'change' }],
  reportContent: [{ required: true, message: '请输入周报内容', trigger: 'blur' }]
}

// 从token中解析用户ID（简化处理，实际应该从后端获取）
const getCurrentUserId = () => {
  // 后端会自动从token中获取当前用户ID，这里可以返回空字符串
  return ''
}

// 过滤列表
const filteredList = computed(() => {
  let list = reportList.value
  
  // 后端已经根据角色返回相应的数据，这里只需要做前端过滤
  if (queryForm.value.reportName) {
    list = list.filter(item => item.reportName.includes(queryForm.value.reportName))
  }
  if (queryForm.value.status) {
    list = list.filter(item => item.status === queryForm.value.status)
  }
  return list
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
    // 如果是领导，传"all"获取所有记录；如果是员工，不传参数，后端自动返回当前用户的记录
    const res = await getWeeklyReportList(isLeader.value ? 'all' : undefined)
    reportList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新建周报'
  // 先重置表单
  form.value = {
    id: null,
    userId: '',
    reportName: '',
    reportTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
    reportContent: '',
    reportRemark: '',
    status: 'draft'
  }
  // 重置表单验证
  if (formRef.value) {
    formRef.value.resetFields()
  }
  // 打开对话框
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '修改周报'
  dialogVisible.value = true
  form.value = {
    id: row.id,
    userId: row.userId,
    reportName: row.reportName,
    reportTime: row.reportTime ? row.reportTime.replace('T', ' ') : null,
    reportContent: row.reportContent || '',
    reportRemark: row.reportRemark || '',
    status: row.status
  }
}

const handleView = (row) => {
  viewDialogVisible.value = true
  viewData.value = { ...row }
}

const handleSubmit = (row) => {
  ElMessageBox.confirm('确定要提交该周报吗？提交后无法修改。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await submitWeeklyReport(row.id)
      ElMessage.success('提交成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.msg || '提交失败')
    }
  })
}

const handleSave = async () => {
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
          await updateWeeklyReport(submitData)
          ElMessage.success('更新成功')
        } else {
          await saveWeeklyReport(submitData)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.msg || error.message || '操作失败')
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该周报吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteWeeklyReport(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.msg || '删除失败')
    }
  })
}

const handlePrint = (row) => {
  const printWindow = window.open('', '_blank')
  const printContent = `
    <html>
      <head>
        <title>周报打印</title>
        <style>
          body { font-family: Arial, sans-serif; padding: 20px; }
          h1 { text-align: center; }
          .info { margin: 20px 0; }
          .label { font-weight: bold; }
          .content { margin-top: 10px; white-space: pre-wrap; }
        </style>
      </head>
      <body>
        <h1>周报</h1>
        <div class="info">
          <div><span class="label">周报时间：</span>${formatDateTime(row.reportTime)}</div>
          <div><span class="label">周报名称：</span>${row.reportName}</div>
          <div><span class="label">周报内容：</span></div>
          <div class="content">${row.reportContent || ''}</div>
          <div><span class="label">周报备注：</span></div>
          <div class="content">${row.reportRemark || '无'}</div>
        </div>
      </body>
    </html>
  `
  printWindow.document.write(printContent)
  printWindow.document.close()
  printWindow.print()
}

const handleQuery = () => {
  // 查询逻辑已在computed中实现
}

const handleReset = () => {
  queryForm.value = {
    reportName: '',
    status: ''
  }
}

const handleExport = async () => {
  try {
    const userId = isLeader.value ? null : getCurrentUserId()
    const res = await exportWeeklyReports(userId)
    const blob = new Blob([res.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '周报列表.xlsx'
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
    reportName: '',
    reportTime: null,
    reportContent: '',
    reportRemark: '',
    status: 'draft'
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m025')) {
    // 判断是否为领导（简化处理，实际应该从权限系统获取）
    // 这里可以根据角色ID或其他方式判断
    // 默认不是领导，如果需要判断，可以从后端获取用户角色信息
    isLeader.value = false
    
    loadData()
  } else {
    ElMessage.warning('您没有查看周报管理的权限')
  }
})
</script>

<style scoped>
.weekly-report-management {
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
</style>

