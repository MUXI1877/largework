<template>
  <div class="price-book-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>价格本管理</span>
          <div>
            <el-button type="primary" @click="handleAdd">新增</el-button>
            <el-button type="success" @click="handleExport">导出</el-button>
            <el-button @click="handleRefresh">刷新</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="产品类别">
          <el-select v-model="queryForm.productType" placeholder="请选择产品类别" clearable style="width: 180px">
            <el-option
              v-for="type in productTypeOptions"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="queryForm.department" placeholder="请选择部门" clearable style="width: 150px">
            <el-option
              v-for="dept in departmentOptions"
              :key="dept.value"
              :label="dept.label"
              :value="dept.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="queryForm.productName" placeholder="请输入产品名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="queryForm.model" placeholder="请输入型号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 价格本列表 -->
      <el-table :data="priceBookList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="productType" label="产品类别" width="140">
          <template #default="scope">
            {{ getProductTypeLabel(scope.row.productType) }}
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" width="200" show-overflow-tooltip />
        <el-table-column prop="model" label="型号" width="150" />
        <el-table-column prop="parameters" label="参数" width="250" show-overflow-tooltip />
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="unitPrice" label="单价" width="120">
          <template #default="scope">
            {{ scope.row.unitPrice ? `¥${scope.row.unitPrice.toFixed(2)}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewLogs(scope.row)">日志</el-button>
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">修改</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 价格本编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="140px">
        <el-form-item label="产品类别" prop="productType">
          <el-select
            v-model="formData.productType"
            placeholder="请选择产品类别"
            style="width: 100%"
            @change="handleProductTypeChange"
          >
            <el-option
              v-for="type in productTypeOptions"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品" prop="productId">
          <el-select
            v-model="formData.productId"
            placeholder="请选择产品"
            filterable
            style="width: 100%"
            @change="handleProductChange"
          >
            <el-option
              v-for="product in productList"
              :key="product.id"
              :label="`${product.name} (${product.model})`"
              :value="product.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="formData.department" placeholder="请选择部门" style="width: 100%">
            <el-option
              v-for="dept in departmentOptions"
              :key="dept.value"
              :label="dept.label"
              :value="dept.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="formData.productName" disabled />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="formData.model" disabled />
        </el-form-item>
        <el-form-item label="参数">
          <el-input v-model="formData.parameters" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number
            v-model="formData.unitPrice"
            :precision="2"
            :min="0"
            style="width: 100%"
            placeholder="请输入单价"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remarks" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 价格修改日志对话框 -->
    <el-dialog
      v-model="logDialogVisible"
      title="价格修改日志"
      width="900px"
    >
      <el-table :data="logList" border style="width: 100%">
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column prop="createTime" label="操作时间" width="180" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.operationType === 'CREATE'" type="success">新增</el-tag>
            <el-tag v-else-if="scope.row.operationType === 'UPDATE'" type="warning">修改</el-tag>
            <el-tag v-else-if="scope.row.operationType === 'DELETE'" type="danger">删除</el-tag>
            <span v-else>{{ scope.row.operationType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="oldPrice" label="原价格" width="120">
          <template #default="scope">
            {{ scope.row.oldPrice ? `¥${scope.row.oldPrice.toFixed(2)}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="newPrice" label="新价格" width="120">
          <template #default="scope">
            {{ scope.row.newPrice ? `¥${scope.row.newPrice.toFixed(2)}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getPriceBooks,
  createPriceBook,
  updatePriceBook,
  deletePriceBook,
  getPriceBookLogs,
  exportPriceBooks,
  getProductsForPriceBook,
  getDepartments,
  getProductTypes
} from '../api/priceBook'

// 数据
const loading = ref(false)
const priceBookList = ref([])
const productList = ref([])
const departmentOptions = ref([])
const productTypeOptions = ref([])
const logList = ref([])

// 查询表单
const queryForm = reactive({
  productType: '',
  department: '',
  productName: '',
  model: ''
})

// 分页
const pagination = reactive({
  page: 1,  // Element Plus 分页从1开始
  size: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const logDialogVisible = ref(false)
const dialogTitle = ref('新增价格本')
const formRef = ref(null)

// 表单数据
const formData = reactive({
  id: '',
  productId: '',
  productName: '',
  productType: '',
  model: '',
  parameters: '',
  department: '',
  unitPrice: null,
  remarks: ''
})

// 表单验证规则
const rules = {
  productType: [{ required: true, message: '请选择产品类别', trigger: 'change' }],
  productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
  department: [{ required: true, message: '请选择部门', trigger: 'change' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

// 获取产品类别标签
const getProductTypeLabel = (type) => {
  const option = productTypeOptions.value.find(opt => opt.value === type)
  return option ? option.label : type
}

// 查询价格本列表
const loadPriceBooks = async () => {
  loading.value = true
  try {
    const params = {
      ...queryForm,
      // Element Plus 分页从1开始，后端从0开始，需要减1
      page: pagination.page - 1,
      size: pagination.size
    }
    const response = await getPriceBooks(params)
    if (response.code === 200) {
      priceBookList.value = response.data.content || []
      pagination.total = response.data.totalElements || 0
    } else {
      ElMessage.error(response.message || '查询失败')
    }
  } catch (error) {
    ElMessage.error('查询失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  pagination.page = 1
  loadPriceBooks()
}

// 重置
const handleReset = () => {
  queryForm.productType = ''
  queryForm.department = ''
  queryForm.productName = ''
  queryForm.model = ''
  handleQuery()
}

// 刷新
const handleRefresh = () => {
  loadPriceBooks()
}

// 分页变化
const handleSizeChange = (val) => {
  pagination.size = val
  pagination.page = 1
  loadPriceBooks()
}

const handlePageChange = (val) => {
  pagination.page = val
  loadPriceBooks()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增价格本'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '修改价格本'
  Object.assign(formData, {
    id: row.id,
    productId: row.productId,
    productName: row.productName,
    productType: row.productType,
    model: row.model,
    parameters: row.parameters,
    department: row.department,
    unitPrice: row.unitPrice,
    remarks: row.remarks || ''
  })
  // 加载对应类别的产品列表
  loadProductsByType(row.productType)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该价格本吗？', '提示', {
      type: 'warning'
    })
    const response = await deletePriceBook(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadPriceBooks()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 查看日志
const handleViewLogs = async (row) => {
  try {
    const response = await getPriceBookLogs(row.id)
    if (response.code === 200) {
      logList.value = response.data || []
      logDialogVisible.value = true
    } else {
      ElMessage.error(response.message || '查询日志失败')
    }
  } catch (error) {
    ElMessage.error('查询日志失败：' + error.message)
  }
}

// 保存
const handleSave = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    const response = dialogTitle.value === '新增价格本'
      ? await createPriceBook(formData)
      : await updatePriceBook(formData.id, formData)
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadPriceBooks()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    if (error !== false) { // 表单验证失败会返回false
      ElMessage.error('保存失败：' + error.message)
    }
  }
}

// 产品类别变化
const handleProductTypeChange = () => {
  formData.productId = ''
  formData.productName = ''
  formData.model = ''
  formData.parameters = ''
  loadProductsByType(formData.productType)
}

// 产品变化
const handleProductChange = () => {
  const product = productList.value.find(p => p.id === formData.productId)
  if (product) {
    formData.productName = product.name
    formData.model = product.model || ''
    formData.parameters = product.parameters || ''
  }
}

// 加载产品列表
const loadProductsByType = async (productType) => {
  try {
    const response = await getProductsForPriceBook(productType)
    if (response.code === 200) {
      productList.value = response.data || []
    }
  } catch (error) {
    console.error('加载产品列表失败：', error)
  }
}

// 导出
const handleExport = async () => {
  try {
    const params = { ...queryForm }
    const response = await exportPriceBooks(params)
    
    // 创建下载链接
    const blob = new Blob([response], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `价格本数据_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + error.message)
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: '',
    productId: '',
    productName: '',
    productType: '',
    model: '',
    parameters: '',
    department: '',
    unitPrice: null,
    remarks: ''
  })
  productList.value = []
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 对话框关闭
const handleDialogClose = () => {
  resetForm()
}

// 初始化
const init = async () => {
  // 加载部门列表
  try {
    const deptResponse = await getDepartments()
    if (deptResponse.code === 200) {
      departmentOptions.value = deptResponse.data || []
    }
  } catch (error) {
    console.error('加载部门列表失败：', error)
  }
  
  // 加载产品类别列表
  try {
    const typeResponse = await getProductTypes()
    if (typeResponse.code === 200) {
      productTypeOptions.value = typeResponse.data || []
    }
  } catch (error) {
    console.error('加载产品类别列表失败：', error)
  }
  
  // 加载价格本列表
  loadPriceBooks()
}

onMounted(() => {
  init()
})
</script>

<style scoped>
.price-book-management {
  padding: 20px;
}

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

