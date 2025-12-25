<template>
  <div class="reduced-stock-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售降库管理</span>
          <div>
            <el-button type="success" v-permission="{ moduleId: 'm016', action: 'read' }" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="口径">
          <el-input v-model="queryForm.caliber" placeholder="请输入口径" clearable />
        </el-form-item>
        <el-form-item label="电机功率">
          <el-input v-model="queryForm.motorPower" placeholder="请输入电机功率" clearable />
        </el-form-item>
        <el-form-item label="流量">
          <el-input v-model="queryForm.flow" placeholder="请输入流量" clearable />
        </el-form-item>
        <el-form-item label="扬程">
          <el-input v-model="queryForm.head" placeholder="请输入扬程" clearable />
        </el-form-item>
        <el-form-item label="过滤材质">
          <el-input v-model="queryForm.filterMaterial" placeholder="请输入过滤材质" clearable />
        </el-form-item>
        <el-form-item label="入口压力">
          <el-input v-model="queryForm.inletPressure" placeholder="请输入入口压力" clearable />
        </el-form-item>
        <el-form-item label="出口压力">
          <el-input v-model="queryForm.outletPressure" placeholder="请输入出口压力" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 降库产品列表 -->
      <el-table :data="productList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="drawingNumber" label="图号" width="150" />
        <el-table-column prop="name" label="名称" width="200" show-overflow-tooltip />
        <el-table-column prop="material" label="材料" width="150" />
        <el-table-column prop="quantity" label="数量" width="120">
          <template #default="scope">
            {{ scope.row.quantity || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="storageAge" label="库龄" width="120" />
        <el-table-column prop="isStagnant" label="是否呆滞" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isStagnant ? 'danger' : 'success'">
              {{ scope.row.isStagnant ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" v-permission="{ moduleId: 'm016', action: 'update' }" @click="handleMarkReduced(scope.row)">
              降库
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 标记降库对话框 -->
    <el-dialog v-model="markDialogVisible" title="标记降库" width="500px">
      <el-form :model="markForm" label-width="120px">
        <el-form-item label="产品名称">
          <el-input v-model="markForm.productName" disabled />
        </el-form-item>
        <el-form-item label="关联销售合同">
          <el-input v-model="markForm.contractId" placeholder="请输入合同ID（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="markDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveMark">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { canRead, loadPermissions } from '../utils/permission'
import {
  queryReducedStockProducts,
  exportReducedStockProducts,
  markReducedStock
} from '../api/product'

const loading = ref(false)
const productList = ref([])
const markDialogVisible = ref(false)
const markForm = ref({
  productId: '',
  productName: '',
  contractId: ''
})

const queryForm = ref({
  caliber: '',
  motorPower: '',
  flow: '',
  head: '',
  filterMaterial: '',
  inletPressure: '',
  outletPressure: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (queryForm.value.caliber) params.caliber = queryForm.value.caliber
    if (queryForm.value.motorPower) params.motorPower = queryForm.value.motorPower
    if (queryForm.value.flow) params.flow = queryForm.value.flow
    if (queryForm.value.head) params.head = queryForm.value.head
    if (queryForm.value.filterMaterial) params.filterMaterial = queryForm.value.filterMaterial
    if (queryForm.value.inletPressure) params.inletPressure = queryForm.value.inletPressure
    if (queryForm.value.outletPressure) params.outletPressure = queryForm.value.outletPressure

    const res = await queryReducedStockProducts(params)
    productList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.value = {
    caliber: '',
    motorPower: '',
    flow: '',
    head: '',
    filterMaterial: '',
    inletPressure: '',
    outletPressure: ''
  }
  loadData()
}

const handleMarkReduced = (row) => {
  markForm.value = {
    productId: row.id,
    productName: row.name,
    contractId: ''
  }
  markDialogVisible.value = true
}

const handleSaveMark = async () => {
  try {
    await markReducedStock(markForm.value.productId, {
      contractId: markForm.value.contractId
    })
    ElMessage.success('标记降库成功')
    markDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '标记失败')
  }
}

const handleExport = async () => {
  try {
    const params = {}
    if (queryForm.value.caliber) params.caliber = queryForm.value.caliber
    if (queryForm.value.motorPower) params.motorPower = queryForm.value.motorPower
    if (queryForm.value.flow) params.flow = queryForm.value.flow
    if (queryForm.value.head) params.head = queryForm.value.head
    if (queryForm.value.filterMaterial) params.filterMaterial = queryForm.value.filterMaterial
    if (queryForm.value.inletPressure) params.inletPressure = queryForm.value.inletPressure
    if (queryForm.value.outletPressure) params.outletPressure = queryForm.value.outletPressure

    const res = await exportReducedStockProducts(params)
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `降库产品列表_${new Date().getTime()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error(error.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  loadData()
})
</script>

<style scoped>
.reduced-stock-management {
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

