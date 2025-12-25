<template>
  <div class="team-management">
    <el-tabs v-model="activeTab">
      <!-- 销售片区管理 -->
      <el-tab-pane label="销售片区管理" name="region">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>销售片区管理</span>
              <div>
                <el-button 
                  v-permission="{ moduleId: 'm009', action: 'add' }"
                  type="primary" @click="handleAddRegion">新增</el-button>
                <el-button type="success" @click="handleExportRegion">导出</el-button>
              </div>
            </div>
          </template>

          <el-table :data="regionList" border style="width: 100%" v-loading="regionLoading">
            <el-table-column prop="regionName" label="片区名称" />
            <el-table-column prop="regionCode" label="片区编号" />
            <el-table-column prop="parentDepartment" label="上级部门" />
            <el-table-column prop="createDate" label="创建日期" />
            <el-table-column prop="remarks" label="备注" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="scope">
                <el-button 
                  v-permission="{ moduleId: 'm009', action: 'update' }"
                  type="warning" size="small" @click="handleEditRegion(scope.row)">修改</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm009', action: 'update' }"
                  type="danger" size="small" @click="handleDeleteRegion(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 营销人员管理 -->
      <el-tab-pane label="营销人员管理" name="person">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>营销人员管理</span>
              <div>
                <el-button 
                  v-permission="{ moduleId: 'm009', action: 'add' }"
                  type="primary" @click="handleAddPerson">新增</el-button>
                <el-button type="success" @click="handleExportPerson">导出</el-button>
              </div>
            </div>
          </template>

          <el-form :inline="true" :model="personQueryForm" class="query-form">
            <el-form-item label="所属片区">
              <el-select v-model="personQueryForm.regionId" placeholder="请选择片区" clearable>
                <el-option
                  v-for="region in regionList"
                  :key="region.id"
                  :label="region.regionName"
                  :value="region.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button 
                v-permission="{ moduleId: 'm009', action: 'read' }"
                type="primary" @click="handleQueryPerson">查询</el-button>
              <el-button @click="handleResetPerson">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="personList" border style="width: 100%" v-loading="personLoading">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="employeeCode" label="工号" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="gender" label="性别" />
            <el-table-column prop="birthday" label="生日" />
            <el-table-column prop="contactInfo" label="联系方式" />
            <el-table-column prop="position" label="职务" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="scope">
                <el-button 
                  v-permission="{ moduleId: 'm009', action: 'update' }"
                  type="warning" size="small" @click="handleEditPerson(scope.row)">修改</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm009', action: 'update' }"
                  type="danger" size="small" @click="handleDeletePerson(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 片区人员调动管理 -->
      <el-tab-pane label="片区人员调动管理" name="transfer">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>片区人员调动管理</span>
            </div>
          </template>

          <el-table
            :data="personList"
            border
            style="width: 100%"
            v-loading="personLoading"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="employeeCode" label="工号" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="position" label="职务" />
            <el-table-column prop="department" label="所属部门" />
            <el-table-column prop="contactInfo" label="联系方式" />
            <el-table-column prop="responsibleArea" label="当前负责区域" />
          </el-table>

          <div style="margin-top: 20px">
            <el-button type="primary" :disabled="selectedPersons.length === 0" @click="handleTransfer">
              调动人员
            </el-button>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 片区编辑对话框 -->
    <el-dialog
      v-model="regionDialogVisible"
      :title="regionDialogTitle"
      width="600px"
      @close="handleRegionDialogClose"
    >
      <el-form :model="regionForm" :rules="regionRules" ref="regionFormRef" label-width="120px">
        <el-form-item label="片区名称" prop="regionName">
          <el-input v-model="regionForm.regionName" />
        </el-form-item>
        <el-form-item label="片区编号" prop="regionCode">
          <el-input v-model="regionForm.regionCode" />
        </el-form-item>
        <el-form-item label="上级部门">
          <el-select v-model="regionForm.parentDepartment" placeholder="请选择">
            <el-option label="成套处" value="成套处" />
            <el-option label="外贸处" value="外贸处" />
            <el-option label="销售处" value="销售处" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建日期" prop="createDate">
          <el-date-picker v-model="regionForm.createDate" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="regionForm.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="regionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRegion">保存</el-button>
      </template>
    </el-dialog>

    <!-- 营销人员编辑对话框 -->
    <el-dialog
      v-model="personDialogVisible"
      :title="personDialogTitle"
      width="600px"
      @close="handlePersonDialogClose"
    >
      <el-form :model="personForm" :rules="personRules" ref="personFormRef" label-width="120px">
        <el-form-item label="工号">
          <el-input v-model="personForm.employeeCode" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="personForm.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="personForm.gender" placeholder="请选择">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker v-model="personForm.birthday" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="personForm.contactInfo" />
        </el-form-item>
        <el-form-item label="所属片区">
          <el-select v-model="personForm.regionId" placeholder="请选择片区">
            <el-option
              v-for="region in regionList"
              :key="region.id"
              :label="region.regionName"
              :value="region.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="职务">
          <el-input v-model="personForm.position" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="personForm.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="personDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePerson">保存</el-button>
      </template>
    </el-dialog>

    <!-- 人员调动对话框 -->
    <el-dialog
      v-model="transferDialogVisible"
      title="批量调动人员"
      width="600px"
    >
      <el-form :model="transferForm" label-width="120px">
        <el-form-item label="目标负责区域">
          <el-input v-model="transferForm.targetArea" placeholder="请输入目标负责区域" />
        </el-form-item>
        <el-form-item label="目标所属部门">
          <el-select v-model="transferForm.targetDepartment" placeholder="请选择">
            <el-option label="成套处" value="成套处" />
            <el-option label="外贸处" value="外贸处" />
            <el-option label="销售处" value="销售处" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标所属片区">
          <el-select v-model="transferForm.targetRegionId" placeholder="请选择片区">
            <el-option
              v-for="region in regionList"
              :key="region.id"
              :label="region.regionName"
              :value="region.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选中人员">
          <div>{{ selectedPersons.length }} 人</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="transferDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTransfer">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { canRead, loadPermissions } from '../utils/permission'
