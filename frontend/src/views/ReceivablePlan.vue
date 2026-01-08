<template>
  <div class="ar-plan">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应收账计划</span>
          <div>
            <el-button 
              v-permission="{ moduleId: 'm021', action: 'add' }"
              type="primary" 
              @click="openPlanDialog()"
            >
              新增计划
            </el-button>
            <el-button type="success" @click="exportPlans">导出</el-button>
            <el-button @click="refresh">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="合同号">
          <el-input v-model="queryForm.contractCode" placeholder="请输入合同号" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="合同名称">
          <el-input v-model="queryForm.contractName" placeholder="请输入合同名称" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="应付时间">
          <el-date-picker
            v-model="queryForm.dueRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            v-permission="{ moduleId: 'm021', action: 'read' }"
            type="primary" 
            @click="handleQuery"
          >
            查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" border style="width: 100%" v-loading="loading">
        <el-table-column prop="contractCode" label="合同号" width="160" />
        <el-table-column prop="contractName" label="合同名称" width="200" show-overflow-tooltip />
        <el-table-column prop="paymentStageName" label="付款阶段名称" width="150" />
        <el-table-column prop="dueAmount" label="应付金额" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.dueAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.paidAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="应付时间" width="120" />
        <el-table-column prop="paidDate" label="付款日期" width="120" />
        <el-table-column prop="owner" label="责任人" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="remarks" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm021', action: 'update' }"
              type="warning" 
              size="small" 
              @click="openPlanDialog(scope.row)"
              :disabled="!scope.row.id"
            >
              修改
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm021', action: 'delete' }"
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
              :disabled="!scope.row.id"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="planDialogVisible" :title="planDialogTitle" width="600px">
      <el-form :model="planForm" label-width="120px" ref="planFormRef">
        <el-form-item label="合同号" required>
          <el-input v-model="planForm.contractCode" :disabled="!!planForm.id" />
        </el-form-item>
        <el-form-item label="合同名称" required>
          <el-input v-model="planForm.contractName" />
        </el-form-item>
        <el-form-item label="付款阶段名称">
          <el-input v-model="planForm.paymentStageName" />
        </el-form-item>
        <el-form-item label="应付金额" required>
          <el-input-number v-model="planForm.dueAmount" :min="0" :precision="2" :step="100" />
        </el-form-item>
        <el-form-item label="已付金额">
          <el-input-number 
            v-model="planForm.paidAmount" 
            :min="0" 
            :max="planForm.dueAmount || undefined"
            :precision="2" 
            :step="100" 
            style="width: 100%"
          />
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            已付金额不能超过应付金额
          </div>
        </el-form-item>
        <el-form-item label="应付时间">
          <el-date-picker v-model="planForm.dueDate" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="付款日期">
          <el-date-picker 
            v-model="planForm.paidDate" 
            type="date" 
            placeholder="选择日期" 
            :disabled-date="(date) => {
              if (!planForm.dueDate) return false
              const dueDate = new Date(planForm.dueDate)
              dueDate.setHours(0, 0, 0, 0)
              date.setHours(0, 0, 0, 0)
              return date < dueDate
            }"
            style="width: 100%"
          />
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            付款日期不能早于应付时间
          </div>
        </el-form-item>
        <el-form-item label="责任人">
          <el-input v-model="planForm.owner" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="planForm.remarks" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="planDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePlan">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getReceivablePlans,
  createReceivablePlan,
  updateReceivablePlan,
  deleteReceivablePlan,
  exportReceivablePlans
} from '../api/receivable'
import { canRead, loadPermissions } from '../utils/permission'

const loading = ref(false)
const list = ref([])

