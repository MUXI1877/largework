<template>
  <div class="after-sale-store-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>售后4S店管理</span>
          <el-button 
            v-permission="{ moduleId: 'm041', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增4S店
          </el-button>
        </div>
      </template>
      
      <el-table :data="storeList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="storeCode" label="4S店编码" width="120" />
        <el-table-column prop="storeName" label="4S店名称" width="200" />
        <el-table-column prop="region" label="区域" width="100" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm041', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm041', action: 'delete' }"
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
        <el-form-item label="4S店编码" prop="storeCode">
          <el-input v-model="form.storeCode" placeholder="请输入4S店编码" />
        </el-form-item>
        <el-form-item label="4S店名称" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入4S店名称" />
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" placeholder="请输入区域" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="启用" value="active" />
            <el-option label="停用" value="inactive" />
          </el-select>
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
import { getAfterSaleStoreList, saveAfterSaleStore, updateAfterSaleStore, deleteAfterSaleStore } from '../../api/after-sale-store'
import { canRead, loadPermissions } from '../../utils/permission'

const storeList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增4S店')
const formRef = ref(null)

const form = ref({
  id: null,
  storeCode: '',
  storeName: '',
  region: '',
  address: '',
  contactPerson: '',
  contactPhone: '',
  status: 'active',
  remark: ''
})

const rules = {
  storeCode: [{ required: true, message: '请输入4S店编码', trigger: 'blur' }],
  storeName: [{ required: true, message: '请输入4S店名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAfterSaleStoreList()
    storeList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增4S店'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑4S店'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该4S店吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAfterSaleStore(row.id)
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
          await updateAfterSaleStore(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveAfterSaleStore(form.value)
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
    storeCode: '',
    storeName: '',
    region: '',
    address: '',
    contactPerson: '',
    contactPhone: '',
    status: 'active',
    remark: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m041')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看售后4S店管理的权限')
  }
})
</script>

<style scoped>
.after-sale-store-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