import {
  getSalesRegionList,
  saveSalesRegion,
  deleteSalesRegion,
  exportSalesRegions,
  getSalesPersonList,
  getSalesPersonByRegion,
  saveSalesPerson,
  deleteSalesPerson,
  batchTransfer,
  exportSalesPersons
} from '../api/team'

const activeTab = ref('region')

// 销售片区管理
const regionLoading = ref(false)
const regionList = ref([])
const regionDialogVisible = ref(false)
const regionDialogTitle = ref('新增片区')
const regionFormRef = ref(null)
const regionForm = reactive({
  id: '',
  regionName: '',
  regionCode: '',
  parentDepartment: '',
  createDate: null,
  remarks: ''
})

const regionRules = {
  regionName: [{ required: true, message: '请输入片区名称', trigger: 'blur' }],
  regionCode: [{ required: true, message: '请输入片区编号', trigger: 'blur' }],
  createDate: [{ required: true, message: '请选择创建日期', trigger: 'change' }]
}

// 营销人员管理
const personLoading = ref(false)
const personList = ref([])
const personQueryForm = reactive({
  regionId: ''
})
const personDialogVisible = ref(false)
const personDialogTitle = ref('新增营销人员')
const personFormRef = ref(null)
const personForm = reactive({
  id: '',
  employeeCode: '',
  name: '',
  gender: '',
  birthday: null,
  contactInfo: '',
  regionId: '',
  position: '',
  department: '',
  responsibleArea: '',
  remarks: ''
})

const personRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

// 人员调动
const selectedPersons = ref([])
const transferDialogVisible = ref(false)
const transferForm = reactive({
  targetRegionId: '',
  targetDepartment: '',
  targetArea: ''
})

const loadRegions = async () => {
  regionLoading.value = true
  try {
    const res = await getSalesRegionList()
    regionList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载片区数据失败')
  } finally {
    regionLoading.value = false
  }
}

