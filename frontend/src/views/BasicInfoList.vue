<template>
  <div class="basic-info-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>基本信息列表管理</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon> 添加信息
          </el-button>
        </div>
      </template>
      
      <div class="table-toolbar">
        <el-select
          v-model="searchForm.category"
          placeholder="选择分类"
          style="width: 150px; margin-right: 10px"
          @change="handleCategoryChange"
        >
          <el-option label="全部" value="" />
          <el-option
            v-for="category in categoryList"
            :key="category.id"
            :label="category.name"
            :value="category.code"
          />
        </el-select>
        <el-input
          v-model="searchForm.name"
          placeholder="搜索名称"
          clearable
          style="width: 200px; margin-right: 10px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
          </template>
        </el-input>
        <el-button type="primary" size="small" @click="handleSearch">搜索</el-button>
        <el-button type="default" size="small" @click="handleReset">重置</el-button>
      </div>
      
      <el-table :data="infoList" border style="width: 100%" stripe>
        <el-table-column prop="name" label="名称" width="180" />
        <el-table-column prop="code" label="编码" width="150" />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="parent.name" label="父级" width="150">
          <template #default="scope">
            {{ scope.row.parent ? scope.row.parent.name : '无' }}
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="isEnabled" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(scope.row)"
              style="margin-right: 5px"
            >
              编辑
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleToggleEnabled(scope.row)"
              style="margin-right: 5px"
            >
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="table-pagination">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="formData" label-width="120px">
        <el-form-item label="分类" prop="category">
          <el-select
            v-model="formData.category"
            placeholder="选择分类"
            style="width: 100%"
          >
            <el-option
              v-for="category in categoryList"
              :key="category.id"
              :label="category.name"
              :value="category.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="父级">
          <el-select
            v-model="formData.parentId"
            placeholder="选择父级"
            style="width: 100%"
          >
            <el-option label="无" value="" />
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            rows="4"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="formData.isEnabled" />
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { 
  getBasicInfoList, 
  getBasicInfoListByCategory, 
  saveBasicInfo, 
  updateBasicInfo, 
  deleteBasicInfo, 
  toggleBasicInfoEnabled
} from '../api/basic-info'

const infoList = ref([])
const categoryList = ref([])
const parentOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加信息')

const searchForm = ref({
  category: '',
  name: ''
})

const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

const formData = ref({
  id: '',
  category: '',
  name: '',
  code: '',
  parentId: '',
  sort: 0,
  content: '',
  isEnabled: true
})

const loadCategories = async () => {
  try {
    const response = await getBasicInfoListByCategory('CATEGORY')
    categoryList.value = response.data || []
  } catch (error) {
    ElMessage.error('加载分类数据失败')
  }
}

const loadParentOptions = async () => {
  try {
    if (!formData.value.category) return
    const response = await getBasicInfoListByCategory(formData.value.category)
    parentOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('加载父级选项失败')
  }
}

const loadData = async () => {
  try {
    const response = await getBasicInfoList()
    const data = response.data || []
    
    // 过滤数据
    let filteredData = data
    if (searchForm.value.category) {
      filteredData = filteredData.filter(item => item.category === searchForm.value.category)
    }
    if (searchForm.value.name) {
      filteredData = filteredData.filter(item => 
        item.name.toLowerCase().includes(searchForm.value.name.toLowerCase())
      )
    }
    
    // 分页
    pagination.value.total = filteredData.length
    const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
    const end = start + pagination.value.pageSize
    infoList.value = filteredData.slice(start, end)
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const handleSearch = () => {
  pagination.value.currentPage = 1
  loadData()
}

const handleReset = () => {
  searchForm.value = {
    category: '',
    name: ''
  }
  pagination.value.currentPage = 1
  loadData()
}

const handleCategoryChange = () => {
  loadData()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.value.currentPage = current
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '添加信息'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑信息'
  formData.value = {
    ...row,
    parentId: row.parent ? row.parent.id : ''
  }
  loadParentOptions()
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该信息吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBasicInfo(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleToggleEnabled = async (row) => {
  try {
    await toggleBasicInfoEnabled(row.id, !row.isEnabled)
    ElMessage.success(row.isEnabled ? '已禁用' : '已启用')
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
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
    category: '',
    name: '',
    code: '',
    parentId: '',
    sort: 0,
    content: '',
    isEnabled: true
  }
  parentOptions.value = []
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style scoped>
.basic-info-list {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-toolbar {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.table-pagination {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>