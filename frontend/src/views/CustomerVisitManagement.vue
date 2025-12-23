<template>
  <div class="customer-visit-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户来访管理</span>
          <div>
            <el-button type="primary" @click="handleAdd">新增</el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="客户名称">
          <el-input v-model="queryForm.customerName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="queryForm.status" placeholder="请输入状态" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 来访列表 -->
      <el-table :data="visitList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="customerSequence" label="客户序号" width="120" />
        <el-table-column prop="customerName" label="客户名称" width="200" />
        <el-table-column prop="visitDate" label="日期" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="contactPerson" label="联络员" width="120" />
        <el-table-column prop="contactDepartment" label="联络员所属部门" width="150" />
        <el-table-column prop="visitMatter" label="来访事宜" width="200" show-overflow-tooltip />
        <el-table-column prop="remarks" label="备注" width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">修改</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 来访记录编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerId">
              <el-select
                v-model="formData.customerId"
                placeholder="请选择客户"
                filterable
                @change="handleCustomerChange"
                style="width: 100%"
              >
                <el-option
                  v-for="customer in customerList"
                  :key="customer.id"
                  :label="customer.unitName"
                  :value="customer.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户序号">
              <el-input v-model="formData.customerSequence" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="日期" prop="visitDate">
              <el-date-picker
                v-model="formData.visitDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-input v-model="formData.status" placeholder="请输入状态" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联络员" prop="contactPerson">
              <el-input v-model="formData.contactPerson" placeholder="请输入联络员" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联络员所属部门" prop="contactDepartment">
              <el-input v-model="formData.contactDepartment" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="来访事宜" prop="visitMatter">
          <el-input
            v-model="formData.visitMatter"
            type="textarea"
            :rows="3"
            placeholder="请输入来访事宜"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="formData.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getCustomerVisitList,
  saveCustomerVisit,
  deleteCustomerVisit,
  exportCustomerVisits
} from '../api/customerVisit'
import { getCustomerList } from '../api/customer'

const loading = ref(false)
const visitList = ref([])
const customerList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增来访记录')
const dateRange = ref(null)
const formRef = ref(null)

const queryForm = ref({
  customerName: '',
  startDate: null,
  endDate: null,
  status: ''
})

const formData = ref({
  id: null,
  customerId: '',
  customerSequence: '',
  customerName: '',
  visitDate: '',
  status: '',
  contactPerson: '',
  contactDepartment: '',
  visitMatter: '',
  remarks: ''
})

const rules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  visitDate: [{ required: true, message: '请选择日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (queryForm.value.customerName) params.customerName = queryForm.value.customerName
    if (queryForm.value.startDate) params.startDate = queryForm.value.startDate
    if (queryForm.value.endDate) params.endDate = queryForm.value.endDate
    if (queryForm.value.status) params.status = queryForm.value.status

    const res = await getCustomerVisitList(params)
    visitList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadCustomers = async () => {
  try {
    const res = await getCustomerList()
    customerList.value = res.data || []
  } catch (error) {
    console.error('加载客户列表失败', error)
  }
}

const handleDateChange = (dates) => {
  if (dates && dates.length === 2) {
    queryForm.value.startDate = dates[0]
    queryForm.value.endDate = dates[1]
  } else {
    queryForm.value.startDate = null
    queryForm.value.endDate = null
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.value = {
    customerName: '',
    startDate: null,
    endDate: null,
    status: ''
  }
  dateRange.value = null
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增来访记录'
  formData.value = {
    id: null,
    customerId: '',
    customerSequence: '',
    customerName: '',
    visitDate: '',
    status: '',
    contactPerson: '',
    contactDepartment: '',
    visitMatter: '',
    remarks: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '修改来访记录'
  formData.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条来访记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCustomerVisit(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleCustomerChange = (customerId) => {
  const customer = customerList.value.find(c => c.id === customerId)
  if (customer) {
    formData.value.customerSequence = customer.customerCode
    formData.value.customerName = customer.unitName
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveCustomerVisit(formData.value)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const handleExport = async () => {
  try {
    const params = {}
    if (queryForm.value.customerName) params.customerName = queryForm.value.customerName
    if (queryForm.value.startDate) params.startDate = queryForm.value.startDate
    if (queryForm.value.endDate) params.endDate = queryForm.value.endDate
    if (queryForm.value.status) params.status = queryForm.value.status

    const res = await exportCustomerVisits(params)
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `客户来访列表_${new Date().getTime()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error(error.message || '导出失败')
  }
}

onMounted(() => {
  loadCustomers()
  loadData()
})
</script>

<style scoped>
.customer-visit-management {
  padding: 0;
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

