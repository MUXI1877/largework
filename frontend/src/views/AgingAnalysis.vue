<template>
  <div class="aging-analysis">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>应收账龄分析</span>
          <div>
            <el-date-picker
              v-model="asOfDate"
              type="date"
              placeholder="账龄统计日期"
              style="width: 200px; margin-right: 10px"
            />
            <el-button type="primary" size="small" @click="loadData">
              重新计算
            </el-button>
          </div>
        </div>
      </template>

      <div class="chart-row">
        <div class="chart-panel">
          <div class="panel-title">账龄分布（金额占比）</div>
          <div ref="pieRef" class="chart"></div>
        </div>
        <div class="chart-panel">
          <div class="panel-title">账龄分布（金额柱状）</div>
          <div ref="barRef" class="chart"></div>
        </div>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%; margin-top: 20px">
        <el-table-column prop="bucketLabel" label="账龄区间" width="160" />
        <el-table-column prop="amount" label="余额合计" width="160" align="right">
          <template #default="scope">
            {{ formatCurrency(scope.row.amount) }}
          </template>
        </el-table-column>
        <el-table-column prop="count" label="单据数量" width="120" align="center" />
        <el-table-column prop="percentage" label="占比" width="120" align="center">
          <template #default="scope">
            {{ (scope.row.percentage * 100).toFixed(2) }}%
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getReceivableList } from '../api/receivable'

const asOfDate = ref(new Date())
const buckets = [
  { key: '0_30', label: '0-30天', min: 0, max: 30 },
  { key: '31_60', label: '31-60天', min: 31, max: 60 },
  { key: '61_90', label: '61-90天', min: 61, max: 90 },
  { key: '91_180', label: '91-180天', min: 91, max: 180 },
  { key: '181_plus', label: '180天以上', min: 181, max: Infinity }
]

const tableData = ref([])
const pieRef = ref(null)
const barRef = ref(null)
let pieChart = null
let barChart = null

const formatCurrency = (value) => {
  const n = Number(value || 0)
  return n.toFixed(2)
}

const computeAging = (list) => {
  const baseDate = new Date(asOfDate.value)
  const result = {}
  buckets.forEach((b) => {
    result[b.key] = { bucketLabel: b.label, amount: 0, count: 0 }
  })

  list.forEach((item) => {
    const due = new Date(item.dueDate)
    const diffDays = Math.floor((baseDate - due) / (1000 * 60 * 60 * 24))
    // 只统计未结清和部分结清的余额
    const balance = Number(item.amount || 0) - Number(item.receivedAmount || 0)
    if (balance <= 0 || isNaN(diffDays)) {
      return
    }
    const days = diffDays < 0 ? 0 : diffDays
    const bucket = buckets.find((b) => days >= b.min && days <= b.max)
    if (bucket) {
      const r = result[bucket.key]
      r.amount += balance
      r.count += 1
    }
  })

  const arr = Object.values(result)
  const totalAmount = arr.reduce((sum, r) => sum + r.amount, 0)
  arr.forEach((r) => {
    r.percentage = totalAmount > 0 ? r.amount / totalAmount : 0
  })
  tableData.value = arr
  updateCharts()
}

const updateCharts = () => {
  const data = tableData.value || []
  const pieData = data.map((d) => ({
    name: d.bucketLabel,
    value: d.amount
  }))
  const barCategories = data.map((d) => d.bucketLabel)
  const barValues = data.map((d) => d.amount)

  if (pieChart && pieData.length) {
    pieChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [
        {
          name: '账龄金额',
          type: 'pie',
          radius: '65%',
          data: pieData
        }
      ]
    })
  }

  if (barChart && barCategories.length) {
    barChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: barCategories },
      yAxis: { type: 'value' },
      series: [
        {
          name: '余额',
          type: 'bar',
          data: barValues
        }
      ]
    })
  }
}

const loadData = async () => {
  try {
    const res = await getReceivableList()
    const list = res.data || []
    computeAging(list)
  } catch (e) {
    ElMessage.error('加载应收数据失败')
  }
}

onMounted(() => {
  if (pieRef.value) {
    pieChart = echarts.init(pieRef.value)
  }
  if (barRef.value) {
    barChart = echarts.init(barRef.value)
  }
  loadData()
  window.addEventListener('resize', () => {
    pieChart && pieChart.resize()
    barChart && barChart.resize()
  })
})

watch(asOfDate, () => {
  loadData()
})
</script>

<style scoped>
.aging-analysis {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.chart-panel {
  flex: 1;
  min-width: 280px;
}

.panel-title {
  font-size: 14px;
  margin-bottom: 8px;
  color: #606266;
}

.chart {
  width: 100%;
  height: 320px;
}
</style>


