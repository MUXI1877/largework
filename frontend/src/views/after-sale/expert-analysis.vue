<template>
  <div class="expert-analysis-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>专家分析</span>
          <el-button 
            v-permission="{ moduleId: 'm048', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增分析
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" class="search-form">
        <el-form-item label="设备ID">
          <el-input v-model="searchForm.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="analysisList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="analysisType" label="分析类型" width="120" />
        <el-table-column prop="score" label="评分" width="100" />
        <el-table-column prop="conclusion" label="结论" min-width="200" show-overflow-tooltip />
        <el-table-column prop="analysisTime" label="分析时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              type="info" 
              size="small" 
              @click="handleViewDetail(scope.row)"
            >
              查看详情
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm048', action: 'delete' }"
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="分析详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备ID">{{ detailData.deviceId }}</el-descriptions-item>
        <el-descriptions-item label="分析类型">{{ detailData.analysisType }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ detailData.score }}</el-descriptions-item>
        <el-descriptions-item label="分析时间">{{ detailData.analysisTime }}</el-descriptions-item>
        <el-descriptions-item label="结论" :span="2">{{ detailData.conclusion }}</el-descriptions-item>
        <el-descriptions-item label="建议" :span="2">{{ detailData.suggestions }}</el-descriptions-item>
        <el-descriptions-item label="分析结果" :span="2">
          <pre>{{ detailData.analysisResult }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getExpertAnalysisList, getExpertAnalysisByDeviceId, deleteExpertAnalysis } from '../../api/expert-analysis'
import { canRead, loadPermissions } from '../../utils/permission'

const analysisList = ref([])
const detailData = ref({})
const loading = ref(false)
const detailDialogVisible = ref(false)
const searchForm = ref({
  deviceId: ''
})

const loadData = async () => {
  loading.value = true
  try {
    let res
    if (searchForm.value.deviceId) {
      res = await getExpertAnalysisByDeviceId(searchForm.value.deviceId)
    } else {
      res = await getExpertAnalysisList()
    }
    analysisList.value = res.data || []
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
  searchForm.value.deviceId = ''
  loadData()
}

const handleAdd = () => {
  ElMessage.info('请通过API接口添加分析结果')
}

const handleViewDetail = (row) => {
  detailData.value = { ...row }
  detailDialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该分析结果吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteExpertAnalysis(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.msg || '删除失败')
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.expert-analysis-management {
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

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>

