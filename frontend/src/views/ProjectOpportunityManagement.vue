<template>
  <div class="project-opportunity-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售机会管理</span>
          <div>
            <el-button type="primary" v-permission="{ moduleId: 'm013', action: 'add' }" @click="handleAdd">新增</el-button>
            <el-button type="success" v-permission="{ moduleId: 'm013', action: 'read' }" @click="handleExport">导出</el-button>
          </div>
        </div>
      </template>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="机会名称">
          <el-input v-model="queryForm.opportunityName" placeholder="请输入机会名称" clearable />
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="草稿" />
            <!-- 普通用户可以看到已提交状态（查看自己提交的机会） -->
            <el-option label="已提交" value="已提交" />
            <!-- 普通用户可以看到已关闭状态（用于查看被系统管理员关闭的机会） -->
            <el-option label="已关闭" value="已关闭" />
            <!-- 超级管理员可以看到已上传状态 -->
            <el-option v-if="isSuperAdmin()" label="已上传" value="已上传" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 机会列表 -->
      <el-table :data="opportunityList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="opportunityDate" label="销售机会日期" width="120" />
        <el-table-column prop="opportunityName" label="销售机会主题" width="200" show-overflow-tooltip />
        <el-table-column prop="inventory" label="存货" width="150" />
        <el-table-column prop="budget" label="预算" width="120">
          <template #default="scope">
            {{ scope.row.budget ? `¥${scope.row.budget}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="source" label="线索来源" width="120" />
        <el-table-column prop="receiveStatus" label="接收状态" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" v-permission="{ moduleId: 'm013', action: 'read' }" @click="handleView(scope.row)">查看</el-button>
            <!-- 普通用户：只能修改和删除自己创建的、未提交的机会 -->
            <el-button
              v-if="!scope.row.isSubmitted && (isCommonUser() ? canEdit(scope.row) : true)"
              v-permission="{ moduleId: 'm013', action: 'update' }"
              type="warning"
              size="small"
              @click="handleEdit(scope.row)"
            >
              修改
            </el-button>
            <!-- 普通用户：只能提交自己创建的、未提交的机会 -->
            <el-button
              v-if="!scope.row.isSubmitted && (isCommonUser() ? canEdit(scope.row) : true)"
              v-permission="{ moduleId: 'm013', action: 'update' }"
              type="success"
              size="small"
              @click="handleSubmit(scope.row)"
            >
              提交
            </el-button>
            <!-- 系统管理员和超级管理员可以传递片区和分配员工 -->
            <el-button 
              v-if="!isCommonUser()"
              type="info" 
              size="small" 
              v-permission="{ moduleId: 'm013', action: 'update' }" 
              @click="handleTransfer(scope.row)"
            >
              传递片区
            </el-button>
            <el-button 
              v-if="!isCommonUser()"
              type="info" 
              size="small" 
              v-permission="{ moduleId: 'm013', action: 'update' }" 
              @click="handleAssign(scope.row)"
            >
              分配员工
            </el-button>
            <!-- 系统管理员可以向超级管理员上传机会 -->
            <el-button 
              v-if="isSystemAdmin() && scope.row.isSubmitted && scope.row.status !== '已关闭' && scope.row.status !== '已上传'"
              type="primary" 
              size="small" 
              v-permission="{ moduleId: 'm013', action: 'update' }" 
              @click="handleUpload(scope.row)"
            >
              上传
            </el-button>
            <!-- 只有系统管理员和超级管理员可以关闭机会 -->
            <el-button 
              v-if="(isSystemAdmin() || isSuperAdmin()) && scope.row.status !== '已关闭'"
              type="warning" 
              size="small" 
              v-permission="{ moduleId: 'm013', action: 'update' }" 
              @click="handleClose(scope.row)"
            >
              关闭机会
            </el-button>
            <!-- 跟踪按钮：所有角色都可以跟踪，包括已关闭的机会 -->
            <el-button 
              type="primary" 
              size="small" 
              v-permission="{ moduleId: 'm013', action: 'read' }" 
              @click="handleTrack(scope.row)"
            >
              跟踪
            </el-button>
            <!-- 普通用户：只能删除自己创建的、未提交的机会 -->
            <el-button
              v-if="!scope.row.isSubmitted && (isCommonUser() ? canEdit(scope.row) : true)"
              v-permission="{ moduleId: 'm013', action: 'update' }"
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

    <!-- 机会编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      @close="handleDialogClose"
    >
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="formData" :rules="rules" ref="formRef" label-width="140px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="机会名称" prop="opportunityName">
                  <el-input v-model="formData.opportunityName" placeholder="请输入机会名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="项目名称" prop="projectName">
                  <el-input v-model="formData.projectName" placeholder="请输入项目名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="阶段">
                  <el-input v-model="formData.stage" placeholder="请输入阶段" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="客户">
                  <el-select
                    v-model="formData.customerId"
                    placeholder="请选择客户（可输入临时客户名称）"
                    filterable
                    allow-create
                    @change="handleCustomerChange"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="customer in customerList"
                      :key="customer.id"
                      :label="customer.unitName"
                      :value="customer.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="客户名称">
                  <el-input v-model="formData.customerName" placeholder="支持临时客户名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预算">
                  <el-input-number
                    v-model="formData.budget"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="行业">
                  <el-input v-model="formData.industry" placeholder="请输入行业" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="销售机会日期" prop="opportunityDate">
                  <el-date-picker
                    v-model="formData.opportunityDate"
                    type="date"
                    placeholder="选择日期"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="线索来源">
                  <el-input v-model="formData.source" placeholder="请输入线索来源" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="存货">
                  <el-input v-model="formData.inventory" placeholder="请输入存货" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="片区（可多选）">
              <el-select
                v-model="formData.regions"
                multiple
                placeholder="请选择片区"
                style="width: 100%"
              >
                <el-option
                  v-for="region in regionList"
                  :key="region.id"
                  :label="region.regionName"
                  :value="region.id"
                />
              </el-select>
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
        </el-tab-pane>
        <el-tab-pane label="客户关键人物" name="keyPersons">
          <div style="margin-bottom: 10px">
            <el-button type="primary" size="small" @click="handleAddKeyPerson">添加</el-button>
            <el-button type="danger" size="small" @click="handleDeleteSelectedKeyPersons" :disabled="selectedKeyPersons.length === 0">批量删除</el-button>
          </div>
          <el-table :data="keyPersonList" border @selection-change="handleKeyPersonSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column label="联系人编码" width="150">
              <template #default="scope">
                <el-input
                  v-model="scope.row.contactCode"
                  placeholder="请输入联系人编码"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="联系人名称" width="180">
              <template #default="scope">
                <el-input
                  v-model="scope.row.contactName"
                  placeholder="请输入联系人名称"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="称呼" width="120">
              <template #default="scope">
                <el-input
                  v-model="scope.row.title"
                  placeholder="请输入称呼"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="职务" width="150">
              <template #default="scope">
                <el-input
                  v-model="scope.row.position"
                  placeholder="请输入职务"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="备注">
              <template #default="scope">
                <el-input
                  v-model="scope.row.remarks"
                  placeholder="请输入备注"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="scope">
                <el-button
                  type="danger"
                  size="small"
                  @click="handleDeleteKeyPerson(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 传递片区对话框 -->
    <el-dialog v-model="transferDialogVisible" title="请选择片区" width="600px">
      <el-table :data="regionList" border style="width: 100%" ref="regionTableRef">
        <el-table-column type="selection" width="55" :reserve-selection="true" />
        <el-table-column prop="regionName" label="片区名称" width="200" />
        <el-table-column prop="manager" label="片区负责人" width="150" />
        <el-table-column prop="contactInfo" label="联系方式" width="150" />
      </el-table>
      <template #footer>
        <el-button @click="transferDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTransfer">传递</el-button>
      </template>
    </el-dialog>

    <!-- 分配员工对话框 -->
    <el-dialog v-model="assignDialogVisible" title="请选择员工" width="700px">
      <el-table :data="employeeList" border style="width: 100%" ref="employeeTableRef">
        <el-table-column type="selection" width="55" :reserve-selection="true" />
        <el-table-column prop="name" label="员工名称" width="150" />
        <el-table-column prop="contactInfo" label="员工联系方式" width="150" />
        <el-table-column prop="directSupervisor" label="直属上级" width="120" />
        <el-table-column prop="supervisorContact" label="上级联系方式" width="150" />
      </el-table>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAssign">分配</el-button>
      </template>
    </el-dialog>

    <!-- 跟踪记录对话框 -->
    <el-dialog v-model="trackDialogVisible" title="机会跟踪" width="800px">
      <el-form :model="trackingData" label-width="120px">
        <el-form-item label="拜访时间">
          <el-date-picker
            v-model="trackingData.visitTime"
            type="datetime"
            placeholder="选择拜访时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="拜访人">
          <el-input v-model="trackingData.visitor" placeholder="请输入拜访人" />
        </el-form-item>
        <el-form-item label="事宜">
          <el-input
            v-model="trackingData.matter"
            type="textarea"
            :rows="3"
            placeholder="请输入事宜"
          />
        </el-form-item>
        <el-form-item label="情况说明">
          <el-input
            v-model="trackingData.description"
            type="textarea"
            :rows="5"
            placeholder="请输入情况说明"
          />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <el-button type="primary">上传附件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="trackingData.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="trackDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTracking">保存</el-button>
      </template>
    </el-dialog>

    <!-- 关闭机会对话框 -->
    <el-dialog v-model="closeDialogVisible" title="关闭机会原因填写" width="600px">
      <el-form :model="closeReasonData" label-width="80px">
        <el-form-item label="原因">
          <el-input
            v-model="closeReasonData.reason"
            type="textarea"
            :rows="8"
            placeholder="请输入关闭机会的原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="closeDialogVisible = false">返回</el-button>
        <el-button type="primary" @click="handleSaveClose">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看信息对话框 -->
    <el-dialog v-model="viewDialogVisible" title="查看信息" width="900px">
      <el-tabs>
        <el-tab-pane label="基本信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="机会名称">{{ currentOpportunity?.opportunityName }}</el-descriptions-item>
            <el-descriptions-item label="项目名称">{{ currentOpportunity?.projectName }}</el-descriptions-item>
            <el-descriptions-item label="阶段">{{ currentOpportunity?.stage }}</el-descriptions-item>
            <el-descriptions-item label="客户">{{ currentOpportunity?.customerName }}</el-descriptions-item>
            <el-descriptions-item label="预算">{{ currentOpportunity?.budget }}</el-descriptions-item>
            <el-descriptions-item label="行业">{{ currentOpportunity?.industry }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="跟踪记录">
          <el-table :data="trackingList" border>
            <el-table-column prop="visitTime" label="拜访时间" width="180" />
            <el-table-column prop="visitor" label="拜访人" width="120" />
            <el-table-column prop="matter" label="事宜" width="200" />
            <el-table-column prop="description" label="情况说明" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getProjectOpportunityList,
  saveProjectOpportunity,
  deleteProjectOpportunity,
  submitProjectOpportunity,
  uploadToSuperAdmin,
  transferRegions,
  assignEmployees,
  closeOpportunity,
  saveTracking,
  getTrackings,
  getKeyPersons,
  saveKeyPersons,
  exportProjectOpportunities
} from '../api/projectOpportunity'
import { getCustomerList } from '../api/customer'
import { getSalesRegionList } from '../api/team'
import { getSalesPersonList } from '../api/team'
import { filterValidCustomers } from '../utils/customer'
import { getToken, isCommonUser, isSystemAdmin, isSuperAdmin } from '../utils/auth'
import { canRead, loadPermissions } from '../utils/permission'

const loading = ref(false)
const opportunityList = ref([])
const customerList = ref([])
const regionList = ref([])
const employeeList = ref([])
const dialogVisible = ref(false)
const transferDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const trackDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const closeDialogVisible = ref(false)
const dialogTitle = ref('新增销售机会')
const activeTab = ref('basic')
const dateRange = ref(null)
const formRef = ref(null)
const regionTableRef = ref(null)
const employeeTableRef = ref(null)
const currentOpportunity = ref(null)
const trackingList = ref([])
const selectedRegions = ref([])
const selectedEmployees = ref([])
const keyPersonList = ref([])
const selectedKeyPersons = ref([])
const closeReasonData = ref({
  reason: ''
})

const uploadUrl = ref('/api/upload')
const uploadHeaders = ref({
  Authorization: `Bearer ${getToken()}`
})

const queryForm = ref({
  opportunityName: '',
  startDate: null,
  endDate: null,
  status: ''
})

const formData = ref({
  id: null,
  opportunityName: '',
  projectName: '',
  stage: '',
  customerId: '',
  customerName: '',
  budget: null,
  industry: '',
  regions: [],
  opportunityDate: '',
  source: '',
  inventory: '',
  remarks: ''
})

const trackingData = ref({
  opportunityId: '',
  visitTime: '',
  visitor: '',
  matter: '',
  description: '',
  attachment: '',
  remarks: ''
})

const rules = {
  opportunityName: [{ required: true, message: '请输入机会名称', trigger: 'blur' }],
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  opportunityDate: [{ required: true, message: '请选择销售机会日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (queryForm.value.opportunityName) params.opportunityName = queryForm.value.opportunityName
    if (queryForm.value.startDate) params.startDate = queryForm.value.startDate
    if (queryForm.value.endDate) params.endDate = queryForm.value.endDate
    if (queryForm.value.status) params.status = queryForm.value.status

    const res = await getProjectOpportunityList(params)
    opportunityList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadCustomers = async () => {
  try {
    const res = await getCustomerList()
    let data = []
    
    if (res && res.code === 200) {
      data = res.data || []
    } else if (res && res.data) {
      data = Array.isArray(res.data) ? res.data : []
    }
    
    // 过滤掉没有有效ID的客户（这些是未保存到数据库的记录）
    customerList.value = filterValidCustomers(data)
    
    console.log('加载的客户列表（已过滤无效数据）:', customerList.value.length, '条')
  } catch (error) {
    console.error('加载客户列表失败', error)
    customerList.value = []
  }
}

const loadRegions = async () => {
  try {
    const res = await getSalesRegionList()
    regionList.value = res.data || []
  } catch (error) {
    console.error('加载片区列表失败', error)
  }
}

const loadEmployees = async () => {
  try {
    const res = await getSalesPersonList()
    employeeList.value = res.data || []
  } catch (error) {
    console.error('加载员工列表失败', error)
  }
}

const handleDateChange = (dates) => {
  if (dates && dates.length === 2) {
    queryForm.value.startDate = dates[0]
    queryForm.value.endDate = dates[1]
  } else {
    queryForm.value.startDate = null
    queryForm.value.endDate = null
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.value = {
    opportunityName: '',
    startDate: null,
    endDate: null,
    status: ''
  }
  dateRange.value = null
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增销售机会'
  formData.value = {
    id: null,
    opportunityName: '',
    projectName: '',
    stage: '',
    customerId: '',
    customerName: '',
    budget: null,
    industry: '',
    regions: [],
    opportunityDate: '',
    source: '',
    inventory: '',
    remarks: ''
  }
  keyPersonList.value = []
  activeTab.value = 'basic'
  dialogVisible.value = true
}

// 判断普通用户是否可以编辑该机会（只能编辑自己创建的）
const canEdit = (row) => {
  if (!isCommonUser()) {
    return true
  }
  // 普通用户只能编辑自己创建的机会（通过creatorId判断，但前端无法直接获取，所以通过其他方式判断）
  // 由于后端已经过滤了数据，前端显示的都是可以操作的
  return true
}

const handleEdit = async (row) => {
  if (row.isSubmitted) {
    ElMessage.warning('已提交的销售机会不可修改')
    return
  }
  // 普通用户只能修改自己创建的机会
  if (isCommonUser() && !canEdit(row)) {
    ElMessage.warning('只能修改自己创建的销售机会')
    return
  }
  dialogTitle.value = '修改销售机会'
  formData.value = { ...row }
  if (row.regions) {
    formData.value.regions = row.regions.split(',').filter(r => r)
  }
  
  // 加载客户关键人物
  try {
    const res = await getKeyPersons(row.id)
    keyPersonList.value = res.data || []
  } catch (error) {
    console.error('加载客户关键人物失败', error)
    keyPersonList.value = []
  }
  
  activeTab.value = 'basic'
  dialogVisible.value = true
}

const handleView = async (row) => {
  currentOpportunity.value = row
  try {
    const res = await getTrackings(row.id)
    trackingList.value = res.data || []
    viewDialogVisible.value = true
  } catch (error) {
    ElMessage.error(error.message || '加载跟踪记录失败')
  }
}

const handleDelete = (row) => {
  if (row.isSubmitted) {
    ElMessage.warning('已提交的销售机会不可删除')
    return
  }
  ElMessageBox.confirm('确定要删除这条销售机会吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteProjectOpportunity(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleSubmit = (row) => {
  ElMessageBox.confirm('确定要提交这条销售机会吗？提交后不可修改删除', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await submitProjectOpportunity(row.id)
      ElMessage.success('提交成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '提交失败')
    }
  })
}

const handleUpload = (row) => {
  ElMessageBox.confirm('确定要向超级管理员上传这条销售机会吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await uploadToSuperAdmin(row.id)
      ElMessage.success('上传成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '上传失败')
    }
  })
}

const handleTransfer = (row) => {
  currentOpportunity.value = row
  selectedRegions.value = []
  transferDialogVisible.value = true
}

const handleSaveTransfer = async () => {
  // 从表格选择中获取选中的片区ID
  const selected = regionTableRef.value?.getSelectionRows() || []
  const regionIds = selected.map(r => r.id)
  
  if (regionIds.length === 0) {
    ElMessage.warning('请至少选择一个片区')
    return
  }
  
  try {
    await transferRegions(currentOpportunity.value.id, { regionIds })
    ElMessage.success('传递片区成功')
    transferDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '传递失败')
  }
}

const handleAssign = async (row) => {
  currentOpportunity.value = row
  selectedEmployees.value = []
  // 加载已分配的员工
  if (row.assignedEmployees) {
    selectedEmployees.value = row.assignedEmployees.split(',').filter(e => e)
  }
  assignDialogVisible.value = true
  // 等待DOM更新后设置选中状态
  await nextTick()
  if (employeeTableRef.value && selectedEmployees.value.length > 0) {
    employeeList.value.forEach(emp => {
      if (selectedEmployees.value.includes(emp.id)) {
        employeeTableRef.value.toggleRowSelection(emp, true)
      }
    })
  }
}

const handleSaveAssign = async () => {
  // 从表格选择中获取选中的员工ID
  const selected = employeeTableRef.value?.getSelectionRows() || []
  const employeeIds = selected.map(e => e.id)
  
  if (employeeIds.length === 0) {
    ElMessage.warning('请至少选择一个员工')
    return
  }
  
  try {
    await assignEmployees(currentOpportunity.value.id, { employeeIds })
    ElMessage.success('分配员工成功')
    assignDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '分配失败')
  }
}

const handleClose = (row) => {
  currentOpportunity.value = row
  closeReasonData.value = {
    reason: ''
  }
  closeDialogVisible.value = true
}

const handleSaveClose = async () => {
  if (!closeReasonData.value.reason || closeReasonData.value.reason.trim() === '') {
    ElMessage.warning('请输入关闭原因')
    return
  }
  try {
    await closeOpportunity(currentOpportunity.value.id, { reason: closeReasonData.value.reason })
    ElMessage.success('关闭机会成功')
    closeDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '关闭失败')
  }
}

const handleTrack = (row) => {
  currentOpportunity.value = row
  trackingData.value = {
    opportunityId: row.id,
    visitTime: '',
    visitor: '',
    matter: '',
    description: '',
    attachment: '',
    remarks: ''
  }
  trackDialogVisible.value = true
}

const handleSaveTracking = async () => {
  try {
    await saveTracking(trackingData.value)
    ElMessage.success('保存跟踪记录成功')
    trackDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
  }
}

const handleCustomerChange = (customerId) => {
  const customer = customerList.value.find(c => c.id === customerId)
  if (customer) {
    formData.value.customerName = customer.unitName
    formData.value.industry = customer.industry
  }
}

const handleAddKeyPerson = () => {
  keyPersonList.value.push({
    contactCode: '',
    contactName: '',
    title: '',
    position: '',
    remarks: ''
  })
}

const handleDeleteKeyPerson = (index) => {
  keyPersonList.value.splice(index, 1)
}

const handleKeyPersonSelectionChange = (selection) => {
  selectedKeyPersons.value = selection
}

const handleDeleteSelectedKeyPersons = () => {
  if (selectedKeyPersons.value.length === 0) {
    ElMessage.warning('请选择要删除的关键人物')
    return
  }
  
  ElMessageBox.confirm('确定要删除选中的关键人物吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 从列表中删除选中的项
    selectedKeyPersons.value.forEach(selected => {
      const index = keyPersonList.value.findIndex(kp => kp === selected)
      if (index !== -1) {
        keyPersonList.value.splice(index, 1)
      }
    })
    selectedKeyPersons.value = []
    ElMessage.success('删除成功')
  }).catch(() => {
    // 用户取消删除
  })
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...formData.value }
        if (data.regions && Array.isArray(data.regions)) {
          data.regions = data.regions.join(',')
        }
        
        // 保存销售机会，获取返回的机会对象（包含ID）
        const saveRes = await saveProjectOpportunity(data)
        const savedOpportunity = saveRes.data || saveRes
        
        // 获取机会ID（新增时从返回结果获取，修改时使用原有ID）
        const opportunityId = savedOpportunity.id || data.id
        
        if (!opportunityId) {
          ElMessage.error('保存失败：无法获取机会ID')
          return
        }
        
        // 过滤掉所有字段都为空的关键人物
        const validKeyPersons = keyPersonList.value.filter(kp => {
          return (kp.contactCode && kp.contactCode.trim()) ||
                 (kp.contactName && kp.contactName.trim()) ||
                 (kp.title && kp.title.trim()) ||
                 (kp.position && kp.position.trim()) ||
                 (kp.remarks && kp.remarks.trim())
        })
        
        // 始终保存关键人物（包括空列表，用于删除所有关键人物的情况）
        // 后端会先删除所有原有的关键人物，然后保存新的列表
        await saveKeyPersons(opportunityId, validKeyPersons)
        
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  keyPersonList.value = []
}

const handleUploadSuccess = (response) => {
  if (response && response.code === 200) {
    trackingData.value.attachment = response.data?.filePath || response.data
    ElMessage.success('上传成功')
  } else if (response) {
    trackingData.value.attachment = response
    ElMessage.success('上传成功')
  }
}

const beforeUpload = () => {
  return true
}

const handleExport = async () => {
  try {
    const params = {}
    if (queryForm.value.opportunityName) params.opportunityName = queryForm.value.opportunityName
    if (queryForm.value.startDate) params.startDate = queryForm.value.startDate
    if (queryForm.value.endDate) params.endDate = queryForm.value.endDate
    if (queryForm.value.status) params.status = queryForm.value.status

    const res = await exportProjectOpportunities(params)
    const blob = new Blob([res], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售机会列表_${new Date().getTime()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error(error.message || '导出失败')
  }
}

onMounted(async () => {
  await loadPermissions()
  loadCustomers()
  loadRegions()
  loadEmployees()
  loadData()
})
</script>

<style scoped>
.project-opportunity-management {
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

