<template>
  <div class="ar-plan">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应收账计划</span>
          <div>
            <el-button type="primary" @click="openPlanDialog()">新增计划</el-button>
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
          <el-button type="primary" @click="handleQuery">查询</el-button>
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
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button type="warning" size="small" @click="openPlanDialog(scope.row)">修改</el-button>
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
        <el-form-item label="应付时间">
          <el-date-picker v-model="planForm.dueDate" type="date" placeholder="选择日期" style="width: 100%" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getReceivablePlans,
  createReceivablePlan,
  updateReceivablePlan,
  exportReceivablePlans
} from '../api/receivable'

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
  dueDate: '',
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
    list.value = res.data?.content || []
    pagination.total = res.data?.totalElements || 0
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
  Object.assign(planForm, {
    id: row?.id || '',
    contractCode: row?.contractCode || '',
    contractName: row?.contractName || '',
    paymentStageName: row?.paymentStageName || '',
    dueAmount: row?.dueAmount || null,
    dueDate: row?.dueDate || '',
    owner: row?.owner || '',
    remarks: row?.remarks || ''
  })
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
    const payload = { ...planForm }
    if (planForm.id) {
      await updateReceivablePlan(planForm.id, payload)
    } else {
      await createReceivablePlan(payload)
    }
    ElMessage.success('保存成功')
    planDialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  }
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

onMounted(() => {
  loadData()
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

