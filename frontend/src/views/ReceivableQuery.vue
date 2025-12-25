<template>
  <div class="ar-query">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应收账查询</span>
          <div>
            <el-button type="success" @click="exportSummaryList">导出</el-button>
            <el-button @click="refresh">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="合同号">
          <el-input v-model="queryForm.contractCode" placeholder="请输入合同号" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入公司名称" style="width: 200px" clearable />
        </el-form-item>
        <el-form-item>
          <el-button 
            v-permission="{ moduleId: 'm023', action: 'read' }"
            type="primary" 
            @click="handleQuery"
          >
            查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" border style="width: 100%" v-loading="loading">
        <el-table-column prop="planId" label="应收账编号" width="220" show-overflow-tooltip />
        <el-table-column prop="contractCode" label="合同号" width="150" />
        <el-table-column prop="contractName" label="合同名称" width="200" show-overflow-tooltip />
        <el-table-column prop="companyName" label="公司名称" width="180" show-overflow-tooltip />
        <el-table-column prop="companyCode" label="公司编号" width="140" />
        <el-table-column prop="item" label="款项" width="150" />
        <el-table-column prop="planAmount" label="计划金额" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.planAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际到款" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.actualAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="planDate" label="计划收款时间" width="140" />
        <el-table-column prop="actualDate" label="实际收款时间" width="140" />
        <el-table-column prop="status" label="应收状态" width="120" />
        <el-table-column prop="deviation" label="偏差金额" width="120">
          <template #default="scope">
            {{ formatMoney(scope.row.deviation) }}
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getReceivableSummary, exportReceivableSummary } from '../api/receivable'
import { canRead, loadPermissions } from '../utils/permission'

const loading = ref(false)
const list = ref([])

const queryForm = reactive({
  contractCode: '',
  companyName: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const formatMoney = (val) => {
  if (val === null || val === undefined) return '-'
  return `¥${Number(val).toFixed(2)}`
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      contractCode: queryForm.contractCode,
      companyName: queryForm.companyName,
      page: pagination.page - 1,
      size: pagination.size
    }
    const res = await getReceivableSummary(params)
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
  queryForm.companyName = ''
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

const exportSummaryList = async () => {
  try {
    const params = {
      contractCode: queryForm.contractCode,
      companyName: queryForm.companyName
    }
    const res = await exportReceivableSummary(params)
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'receivable_summary.xlsx'
    a.click()
    window.URL.revokeObjectURL(url)
  } catch (e) {
    ElMessage.error(e.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m023')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看应收账查询的权限')
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

