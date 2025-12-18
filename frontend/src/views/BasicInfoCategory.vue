<template>
  <div class="basic-info-category">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>基本信息分类管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon> 添加
          </el-button>
        </div>
      </template>
      
      <el-tree
        :data="treeData"
        :props="defaultProps"
        :expand-on-click-node="false"
        :default-expand-all="true"
        @node-click="handleNodeClick"
        @node-contextmenu="handleContextMenu"
      >
        <template #default="{ node, data }">
          <div class="tree-node">
            <span>{{ node.label }}</span>
            <span class="tree-actions">
              <el-button
                type="text"
                size="small"
                @click.stop="handleEdit(data)"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                size="small"
                @click.stop="handleToggleEnabled(data)"
              >
                {{ data.isEnabled ? '禁用' : '启用' }}
              </el-button>
              <el-button
                type="text"
                size="small"
                @click.stop="handleDelete(data)"
              >
                删除
              </el-button>
            </span>
          </div>
        </template>
      </el-tree>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form :model="formData" label-width="120px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="父分类">
          <el-select
            v-model="formData.parentId"
            placeholder="选择父分类"
            style="width: 100%"
          >
            <el-option label="根分类" value="" />
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="描述" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            rows="3"
            placeholder="请输入分类描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { 
  getBasicInfoListByCategory, 
  saveBasicInfo, 
  updateBasicInfo, 
  deleteBasicInfo, 
  toggleBasicInfoEnabled
} from '../api/basic-info'

const treeData = ref([])
const categoryOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加分类')
const formData = ref({
  id: '',
  category: 'CATEGORY',
  name: '',
  code: '',
  parentId: '',
  sort: 0,
  content: '',
  isEnabled: true
})

const defaultProps = {
  children: 'children',
  label: 'cnName'
}

const loadData = async () => {
  try {
    const response = await getBasicInfoListByCategory('CATEGORY')
    const data = response.data || []
    categoryOptions.value = data
    treeData.value = buildTree(data)
  } catch (error) {
    ElMessage.error('加载分类数据失败')
  }
}

const buildTree = (data, parentId = '') => {
  return data
    .filter(item => item.parentId === parentId)
    .map(item => {
      const children = buildTree(data, item.id)
      return {
        ...item,
        children: children.length > 0 ? children : []
      }
    })
}

const handleAdd = () => {
  dialogTitle.value = '添加分类'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (data) => {
  dialogTitle.value = '编辑分类'
  formData.value = { ...data }
  dialogVisible.value = true
}

const handleDelete = (data) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBasicInfo(data.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleToggleEnabled = async (data) => {
  try {
    await toggleBasicInfoEnabled(data.id, !data.isEnabled)
    ElMessage.success(data.isEnabled ? '已禁用' : '已启用')
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleNodeClick = (data) => {
  console.log('节点点击:', data)
}

const handleContextMenu = (event, data) => {
  event.preventDefault()
  console.log('右键菜单:', data)
}

const handleSubmit = async () => {
  try {
    if (formData.value.id) {
      await updateBasicInfo(formData.value)
      ElMessage.success('编辑成功')
    } else {
      await saveBasicInfo(formData.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const resetForm = () => {
  formData.value = {
    id: '',
    category: 'CATEGORY',
    name: '',
    code: '',
    parentId: '',
    sort: 0,
    content: '',
    isEnabled: true
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.basic-info-category {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.tree-actions {
  opacity: 0;
  transition: opacity 0.3s;
}

.tree-node:hover .tree-actions {
  opacity: 1;
}

.tree-actions .el-button {
  padding: 0;
  margin-left: 10px;
}
</style>