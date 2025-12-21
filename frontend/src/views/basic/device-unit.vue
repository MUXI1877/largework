<template>
  <div class="device-unit-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>产品管理 - 单体设备</span>
          <el-button type="primary" @click="handleAdd">新增设备</el-button>
        </div>
      </template>
      
      <el-table :data="deviceUnitList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="名称" width="150" />
        <el-table-column prop="model" label="型号" width="150" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="scope">
            <span v-if="scope.row.price">¥{{ scope.row.price.toFixed(2) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="重量(kg)" width="120">
          <template #default="scope">
            <span v-if="scope.row.weight">{{ scope.row.weight }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="leadDay" label="交期(天)" width="120">
          <template #default="scope">
            <span v-if="scope.row.leadDay">{{ scope.row.leadDay }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="params" label="参数" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :precision="2" :min="0" placeholder="请输入价格" style="width: 100%" />
        </el-form-item>
        <el-form-item label="重量(kg)" prop="weight">
          <el-input-number v-model="form.weight" :precision="2" :min="0" placeholder="请输入重量" style="width: 100%" />
        </el-form-item>
        <el-form-item label="交期(天)" prop="leadDay">
          <el-input-number v-model="form.leadDay" :min="0" placeholder="请输入交期天数" style="width: 100%" />
        </el-form-item>
        <el-form-item label="参数" prop="params">
          <el-input v-model="form.params" type="textarea" :rows="4" placeholder="请输入设备参数" />
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
import { getDeviceUnitList, saveDeviceUnit, updateDeviceUnit, deleteDeviceUnit } from '../../api/device-unit'

const deviceUnitList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增设备')
const formRef = ref(null)

const form = ref({
  id: null,
  name: '',
  model: '',
  price: null,
  weight: null,
  leadDay: null,
  params: ''
})

const rules = {
  name: [{ required: true, message: '请输入设备名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDeviceUnitList()
    deviceUnitList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增设备'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑设备'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该设备吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDeviceUnit(row.id)
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
          await updateDeviceUnit(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveDeviceUnit(form.value)
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
    name: '',
    model: '',
    price: null,
    weight: null,
    leadDay: null,
    params: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.device-unit-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

