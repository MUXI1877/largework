<template>
  <div class="sales-inventory-reduction-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售降库管理</span>
          <el-button 
            v-permission="{ moduleId: 'm026', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增降库单
          </el-button>
        </div>
      </template>
      
      <el-table :data="reductionList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="reductionNo" label="降库单号" width="150" />
        <el-table-column prop="reductionDate" label="降库日期" width="120" />
        <el-table-column prop="customerId" label="客户ID" width="120" />
        <el-table-column prop="salesPersonId" label="销售人员ID" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="120" />
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
              v-permission="{ moduleId: 'm026', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm026', action: 'delete' }"
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
        <el-form-item label="降库单号" prop="reductionNo">
          <el-input v-model="form.reductionNo" placeholder="请输入降库单号" />
        </el-form-item>
        <el-form-item label="降库日期" prop="reductionDate">
          <el-date-picker v-model="form.reductionDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="客户ID" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户ID" />
        </el-form-item>
        <el-form-item label="销售人员ID" prop="salesPersonId">
          <el-input v-model="form.salesPersonId" placeholder="请输入销售人员ID" />
        </el-form-item>
        <el-form-item label="总金额" prop="totalAmount">
          <el-input-number v-model="form.totalAmount" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="草稿" value="draft" />
            <el-option label="已批准" value="approved" />
            <el-option label="已完成" value="completed" />
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

    <!-- 明细对话框 -->
    <el-dialog
      v-model="itemsDialogVisible"
      title="降库明细"
      width="800px"
    >
      <el-table :data="itemsList" border>
        <el-table-column prop="productType" label="产品类型" width="120" />
        <el-table-column prop="productId" label="产品ID" width="120" />
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
import { getSalesInventoryReductionList, saveSalesInventoryReduction, updateSalesInventoryReduction, deleteSalesInventoryReduction, getSalesInventoryReductionItems } from '../../api/sales-inventory-reduction'
import { canRead, loadPermissions } from '../../utils/permission'

const reductionList = ref([])
const itemsList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const itemsDialogVisible = ref(false)
const dialogTitle = ref('新增降库单')
const formRef = ref(null)

const form = ref({
  id: null,
  reductionNo: '',
  reductionDate: null,
  customerId: '',
  salesPersonId: '',
  totalAmount: null,
  status: 'draft',
  remark: ''
})

const rules = {
  reductionNo: [{ required: true, message: '请输入降库单号', trigger: 'blur' }],
  reductionDate: [{ required: true, message: '请选择降库日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getSalesInventoryReductionList()
    reductionList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增降库单'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑降库单'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleViewItems = async (row) => {
  try {
    const res = await getSalesInventoryReductionItems(row.id)
    itemsList.value = res.data || []
    itemsDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载明细失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该降库单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSalesInventoryReduction(row.id)
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
          await updateSalesInventoryReduction(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveSalesInventoryReduction(form.value)
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
    reductionNo: '',
    reductionDate: null,
    customerId: '',
    salesPersonId: '',
    totalAmount: null,
    status: 'draft',
    remark: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m026')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看销售降库管理的权限')
  }
})
</script>

<style scoped>
.sales-inventory-reduction-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

