<template>
  <div class="module-management">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>模块树</span>
              <el-button type="primary" size="small" @click="handleAddRoot">新增根节点</el-button>
            </div>
          </template>
          <el-tree
            :data="moduleTree"
            :props="{ children: 'children', label: 'cnName' }"
            node-key="id"
            default-expand-all
            @node-click="handleNodeClick"
            @node-contextmenu="handleContextMenu"
          >
            <template #default="{ node, data }">
              <span class="tree-node">
                <span>{{ node.label }}</span>
                <el-button
                  type="primary"
                  size="small"
                  text
                  @click.stop="handleAddChild(data)"
                >
                  新增
                </el-button>
              </span>
            </template>
          </el-tree>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>模块详情</span>
          </template>
          <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" v-if="selectedModule">
            <el-form-item label="中文名称" prop="cnName">
              <el-input v-model="form.cnName" />
            </el-form-item>
            <el-form-item label="英文名称" prop="enName">
              <el-input v-model="form.enName" />
            </el-form-item>
            <el-form-item label="菜单级别" prop="menuLevel">
              <el-input-number v-model="form.menuLevel" :min="1" />
            </el-form-item>
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
            <el-form-item label="路径" prop="path">
              <el-input v-model="form.path" />
            </el-form-item>
            <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" />
            </el-form-item>
            <el-form-item label="分组名称" prop="groupName">
              <el-input v-model="form.groupName" />
            </el-form-item>
            <el-form-item label="父节点ID" prop="parentId">
              <el-input v-model="form.parentId" disabled />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存</el-button>
              <el-button type="danger" @click="handleDelete">删除</el-button>
            </el-form-item>
          </el-form>
          <el-empty v-else description="请选择模块节点" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="dialogVisible" title="新增模块" width="500px">
      <el-form :model="dialogForm" :rules="rules" ref="dialogFormRef" label-width="120px">
        <el-form-item label="中文名称" prop="cnName">
          <el-input v-model="dialogForm.cnName" />
        </el-form-item>
        <el-form-item label="英文名称" prop="enName">
          <el-input v-model="dialogForm.enName" />
        </el-form-item>
        <el-form-item label="菜单级别" prop="menuLevel">
          <el-input-number v-model="dialogForm.menuLevel" :min="1" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="dialogForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="dialogForm.path" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="dialogForm.icon" />
        </el-form-item>
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model="dialogForm.groupName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDialogSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getModuleTree, getModuleChildren, getModuleById, saveModule, deleteModule } from '../api/module'

const moduleTree = ref([])
const selectedModule = ref(null)
const dialogVisible = ref(false)
const formRef = ref(null)
const dialogFormRef = ref(null)
const parentModule = ref(null)

const form = reactive({
  id: null,
  cnName: '',
  enName: '',
  menuLevel: 1,
  sort: 0,
  path: '',
  icon: '',
  groupName: '',
  parentId: null
})

const dialogForm = reactive({
  cnName: '',
  enName: '',
  menuLevel: 1,
  sort: 0,
  path: '',
  icon: '',
  groupName: '',
  parentId: null
})

const rules = {
  cnName: [{ required: true, message: '请输入中文名称', trigger: 'blur' }]
}

const loadTree = async () => {
  try {
    const res = await getModuleTree()
    moduleTree.value = await buildTree(res.data || [])
  } catch (error) {
    ElMessage.error('加载模块树失败')
  }
}

const buildTree = async (modules) => {
  const tree = []
  const map = {}
  
  modules.forEach(m => {
    map[m.id] = { ...m, children: [] }
  })
  
  modules.forEach(m => {
    if (m.parentId) {
      if (map[m.parentId]) {
        map[m.parentId].children.push(map[m.id])
      }
    } else {
      tree.push(map[m.id])
    }
  })
  
  return tree.sort((a, b) => (a.sort || 0) - (b.sort || 0))
}

const handleNodeClick = async (data) => {
  try {
    const res = await getModuleById(data.id)
    selectedModule.value = res.data
    Object.assign(form, res.data)
  } catch (error) {
    ElMessage.error('加载模块详情失败')
  }
}

const handleContextMenu = (event, data) => {
  event.preventDefault()
}

const handleAddRoot = () => {
  parentModule.value = null
  dialogForm.parentId = null
  dialogForm.cnName = ''
  dialogForm.enName = ''
  dialogForm.menuLevel = 1
  dialogForm.sort = 0
  dialogForm.path = ''
  dialogForm.icon = ''
  dialogForm.groupName = ''
  dialogVisible.value = true
}

const handleAddChild = (data) => {
  parentModule.value = data
  dialogForm.parentId = data.id
  dialogForm.cnName = ''
  dialogForm.enName = ''
  dialogForm.menuLevel = (data.menuLevel || 1) + 1
  dialogForm.sort = 0
  dialogForm.path = ''
  dialogForm.icon = ''
  dialogForm.groupName = ''
  dialogVisible.value = true
}

const handleDialogSave = async () => {
  await dialogFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveModule(dialogForm)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadTree()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleSave = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveModule(form)
        ElMessage.success('保存成功')
        loadTree()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDelete = () => {
  if (!selectedModule.value) return
  
  ElMessageBox.confirm('确定要删除该模块吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteModule(selectedModule.value.id)
      ElMessage.success('删除成功')
      selectedModule.value = null
      loadTree()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

onMounted(() => {
  loadTree()
})
</script>

<style scoped>
.module-management {
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
  flex: 1;
}
</style>

