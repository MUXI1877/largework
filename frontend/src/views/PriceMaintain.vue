<template>
  <div class="price-maintain">
    <div class="page-header">
      <h1>价格维护</h1>
      <el-button type="primary" @click="openAddDialog">新增价格版本</el-button>
    </div>

    <div class="filter-section">
      <el-select v-model="statusFilter" placeholder="按状态筛选" style="width: 200px; margin-right: 10px;">
        <el-option label="全部" value=""></el-option>
        <el-option label="有效" value="ACTIVE"></el-option>
        <el-option label="过期" value="EXPIRED"></el-option>
        <el-option label="草稿" value="DRAFT"></el-option>
      </el-select>
      <el-button type="primary" @click="filterPriceBooks">筛选</el-button>
      <el-button @click="resetFilter">重置</el-button>
    </div>

    <el-table :data="priceBooks" stripe style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="productCode" label="产品代码" width="150"></el-table-column>
      <el-table-column prop="productName" label="产品名称" width="200"></el-table-column>
      <el-table-column prop="unitPrice" label="单价" width="120" align="right">
        <template #default="scope">
          {{ formatCurrency(scope.row.unitPrice) }}
        </template>
      </el-table-column>
      <el-table-column prop="effectiveDate" label="生效日期" width="150"></el-table-column>
      <el-table-column prop="expireDate" label="失效日期" width="150"></el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="statusTypeMap[scope.row.status]">
            {{ statusLabelMap[scope.row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createBy" label="创建人" width="120"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="产品代码" prop="productCode">
          <el-input v-model="formData.productCode" placeholder="请输入产品代码"></el-input>
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称"></el-input>
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number
            v-model="formData.unitPrice"
            :min="0"
            :step="0.01"
            :precision="2"
            placeholder="请输入单价"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="生效日期" prop="effectiveDate">
          <el-date-picker
            v-model="formData.effectiveDate"
            type="date"
            placeholder="选择生效日期"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="失效日期" prop="expireDate">
          <el-date-picker
            v-model="formData.expireDate"
            type="date"
            placeholder="选择失效日期（可选）"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="选择状态">
            <el-option label="有效" value="ACTIVE"></el-option>
            <el-option label="过期" value="EXPIRED"></el-option>
            <el-option label="草稿" value="DRAFT"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as priceBookApi from '../api/price-book'

const priceBooks = ref([])
const statusFilter = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增价格版本')
const formData = ref({
  id: '',
  productCode: '',
  productName: '',
  unitPrice: 0,
  effectiveDate: null,
  expireDate: null,
  status: 'ACTIVE'
})
const formRef = ref(null)

// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 状态映射
const statusLabelMap = {
  'ACTIVE': '有效',
  'EXPIRED': '过期',
  'DRAFT': '草稿'
}

const statusTypeMap = {
  'ACTIVE': 'success',
  'EXPIRED': 'danger',
  'DRAFT': 'warning'
}

// 表单验证规则
const rules = {
  productCode: [{ required: true, message: '请输入产品代码', trigger: 'blur' }],
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }, { type: 'number', min: 0, message: '单价不能为负数', trigger: 'blur' }],
  effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 格式化货币
const formatCurrency = (value) => {
  return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(value)
}

// 获取所有价格记录
const getAllPriceBooks = async () => {
  try {
    const response = await priceBookApi.getAllPriceBooks()
    priceBooks.value = response.data
    total.value = response.data.length
  } catch (error) {
    ElMessage.error('获取价格记录失败')
    console.error('获取价格记录失败:', error)
  }
}

// 按状态筛选价格记录
const filterPriceBooks = async () => {
  try {
    if (statusFilter.value) {
      const response = await priceBookApi.getPriceBooksByStatus(statusFilter.value)
      priceBooks.value = response.data
      total.value = response.data.length
    } else {
      getAllPriceBooks()
    }
  } catch (error) {
    ElMessage.error('筛选价格记录失败')
    console.error('筛选价格记录失败:', error)
  }
}

// 重置筛选条件
const resetFilter = () => {
  statusFilter.value = ''
  getAllPriceBooks()
}

// 打开新增对话框
const openAddDialog = () => {
  dialogTitle.value = '新增价格版本'
  formData.value = {
    id: '',
    productCode: '',
    productName: '',
    unitPrice: 0,
    effectiveDate: null,
    expireDate: null,
    status: 'ACTIVE'
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogTitle.value = '编辑价格记录'
  formData.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (formData.value.id) {
      // 更新价格记录
      await priceBookApi.updatePriceBook(formData.value.id, formData.value)
      ElMessage.success('更新价格记录成功')
    } else {
      // 创建新的价格版本
      await priceBookApi.createNewPriceVersion(formData.value)
      ElMessage.success('创建价格版本成功')
    }
    
    dialogVisible.value = false
    getAllPriceBooks()
  } catch (error) {
    if (error.name === 'Error') {
      ElMessage.error(error.message)
    } else {
      console.error('提交表单失败:', error)
    }
  }
}

// 删除价格记录
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条价格记录吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await priceBookApi.deletePriceBook(id)
    ElMessage.success('删除价格记录成功')
    getAllPriceBooks()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除价格记录失败')
      console.error('删除价格记录失败:', error)
    }
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  getAllPriceBooks()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  getAllPriceBooks()
}

// 页面加载时获取数据
onMounted(() => {
  getAllPriceBooks()
})
</script>

<style scoped>
.price-maintain {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
