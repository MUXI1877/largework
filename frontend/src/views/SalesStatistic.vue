<template>
  <div class="sales-statistic">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售统计报表</span>
        </div>
      </template>
      
      <div class="statistic-toolbar">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-right: 10px"
        />
        <el-button type="primary" size="small" @click="handleQuery">
          <el-icon><Search /></el-icon> 查询数据
        </el-button>
        <el-button type="success" size="small" @click="handleExport">
          <el-icon><Download /></el-icon> 导出报表
        </el-button>
      </div>
      
      <!-- 统计卡片区域 -->
      <div class="statistic-cards">
        <el-card shadow="hover" class="statistic-card">
          <div class="statistic-card-content">
            <div class="statistic-label">销售总额</div>
            <div class="statistic-value">{{ formatCurrency(totalAmount) }}</div>
            <div class="statistic-desc">已成交订单总金额</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="statistic-card">
          <div class="statistic-card-content">
            <div class="statistic-label">订单总数</div>
            <div class="statistic-value">{{ orderCount }}</div>
            <div class="statistic-desc">所有状态订单数量</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="statistic-card">
          <div class="statistic-card-content">
            <div class="statistic-label">成交订单</div>
            <div class="statistic-value">{{ completedOrderCount }}</div>
            <div class="statistic-desc">已完成的订单数量</div>
          </div>
        </el-card>
        <el-card shadow="hover" class="statistic-card">
          <div class="statistic-card-content">
            <div class="statistic-label">平均订单</div>
            <div class="statistic-value">{{ formatCurrency(averageOrderAmount) }}</div>
            <div class="statistic-desc">平均每笔订单金额</div>
          </div>
        </el-card>
      </div>
      
      <!-- 订单状态分布 -->
      <div class="chart-section">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>订单状态分布</span>
            </div>
          </template>
          <div class="chart-container">
            <el-table :data="statusCountData" border style="width: 100%">
              <el-table-column prop="status" label="订单状态" width="120">
                <template #default="scope">
                  <el-tag :type="getStatusTagType(scope.row.status)">
                    {{ getStatusLabel(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="count" label="数量" width="100" align="center" />
              <el-table-column prop="percentage" label="占比" width="120" align="center">
                <template #default="scope">
                  {{ (scope.row.percentage * 100).toFixed(2) }}%
                </template>
              </el-table-column>
            </el-table>
            <div class="chart-placeholder">
              <el-empty description="暂无数据" />
              <!-- 这里可以添加图表组件，如ECharts等 -->
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 日销售报表 -->
      <div class="chart-section">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>日销售趋势</span>
            </div>
          </template>
          <div class="chart-container">
            <el-table :data="dailyReportData" border style="width: 100%">
              <el-table-column prop="date" label="日期" width="120" />
              <el-table-column prop="amount" label="销售金额" width="150" align="right">
                <template #default="scope">
                  {{ formatCurrency(scope.row.amount) }}
                </template>
              </el-table-column>
              <el-table-column prop="orderCount" label="订单数量" width="120" align="center" />
            </el-table>
            <div class="chart-placeholder">
              <el-empty description="暂无数据" />
              <!-- 这里可以添加图表组件，如ECharts等 -->
            </div>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Download } from '@element-plus/icons-vue'
import { 
  getTotalSalesAmount, 
  getOrderStatusCount, 
  getDailySalesReport
} from '../api/sales'

const dateRange = ref([])

// 统计数据
const totalAmount = ref(0)
const orderCount = ref(0)
const completedOrderCount = ref(0)
const averageOrderAmount = ref(0)
const statusCountData = ref([])
const dailyReportData = ref([])

// 格式化货币
const formatCurrency = (amount) => {
  return (amount || 0).toFixed(2)
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

// 计算平均订单金额
const calculateAverageOrderAmount = () => {
  if (orderCount.value === 0) {
    return 0
  }
  return totalAmount.value / orderCount.value
}

// 查询统计数据
const handleQuery = async () => {
  try {
    const [startDate, endDate] = dateRange.value
    
    // 获取销售总额
    const totalAmountResponse = await getTotalSalesAmount(startDate, endDate)
    totalAmount.value = totalAmountResponse.data
    
    // 获取订单状态统计
    const statusCountResponse = await getOrderStatusCount()
    const statusCount = statusCountResponse.data || {}
    
    // 计算总订单数和成交订单数
    const counts = Object.values(statusCount)
    orderCount.value = counts.reduce((sum, count) => sum + count, 0)
    completedOrderCount.value = statusCount.COMPLETED || 0
    
    // 计算占比
    statusCountData.value = Object.entries(statusCount).map(([status, count]) => ({
      status,
      count,
      percentage: orderCount.value > 0 ? count / orderCount.value : 0
    }))
    
    // 获取日销售报表
    const dailyReportResponse = await getDailySalesReport(startDate, endDate)
    const dailyReport = dailyReportResponse.data || {}
    dailyReportData.value = Object.entries(dailyReport).map(([date, amount]) => ({
      date,
      amount,
      orderCount: 0 // 这里可以扩展获取每日订单数
    }))
    
    // 计算平均订单金额
    averageOrderAmount.value = calculateAverageOrderAmount()
    
    ElMessage.success('数据查询成功')
  } catch (error) {
    ElMessage.error(error.message || '数据查询失败')
  }
}

// 导出报表
const handleExport = () => {
  ElMessage.info('报表导出功能待实现')
}

onMounted(() => {
  // 初始化时查询默认日期范围的数据
  // 这里可以设置默认日期范围，比如最近30天
  handleQuery()
})
</script>

<style scoped>
.sales-statistic {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistic-toolbar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.statistic-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.statistic-card {
  flex: 1;
  min-width: 200px;
}

.statistic-card-content {
  text-align: center;
}

.statistic-label {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-desc {
  font-size: 12px;
  color: #909399;
}

.chart-section {
  margin-bottom: 20px;
}

.chart-container {
  display: flex;
  gap: 20px;
}

.chart-container > * {
  flex: 1;
  min-width: 0;
}

.chart-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style>