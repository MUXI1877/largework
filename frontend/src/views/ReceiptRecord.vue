<template>
  <div class="receipt-record">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收款记录</span>
        </div>
      </template>

      <div class="table-toolbar">
        <el-input
          v-model="searchForm.receivableId"
          placeholder="应收ID"
          clearable
          style="width: 200px; margin-right: 10px"
          @keyup.enter="handleSearch"
        />
        <el-select
          v-model="searchForm.method"
          placeholder="收款方式"
          clearable
          style="width: 160px; margin-right: 10px"
        >
          <el-option label="银行转账" value="银行转账" />
          <el-option label="现金" value="现金" />
          <el-option label="支付宝" value="支付宝" />
          <el-option label="微信" value="微信" />
        </el-select>
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 320px; margin-right: 10px"
        />
        <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
        <el-button type="default" size="small" @click="handleReset">重置</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="receivable.id" label="应收ID" width="220">
          <template #default="scope">
            {{ scope.row.receivable ? scope.row.receivable.id : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="收款金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.amount) }}
          </template>
        </el-table-column>
        <el-table-column prop="receiptDate" label="收款日期" width="140" />
        <el-table-column prop="receiptMethod" label="收款方式" width="120" />
        <el-table-column prop="operator.name" label="操作人" width="120">
          <template #default="scope">
            {{ scope.row.operator ? scope.row.operator.name : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      </el-table>

      <div class="table-pagination">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getReceiptRecordList,
  getReceiptRecordListByReceivableId,
  getReceiptRecordListByDateRange,
  getReceiptRecordListByReceiptMethod
} from '../api/receivable'

const rawData = ref([])
const tableData = ref([])

const searchForm = ref({
  receivableId: '',
  method: '',
  dateRange: []
})

const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const formatCurrency = (value) => {
  const n = Number(value || 0)
  return n.toFixed(2)
}

const fetchAll = async () => {
  try {
    const res = await getReceiptRecordList()
    rawData.value = res.data || []
    applyFilterAndPage()
  } catch (e) {
    ElMessage.error('加载收款记录失败')
  }
}

const applyFilterAndPage = () => {
  let list = [...rawData.value]

  if (searchForm.value.receivableId) {
    list = list.filter(
      (item) =>
        item.receivable &&
        item.receivable.id &&
        item.receivable.id.includes(searchForm.value.receivableId)
    )
  }

  if (searchForm.value.method) {
    list = list.filter((item) => item.receiptMethod === searchForm.value.method)
  }

  if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
    const [start, end] = searchForm.value.dateRange
    list = list.filter((item) => {
      const d = new Date(item.receiptDate)
      return d >= start && d <= end
    })
  }

  pagination.value.total = list.length
  const startIndex = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const endIndex = startIndex + pagination.value.pageSize
  tableData.value = list.slice(startIndex, endIndex)
}

const handleSearch = async () => {
  // 为了简单，统一先加载全部，再在前端过滤
  await fetchAll()
}

const handleReset = () => {
  searchForm.value = {
    receivableId: '',
    method: '',
    dateRange: []
  }
  pagination.value.currentPage = 1
  applyFilterAndPage()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  applyFilterAndPage()
}

const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  applyFilterAndPage()
}

onMounted(() => {
  fetchAll()
})
</script>

<style scoped>
.receipt-record {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-toolbar {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.table-pagination {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>


