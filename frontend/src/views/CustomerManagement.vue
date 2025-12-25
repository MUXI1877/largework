<template>
  <div class="customer-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户管理</span>
          <div>
            <el-button 
              v-permission="{ moduleId: 'm008', action: 'add' }"
              type="primary" @click="handleAdd">新增</el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="单位名称">
          <el-input v-model="queryForm.unitName" placeholder="请输入单位名称" clearable />
        </el-form-item>
        <el-form-item label="所属区域">
          <el-input v-model="queryForm.region" placeholder="请输入所属区域" clearable />
        </el-form-item>
        <el-form-item>
          <el-button 
            v-permission="{ moduleId: 'm008', action: 'read' }"
            type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 客户列表 -->
      <el-table :data="customerList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="customerCode" label="客户编号" width="120" />
        <el-table-column prop="unitName" label="单位" width="200" />
        <el-table-column prop="region" label="所属区域" width="120" />
        <el-table-column prop="industry" label="行业" width="120" />
        <el-table-column prop="area" label="地区" width="120" />
        <el-table-column prop="buyerAttribute" label="客户属性" width="120" />
        <el-table-column prop="creditLevel" label="信用评级" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              v-permission="{ moduleId: 'm008', action: 'update' }"
              type="warning" size="small" @click="handleEdit(scope.row)">修改</el-button>
            <el-button 
              v-permission="{ moduleId: 'm008', action: 'update' }"
              type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 客户编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户编号" prop="customerCode">
              <el-input v-model="formData.customerCode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位名称" prop="unitName">
              <el-input v-model="formData.unitName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属区域">
              <el-input v-model="formData.region" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地区">
              <el-input v-model="formData.area" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属行业">
              <el-input v-model="formData.industry" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司地址">
              <el-input v-model="formData.companyAddress" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订货代表">
              <el-input v-model="formData.orderRepresentative" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买方属性">
              <el-input v-model="formData.buyerAttribute" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开票电话">
              <el-input v-model="formData.invoicePhone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="formData.contactPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="传真">
              <el-input v-model="formData.fax" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮编">
              <el-input v-model="formData.postalCode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="开票地址">
          <el-input v-model="formData.invoiceAddress" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开票银行">
              <el-input v-model="formData.invoiceBank" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号">
              <el-input v-model="formData.bankAccount" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="税号">
              <el-input v-model="formData.taxNumber" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="欠款金额">
              <el-input-number v-model="formData.arrearsAmount" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="信用等级">
              <el-input v-model="formData.creditLevel" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="formData.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 客户详细信息对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="客户详细信息"
      width="1200px"
    >
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="客户编号">{{ currentCustomer.customerCode }}</el-descriptions-item>
            <el-descriptions-item label="单位名称">{{ currentCustomer.unitName }}</el-descriptions-item>
            <el-descriptions-item label="所属区域">{{ currentCustomer.region }}</el-descriptions-item>
            <el-descriptions-item label="地区">{{ currentCustomer.area }}</el-descriptions-item>
            <el-descriptions-item label="所属行业">{{ currentCustomer.industry }}</el-descriptions-item>
            <el-descriptions-item label="公司地址">{{ currentCustomer.companyAddress }}</el-descriptions-item>
            <el-descriptions-item label="信用等级">{{ currentCustomer.creditLevel }}</el-descriptions-item>
            <el-descriptions-item label="欠款金额">{{ currentCustomer.arrearsAmount }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="客户关键人物" name="keyPersons">
          <div style="margin-bottom: 10px">
            <el-button type="primary" size="small" @click="handleAddKeyPerson">新增</el-button>
          </div>
          <el-table :data="keyPersonList" border>
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="gender" label="性别" />
            <el-table-column prop="birthday" label="生日" />
            <el-table-column prop="position" label="职位" />
            <el-table-column prop="contactInfo" label="联系方式" />
            <el-table-column prop="jobType" label="职务" />
            <el-table-column prop="directSupervisor" label="直接上级" />
            <el-table-column prop="isMainContact" label="主要联系人">
              <template #default="scope">
                <el-tag :type="scope.row.isMainContact ? 'success' : 'info'">
                  {{ scope.row.isMainContact ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button type="danger" size="small" @click="handleDeleteKeyPerson(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="项目机会" name="projects">
          <el-table :data="projectList" border>
            <el-table-column prop="projectName" label="项目名称" />
            <el-table-column prop="projectRegion" label="项目所属片区" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="projectTime" label="时间" />
            <el-table-column prop="remarks" label="备注" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="合同信息" name="contracts">
          <el-table :data="contractList" border>
            <el-table-column prop="contractCode" label="合同编号" />
            <el-table-column prop="contractName" label="合同名称" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="售后信息" name="afterSales">
          <el-table :data="afterSalesList" border>
            <el-table-column prop="contractCode" label="合同编号" />
            <el-table-column prop="contractName" label="合同名称" />
            <el-table-column prop="region" label="区域" />
            <el-table-column prop="afterSalesPerson" label="售后人员" />
            <el-table-column prop="remarks" label="备注" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="客户来访信息" name="visits">
          <el-table :data="visitList" border>
            <el-table-column prop="visitDate" label="日期" />
            <el-table-column prop="customerSequence" label="客户序号" />
            <el-table-column prop="customerName" label="客户名称" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="remarks" label="备注" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 关键人物编辑对话框 -->
    <el-dialog
      v-model="keyPersonDialogVisible"
      :title="keyPersonDialogTitle"
      width="600px"
    >
      <el-form :model="keyPersonForm" :rules="keyPersonRules" ref="keyPersonFormRef" label-width="120px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="keyPersonForm.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="keyPersonForm.gender" placeholder="请选择">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker v-model="keyPersonForm.birthday" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="keyPersonForm.position" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="keyPersonForm.contactInfo" />
        </el-form-item>
        <el-form-item label="职务">
          <el-select v-model="keyPersonForm.jobType" placeholder="请选择">
            <el-option label="决策者" value="决策者" />
            <el-option label="部门主管" value="部门主管" />
            <el-option label="普通员工" value="普通员工" />
          </el-select>
        </el-form-item>
        <el-form-item label="直接上级">
          <el-input v-model="keyPersonForm.directSupervisor" />
        </el-form-item>
        <el-form-item label="是否主要联系人">
          <el-switch v-model="keyPersonForm.isMainContact" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="keyPersonForm.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="keyPersonDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveKeyPerson">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { canRead, loadPermissions } from '../utils/permission'
import {
  getCustomerList,
  saveCustomer,
  deleteCustomer,
  exportCustomers,
  getKeyPersons,
  saveKeyPerson,
  deleteKeyPerson,
  getProjectOpportunities,
  getContracts,
  getAfterSales,
  getCustomerVisits
} from '../api/customer'

const loading = ref(false)
const customerList = ref([])
const queryForm = reactive({
  unitName: '',
  region: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const formRef = ref(null)
const formData = reactive({
  id: '',
  customerCode: '',
  unitName: '',
  region: '',
  area: '',
  industry: '',
  companyAddress: '',
  orderRepresentative: '',
  buyerAttribute: '',
  invoicePhone: '',
  contactPhone: '',
  fax: '',
  postalCode: '',
  invoiceAddress: '',
  invoiceBank: '',
  bankAccount: '',
  taxNumber: '',
  arrearsAmount: 0,
  creditLevel: '',
  remarks: ''
})

const rules = {
  customerCode: [{ required: true, message: '请输入客户编号', trigger: 'blur' }],
  unitName: [{ required: true, message: '请输入单位名称', trigger: 'blur' }]
}

const detailDialogVisible = ref(false)
const activeTab = ref('basic')
const currentCustomer = ref({})
const keyPersonList = ref([])
const projectList = ref([])
const contractList = ref([])
const afterSalesList = ref([])
const visitList = ref([])

const keyPersonDialogVisible = ref(false)
const keyPersonDialogTitle = ref('新增关键人物')
const keyPersonFormRef = ref(null)
const keyPersonForm = reactive({
  id: '',
  customerId: '',
  name: '',
  gender: '',
  birthday: null,
  position: '',
  contactInfo: '',
  jobType: '',
  directSupervisor: '',
  isMainContact: false,
  remarks: ''
})

const keyPersonRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCustomerList()
    customerList.value = res.data || []
    // 简单过滤
    if (queryForm.unitName) {
      customerList.value = customerList.value.filter(c => 
        c.unitName.includes(queryForm.unitName)
      )
    }
    if (queryForm.region) {
      customerList.value = customerList.value.filter(c => 
        c.region && c.region.includes(queryForm.region)
      )
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.unitName = ''
  queryForm.region = ''
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  Object.assign(formData, {
    id: '',
    customerCode: '',
    unitName: '',
    region: '',
    area: '',
    industry: '',
    companyAddress: '',
    orderRepresentative: '',
    buyerAttribute: '',
    invoicePhone: '',
    contactPhone: '',
    fax: '',
    postalCode: '',
    invoiceAddress: '',
    invoiceBank: '',
    bankAccount: '',
    taxNumber: '',
    arrearsAmount: 0,
    creditLevel: '',
    remarks: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '修改客户'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveCustomer(formData)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  ElMessageBox.confirm('确定要删除该客户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCustomer(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleExport = async () => {
  try {
    const response = await exportCustomers()
    // 创建下载链接
    const blob = new Blob([response], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    const filename = `客户列表_${new Date().getTime()}.xlsx`
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + (error.message || '未知错误'))
  }
}

const handleView = async (row) => {
  currentCustomer.value = row
  detailDialogVisible.value = true
  activeTab.value = 'basic'
  
  // 加载关联数据
  try {
    const [keyRes, projectRes, contractRes, afterSalesRes, visitRes] = await Promise.all([
      getKeyPersons(row.id),
      getProjectOpportunities(row.id),
      getContracts(row.id),
      getAfterSales(row.id),
      getCustomerVisits(row.id)
    ])
    keyPersonList.value = keyRes.data || []
    projectList.value = projectRes.data || []
    contractList.value = contractRes.data || []
    afterSalesList.value = afterSalesRes.data || []
    visitList.value = visitRes.data || []
  } catch (error) {
    ElMessage.error('加载关联数据失败')
  }
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const handleAddKeyPerson = () => {
  keyPersonDialogTitle.value = '新增关键人物'
  Object.assign(keyPersonForm, {
    id: '',
    customerId: currentCustomer.value.id,
    name: '',
    gender: '',
    birthday: null,
    position: '',
    contactInfo: '',
    jobType: '',
    directSupervisor: '',
    isMainContact: false,
    remarks: ''
  })
  keyPersonDialogVisible.value = true
}

const handleSaveKeyPerson = async () => {
  if (!keyPersonFormRef.value) return
  await keyPersonFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveKeyPerson(keyPersonForm)
        ElMessage.success('保存成功')
        keyPersonDialogVisible.value = false
        // 重新加载关键人物列表
        const res = await getKeyPersons(currentCustomer.value.id)
        keyPersonList.value = res.data || []
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDeleteKeyPerson = async (row) => {
  ElMessageBox.confirm('确定要删除该关键人物吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteKeyPerson(row.id)
      ElMessage.success('删除成功')
      const res = await getKeyPersons(currentCustomer.value.id)
      keyPersonList.value = res.data || []
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m008')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看客户管理的权限')
  }
})
</script>

<style scoped>
.customer-management {
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

