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
              ref="treeRef"
              :data="moduleTree"
              :props="treeProps"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="handleNodeClick"
              @node-contextmenu="handleContextMenu"
          >
            <template #default="{ node, data }">
              <span class="tree-node">
                <span>{{ node.label }}</span>
                <span class="tree-actions">
                  <el-button
                      type="primary"
                      size="small"
                      text
                      @click.stop="handleAddChild(data)"
                  >
                    新增
                  </el-button>
                  <el-button
                      type="danger"
                      size="small"
                      text
                      :disabled="data.children && data.children.length > 0"
                      @click.stop="handleDelete(data)"
                  >
                    删除
                  </el-button>
                </span>
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
              <el-input v-model="form.cnName" placeholder="请输入中文名称" />
            </el-form-item>
            <el-form-item label="英文名称" prop="enName">
              <el-input v-model="form.enName" placeholder="请输入英文名称（驼峰命名）" />
            </el-form-item>
            <el-form-item label="菜单级别" prop="menuLevel">
              <el-input-number v-model="form.menuLevel" :min="1" :disabled="true" />
            </el-form-item>
            <el-form-item label="菜单序号" prop="sort">
              <el-input-number v-model="form.sort" :min="-999" :max="999" />
              <span class="form-tip">（越小越靠前，支持负数）</span>
            </el-form-item>
            <el-form-item label="链接路径" prop="path">
              <el-input v-model="form.path" placeholder="如：/sys/user，纯目录可空" />
            </el-form-item>
            <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" placeholder="Element图标名，如：el-icon-menu" />
            </el-form-item>
            <el-form-item label="分组" prop="groupName">
              <el-input v-model="form.groupName" placeholder="扩展字段，可空" />
            </el-form-item>
            <el-form-item label="权限字符串" prop="permission">
              <el-input v-model="form.permission" placeholder="如：system:user:add，纯目录可空" />
            </el-form-item>
            <el-form-item label="菜单可见" prop="visible">
              <el-switch v-model="form.visible" />
              <span class="form-tip">（若否，仍可作为权限节点被角色引用）</span>
            </el-form-item>
            <el-form-item label="父节点ID" prop="parentId">
              <el-input v-model="form.parentId" disabled />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存</el-button>
              <el-button type="danger" @click="handleDelete" :disabled="selectedModule.children && selectedModule.children.length > 0">
                删除
              </el-button>
            </el-form-item>
          </el-form>
          <el-empty v-else description="请选择模块节点" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 右键菜单 -->
    <div v-if="contextMenu.visible" class="context-menu"
         :style="{ left: contextMenu.x + 'px', top: contextMenu.y + 'px' }">
      <div class="menu-item" @click="handleContextMenuAdd">新增子节点</div>
      <div class="menu-item" @click="handleContextMenuEdit">编辑</div>
      <div class="menu-item" @click="handleContextMenuDelete"
           :class="{ disabled: contextMenu.data.children && contextMenu.data.children.length > 0 }">
        删除
      </div>
    </div>

    <!-- 新增对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="dialogForm" :rules="rules" ref="dialogFormRef" label-width="120px">
        <el-form-item label="中文名称" prop="cnName">
          <el-input v-model="dialogForm.cnName" placeholder="请输入中文名称" />
        </el-form-item>
        <el-form-item label="英文名称" prop="enName">
          <el-input v-model="dialogForm.enName" placeholder="请输入英文名称（驼峰命名）" />
        </el-form-item>
        <el-form-item label="菜单序号" prop="sort">
          <el-input-number v-model="dialogForm.sort" :min="-999" :max="999" />
          <span class="form-tip">（越小越靠前，支持负数）</span>
        </el-form-item>
        <el-form-item label="链接路径" prop="path">
          <el-input v-model="dialogForm.path" placeholder="如：/sys/user，纯目录可空" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="dialogForm.icon" placeholder="Element图标名，如：el-icon-menu" />
        </el-form-item>
        <el-form-item label="分组" prop="groupName">
          <el-input v-model="dialogForm.groupName" placeholder="扩展字段，可空" />
        </el-form-item>
        <el-form-item label="权限字符串" prop="permission">
          <el-input v-model="dialogForm.permission" placeholder="如：system:user:add，纯目录可空" />
        </el-form-item>
        <el-form-item label="菜单可见" prop="visible">
          <el-switch v-model="dialogForm.visible" />
          <span class="form-tip">（若否，仍可作为权限节点被角色引用）</span>
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
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getModuleTree, getModuleChildren, getModuleById, saveModule, deleteModule } from '../api/module'

