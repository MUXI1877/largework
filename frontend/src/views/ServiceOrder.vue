<template>
  <div class="service-order">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>售后服务工单</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新建工单
          </el-button>
        </div>
      </template>

      <div class="table-toolbar">
        <el-input
          v-model="searchForm.orderNo"
          placeholder="工单号"
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
          placeholder="工单状态"
          style="width: 140px; margin-right: 10px"
        >
          <el-option label="全部" value="" />
          <el-option label="新建" value="NEW" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="已关闭" value="CLOSED" />
        </el-select>
        <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
        <el-button type="default" size="small" @click="handleReset">重置</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="orderNo" label="工单号" width="160" />
        <el-table-column prop="customerId" label="客户ID" width="160" />
        <el-table-column prop="productId" label="产品ID" width="160" />
        <el-table-column prop="problemDesc" label="问题描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="110" align="center">
          <template #default="scope">
            <el-tag :type="statusTagType(scope.row.status)">
              {{ statusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignTo.name" label="指派给" width="120">
          <template #default="scope">
            {{ scope.row.assignTo ? scope.row.assignTo.name : '未指派' }}
          </template>
        </el-table-column>
        <el-table-column prop="completeTime" label="完成时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right" align="center">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(scope.row)"
              style="margin-right: 5px"
            >
              编辑
            </el-button>
            <el-button
              type="success"
              size="small"
              @click="openAssign(scope.row)"
              style="margin-right: 5px"
            >
              指派
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="changeStatus(scope.row)"
              style="margin-right: 5px"
            >
              改状态
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

    <!-- 新建/编辑工单 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form :model="formData" label-width="120px">
        <el-form-item label="工单号">
          <el-input v-model="formData.orderNo" placeholder="可留空自动生成" />
        </el-form-item>
        <el-form-item label="客户ID">
          <el-input v-model="formData.customerId" placeholder="请输入客户ID" />
        </el-form-item>
        <el-form-item label="产品ID">
          <el-input v-model="formData.productId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="问题描述">
          <el-input
            v-model="formData.problemDesc"
            type="textarea"
            rows="4"
            placeholder="请输入问题描述"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" placeholder="请选择状态">
            <el-option label="新建" value="NEW" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 指派对话框 -->
    <el-dialog
      v-model="assignDialogVisible"
      title="指派服务人员"
      width="400px"
    >
      <el-form :model="assignForm" label-width="120px">
        <el-form-item label="工单号">
          <el-input v-model="assignForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="服务人员ID">
          <el-input v-model="assignForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="assignDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAssign">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import {
  getServiceOrderList,
  saveServiceOrder,
  updateServiceOrder,
  deleteServiceOrder,
  updateServiceOrderStatus,
  assignServiceOrder
} from '../api/service'

const rawData = ref([])
const tableData = ref([])

const searchForm = ref({
  orderNo: '',
  status: ''
})

const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新建工单')
const formData = ref({
  id: '',
  orderNo: '',
  customerId: '',
  productId: '',
  problemDesc: '',
  status: 'NEW'
})

const assignDialogVisible = ref(false)
const assignForm = ref({
  id: '',
  orderNo: '',
  userId: ''
})

const statusLabel = (status) => {
  const map = {
    NEW: '新建',
    PROCESSING: '处理中',
    COMPLETED: '已完成',
    CLOSED: '已关闭'
  }
  return map[status] || status
}

const statusTagType = (status) => {
  const map = {
    NEW: 'info',
    PROCESSING: 'warning',
    COMPLETED: 'success',
    CLOSED: 'danger'
  }
  return map[status] || 'info'
}

const fetchAll = async () => {
  try {
    const res = await getServiceOrderList()
    rawData.value = res.data || []
    applyFilterAndPage()
  } catch (e) {
    ElMessage.error('加载工单数据失败')
  }
}

const applyFilterAndPage = () => {
  let list = [...rawData.value]
  if (searchForm.value.orderNo) {
    list = list.filter((item) => item.orderNo && item.orderNo.includes(searchForm.value.orderNo))
  }
  if (searchForm.value.status) {
    list = list.filter((item) => item.status === searchForm.value.status)
  }

  pagination.value.total = list.length
  const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const end = start + pagination.value.pageSize
  tableData.value = list.slice(start, end)
}

const handleSearch = () => {
  pagination.value.currentPage = 1
  applyFilterAndPage()
}

const handleReset = () => {
  searchForm.value = {
    orderNo: '',
    status: ''
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

const handleAdd = () => {
  dialogTitle.value = '新建工单'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑工单'
  formData.value = {
    id: row.id,
    orderNo: row.orderNo,
    customerId: row.customerId,
    productId: row.productId,
    problemDesc: row.problemDesc,
    status: row.status
  }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该工单？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteServiceOrder(row.id)
      ElMessage.success('删除成功')
      fetchAll()
    } catch (e) {
      ElMessage.error(e.message || '删除失败')
    }
  })
}

const changeStatus = (row) => {
  const nextStatus = row.status === 'NEW' ? 'PROCESSING' : row.status === 'PROCESSING' ? 'COMPLETED' : 'CLOSED'
  ElMessageBox.confirm(`确定将状态修改为【${statusLabel(nextStatus)}】?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await updateServiceOrderStatus(row.id, nextStatus)
      ElMessage.success('状态更新成功')
      fetchAll()
    } catch (e) {
      ElMessage.error(e.message || '状态更新失败')
    }
  })
}

const openAssign = (row) => {
  assignForm.value = {
    id: row.id,
    orderNo: row.orderNo,
    userId: ''
  }
  assignDialogVisible.value = true
}

const submitAssign = async () => {
  try {
    if (!assignForm.value.userId) {
      ElMessage.warning('请输入服务人员ID')
      return
    }
    await assignServiceOrder(assignForm.value.id, assignForm.value.userId)
    ElMessage.success('指派成功')
    assignDialogVisible.value = false
    fetchAll()
  } catch (e) {
    ElMessage.error(e.message || '指派失败')
  }
}

const submitForm = async () => {
  try {
    if (formData.value.id) {
      await updateServiceOrder(formData.value)
      ElMessage.success('更新成功')
    } else {
      await saveServiceOrder(formData.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchAll()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

const resetForm = () => {
  formData.value = {
    id: '',
    orderNo: '',
    customerId: '',
    productId: '',
    problemDesc: '',
    status: 'NEW'
  }
}

onMounted(() => {
  fetchAll()
})
</script>

<style scoped>
.service-order {
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


