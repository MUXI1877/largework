<template>
  <div class="equipment-set-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>设备成套管理</span>
          <el-button 
            v-permission="{ moduleId: 'm017', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增成套
          </el-button>
        </div>
      </template>
      
      <el-table :data="equipmentSetList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="code" label="成套编码" width="120" />
        <el-table-column prop="name" label="成套名称" width="200" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="totalPrice" label="总价" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button 
              type="info" 
              size="small" 
              @click="handleViewItems(scope.row)"
            >
              查看明细
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm017', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm017', action: 'delete' }"
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
        <el-form-item label="成套编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入成套编码" />
        </el-form-item>
        <el-form-item label="成套名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入成套名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="总价" prop="totalPrice">
          <el-input-number v-model="form.totalPrice" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="启用" value="active" />
            <el-option label="停用" value="inactive" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 明细对话框 -->
    <el-dialog
      v-model="itemsDialogVisible"
      title="成套明细"
      width="800px"
    >
      <el-table :data="itemsList" border>
        <el-table-column prop="deviceUnitId" label="设备ID" width="120" />
        <el-table-column prop="sparePartsId" label="备件ID" width="120" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="unitPrice" label="单价" width="120" />
        <el-table-column prop="totalPrice" label="小计" width="120" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEquipmentSetList, saveEquipmentSet, updateEquipmentSet, deleteEquipmentSet, getEquipmentSetItems } from '../../api/equipment-set'
import { canRead, loadPermissions } from '../../utils/permission'

const equipmentSetList = ref([])
const itemsList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const itemsDialogVisible = ref(false)
const dialogTitle = ref('新增成套')
const formRef = ref(null)

const form = ref({
  id: null,
  code: '',
  name: '',
  description: '',
  totalPrice: null,
  status: 'active'
})

const rules = {
  code: [{ required: true, message: '请输入成套编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入成套名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getEquipmentSetList()
    equipmentSetList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增成套'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑成套'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleViewItems = async (row) => {
  try {
    const res = await getEquipmentSetItems(row.id)
    itemsList.value = res.data || []
    itemsDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载明细失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该成套吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteEquipmentSet(row.id)
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
          await updateEquipmentSet(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveEquipmentSet(form.value)
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
    code: '',
    name: '',
    description: '',
    totalPrice: null,
    status: 'active'
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m017')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看设备成套管理的权限')
  }
})
</script>

<style scoped>
.equipment-set-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

