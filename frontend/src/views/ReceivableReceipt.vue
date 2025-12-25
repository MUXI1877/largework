<template>
  <div class="ar-receipt">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>回款登记</span>
          <div>
            <el-button 
              v-permission="{ moduleId: 'm022', action: 'add' }"
              type="primary" 
              @click="openDialog()"
            >
              新增回款
            </el-button>
            <el-button type="success" @click="exportReceiptsList">导出</el-button>
            <el-button @click="refresh">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="合同号">
          <el-input v-model="queryForm.contractCode" placeholder="请输入合同号" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="到款时间">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            v-permission="{ moduleId: 'm022', action: 'read' }"
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
        <el-table-column prop="companyName" label="公司名称" width="200" show-overflow-tooltip />
        <el-table-column prop="companyCode" label="公司编号" width="140" />
        <el-table-column prop="receiveDate" label="到款时间" width="140" />
        <el-table-column prop="receiveAmount" label="到款金额" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.receiveAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="剩余金额" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.remainingAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm022', action: 'update' }"
              type="warning" 
              size="small" 
              @click="openDialog(scope.row)"
            >
              备注
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm022', action: 'delete' }"
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="120px" ref="formRef">
        <el-form-item label="合同号" required>
          <el-input v-model="form.contractCode" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="合同名称" required>
          <el-input v-model="form.contractName" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="合同总额">
          <el-input-number v-model="form.contractAmount" :min="0" :precision="2" :step="1000" />
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input v-model="form.companyName" />
        </el-form-item>
        <el-form-item label="公司编号">
          <el-input v-model="form.companyCode" />
        </el-form-item>
        <el-form-item label="到款时间">
          <el-date-picker v-model="form.receiveDate" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="到款金额" required>
          <el-input-number v-model="form.receiveAmount" :min="0" :precision="2" :step="1000" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { canRead, loadPermissions } from '../utils/permission'
import {
  getReceivableReceipts,
  createReceivableReceipt,
  updateReceiptRemark,
  deleteReceivableReceipt,
  exportReceivableReceipts
} from '../api/receivable'

const loading = ref(false)
const list = ref([])

const queryForm = reactive({
  contractCode: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增回款')
const formRef = ref(null)
const form = reactive({
  id: '',
  contractCode: '',
  contractName: '',
  contractAmount: null,
  companyName: '',
  companyCode: '',
  receiveDate: '',
  receiveAmount: null,
  remarks: '',
  remainingAmount: null
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
      startDate: formatDate(queryForm.dateRange?.[0]),
      endDate: formatDate(queryForm.dateRange?.[1]),
      page: pagination.page - 1,
      size: pagination.size
    }
    const res = await getReceivableReceipts(params)
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
  queryForm.dateRange = []
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

const openDialog = (row) => {
  dialogTitle.value = row ? '修改备注' : '新增回款'
  Object.assign(form, {
    id: row?.id || null,
    contractCode: row?.contractCode || '',
    contractName: row?.contractName || '',
    contractAmount: row?.contractAmount || null,
    companyName: row?.companyName || '',
    companyCode: row?.companyCode || '',
    receiveDate: row?.receiveDate || '',
    receiveAmount: row?.receiveAmount || null,
    remarks: row?.remarks || '',
    remainingAmount: row?.remainingAmount || null
  })
  dialogVisible.value = true
}

const save = async () => {
  try {
    if (!form.contractCode || !form.contractName) {
      ElMessage.warning('请填写合同号和合同名称')
      return
    }
    if (!form.receiveAmount || form.receiveAmount <= 0) {
      ElMessage.warning('请填写到款金额')
      return
    }
    const payload = { ...form }
    // 新增时，确保id为null或undefined，不发送id字段
    if (!form.id || form.id === '') {
      delete payload.id
      await createReceivableReceipt(payload)
    } else {
      await updateReceiptRemark(form.id, { remarks: form.remarks })
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.message || '保存失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该回款记录吗？删除后将会重新计算应收账计划状态。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteReceivableReceipt(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (e) {
      ElMessage.error(e.message || e.msg || '删除失败')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

const exportReceiptsList = async () => {
  try {
    const params = { contractCode: queryForm.contractCode }
    const res = await exportReceivableReceipts(params)
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'receivable_receipt.xlsx'
    a.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    ElMessage.error(e.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m022')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看回款登记的权限')
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

