<template>
  <div class="price-history">
    <div class="page-header">
      <h1>价格历史查询</h1>
    </div>

    <div class="search-section">
      <el-form :model="searchForm" ref="searchFormRef" label-width="120px" inline>
        <el-form-item label="产品代码" prop="productCode">
          <el-input v-model="searchForm.productCode" placeholder="请输入产品代码" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 当前价格信息 -->
    <div v-if="currentPrice" class="current-price-card">
      <h2>当前价格信息</h2>
      <div class="price-info">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="产品代码">{{ currentPrice.productCode }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ currentPrice.productName }}</el-descriptions-item>
          <el-descriptions-item label="当前单价">
            <span class="price-value">{{ formatCurrency(currentPrice.unitPrice) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ currentPrice.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="失效日期">{{ currentPrice.expireDate || '永久有效' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTypeMap[currentPrice.status]">
              {{ statusLabelMap[currentPrice.status] }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <!-- 价格历史记录 -->
    <div v-if="priceHistory.length > 0" class="history-section">
      <h2>价格历史记录</h2>
      <el-table :data="priceHistory" stripe style="width: 100%" border>
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
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 无数据提示 -->
    <div v-if="searchPerformed && priceHistory.length === 0" class="no-data">
      <el-empty description="暂无价格历史记录"></el-empty>
    </div>

    <!-- 详情对话框 -->
    <el-dialog
      title="价格记录详情"
      v-model="detailDialogVisible"
      width="600px"
    >
      <el-descriptions v-if="selectedPrice" :column="2" border>
        <el-descriptions-item label="ID">{{ selectedPrice.id }}</el-descriptions-item>
        <el-descriptions-item label="产品代码">{{ selectedPrice.productCode }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ selectedPrice.productName }}</el-descriptions-item>
        <el-descriptions-item label="单价">{{ formatCurrency(selectedPrice.unitPrice) }}</el-descriptions-item>
        <el-descriptions-item label="生效日期">{{ selectedPrice.effectiveDate }}</el-descriptions-item>
        <el-descriptions-item label="失效日期">{{ selectedPrice.expireDate || '永久有效' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusLabelMap[selectedPrice.status] }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ selectedPrice.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedPrice.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ selectedPrice.updateBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ selectedPrice.updateTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as priceBookApi from '../api/price-book'

const searchForm = reactive({
  productCode: ''
})

const searchFormRef = ref(null)
const priceHistory = ref([])
const currentPrice = ref(null)
const searchPerformed = ref(false)
const detailDialogVisible = ref(false)
const selectedPrice = ref(null)

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

// 格式化货币
const formatCurrency = (value) => {
  return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(value)
}

// 搜索价格历史
const handleSearch = async () => {
  if (!searchFormRef.value) return
  
  try {
    await searchFormRef.value.validate()
    
    // 获取当前价格
    const currentPriceResponse = await priceBookApi.getCurrentPriceByProductCode(searchForm.productCode)
    currentPrice.value = currentPriceResponse.data
    
    // 获取价格历史记录
    const historyResponse = await priceBookApi.getPriceHistoryByProductCode(searchForm.productCode)
    priceHistory.value = historyResponse.data
    
    searchPerformed.value = true
  } catch (error) {
    if (error.name === 'Error') {
      // 如果是 API 返回的错误，如找不到当前价格
      console.log('API 错误:', error.message)
      // 仍然尝试获取历史记录
      try {
        const historyResponse = await priceBookApi.getPriceHistoryByProductCode(searchForm.productCode)
        priceHistory.value = historyResponse.data
        currentPrice.value = null
        searchPerformed.value = true
        ElMessage.warning('未找到当前有效价格，但已显示历史记录')
      } catch (historyError) {
        ElMessage.error('查询价格历史失败')
        console.error('查询价格历史失败:', historyError)
      }
    } else {
      console.error('搜索失败:', error)
    }
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.productCode = ''
  priceHistory.value = []
  currentPrice.value = null
  searchPerformed.value = false
}

// 查看详情
const viewDetail = (row) => {
  selectedPrice.value = row
  detailDialogVisible.value = true
}
</script>

<style scoped>
.price-history {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.search-section {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.current-price-card {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f0f9eb;
  border-radius: 4px;
}

.current-price-card h2 {
  margin-bottom: 15px;
  color: #10b981;
  font-size: 18px;
}

.price-info {
  background-color: white;
  padding: 10px;
  border-radius: 4px;
}

.price-value {
  font-size: 24px;
  font-weight: bold;
  color: #ef4444;
}

.history-section {
  margin-bottom: 20px;
}

.history-section h2 {
  margin-bottom: 15px;
  color: #3b82f6;
  font-size: 18px;
}

.no-data {
  margin: 50px 0;
}

.dialog-footer {
  text-align: right;
}
</style>