const loadPersons = async () => {
  personLoading.value = true
  try {
    let res
    if (personQueryForm.regionId) {
      res = await getSalesPersonByRegion(personQueryForm.regionId)
    } else {
      res = await getSalesPersonList()
    }
    personList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载人员数据失败')
  } finally {
    personLoading.value = false
  }
}

const handleQueryPerson = () => {
  loadPersons()
}

const handleResetPerson = () => {
  personQueryForm.regionId = ''
  loadPersons()
}

const handleAddRegion = () => {
  regionDialogTitle.value = '新增片区'
  Object.assign(regionForm, {
    id: '',
    regionName: '',
    regionCode: '',
    parentDepartment: '',
    createDate: new Date(),
    remarks: ''
  })
  regionDialogVisible.value = true
}

const handleEditRegion = (row) => {
  regionDialogTitle.value = '修改片区'
  Object.assign(regionForm, row)
  regionDialogVisible.value = true
}

const handleSaveRegion = async () => {
  if (!regionFormRef.value) return
  await regionFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveSalesRegion(regionForm)
        ElMessage.success('保存成功')
        regionDialogVisible.value = false
        loadRegions()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDeleteRegion = async (row) => {
  ElMessageBox.confirm('确定要删除该片区吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSalesRegion(row.id)
      ElMessage.success('删除成功')
      loadRegions()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleExportRegion = async () => {
  try {
    const response = await exportSalesRegions()
    const blob = new Blob([response], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `销售片区列表_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + (error.message || '未知错误'))
  }
}

const handleAddPerson = () => {
  personDialogTitle.value = '新增营销人员'
  Object.assign(personForm, {
    id: '',
    employeeCode: '',
    name: '',
    gender: '',
    birthday: null,
    contactInfo: '',
    regionId: '',
    position: '',
    department: '',
    responsibleArea: '',
    remarks: ''
  })
  personDialogVisible.value = true
}

const handleEditPerson = (row) => {
  personDialogTitle.value = '修改营销人员'
  Object.assign(personForm, row)
  personDialogVisible.value = true
}

const handleSavePerson = async () => {
  if (!personFormRef.value) return
  await personFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveSalesPerson(personForm)
        ElMessage.success('保存成功')
        personDialogVisible.value = false
        loadPersons()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDeletePerson = async (row) => {
  ElMessageBox.confirm('确定要删除该营销人员吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSalesPerson(row.id)
      ElMessage.success('删除成功')
      loadPersons()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleExportPerson = async () => {
  try {
    const response = await exportSalesPersons()
    const blob = new Blob([response], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `营销人员列表_${new Date().getTime()}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + (error.message || '未知错误'))
  }
}

const handleSelectionChange = (selection) => {
  selectedPersons.value = selection.map(p => p.id)
}

const handleTransfer = () => {
  if (selectedPersons.value.length === 0) {
    ElMessage.warning('请先选择要调动的人员')
    return
  }
  Object.assign(transferForm, {
    targetRegionId: '',
    targetDepartment: '',
    targetArea: ''
  })
  transferDialogVisible.value = true
}

const handleSaveTransfer = async () => {
  try {
    await batchTransfer({
      personIds: selectedPersons.value,
      targetRegionId: transferForm.targetRegionId,
      targetDepartment: transferForm.targetDepartment,
      targetArea: transferForm.targetArea
    })
    ElMessage.success('批量调动成功')
    transferDialogVisible.value = false
    selectedPersons.value = []
    loadPersons()
  } catch (error) {
    ElMessage.error(error.message || '调动失败')
  }
}

const handleRegionDialogClose = () => {
  regionFormRef.value?.resetFields()
}

const handlePersonDialogClose = () => {
  personFormRef.value?.resetFields()
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m009')) {
    loadRegions()
    loadPersons()
  } else {
    ElMessage.warning('您没有查看团队信息管理的权限')
  }
})
</script>

<style scoped>
.team-management {
  height: 100%;
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

