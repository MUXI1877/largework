<template>
  <div class="permission-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>权限配置</span>
          <el-select v-model="selectedRoleId" placeholder="请选择角色" style="width: 200px" @change="handleRoleChange">
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card>
            <template #header>
              <span>模块列表</span>
            </template>
            <el-tree
              :data="moduleTree"
              :props="{ children: 'children', label: 'cnName' }"
              node-key="id"
              show-checkbox
              :default-checked-keys="checkedKeys"
              @check="handleCheck"
              ref="treeRef"
            />
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card>
            <template #header>
              <span>权限设置</span>
            </template>
            <div v-if="selectedModuleId">
              <el-form :model="permissionForm" label-width="120px">
                <el-form-item label="模块">
                  <el-input :value="selectedModuleName" disabled />
                </el-form-item>
                <el-form-item label="可读">
                  <el-switch v-model="permissionForm.canRead" />
                </el-form-item>
                <el-form-item label="可新增">
                  <el-switch v-model="permissionForm.canAdd" />
                </el-form-item>
                <el-form-item label="可更新">
                  <el-switch v-model="permissionForm.canUpdate" />
                </el-form-item>
                <el-form-item label="可见">
                  <el-switch v-model="permissionForm.canSee" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSavePermission">保存</el-button>
                </el-form-item>
              </el-form>
            </div>
            <el-empty v-else description="请选择模块" />
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getRoleList } from '../api/role'
import { getModuleTree } from '../api/module'
import { getPermissionsByRole, savePermission } from '../api/permission'

const roleList = ref([])
const moduleTree = ref([])
const selectedRoleId = ref('')
const selectedModuleId = ref('')
const selectedModuleName = ref('')
const treeRef = ref(null)
const checkedKeys = ref([])
const permissionMap = ref({})

const permissionForm = reactive({
  roleId: '',
  moduleId: '',
  canRead: false,
  canAdd: false,
  canUpdate: false,
  canSee: false
})

const loadRoles = async () => {
  try {
    const res = await getRoleList()
    roleList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  }
}

const loadModules = async () => {
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

const handleRoleChange = async () => {
  if (!selectedRoleId.value) {
    checkedKeys.value = []
    permissionMap.value = {}
    return
  }
  
  try {
    const res = await getPermissionsByRole(selectedRoleId.value)
    const permissions = res.data || []
    permissionMap.value = {}
    permissions.forEach(p => {
      permissionMap.value[p.moduleId] = p
    })
    checkedKeys.value = permissions.map(p => p.moduleId)
    treeRef.value?.setCheckedKeys(checkedKeys.value)
  } catch (error) {
    ElMessage.error('加载权限配置失败')
  }
}

const handleCheck = (data, { checkedKeys: keys }) => {
  selectedModuleId.value = data.id
  selectedModuleName.value = data.cnName
  
  const permission = permissionMap.value[data.id]
  if (permission) {
    permissionForm.roleId = permission.roleId
    permissionForm.moduleId = permission.moduleId
    permissionForm.canRead = permission.canRead || false
    permissionForm.canAdd = permission.canAdd || false
    permissionForm.canUpdate = permission.canUpdate || false
    permissionForm.canSee = permission.canSee || false
  } else {
    permissionForm.roleId = selectedRoleId.value
    permissionForm.moduleId = data.id
    permissionForm.canRead = false
    permissionForm.canAdd = false
    permissionForm.canUpdate = false
    permissionForm.canSee = false
  }
}

const handleSavePermission = async () => {
  if (!selectedRoleId.value) {
    ElMessage.warning('请先选择角色')
    return
  }
  
  try {
    permissionForm.roleId = selectedRoleId.value
    await savePermission(permissionForm)
    ElMessage.success('保存成功')
    handleRoleChange()
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
  }
}

onMounted(() => {
  loadRoles()
  loadModules()
})
</script>

<style scoped>
.permission-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

