<template>
  <div class="after-sale-plan-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>售后计划管理</span>
          <el-button 
            v-permission="{ moduleId: 'm043', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增计划
          </el-button>
        </div>
      </template>
      
      <el-table :data="planList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="planNo" label="计划编号" width="150" />
        <el-table-column prop="planName" label="计划名称" width="200" />
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="customerId" label="客户ID" width="120" />
        <el-table-column prop="storeId" label="4S店ID" width="120" />
        <el-table-column prop="planType" label="计划类型" width="100" />
        <el-table-column prop="planDate" label="计划日期" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm043', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm043', action: 'delete' }"
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
      width="700px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="计划编号" prop="planNo">
          <el-input v-model="form.planNo" placeholder="请输入计划编号" />
        </el-form-item>
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="客户ID" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户ID" />
        </el-form-item>
        <el-form-item label="4S店ID" prop="storeId">
          <el-input v-model="form.storeId" placeholder="请输入4S店ID" />
        </el-form-item>
        <el-form-item label="计划类型" prop="planType">
          <el-select v-model="form.planType" placeholder="请选择计划类型">
            <el-option label="保养" value="maintenance" />
            <el-option label="巡检" value="inspection" />
            <el-option label="维修" value="repair" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划日期" prop="planDate">
          <el-date-picker v-model="form.planDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="执行人ID" prop="executorId">
          <el-input v-model="form.executorId" placeholder="请输入执行人ID" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="待执行" value="pending" />
            <el-option label="执行中" value="executing" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划内容" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="请输入计划内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAfterSalePlanList, saveAfterSalePlan, updateAfterSalePlan, deleteAfterSalePlan } from '../../api/after-sale-plan'
import { canRead, loadPermissions } from '../../utils/permission'

const planList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增计划')
const formRef = ref(null)

const form = ref({
  id: null,
  planNo: '',
  planName: '',
  deviceId: '',
  customerId: '',
  storeId: '',
  planType: '',
  planDate: null,
  executorId: '',
  status: 'pending',
  content: '',
  remark: ''
})

const rules = {
  planNo: [{ required: true, message: '请输入计划编号', trigger: 'blur' }],
  planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  planDate: [{ required: true, message: '请选择计划日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAfterSalePlanList()
    planList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增计划'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑计划'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该计划吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAfterSalePlan(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.msg || '删除失败')
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.value.id) {
          await updateAfterSalePlan(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveAfterSalePlan(form.value)
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
    planNo: '',
    planName: '',
    deviceId: '',
    customerId: '',
    storeId: '',
    planType: '',
    planDate: null,
    executorId: '',
    status: 'pending',
    content: '',
    remark: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m043')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看售后计划管理的权限')
  }
})
</script>

<style scoped>
.after-sale-plan-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

