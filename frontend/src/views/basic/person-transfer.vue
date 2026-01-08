<template>
  <div class="person-transfer-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>片区人员调动管理</span>
          <el-button 
            v-permission="{ moduleId: 'm013', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增调动
          </el-button>
        </div>
      </template>
      
      <el-table :data="transferList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="personId" label="人员ID" width="120" />
        <el-table-column prop="fromRegionId" label="原片区" width="120" />
        <el-table-column prop="toRegionId" label="目标片区" width="120" />
        <el-table-column prop="transferDate" label="调动日期" width="120" />
        <el-table-column prop="reason" label="调动原因" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button 
              type="info" 
              size="small" 
              @click="handleViewHistory(scope.row)"
            >
              调动记录
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm013', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm013', action: 'delete' }"
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="人员ID" prop="personId">
          <el-input v-model="form.personId" placeholder="请输入人员ID" />
        </el-form-item>
        <el-form-item label="原片区ID" prop="fromRegionId">
          <el-input v-model="form.fromRegionId" placeholder="请输入原片区ID" />
        </el-form-item>
        <el-form-item label="目标片区ID" prop="toRegionId">
          <el-input v-model="form.toRegionId" placeholder="请输入目标片区ID" />
        </el-form-item>
        <el-form-item label="调动日期" prop="transferDate">
          <el-date-picker v-model="form.transferDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="调动原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入调动原因" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="待审批" value="pending" />
            <el-option label="已批准" value="approved" />
            <el-option label="已拒绝" value="rejected" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 调动记录对话框 -->
    <el-dialog
      v-model="historyDialogVisible"
      :title="`调动记录 - 人员 ${historyPersonId || ''}`"
      width="800px"
    >
      <el-table :data="historyList" border v-loading="historyLoading">
        <el-table-column prop="transferDate" label="调动日期" width="140" />
        <el-table-column prop="fromRegionId" label="原片区" width="140" />
        <el-table-column prop="toRegionId" label="目标片区" width="140" />
        <el-table-column prop="reason" label="调动原因" min-width="180" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" />
      </el-table>
      <template #footer>
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPersonTransferList, savePersonTransfer, updatePersonTransfer, deletePersonTransfer, getPersonTransferByPerson } from '../../api/person-transfer'
import { canRead, loadPermissions } from '../../utils/permission'

const transferList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增调动')
const formRef = ref(null)
const historyDialogVisible = ref(false)
const historyLoading = ref(false)
const historyList = ref([])
const historyPersonId = ref('')

const form = ref({
  id: null,
  personId: '',
  fromRegionId: '',
  toRegionId: '',
  transferDate: null,
  reason: '',
  status: 'pending'
})

const rules = {
  personId: [{ required: true, message: '请输入人员ID', trigger: 'blur' }],
  toRegionId: [{ required: true, message: '请输入目标片区ID', trigger: 'blur' }],
  transferDate: [{ required: true, message: '请选择调动日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPersonTransferList()
    transferList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增调动'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑调动'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该调动记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePersonTransfer(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.msg || '删除失败')
    }
  })
}

const handleViewHistory = async (row) => {
  historyPersonId.value = row.personId
  historyDialogVisible.value = true
  historyLoading.value = true
  try {
    const res = await getPersonTransferByPerson(row.personId)
    historyList.value = res.data || []
  } catch (error) {
    ElMessage.error(error.msg || '加载调动记录失败')
  } finally {
    historyLoading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.value.id) {
          await updatePersonTransfer(form.value)
          ElMessage.success('更新成功')
        } else {
          await savePersonTransfer(form.value)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.msg || '操作失败')
      }
    }
  })
}

const resetForm = () => {
  form.value = {
    id: null,
    personId: '',
    fromRegionId: '',
    toRegionId: '',
    transferDate: null,
    reason: '',
    status: 'pending'
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m013')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看片区人员调动管理的权限')
  }
})
</script>

<style scoped>
.person-transfer-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

