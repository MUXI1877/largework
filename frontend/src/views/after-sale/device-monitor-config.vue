<template>
  <div class="device-monitor-config-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>基础信息管理配置</span>
          <el-button 
            v-permission="{ moduleId: 'm046', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增配置
          </el-button>
        </div>
      </template>
      
      <el-table :data="configList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="deviceId" label="设备ID" width="120" />
        <el-table-column prop="metricKey" label="指标键" width="150" />
        <el-table-column prop="metricName" label="指标名称" width="150" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="minValue" label="最小值" width="100" />
        <el-table-column prop="maxValue" label="最大值" width="100" />
        <el-table-column prop="warningMin" label="警告下限" width="100" />
        <el-table-column prop="warningMax" label="警告上限" width="100" />
        <el-table-column prop="isEnabled" label="是否启用" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm046', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm046', action: 'delete' }"
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="留空表示全局配置" />
        </el-form-item>
        <el-form-item label="指标键" prop="metricKey">
          <el-input v-model="form.metricKey" placeholder="请输入指标键" />
        </el-form-item>
        <el-form-item label="指标名称" prop="metricName">
          <el-input v-model="form.metricName" placeholder="请输入指标名称" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="最小值" prop="minValue">
          <el-input-number v-model="form.minValue" :precision="4" />
        </el-form-item>
        <el-form-item label="最大值" prop="maxValue">
          <el-input-number v-model="form.maxValue" :precision="4" />
        </el-form-item>
        <el-form-item label="警告下限" prop="warningMin">
          <el-input-number v-model="form.warningMin" :precision="4" />
        </el-form-item>
        <el-form-item label="警告上限" prop="warningMax">
          <el-input-number v-model="form.warningMax" :precision="4" />
        </el-form-item>
        <el-form-item label="报警下限" prop="alarmMin">
          <el-input-number v-model="form.alarmMin" :precision="4" />
        </el-form-item>
        <el-form-item label="报警上限" prop="alarmMax">
          <el-input-number v-model="form.alarmMax" :precision="4" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="form.isEnabled" />
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
import { getDeviceMonitorConfigList, saveDeviceMonitorConfig, updateDeviceMonitorConfig, deleteDeviceMonitorConfig } from '../../api/device-monitor-config'
import { canRead, loadPermissions } from '../../utils/permission'

const configList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增配置')
const formRef = ref(null)

const form = ref({
  id: null,
  deviceId: null,
  metricKey: '',
  metricName: '',
  unit: '',
  minValue: null,
  maxValue: null,
  warningMin: null,
  warningMax: null,
  alarmMin: null,
  alarmMax: null,
  isEnabled: true
})

const rules = {
  metricKey: [{ required: true, message: '请输入指标键', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDeviceMonitorConfigList()
    configList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增配置'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑配置'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该配置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDeviceMonitorConfig(row.id)
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
          await updateDeviceMonitorConfig(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveDeviceMonitorConfig(form.value)
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
    deviceId: null,
    metricKey: '',
    metricName: '',
    unit: '',
    minValue: null,
    maxValue: null,
    warningMin: null,
    warningMax: null,
    alarmMin: null,
    alarmMax: null,
    isEnabled: true
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m046')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看基础信息管理配置的权限')
  }
})
</script>

<style scoped>
.device-monitor-config-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

