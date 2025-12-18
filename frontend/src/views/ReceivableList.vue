<template>
  <div class="receivable-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应收账列表</span>
        </div>
      </template>

      <div class="table-toolbar">
        <el-input
          v-model="searchForm.orderId"
          placeholder="订单ID"
          clearable
          style="width: 200px; margin-right: 10px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        <el-select
          v-model="searchForm.status"
          placeholder="状态"
          style="width: 140px; margin-right: 10px"
        >
          <el-option label="全部" value="" />
          <el-option label="未结清" value="未结清" />
          <el-option label="部分结清" value="部分结清" />
          <el-option label="已结清" value="已结清" />
        </el-select>
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="到期开始"
          end-placeholder="到期结束"
          style="width: 320px; margin-right: 10px"
        />
        <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
        <el-button type="default" size="small" @click="handleReset">重置</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="order.id" label="订单ID" width="200">
          <template #default="scope">
            {{ scope.row.order ? scope.row.order.id : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="应收金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.amount) }}
          </template>
        </el-table-column>
        <el-table-column prop="receivedAmount" label="已收金额" width="120" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.receivedAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日" width="130" />
        <el-table-column prop="status" label="状态" width="110" align="center">
          <template #default="scope">
            <el-tag :type="statusTagType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right" align="center">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="openDetail(scope.row)"
              style="margin-right: 5px"
            >
              详情
            </el-button>
            <el-button
              type="success"
              size="small"
              @click="openReceipt(scope.row)"
              style="margin-right: 5px"
            >
              收款
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
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

    <!-- 收款登记弹窗 -->
    <el-dialog
      v-model="receiptDialogVisible"
      title="收款登记"
      width="600px"
      @close="resetReceiptForm"
    >
      <el-form :model="receiptForm" label-width="120px">
        <el-form-item label="应收ID">
          <el-input v-model="receiptForm.receivableId" disabled />
        </el-form-item>
        <el-form-item label="本次收款金额">
          <el-input-number
            v-model="receiptForm.amount"
            :min="0"
            :step="0.01"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="收款日期">
          <el-date-picker
            v-model="receiptForm.receiptDate"
            type="date"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="收款方式">
          <el-select v-model="receiptForm.receiptMethod" placeholder="请选择收款方式">
            <el-option label="银行转账" value="银行转账" />
            <el-option label="现金" value="现金" />
            <el-option label="支付宝" value="支付宝" />
            <el-option label="微信" value="微信" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="receiptForm.remark" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="receiptDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReceipt">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import {
  getReceivableList,
  getReceivableListByStatus,
  getReceivableListByDateRange,
  deleteReceivable,
  saveReceiptRecord
} from '../api/receivable'

const rawData = ref([])
const tableData = ref([])

const searchForm = ref({
  orderId: '',
  status: '',
  dateRange: []
})

const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const receiptDialogVisible = ref(false)
const receiptForm = ref({
  receivableId: '',
  amount: 0,
  receiptDate: '',
  receiptMethod: '',
  remark: ''
})

const formatCurrency = (value) => {
  const n = Number(value || 0)
  return n.toFixed(2)
}

const statusTagType = (status) => {
  if (status === '已结清') return 'success'
  if (status === '部分结清') return 'warning'
  if (status === '未结清') return 'danger'
  return 'info'
}

const fetchAll = async () => {
  try {
    const res = await getReceivableList()
    rawData.value = res.data || []
    applyFilterAndPage()
  } catch (e) {
    ElMessage.error('加载应收数据失败')
  }
}

const applyFilterAndPage = () => {
  let list = [...rawData.value]

  if (searchForm.value.status) {
    list = list.filter((item) => item.status === searchForm.value.status)
  }

  if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
    const [start, end] = searchForm.value.dateRange
    list = list.filter((item) => {
      const d = new Date(item.dueDate)
      return d >= start && d <= end
    })
  }

  if (searchForm.value.orderId) {
    list = list.filter(
      (item) => item.order && item.order.id && item.order.id.includes(searchForm.value.orderId)
    )
  }

  pagination.value.total = list.length
  const startIndex = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const endIndex = startIndex + pagination.value.pageSize
  tableData.value = list.slice(startIndex, endIndex)
}

const handleSearch = () => {
  pagination.value.currentPage = 1
  applyFilterAndPage()
}

const handleReset = () => {
  searchForm.value = {
    orderId: '',
    status: '',
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

const openDetail = (row) => {
  ElMessage.info(`应收ID：${row.id}，详细信息可在后续扩展页面中查看`)
}

const openReceipt = (row) => {
  receiptForm.value = {
    receivableId: row.id,
    amount: 0,
    receiptDate: new Date(),
    receiptMethod: '',
    remark: ''
  }
  receiptDialogVisible.value = true
}

const resetReceiptForm = () => {
  receiptForm.value = {
    receivableId: '',
    amount: 0,
    receiptDate: '',
    receiptMethod: '',
    remark: ''
  }
}

const submitReceipt = async () => {
  try {
    if (!receiptForm.value.receivableId || !receiptForm.value.amount) {
      ElMessage.warning('请填写完整收款信息')
      return
    }
    await saveReceiptRecord({
      receivable: { id: receiptForm.value.receivableId },
      amount: receiptForm.value.amount,
      receiptDate: receiptForm.value.receiptDate,
      receiptMethod: receiptForm.value.receiptMethod,
      remark: receiptForm.value.remark
    })
    ElMessage.success('收款登记成功')
    receiptDialogVisible.value = false
    fetchAll()
  } catch (e) {
    ElMessage.error(e.message || '收款登记失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该应收记录？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteReceivable(row.id)
      ElMessage.success('删除成功')
      fetchAll()
    } catch (e) {
      ElMessage.error(e.message || '删除失败')
    }
  })
}

onMounted(() => {
  fetchAll()
})
</script>

<style scoped>
.receivable-list {
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>


