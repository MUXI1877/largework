<template>
  <div class="bidding-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>投标管理</span>
          <el-button 
            v-permission="{ moduleId: 'm027', action: 'add' }"
            type="primary" 
            @click="handleAdd"
          >
            新增投标
          </el-button>
        </div>
      </template>
      
      <el-table :data="biddingList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="biddingNo" label="投标编号" width="150" />
        <el-table-column prop="projectName" label="项目名称" width="200" />
        <el-table-column prop="customerId" label="客户ID" width="120" />
        <el-table-column prop="biddingDate" label="投标日期" width="120" />
        <el-table-column prop="deadline" label="截止日期" width="120" />
        <el-table-column prop="totalAmount" label="投标金额" width="120" />
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
              v-permission="{ moduleId: 'm027', action: 'update' }"
              type="primary" 
              size="small" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-permission="{ moduleId: 'm027', action: 'delete' }"
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
        <el-form-item label="投标编号" prop="biddingNo">
          <el-input v-model="form.biddingNo" placeholder="请输入投标编号" />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="客户ID" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户ID" />
        </el-form-item>
        <el-form-item label="投标日期" prop="biddingDate">
          <el-date-picker v-model="form.biddingDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="截止日期" prop="deadline">
          <el-date-picker v-model="form.deadline" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="投标金额" prop="totalAmount">
          <el-input-number v-model="form.totalAmount" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="草稿" value="draft" />
            <el-option label="已提交" value="submitted" />
            <el-option label="中标" value="won" />
            <el-option label="未中标" value="lost" />
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
      title="投标明细"
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
import { getBiddingList, saveBidding, updateBidding, deleteBidding, getBiddingItems } from '../../api/bidding'
import { canRead, loadPermissions } from '../../utils/permission'

const biddingList = ref([])
const itemsList = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const itemsDialogVisible = ref(false)
const dialogTitle = ref('新增投标')
const formRef = ref(null)

const form = ref({
  id: null,
  biddingNo: '',
  projectName: '',
  customerId: '',
  biddingDate: null,
  deadline: null,
  totalAmount: null,
  status: 'draft',
  salesPersonId: '',
  remark: ''
})

const rules = {
  biddingNo: [{ required: true, message: '请输入投标编号', trigger: 'blur' }],
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  biddingDate: [{ required: true, message: '请选择投标日期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getBiddingList()
    biddingList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增投标'
  dialogVisible.value = true
  resetForm()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑投标'
  dialogVisible.value = true
  form.value = { ...row }
}

const handleViewItems = async (row) => {
  try {
    const res = await getBiddingItems(row.id)
    itemsList.value = res.data || []
    itemsDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载明细失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该投标记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBidding(row.id)
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
          await updateBidding(form.value)
          ElMessage.success('更新成功')
        } else {
          await saveBidding(form.value)
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
    biddingNo: '',
    projectName: '',
    customerId: '',
    biddingDate: null,
    deadline: null,
    totalAmount: null,
    status: 'draft',
    salesPersonId: '',
    remark: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m027')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看投标管理的权限')
  }
})
</script>

<style scoped>
.bidding-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

