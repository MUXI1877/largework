<template>
  <div class="option-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>选项管理</span>
          <div>
            <el-select v-model="filterGroup" placeholder="筛选分组" style="width: 150px; margin-right: 10px" clearable @change="loadData">
              <el-option label="全部" value="" />
              <el-option label="岗位" value="post" />
              <el-option label="片区" value="area" />
            </el-select>
            <el-button type="primary" @click="handleAdd">新增选项</el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="optionList" border style="width: 100%">
        <el-table-column prop="groupName" label="分组" width="120" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="optionValue" label="选项值" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="分组" prop="groupName">
          <el-select v-model="form.groupName" placeholder="请选择分组" style="width: 100%">
            <el-option label="岗位" value="post" />
            <el-option label="片区" value="area" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="选项值" prop="optionValue">
          <el-input v-model="form.optionValue" placeholder="请输入选项值" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOptionList, saveOption, deleteOption } from '../api/option'

const optionList = ref([])
const filterGroup = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增选项')
const formRef = ref(null)

const form = reactive({
  id: null,
  groupName: '',
  title: '',
  optionValue: '',
  sort: 0
})

const rules = {
  groupName: [{ required: true, message: '请选择分组', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getOptionList(filterGroup.value || undefined)
    optionList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增选项'
  form.id = null
  form.groupName = ''
  form.title = ''
  form.optionValue = ''
  form.sort = 0
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑选项'
  form.id = row.id
  form.groupName = row.groupName
  form.title = row.title
  form.optionValue = row.optionValue
  form.sort = row.sort || 0
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveOption(form)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该选项吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteOption(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.option-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

