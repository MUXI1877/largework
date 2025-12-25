<template>
  <div class="sales-quotation-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售报价管理</span>
          <div>
            <el-button type="primary" v-permission="{ moduleId: 'm015', action: 'add' }" @click="handleAdd">新增</el-button>
            <el-button type="success" v-permission="{ moduleId: 'm015', action: 'read' }" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="报价名称">
          <el-input v-model="queryForm.quotationName" placeholder="请输入报价名称" clearable />
        </el-form-item>
        <el-form-item label="报价客户">
          <el-input v-model="queryForm.customerName" placeholder="请输入报价客户" clearable />
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
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 报价单列表 -->
      <el-table :data="quotationList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="quotationCode" label="报价编号" width="150" />
        <el-table-column prop="quotationDate" label="日期" width="120" />
        <el-table-column prop="projectName" label="项目名称" width="200" show-overflow-tooltip />
        <el-table-column prop="customerName" label="报价客户" width="200" />
        <el-table-column prop="totalPrice" label="报价总额" width="120">
          <template #default="scope">
            {{ scope.row.totalPrice ? `¥${scope.row.totalPrice}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" v-permission="{ moduleId: 'm015', action: 'read' }" @click="handleView(scope.row)">查看</el-button>
            <el-button type="warning" size="small" v-permission="{ moduleId: 'm015', action: 'update' }" @click="handleEdit(scope.row)">修改</el-button>
            <el-button type="success" size="small" v-permission="{ moduleId: 'm015', action: 'read' }" @click="handlePrint(scope.row)">打印</el-button>
            <el-button type="danger" size="small" v-permission="{ moduleId: 'm015', action: 'update' }" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 报价单编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="1000px"
      @close="handleDialogClose"
    >
      <el-tabs v-model="activeTab">
        <el-tab-pane label="机会信息" name="opportunity">
          <el-form :model="formData" :rules="rules" ref="formRef" label-width="140px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="关联销售机会">
                  <el-select
                    v-model="formData.opportunityId"
                    placeholder="请选择销售机会"
                    filterable
                    @change="handleOpportunityChange"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="opp in opportunityList"
                      :key="opp.id"
                      :label="opp.opportunityName"
                      :value="opp.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="机会编号">
                  <el-input v-model="formData.opportunityCode" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="阶段">
                  <el-input v-model="formData.stage" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预算">
                  <el-input v-model="formData.budget" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="行业">
              <el-input v-model="formData.industry" disabled />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="报价信息" name="quotation">
          <el-form :model="formData" :rules="rules" ref="formRef" label-width="140px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报价名称" prop="quotationName">
                  <el-input v-model="formData.quotationName" placeholder="请输入报价名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报价类型">
                  <el-select v-model="formData.quotationType" placeholder="请选择报价类型" style="width: 100%">
                    <el-option label="标准报价" value="标准报价" />
                    <el-option label="优惠报价" value="优惠报价" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="税率">
                  <el-input-number
                    v-model="formData.taxRate"
                    :precision="2"
                    :min="0"
                    :max="100"
                    style="width: 100%"
                  />
                  <span style="margin-left: 10px">%</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报价日期" prop="quotationDate">
                  <el-date-picker
                    v-model="formData.quotationDate"
                    type="date"
                    placeholder="选择日期"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="竞争对手名称">
                  <el-input v-model="formData.competitorName" placeholder="请输入竞争对手名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="竞争对手报价">
                  <el-input-number
                    v-model="formData.competitorQuotation"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="总价">
              <el-input v-model="formData.totalPrice" disabled>
                <template #prefix>¥</template>
              </el-input>
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
        </el-tab-pane>
        <el-tab-pane label="设备及零件" name="items">
          <div style="margin-bottom: 10px">
            <el-button type="primary" size="small" @click="handleAddItem">添加产品</el-button>
          </div>
          <el-table :data="itemList" border>
            <el-table-column prop="productName" label="产品/零件名称" width="200" />
            <el-table-column prop="drawingNumber" label="图号" width="150" />
            <el-table-column prop="inventoryQuantity" label="零件库存" width="120" />
            <el-table-column prop="quantity" label="数量" width="120">
              <template #default="scope">
                <el-input-number
                  v-model="scope.row.quantity"
                  :precision="2"
                  :min="0"
                  size="small"
                  @change="calculateItemTotal(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="isStagnant" label="是否呆滞" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.isStagnant ? 'danger' : 'success'" size="small">
                  {{ scope.row.isStagnant ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="unitPrice" label="报价（单价）" width="150">
              <template #default="scope">
                <el-input-number
                  v-model="scope.row.unitPrice"
                  :precision="2"
                  :min="0"
                  size="small"
                  @change="calculateItemTotal(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="totalPrice" label="报价（总价）" width="150">
              <template #default="scope">
                {{ scope.row.totalPrice ? `¥${scope.row.totalPrice}` : '-' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button
                  type="danger"
                  size="small"
                  @click="handleDeleteItem(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 产品选择对话框 -->
    <el-dialog v-model="productDialogVisible" title="选择产品" width="800px">
      <el-form :inline="true">
        <el-form-item label="名称">
          <el-input v-model="productQuery" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadProducts">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="productList" border @selection-change="handleProductSelection">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="drawingNumber" label="图号" />
        <el-table-column prop="quantity" label="库存" />
        <el-table-column prop="isStagnant" label="是否呆滞">
          <template #default="scope">
            <el-tag :type="scope.row.isStagnant ? 'danger' : 'success'" size="small">
              {{ scope.row.isStagnant ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmProducts">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getSalesQuotationList,
  getSalesQuotationById,
  saveSalesQuotation,
  deleteSalesQuotation,
  getQuotationItems,
  saveQuotationItems,
  exportSalesQuotations
} from '../api/salesQuotation'
import { getProjectOpportunityList } from '../api/projectOpportunity'
import { queryInventory } from '../api/product'
import { canRead, loadPermissions } from '../utils/permission'

const loading = ref(false)
const quotationList = ref([])
const opportunityList = ref([])
const productList = ref([])
const selectedProducts = ref([])
const dialogVisible = ref(false)
const productDialogVisible = ref(false)
const dialogTitle = ref('新增报价单')
const activeTab = ref('opportunity')
const dateRange = ref(null)
const formRef = ref(null)
const productQuery = ref('')
const itemList = ref([])

const queryForm = ref({
  quotationName: '',
  customerName: '',
  startDate: null,
  endDate: null
})

const formData = ref({
  id: null,
  quotationName: '',
  quotationType: '',
  opportunityId: '',
  opportunityName: '',
  opportunityCode: '',
  stage: '',
  budget: null,
  industry: '',
  customerName: '',
  projectName: '',
  totalPrice: null,
  taxRate: null,
  competitorName: '',
  competitorQuotation: null,
  quotationDate: '',
  remarks: ''
})

const rules = {
  quotationName: [{ required: true, message: '请输入报价名称', trigger: 'blur' }],
  quotationDate: [{ required: true, message: '请选择报价日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (queryForm.value.quotationName) params.quotationName = queryForm.value.quotationName
    if (queryForm.value.customerName) params.customerName = queryForm.value.customerName
    if (queryForm.value.startDate) params.startDate = queryForm.value.startDate
    if (queryForm.value.endDate) params.endDate = queryForm.value.endDate

    const res = await getSalesQuotationList(params)
    quotationList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadOpportunities = async () => {
  try {
    const res = await getProjectOpportunityList({})
    opportunityList.value = res.data || []
  } catch (error) {
    console.error('加载销售机会列表失败', error)
  }
}

const loadProducts = async () => {
  try {
    const params = {}
    if (productQuery.value) params.name = productQuery.value
    const res = await queryInventory(params)
    productList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '加载产品列表失败')
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
    quotationName: '',
    customerName: '',
    startDate: null,
    endDate: null
  }
  dateRange.value = null
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增报价单'
  formData.value = {
    id: null,
    quotationName: '',
    quotationType: '',
    opportunityId: '',
    opportunityName: '',
    opportunityCode: '',
    stage: '',
    budget: null,
    industry: '',
    customerName: '',
    projectName: '',
    totalPrice: null,
    taxRate: null,
    competitorName: '',
    competitorQuotation: null,
    quotationDate: '',
    remarks: ''
  }
  itemList.value = []
  activeTab.value = 'opportunity'
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogTitle.value = '修改报价单'
  try {
    const res = await getSalesQuotationById(row.id)
    formData.value = res.data
    const itemsRes = await getQuotationItems(row.id)
    itemList.value = itemsRes.data || []
    activeTab.value = 'opportunity'
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  }
}

const handleView = async (row) => {
  await handleEdit(row)
  // 可以添加只读模式
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条报价单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSalesQuotation(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handlePrint = async (row) => {
  // 打印功能需要前端实现，这里可以打开新窗口或使用打印插件
  ElMessage.info('打印功能需要前端实现')
}

const handleOpportunityChange = (opportunityId) => {
  const opportunity = opportunityList.value.find(o => o.id === opportunityId)
  if (opportunity) {
    formData.value.opportunityName = opportunity.opportunityName
    formData.value.opportunityCode = opportunity.opportunityCode
    formData.value.stage = opportunity.stage
    formData.value.budget = opportunity.budget
    formData.value.industry = opportunity.industry
    formData.value.projectName = opportunity.projectName
    formData.value.customerName = opportunity.customerName
  }
}

const handleAddItem = () => {
  productQuery.value = ''
  productList.value = []
  selectedProducts.value = []
  productDialogVisible.value = true
  loadProducts()
}

const handleProductSelection = (selection) => {
  selectedProducts.value = selection
}

const handleConfirmProducts = () => {
  selectedProducts.value.forEach(product => {
    itemList.value.push({
      productId: product.id,
      productName: product.name,
      drawingNumber: product.drawingNumber,
      inventoryQuantity: product.quantity,
      quantity: 1,
      isStagnant: product.isStagnant,
      unitPrice: product.price || 0,
      totalPrice: product.price || 0
    })
  })
  productDialogVisible.value = false
  calculateTotal()
}

const calculateItemTotal = (item) => {
  if (item.quantity && item.unitPrice) {
    item.totalPrice = item.quantity * item.unitPrice
  } else {
    item.totalPrice = 0
  }
  calculateTotal()
}

const calculateTotal = () => {
  let total = 0
  itemList.value.forEach(item => {
    if (item.totalPrice) {
      total += item.totalPrice
    }
  })
  formData.value.totalPrice = total
}

const handleDeleteItem = (index) => {
  itemList.value.splice(index, 1)
  calculateTotal()
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...formData.value }
        const res = await saveSalesQuotation(data)
        const quotationId = res.data.id || formData.value.id
        
        // 保存报价明细
        if (itemList.value.length > 0) {
          await saveQuotationItems(quotationId, itemList.value)
        }
        
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
  itemList.value = []
}

const handleExport = async () => {
  try {
    const params = {}
    if (queryForm.value.quotationName) params.quotationName = queryForm.value.quotationName
    if (queryForm.value.customerName) params.customerName = queryForm.value.customerName
    if (queryForm.value.startDate) params.startDate = queryForm.value.startDate
    if (queryForm.value.endDate) params.endDate = queryForm.value.endDate

    const res = await exportSalesQuotations(params)
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `报价单列表_${new Date().getTime()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error(error.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  loadOpportunities()
  loadData()
})
</script>

<style scoped>
.sales-quotation-management {
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

