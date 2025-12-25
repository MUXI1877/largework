<template>
  <div class="device-runtime-monitor">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>设备运行监视</span>
        </div>
      </template>
      
      <el-form :inline="true" class="search-form">
        <el-form-item label="设备ID">
          <el-input v-model="searchForm.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="searchForm.startTime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="searchForm.endTime" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="telemetryList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="metricKey" label="指标键" width="150" />
        <el-table-column prop="metricValue" label="指标值" width="120" />
        <el-table-column prop="collectTime" label="采集时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDeviceTelemetryList, getDeviceTelemetryByDeviceId, getDeviceTelemetryByTimeRange } from '../../api/device-telemetry'

const telemetryList = ref([])
const loading = ref(false)
const searchForm = ref({
  deviceId: '',
  startTime: null,
  endTime: null
})

const loadData = async () => {
  loading.value = true
  try {
    let res
    if (searchForm.value.deviceId && searchForm.value.startTime && searchForm.value.endTime) {
      res = await getDeviceTelemetryByTimeRange(
        searchForm.value.deviceId,
        searchForm.value.startTime,
        searchForm.value.endTime
      )
    } else if (searchForm.value.deviceId) {
      res = await getDeviceTelemetryByDeviceId(searchForm.value.deviceId)
    } else {
      res = await getDeviceTelemetryList()
    }
    telemetryList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  loadData()
}

const handleReset = () => {
  searchForm.value = {
    deviceId: '',
    startTime: null,
    endTime: null
  }
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.device-runtime-monitor {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>