const queryForm = reactive({
  contractCode: '',
  contractName: '',
  dueRange: []
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const planDialogVisible = ref(false)
const planDialogTitle = ref('新增计划')
const planFormRef = ref(null)
const planForm = reactive({
  id: '',
  contractCode: '',
  contractName: '',
  paymentStageName: '',
  dueAmount: null,
  paidAmount: null,
  dueDate: '',
  paidDate: '',
  owner: '',
  remarks: ''
})

const formatMoney = (val) => {
  if (val === null || val === undefined) return '-'
  return `¥${Number(val).toFixed(2)}`
}

const formatDate = (val) => {
  if (!val) return ''
  const d = new Date(val)
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      contractCode: queryForm.contractCode,
      contractName: queryForm.contractName,
      startDueDate: formatDate(queryForm.dueRange?.[0]),
      endDueDate: formatDate(queryForm.dueRange?.[1]),
      page: pagination.page - 1,
      size: pagination.size
    }
    const res = await getReceivablePlans(params)
    const dataList = res.data?.content || []
    // 过滤掉没有ID的记录，并添加调试信息
    list.value = dataList.filter(item => {
      if (!item || !item.id) {
        console.warn('发现没有ID的记录:', item)
        return false
      }
      return true
    })
    pagination.total = res.data?.totalElements || 0
    console.log('加载的数据:', list.value.map(item => ({ id: item.id, contractCode: item.contractCode })))
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  queryForm.contractCode = ''
  queryForm.contractName = ''
  queryForm.dueRange = []
  handleQuery()
}

const refresh = () => {
  loadData()
}

const handleSizeChange = (val) => {
  pagination.size = val
  pagination.page = 1
  loadData()
}

const handlePageChange = (val) => {
  pagination.page = val
  loadData()
}

const openPlanDialog = (row) => {
  planDialogTitle.value = row ? '修改计划' : '新增计划'
  if (row) {
    // 修改：复制所有字段，确保 id 存在
    Object.assign(planForm, {
      id: row.id || null,
      contractCode: row.contractCode || '',
      contractName: row.contractName || '',
      paymentStageName: row.paymentStageName || '',
      dueAmount: row.dueAmount || null,
      paidAmount: row.paidAmount || 0,
      dueDate: row.dueDate || '',
      paidDate: row.paidDate || '',
      owner: row.owner || '',
      remarks: row.remarks || ''
    })
  } else {
    // 新增：重置所有字段
    Object.assign(planForm, {
      id: null,
      contractCode: '',
      contractName: '',
      paymentStageName: '',
      dueAmount: null,
      paidAmount: 0,
      dueDate: '',
      paidDate: '',
      owner: '',
      remarks: ''
    })
  }
  planDialogVisible.value = true
}

const savePlan = async () => {
  try {
    if (!planForm.contractCode || !planForm.contractName) {
      ElMessage.warning('请填写合同号和合同名称')
      return
    }
    if (!planForm.dueAmount || planForm.dueAmount <= 0) {
      ElMessage.warning('请填写应付金额')
      return
    }
    
    // 验证已付金额不能超过应付金额
    if (planForm.paidAmount != null && planForm.paidAmount > 0) {
      if (planForm.paidAmount > planForm.dueAmount) {
        ElMessage.warning('已付金额不能超过应付金额')
        return
      }
    }
    
    // 验证付款日期不能早于应付时间
    if (planForm.paidDate && planForm.dueDate) {
      const paidDateStr = typeof planForm.paidDate === 'string' ? planForm.paidDate : planForm.paidDate
      const dueDateStr = typeof planForm.dueDate === 'string' ? planForm.dueDate : planForm.dueDate
      const paidDate = new Date(paidDateStr)
      const dueDate = new Date(dueDateStr)
      // 只比较日期部分，忽略时间
      paidDate.setHours(0, 0, 0, 0)
      dueDate.setHours(0, 0, 0, 0)
      if (paidDate < dueDate) {
        ElMessage.warning('付款日期不能早于应付时间')
        return
      }
    }
    
    // 判断是新增还是修改
    const isEdit = planForm.id && planForm.id !== '' && planForm.id !== null && planForm.id !== undefined
    
    // 构建payload，包含所有可修改的字段
    const payload = {
      contractCode: planForm.contractCode,
      contractName: planForm.contractName,
      paymentStageName: planForm.paymentStageName,
      dueAmount: planForm.dueAmount,
      paidAmount: planForm.paidAmount || 0,
      dueDate: planForm.dueDate,
      paidDate: planForm.paidDate || null,
      owner: planForm.owner,
      remarks: planForm.remarks
    }
    
    if (isEdit) {
      // 修改：传递对象（不包含id，id在URL路径中）
      await updateReceivablePlan(planForm.id, payload)
    } else {
      // 新增：不传递 id
      await createReceivablePlan(payload)
    }
    ElMessage.success('保存成功')
    planDialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  }
}

const handleDelete = (row) => {
  console.log('删除操作 - row数据:', row)
  console.log('删除操作 - row.id:', row?.id)
  
  if (!row) {
    ElMessage.error('无法删除：记录数据不存在')
    return
  }
  
  if (!row.id || row.id === '' || row.id === null || row.id === undefined) {
    console.error('记录缺少ID:', row)
    ElMessage.error('无法删除：记录ID不存在，请刷新页面后重试')
    // 尝试重新加载数据
    loadData()
    return
  }
  
  ElMessageBox.confirm('确定要删除该应收账计划记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      console.log('执行删除，ID:', row.id)
      await deleteReceivablePlan(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (e) {
      console.error('删除失败:', e)
      ElMessage.error(e.message || e.msg || '删除失败')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

const exportPlans = async () => {
  try {
    const params = {
      contractCode: queryForm.contractCode,
      contractName: queryForm.contractName
    }
    const res = await exportReceivablePlans(params)
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'receivable_plan.xlsx'
    a.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    ElMessage.error(e.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m021')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看应收账计划的权限')
  }
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.query-form {
  margin-bottom: 20px;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>

