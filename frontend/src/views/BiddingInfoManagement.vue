<template>
  <div class="bidding-info-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>投标管理</span>
          <div>
            <el-button type="primary" v-permission="{ moduleId: 'm018', action: 'add' }" @click="handleAdd">新增</el-button>
            <el-button type="success" v-permission="{ moduleId: 'm018', action: 'read' }" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="投标名称">
          <el-input v-model="queryForm.biddingName" placeholder="请输入投标名称" clearable />
        </el-form-item>
        <el-form-item label="投标类型">
          <el-select v-model="queryForm.biddingType" placeholder="请选择投标类型" clearable>
            <el-option label="公开招标" value="公开招标" />
            <el-option label="邀请招标" value="邀请招标" />
            <el-option label="竞争性谈判" value="竞争性谈判" />
            <el-option label="单一来源" value="单一来源" />
          </el-select>
        </el-form-item>
        <el-form-item label="投标状态">
          <el-select v-model="queryForm.biddingStatus" placeholder="请选择投标状态" clearable>
            <el-option label="待审批" value="待审批" />
            <el-option label="已审批" value="已审批" />
            <el-option label="已归档" value="已归档" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 投标信息列表 -->
      <el-table :data="biddingList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="biddingCode" label="投标编号" width="150" />
        <el-table-column prop="biddingName" label="投标名称" width="200" show-overflow-tooltip />
        <el-table-column prop="biddingType" label="投标类型" width="120" />
        <el-table-column prop="customerName" label="投标客户" width="200" />
        <el-table-column prop="opportunityName" label="机会名称" width="200" show-overflow-tooltip />
        <el-table-column prop="opportunityCode" label="机会编号" width="150" />
        <el-table-column prop="projectName" label="项目名称" width="200" show-overflow-tooltip />
        <el-table-column prop="technicalSolution" label="技术方案" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.technicalSolution ? 'success' : 'info'" size="small">
              {{ scope.row.technicalSolution ? '已上传' : '未上传' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quotationFile" label="报价单" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.quotationFile ? 'success' : 'info'" size="small">
              {{ scope.row.quotationFile ? '已上传' : '未上传' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="biddingStatus" label="投标状态" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" v-permission="{ moduleId: 'm018', action: 'read' }" @click="handleView(scope.row)">查看</el-button>
            <el-button type="warning" size="small" v-permission="{ moduleId: 'm018', action: 'update' }" @click="handleEdit(scope.row)">修改</el-button>
            <el-button type="success" size="small" v-permission="{ moduleId: 'm018', action: 'update' }" @click="handleSummary(scope.row)">总结</el-button>
            <el-button type="danger" size="small" v-permission="{ moduleId: 'm018', action: 'update' }" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 投标信息编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="投标名称" prop="biddingName">
              <el-input v-model="formData.biddingName" placeholder="请输入投标名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投标类型" prop="biddingType">
              <el-select v-model="formData.biddingType" placeholder="请选择投标类型" style="width: 100%">
                <el-option label="公开招标" value="公开招标" />
                <el-option label="邀请招标" value="邀请招标" />
                <el-option label="竞争性谈判" value="竞争性谈判" />
                <el-option label="单一来源" value="单一来源" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="关联销售机会">
              <el-select
                v-model="formData.opportunityId"
                placeholder="请选择销售机会"
                filterable
                @change="handleOpportunityChange"
                style="width: 100%"
              >
                <el-option
                  v-for="opp in opportunityList"
                  :key="opp.id"
                  :label="opp.opportunityName"
                  :value="opp.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机会编号">
              <el-input v-model="formData.opportunityCode" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="技术方案（附件）">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleTechSolutionSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">上传技术方案</el-button>
            <span v-if="formData.technicalSolution" style="margin-left: 10px; color: green">
              已上传
            </span>
          </el-upload>
        </el-form-item>
        <el-form-item label="报价单（附件）">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleQuotationSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">上传报价单</el-button>
            <span v-if="formData.quotationFile" style="margin-left: 10px; color: green">
              已上传
            </span>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="formData.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 投标总结对话框 -->
    <el-dialog v-model="summaryDialogVisible" title="投标总结" width="800px">
      <el-form :model="summaryForm" label-width="120px">
        <el-form-item label="投标结果" prop="biddingResult">
          <el-select v-model="summaryForm.biddingResult" placeholder="请选择投标结果" style="width: 100%">
            <el-option label="已中" value="已中" />
            <el-option label="未中" value="未中" />
            <el-option label="流标" value="流标" />
            <el-option label="废标" value="废标" />
          </el-select>
        </el-form-item>
        <el-form-item label="投标总结" prop="biddingSummary">
          <el-input
            v-model="summaryForm.biddingSummary"
            type="textarea"
            :rows="5"
            placeholder="请输入投标总结"
          />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleSummaryAttachmentSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">上传附件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="summaryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSummary">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getBiddingInfoList,
  getBiddingInfoById,
  saveBiddingInfo,
  deleteBiddingInfo,
  submitSummary,
  exportBiddingInfos
} from '../api/biddingInfo'
import { getProjectOpportunityList } from '../api/projectOpportunity'
import { getToken } from '../utils/auth'

const loading = ref(false)
const biddingList = ref([])
const opportunityList = ref([])
const dialogVisible = ref(false)
const summaryDialogVisible = ref(false)
const dialogTitle = ref('新增投标信息')
const formRef = ref(null)
const currentBidding = ref(null)

const uploadUrl = ref('/api/upload')
const uploadHeaders = ref({
  Authorization: `Bearer ${getToken()}`
})

const queryForm = ref({
  biddingName: '',
  biddingType: '',
  biddingStatus: '',
  customerName: ''
})

const formData = ref({
  id: null,
  biddingName: '',
  biddingType: '',
  opportunityId: '',
  opportunityName: '',
  opportunityCode: '',
  customerName: '',
  projectName: '',
  technicalSolution: '',
  quotationFile: '',
  remarks: ''
})

const summaryForm = ref({
  biddingResult: '',
  biddingSummary: '',
  attachment: ''
})

const rules = {
  biddingName: [{ required: true, message: '请输入投标名称', trigger: 'blur' }],
  biddingType: [{ required: true, message: '请选择投标类型', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (queryForm.value.biddingName) params.biddingName = queryForm.value.biddingName
    if (queryForm.value.biddingType) params.biddingType = queryForm.value.biddingType
    if (queryForm.value.biddingStatus) params.biddingStatus = queryForm.value.biddingStatus
    if (queryForm.value.customerName) params.customerName = queryForm.value.customerName

    const res = await getBiddingInfoList(params)
    biddingList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadOpportunities = async () => {
  try {
    const res = await getProjectOpportunityList({})
    opportunityList.value = res.data || []
  } catch (error) {
    console.error('加载销售机会列表失败', error)
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.value = {
    biddingName: '',
    biddingType: '',
    biddingStatus: '',
    customerName: ''
  }
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增投标信息'
  formData.value = {
    id: null,
    biddingName: '',
    biddingType: '',
    opportunityId: '',
    opportunityName: '',
    opportunityCode: '',
    customerName: '',
    projectName: '',
    technicalSolution: '',
    quotationFile: '',
    remarks: ''
  }
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogTitle.value = '修改投标信息'
  try {
    const res = await getBiddingInfoById(row.id)
    formData.value = res.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  }
}

const handleView = async (row) => {
  await handleEdit(row)
  // 可以添加只读模式
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条投标信息吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBiddingInfo(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleSummary = (row) => {
  currentBidding.value = row
  summaryForm.value = {
    biddingResult: '',
    biddingSummary: '',
    attachment: ''
  }
  summaryDialogVisible.value = true
}

const handleOpportunityChange = (opportunityId) => {
  const opportunity = opportunityList.value.find(o => o.id === opportunityId)
  if (opportunity) {
    formData.value.opportunityName = opportunity.opportunityName
    formData.value.opportunityCode = opportunity.opportunityCode
    formData.value.customerName = opportunity.customerName
    formData.value.projectName = opportunity.projectName
  }
}

const handleTechSolutionSuccess = (response) => {
  if (response.code === 200) {
    formData.value.technicalSolution = response.data.filePath
    ElMessage.success('上传成功')
  }
}

const handleQuotationSuccess = (response) => {
  if (response.code === 200) {
    formData.value.quotationFile = response.data.filePath
    ElMessage.success('上传成功')
  }
}

const handleSummaryAttachmentSuccess = (response) => {
  if (response.code === 200) {
    summaryForm.value.attachment = response.data.filePath
    ElMessage.success('上传成功')
  }
}

const beforeUpload = () => {
  return true
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveBiddingInfo(formData.value)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleSaveSummary = async () => {
  try {
    await submitSummary(currentBidding.value.id, summaryForm.value)
    ElMessage.success('投标总结提交成功')
    summaryDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '提交失败')
  }
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const handleExport = async () => {
  try {
    const params = {}
    if (queryForm.value.biddingName) params.biddingName = queryForm.value.biddingName
    if (queryForm.value.biddingType) params.biddingType = queryForm.value.biddingType
    if (queryForm.value.biddingStatus) params.biddingStatus = queryForm.value.biddingStatus
    if (queryForm.value.customerName) params.customerName = queryForm.value.customerName

    const res = await exportBiddingInfos(params)
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `投标信息列表_${new Date().getTime()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error(error.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  loadOpportunities()
  loadData()
})
</script>

<style scoped>
.bidding-info-management {
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