const treeRef = ref(null)
const moduleTree = ref([])
const selectedModule = ref(null)
const dialogVisible = ref(false)
const formRef = ref(null)
const dialogFormRef = ref(null)
const contextMenuNode = ref(null)

const treeProps = {
  children: 'children',
  label: 'cnName'
}

const contextMenu = reactive({
  visible: false,
  x: 0,
  y: 0,
  data: null
})

const form = reactive({
  id: null,
  cnName: '',
  enName: '',
  menuLevel: 1,
  sort: 0,
  path: '',
  icon: '',
  groupName: '',
  permission: '',
  visible: true,
  parentId: null
})

const dialogForm = reactive({
  cnName: '',
  enName: '',
  sort: 0,
  path: '',
  icon: '',
  groupName: '',
  permission: '',
  visible: true,
  parentId: null
})

const dialogTitle = ref('新增模块')

const rules = {
  cnName: [{ required: true, message: '请输入中文名称', trigger: 'blur' }],
  enName: [
    { required: true, message: '请输入英文名称', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9]*$/, message: '英文名称需符合驼峰命名规范', trigger: 'blur' }
  ]
}

const loadTree = async () => {
  try {
    const res = await getModuleTree()
    console.log('树形数据:', res.data) // 调试用，查看数据结构
    moduleTree.value = res.data || []
    if (moduleTree.value.length === 0) {
      ElMessage.info('暂无模块数据，请先添加根节点')
    }
  } catch (error) {
    console.error('加载模块树失败:', error)
    ElMessage.error('加载模块树失败')
  }
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

const handleContextMenu = (event, data, node) => {
  event.preventDefault()
  contextMenu.visible = true
  contextMenu.x = event.clientX
  contextMenu.y = event.clientY
  contextMenu.data = data
  contextMenuNode.value = node

  // 点击其他地方关闭菜单
  nextTick(() => {
    document.addEventListener('click', closeContextMenu)
  })
}

const closeContextMenu = () => {
  contextMenu.visible = false
  document.removeEventListener('click', closeContextMenu)
}

const handleContextMenuAdd = () => {
  handleAddChild(contextMenu.data)
  closeContextMenu()
}

const handleContextMenuEdit = () => {
  handleNodeClick(contextMenu.data)
  closeContextMenu()
}

const handleContextMenuDelete = () => {
  if (contextMenu.data.children && contextMenu.data.children.length > 0) {
    ElMessage.warning('请先删除下级模块')
    return
  }
  handleDelete(contextMenu.data)
  closeContextMenu()
}

const handleAddRoot = () => {
  dialogTitle.value = '新增根节点'
  Object.assign(dialogForm, {
    cnName: '',
    enName: '',
    sort: 0,
    path: '',
    icon: '',
    groupName: '',
    permission: '',
    visible: true,
    parentId: null
  })
  dialogVisible.value = true
}

const handleAddChild = (data) => {
  dialogTitle.value = '新增子节点'
  Object.assign(dialogForm, {
    cnName: '',
    enName: '',
    sort: 0,
    path: '',
    icon: '',
    groupName: '',
    permission: '',
    visible: true,
    parentId: data.id
  })
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

const handleDelete = async (data) => {
  const moduleToDelete = data || selectedModule.value
  if (!moduleToDelete) return

  // 检查是否有子节点
  if (moduleToDelete.children && moduleToDelete.children.length > 0) {
    ElMessage.warning('请先删除下级模块')
    return
  }

  ElMessageBox.confirm('确定要删除该模块吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteModule(moduleToDelete.id)
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
  padding: 20px;
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

.tree-actions {
  display: flex;
  gap: 4px;
}

.form-tip {
  margin-left: 8px;
  color: #999;
  font-size: 12px;
}

.context-menu {
  position: fixed;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 2000;
}

.menu-item {
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
}

.menu-item:hover {
  background: #f5f7fa;
}

.menu-item.disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.menu-item.disabled:hover {
  background: white;
}
</style>
