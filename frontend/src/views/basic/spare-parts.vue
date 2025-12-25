<template>
  <div class="spare-parts-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>备品备件管理</span>
          <el-button 
            v-permission="{ moduleId: 'm016', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增备件
          </el-button>
        </div>
      </template>
      
      <el-table :data="sparePartsList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="code" label="备件编码" width="120" />
        <el-table-column prop="name" label="备件名称" width="200" />
        <el-table-column prop="model" label="型号" width="120" />
        <el-table-column prop="category" label="类别" width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="单价" width="100" />
        <el-table-column prop="stockQuantity" label="库存数量" width="100" />
        <el-table-column prop="minStock" label="最低库存" width="100" />
        <el-table-column prop="supplier" label="供应商" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              v-permission="{ moduleId: 'm016', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm016', action: 'delete' }"
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
        <el-form-item label="备件编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入备件编码" />
        </el-form-item>
        <el-form-item label="备件名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入备件名称" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-input v-model="form.category" placeholder="请输入类别" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input-number v-model="form.price" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="库存数量" prop="stockQuantity">
          <el-input-number v-model="form.stockQuantity" :min="0" />
        </el-form-item>
        <el-form-item label="最低库存" prop="minStock">
          <el-input-number v-model="form.minStock" :min="0" />
        </el-form-item>
        <el-form-item label="供应商" prop="supplier">
          <el-input v-model="form.supplier" placeholder="请输入供应商" />
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
import { canRead, loadPermissions } from '../../utils/permission'
import { getSparePartsList, saveSpareParts, updateSpareParts, deleteSpareParts } from '../../api/spare-parts'

const sparePartsList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增备件')
const formRef = ref(null)

const form = ref({
  id: null,
  code: '',
  name: '',
  model: '',
  category: '',
  unit: '',
  price: null,
  stockQuantity: 0,
  minStock: 0,
  supplier: '',
  remark: ''
})

const rules = {
  code: [{ required: true, message: '请输入备件编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入备件名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getSparePartsList()
    sparePartsList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增备件'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑备件'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该备件吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSpareParts(row.id)
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
          await updateSpareParts(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveSpareParts(form.value)
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
    model: '',
    category: '',
    unit: '',
    price: null,
    stockQuantity: 0,
    minStock: 0,
    supplier: '',
    remark: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m016')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看备品备件管理的权限')
  }
})
</script>

<style scoped>
.spare-parts-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

