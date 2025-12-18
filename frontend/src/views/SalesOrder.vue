<template>
  <div class="sales-order">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售订单管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新建订单
          </el-button>
        </div>
      </template>
      
      <div class="table-toolbar">
        <el-input
          v-model="searchForm.orderNo"
          placeholder="订单编号"
          clearable
          style="width: 180px; margin-right: 10px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
          </template>
        </el-input>
        <el-select
          v-model="searchForm.status"
          placeholder="订单状态"
          style="width: 120px; margin-right: 10px"
        >
          <el-option label="全部" value="" />
          <el-option label="待付款" value="PENDING" />
          <el-option label="已付款" value="PAID" />
          <el-option label="已发货" value="SHIPPED" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-right: 10px"
        />
        <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
        <el-button type="default" size="small" @click="handleReset">重置</el-button>
      </div>
      
      <el-table :data="orderList" border style="width: 100%" stripe>
        <el-table-column prop="orderNo" label="订单编号" width="180" />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="orderDate" label="下单日期" width="120" />
        <el-table-column prop="totalAmount" label="订单金额" width="120" align="right">
          <template #default="scope">
            {{ scope.row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100" align="center">
          <template #default="scope">
            <el-tag
              :type="getStatusTagType(scope.row.status)"
            >
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleView(scope.row)"
              style="margin-right: 5px"
            >
              查看
            </el-button>
            <el-button
              type="success"
              size="small"
              @click="handleEdit(scope.row)"
              style="margin-right: 5px"
            >
              编辑
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleChangeStatus(scope.row)"
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

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="resetForm"
    >
      <el-form :model="formData" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="formData.orderNo" placeholder="自动生成" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="下单日期" prop="orderDate">
              <el-date-picker
                v-model="formData.orderDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单状态" prop="status">
              <el-select
                v-model="formData.status"
                placeholder="选择状态"
                style="width: 100%"
              >
                <el-option label="待付款" value="PENDING" />
                <el-option label="已付款" value="PAID" />
                <el-option label="已发货" value="SHIPPED" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
        
        <!-- 订单商品列表 -->
        <el-form-item label="商品明细">
          <el-table :data="orderItems" border style="width: 100%">
            <el-table-column prop="productName" label="商品名称" width="180" />
            <el-table-column prop="productCode" label="商品编码" width="120" />
            <el-table-column prop="quantity" label="数量" width="80" align="center" />
            <el-table-column prop="unitPrice" label="单价" width="100" align="right" />
            <el-table-column prop="totalPrice" label="金额" width="120" align="right" />
            <el-table-column label="操作" width="100" align="center">
              <template #default="scope">
                <el-button
                  type="danger"
                  size="small"
                  @click="handleDeleteItem(scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button type="primary" size="small" @click="handleAddItem">
            <el-icon><Plus /></el-icon> 添加商品
          </el-button>
        </el-form-item>
        
        <el-form-item label="订单总价" prop="totalAmount">
          <el-input
            v-model="formData.totalAmount"
            placeholder="自动计算"
            readonly
            style="width: 150px"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 添加商品对话框 -->
    <el-dialog
      v-model="itemDialogVisible"
      title="添加商品"
      width="500px"
      @close="resetItemForm"
    >
      <el-form :model="itemFormData" label-width="100px">
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="itemFormData.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品编码" prop="productCode">
          <el-input v-model="itemFormData.productCode" placeholder="请输入商品编码" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="itemFormData.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input v-model="itemFormData.unitPrice" type="number" placeholder="请输入单价" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="itemDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitItem">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { 
  getSalesOrderList, 
  getSalesOrderById, 
  saveSalesOrder, 
  updateSalesOrder, 
  deleteSalesOrder, 
  updateSalesOrderStatus
} from '../api/sales'

const orderList = ref([])
const dialogVisible = ref(false)
const itemDialogVisible = ref(false)
const dialogTitle = ref('新建订单')

const searchForm = ref({
  orderNo: '',
  status: '',
  dateRange: []
})

const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const formData = ref({
  id: '',
  orderNo: '',
  customerName: '',
  orderDate: '',
  totalAmount: 0,
  status: 'PENDING',
  remark: ''
})

const orderItems = ref([])
const itemFormData = ref({
  productName: '',
  productCode: '',
  quantity: 1,
  unitPrice: 0,
  totalPrice: 0
})

// 计算订单总价
const calculateTotalAmount = () => {
  formData.value.totalAmount = orderItems.value.reduce((sum, item) => {
    return sum + (item.totalPrice || 0)
  }, 0)
}

// 格式化状态标签类型
const getStatusTagType = (status) => {
  const typeMap = {
    PENDING: 'warning',
    PAID: 'info',
    SHIPPED: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return typeMap[status] || 'info'
}

// 格式化状态标签
const getStatusLabel = (status) => {
  const labelMap = {
    PENDING: '待付款',
    PAID: '已付款',
    SHIPPED: '已发货',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return labelMap[status] || status
}

const loadData = async () => {
  try {
    const response = await getSalesOrderList()
    const data = response.data || []
    
    // 过滤数据
    let filteredData = data
    if (searchForm.value.orderNo) {
      filteredData = filteredData.filter(item => 
        item.orderNo.includes(searchForm.value.orderNo)
      )
    }
    if (searchForm.value.status) {
      filteredData = filteredData.filter(item => 
        item.status === searchForm.value.status
      )
    }
    if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
      filteredData = filteredData.filter(item => {
        const orderDate = new Date(item.orderDate)
        return orderDate >= searchForm.value.dateRange[0] && orderDate <= searchForm.value.dateRange[1]
      })
    }
    
    // 分页
    pagination.value.total = filteredData.length
    const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
    const end = start + pagination.value.pageSize
    orderList.value = filteredData.slice(start, end)
  } catch (error) {
    ElMessage.error('加载订单数据失败')
  }
}

const handleSearch = () => {
  pagination.value.currentPage = 1
  loadData()
}

const handleReset = () => {
  searchForm.value = {
    orderNo: '',
    status: '',
    dateRange: []
  }
  pagination.value.currentPage = 1
  loadData()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.value.currentPage = current
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新建订单'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogTitle.value = '编辑订单'
  try {
    const response = await getSalesOrderById(row.id)
    const order = response.data
    formData.value = {
      ...order
    }
    orderItems.value = order.items || []
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载订单详情失败')
  }
}

const handleView = async (row) => {
  dialogTitle.value = '查看订单'
  try {
    const response = await getSalesOrderById(row.id)
    const order = response.data
    formData.value = {
      ...order
    }
    orderItems.value = order.items || []
    dialogVisible.value = true
    // 这里可以添加只读逻辑
  } catch (error) {
    ElMessage.error('加载订单详情失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSalesOrder(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleChangeStatus = (row) => {
  // 这里可以添加状态变更的逻辑
  ElMessage.info('状态变更功能待实现')
}

const handleAddItem = () => {
  resetItemForm()
  itemDialogVisible.value = true
}

const handleSubmitItem = () => {
  // 计算商品总价
  itemFormData.value.totalPrice = itemFormData.value.quantity * itemFormData.value.unitPrice
  orderItems.value.push({
    ...itemFormData.value
  })
  itemDialogVisible.value = false
  calculateTotalAmount()
}

const handleDeleteItem = (row) => {
  const index = orderItems.value.findIndex(item => item.id === row.id)
  if (index > -1) {
    orderItems.value.splice(index, 1)
    calculateTotalAmount()
  }
}

const handleSubmit = async () => {
  try {
    const orderData = {
      ...formData.value,
      items: orderItems.value
    }
    if (formData.value.id) {
      await updateSalesOrder(orderData)
      ElMessage.success('更新订单成功')
    } else {
      await saveSalesOrder(orderData)
      ElMessage.success('创建订单成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const resetForm = () => {
  formData.value = {
    id: '',
    orderNo: '',
    customerName: '',
    orderDate: '',
    totalAmount: 0,
    status: 'PENDING',
    remark: ''
  }
  orderItems.value = []
}

const resetItemForm = () => {
  itemFormData.value = {
    productName: '',
    productCode: '',
    quantity: 1,
    unitPrice: 0,
    totalPrice: 0
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.sales-order {
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