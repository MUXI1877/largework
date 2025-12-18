<template>
  <div class="service-record">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>服务记录</span>
        </div>
      </template>

      <div class="table-toolbar">
        <el-input
          v-model="searchForm.serviceId"
          placeholder="工单ID"
          clearable
          style="width: 220px; margin-right: 10px"
          @keyup.enter="handleSearch"
        />
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          style="width: 320px; margin-right: 10px"
        />
        <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
        <el-button type="default" size="small" @click="handleReset">重置</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="service.id" label="工单ID" width="220">
          <template #default="scope">
            {{ scope.row.service ? scope.row.service.id : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="operator.name" label="服务人员" width="140">
          <template #default="scope">
            {{ scope.row.operator ? scope.row.operator.name : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="operateTime" label="服务时间" width="180" />
        <el-table-column prop="content" label="处理内容" show-overflow-tooltip />
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getServiceRecordList
} from '../api/service'

const rawData = ref([])
const tableData = ref([])

const searchForm = ref({
  serviceId: '',
  dateRange: []
})

const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const fetchAll = async () => {
  try {
    const res = await getServiceRecordList()
    rawData.value = res.data || []
    applyFilterAndPage()
  } catch (e) {
    ElMessage.error('加载服务记录失败')
  }
}

const applyFilterAndPage = () => {
  let list = [...rawData.value]
  if (searchForm.value.serviceId) {
    list = list.filter(
      (item) =>
        item.service &&
        item.service.id &&
        item.service.id.includes(searchForm.value.serviceId)
    )
  }
  if (searchForm.value.dateRange && searchForm.value.dateRange.length === 2) {
    const [start, end] = searchForm.value.dateRange
    list = list.filter((item) => {
      const d = new Date(item.operateTime)
      return d >= start && d <= end
    })
  }

  pagination.value.total = list.length
  const startIndex = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const endIndex = startIndex + pagination.value.pageSize
  tableData.value = list.slice(startIndex, endIndex)
}

const handleSearch = () => {
  pagination.value.currentPage = 1
  applyFilterAndPage()
}

const handleReset = () => {
  searchForm.value = {
    serviceId: '',
    dateRange: []
  }
  pagination.value.currentPage = 1
  applyFilterAndPage()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  applyFilterAndPage()
}

const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  applyFilterAndPage()
}

onMounted(() => {
  fetchAll()
})
</script>

<style scoped>
.service-record {
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
</style>


