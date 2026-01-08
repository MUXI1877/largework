<template>
  <div class="product-inventory">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售库存查询</span>
          <div>
            <el-button type="success" v-permission="{ moduleId: 'm014', action: 'read' }" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="图号">
          <el-input v-model="queryForm.drawingNumber" placeholder="请输入图号" clearable />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryForm.name" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="是否呆滞">
          <el-select v-model="queryForm.isStagnant" placeholder="请选择" clearable>
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 库存列表 -->
      <el-table :data="productList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="drawingNumber" label="图号" width="150" />
        <el-table-column prop="name" label="名称" width="200" show-overflow-tooltip />
        <el-table-column prop="material" label="材料" width="150" />
        <el-table-column prop="quantity" label="数量" width="120">
          <template #default="scope">
            {{ scope.row.quantity || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="isStagnant" label="是否呆滞" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isStagnant ? 'danger' : 'success'">
              {{ scope.row.isStagnant ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expectedDeliveryDate" label="预计交货期" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { queryInventory, exportInventory } from '../api/product'
import { canRead, loadPermissions } from '../utils/permission'

const loading = ref(false)
const productList = ref([])

const queryForm = ref({
  drawingNumber: '',
  name: '',
  isStagnant: null
})

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (queryForm.value.drawingNumber) params.drawingNumber = queryForm.value.drawingNumber
    if (queryForm.value.name) params.name = queryForm.value.name
    if (queryForm.value.isStagnant !== null && queryForm.value.isStagnant !== undefined) {
      // 确保布尔值正确传递，false 也会被包含
      params.isStagnant = queryForm.value.isStagnant
    }

    console.log('查询参数:', params) // 调试日志
    const res = await queryInventory(params)
    productList.value = res.data || []
    console.log('查询结果数量:', productList.value.length) // 调试日志
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
    drawingNumber: '',
    name: '',
    isStagnant: null
  }
  loadData()
}

const handleExport = async () => {
  try {
    const params = {}
    if (queryForm.value.drawingNumber) params.drawingNumber = queryForm.value.drawingNumber
    if (queryForm.value.name) params.name = queryForm.value.name
    if (queryForm.value.isStagnant !== null && queryForm.value.isStagnant !== undefined) {
      // 确保布尔值正确传递，false 也会被包含
      params.isStagnant = queryForm.value.isStagnant
    }

    const res = await exportInventory(params)
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `库存查询列表_${new Date().getTime()}.xlsx`
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
.product-inventory {
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

