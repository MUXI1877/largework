<template>
  <div class="permission-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>权限配置</span>
          <div class="header-actions">
            <el-select v-model="selectedRoleId" placeholder="请选择角色" style="width: 200px" @change="handleRoleChange">
              <el-option
                v-for="role in roleList"
                :key="role.id"
                :label="role.roleName"
                :value="role.id"
              />
            </el-select>
            <el-button type="primary" link @click="refreshModules">刷新模块</el-button>
          </div>
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
                default-expand-all
                check-strictly
                :default-checked-keys="checkedKeys"
                @check="handleCheck"
                ref="treeRef"
                style="max-height: 500px; overflow-y: auto;"
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
                <el-button @click="applyToChildren" :disabled="!selectedModuleId">同步到子节点</el-button>
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
import { getPermissionsByRole, savePermission, bulkSavePermission, configurePermissionsForChildren } from '../api/permission'
import { clearPermissionCache } from '../utils/permission'

const roleList = ref([])
const moduleTree = ref([])
const moduleMap = ref({})
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
    moduleTree.value = res.data || []
    
    // 构建模块映射表，用于快速查找模块
    buildModuleMap(moduleTree.value)
  } catch (error) {
    ElMessage.error('加载模块树失败')
  }
}

const buildModuleMap = (nodes) => {
  moduleMap.value = {}
  
  const traverse = (node) => {
    moduleMap.value[node.id] = node
    if (node.children && node.children.length) {
      node.children.forEach(child => traverse(child))
    }
  }
  
  nodes.forEach(node => traverse(node))
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

    // ✅ 关键修复：只勾选 canSee 为 true 的模块，避免自动勾选所有模块
    checkedKeys.value = permissions
        .filter(p => p.canSee === true)  // 只选择有访问权限的
        .map(p => p.moduleId)

    treeRef.value?.setCheckedKeys(checkedKeys.value)
  } catch (error) {
    ElMessage.error('加载权限配置失败')
  }
}

// 重新从模块管理获取最新模块树并与当前角色权限同步
const refreshModules = async () => {
  await loadModules()
  if (selectedRoleId.value) {
    await handleRoleChange()
  }
}

const saveCurrentPermission = async () => {
  if (!selectedRoleId.value) {
    ElMessage.warning('请先选择角色')
    return
  }
  permissionForm.roleId = selectedRoleId.value
  await savePermission(permissionForm)
  // 清除权限缓存，使权限修改立即生效
  clearPermissionCache()
  // 重新加载当前角色权限，保持数据同步
  await handleRoleChange()
}

const pendingChanges = ref(new Map())  // 记录待保存的修改

const getDescendantIds = (id) => {
  const node = moduleMap.value[id]
  if (!node || !node.children) return []
  const res = []
  const stack = [...node.children]
  while (stack.length) {
    const cur = stack.pop()
    res.push(cur.id)
    if (cur.children && cur.children.length) {
      stack.push(...cur.children)
    }
  }
  return res
}

const applyToChildren = async () => {
  if (!selectedModuleId.value) {
    ElMessage.warning('请先选择模块')
    return
  }
  
  if (!selectedRoleId.value) {
    ElMessage.warning('请先选择角色')
    return
  }

  const ids = getDescendantIds(selectedModuleId.value)
  if (!ids.length) {
    ElMessage.info('该模块没有子节点')
    return
  }

  try {
    const configData = {
      roleId: selectedRoleId.value,
      moduleId: selectedModuleId.value,
      canRead: !!permissionForm.canRead,
      canAdd: !!permissionForm.canAdd,
      canUpdate: !!permissionForm.canUpdate,
      canSee: !!permissionForm.canSee
    }

    await configurePermissionsForChildren(configData)
    
    // 清除权限缓存，使权限修改立即生效
    clearPermissionCache()
    
    // 更新本地权限映射
    const base = {
      roleId: selectedRoleId.value,
      canRead: !!permissionForm.canRead,
      canAdd: !!permissionForm.canAdd,
      canUpdate: !!permissionForm.canUpdate,
      canSee: !!permissionForm.canSee
    }

    ids.forEach((mid) => {
      const existing = permissionMap.value[mid]
      const data = {
        ...base,
        moduleId: mid,
        id: existing ? existing.id : undefined
      }
      permissionMap.value[mid] = data

      if (base.canSee) {
        if (!checkedKeys.value.includes(mid)) {
          checkedKeys.value.push(mid)
        }
      } else {
        const idx = checkedKeys.value.indexOf(mid)
        if (idx > -1) checkedKeys.value.splice(idx, 1)
      }
    })

    treeRef.value?.setCheckedKeys(checkedKeys.value)
    ElMessage.success(`权限已成功同步到 ${ids.length} 个子节点`)
    
  } catch (error) {
    ElMessage.error('同步到子节点失败')
  }
}

const handleCheck = async (data, { checkedKeys: keys }) => {
  selectedModuleId.value = data.id
  selectedModuleName.value = data.cnName

  const permission = permissionMap.value[data.id]
  const isChecked = keys.includes(data.id)

  // 更新权限表单数据
  if (permission) {
    permissionForm.roleId = permission.roleId
    permissionForm.moduleId = permission.moduleId
    permissionForm.canRead = permission.canRead || false
    permissionForm.canAdd = permission.canAdd || false
    permissionForm.canUpdate = permission.canUpdate || false
    permissionForm.canSee = isChecked
  } else {
    permissionForm.roleId = selectedRoleId.value
    permissionForm.moduleId = data.id
    permissionForm.canRead = isChecked
    permissionForm.canAdd = false
    permissionForm.canUpdate = false
    permissionForm.canSee = isChecked
  }

  // ✅ 记录待保存的修改 - 关键修复：确保包含ID字段和正确的roleId
  const permissionData = {
    ...permissionForm,
    roleId: selectedRoleId.value,  // 确保使用当前选中的角色ID
    moduleId: data.id,  // 确保使用当前模块ID
    id: permission ? permission.id : undefined  // 包含原有权限记录的ID
  }
  pendingChanges.value.set(data.id, permissionData)

  // 立即更新本地显示，但不立即保存到服务器
  permissionMap.value[data.id] = permissionData

  // 更新勾选状态
  if (isChecked) {
    if (!checkedKeys.value.includes(data.id)) {
      checkedKeys.value.push(data.id)
    }
  } else {
    const index = checkedKeys.value.indexOf(data.id)
    if (index > -1) {
      checkedKeys.value.splice(index, 1)
    }
  }

  ElMessage.success(`已${isChecked ? '开启' : '关闭'}模块访问权限`)
}

// ✅ 批量保存方法
const saveAllChanges = async () => {
  if (!selectedRoleId.value) {
    ElMessage.warning('请先选择角色')
    return
  }

  if (pendingChanges.value.size === 0) {
    ElMessage.info('没有需要保存的修改')
    return
  }

  try {
    const changes = Array.from(pendingChanges.value.values())
    
    // ✅ 关键修复：确保所有记录的roleId都正确设置，并验证数据完整性
    changes.forEach((change, index) => {
      // 强制设置roleId为当前选中的角色
      change.roleId = selectedRoleId.value
      
      // 确保所有必填字段都有值
      if (!change.moduleId) {
        throw new Error(`模块ID为空，无法保存（记录索引: ${index}）`)
      }
      
      // 确保布尔字段有默认值
      if (change.canRead === undefined) change.canRead = false
      if (change.canAdd === undefined) change.canAdd = false
      if (change.canUpdate === undefined) change.canUpdate = false
      if (change.canSee === undefined) change.canSee = false
      
      // 移除undefined的id字段，避免序列化问题
      if (change.id === undefined) {
        delete change.id
      }
    })
    
    console.log('批量保存权限数据:', {
      roleId: selectedRoleId.value,
      count: changes.length,
      changes: changes.map(c => ({ roleId: c.roleId, moduleId: c.moduleId, canSee: c.canSee }))
    })

    // 后端批量保存，避免频繁请求遗漏
    await bulkSavePermission(changes)

    // 清除权限缓存，使权限修改立即生效
    clearPermissionCache()

    pendingChanges.value.clear()  // 清空待保存列表
    ElMessage.success(`成功保存 ${changes.length} 个模块的权限配置`)

    // 重新加载权限数据，确保显示最新状态
    await handleRoleChange()
  } catch (error) {
    console.error('批量保存权限失败:', error)
    const errorMsg = error.response?.data?.message || error.message || '批量保存权限失败'
    ElMessage.error(errorMsg)
  }
}

// ✅ 修改现有的保存按钮处理
const handleSavePermission = async () => {
  if (!selectedRoleId.value) {
    ElMessage.warning('请先选择角色')
    return
  }

  if (!selectedModuleId.value) {
    ElMessage.warning('请先选择模块')
    return
  }

  // 确保表单数据同步到待保存列表
  if (pendingChanges.value.has(selectedModuleId.value)) {
    // 如果当前模块在待保存列表中，更新它
    const existing = pendingChanges.value.get(selectedModuleId.value)
    pendingChanges.value.set(selectedModuleId.value, {
      ...existing,
      ...permissionForm,
      roleId: selectedRoleId.value,
      moduleId: selectedModuleId.value
    })
  } else {
    // 如果当前模块不在待保存列表中，添加到列表
    const permission = permissionMap.value[selectedModuleId.value]
    pendingChanges.value.set(selectedModuleId.value, {
      ...permissionForm,
      roleId: selectedRoleId.value,
      moduleId: selectedModuleId.value,
      id: permission ? permission.id : undefined
    })
  }

  // 确保布尔字段有值
  const permissionData = pendingChanges.value.get(selectedModuleId.value)
  if (permissionData) {
    if (permissionData.canRead === undefined) permissionData.canRead = false
    if (permissionData.canAdd === undefined) permissionData.canAdd = false
    if (permissionData.canUpdate === undefined) permissionData.canUpdate = false
    if (permissionData.canSee === undefined) permissionData.canSee = false
    // 移除undefined的id字段
    if (permissionData.id === undefined) {
      delete permissionData.id
    }
  }

  // 判断是单独保存还是批量保存
  // 如果只有当前模块在待保存列表中，单独保存；否则批量保存
  const hasOtherPendingChanges = pendingChanges.value.size > 1 || 
    (pendingChanges.value.size === 1 && !pendingChanges.value.has(selectedModuleId.value))

  if (hasOtherPendingChanges) {
    // 如果有其他待保存的修改，执行批量保存
    await saveAllChanges()
  } else {
    // 只保存当前模块（单独保存）
    try {
      // 使用待保存列表中的数据，确保包含所有字段
      const saveData = permissionData || {
        ...permissionForm,
        roleId: selectedRoleId.value,
        moduleId: selectedModuleId.value
      }
      
      // 确保布尔字段有值
      if (saveData.canRead === undefined) saveData.canRead = false
      if (saveData.canAdd === undefined) saveData.canAdd = false
      if (saveData.canUpdate === undefined) saveData.canUpdate = false
      if (saveData.canSee === undefined) saveData.canSee = false
      // 移除undefined的id字段
      if (saveData.id === undefined) {
        delete saveData.id
      }
      
      console.log('保存权限数据:', saveData)
      await savePermission(saveData)
      // 清除权限缓存，使权限修改立即生效
      clearPermissionCache()
      ElMessage.success('权限保存成功')
      // 从待保存列表中移除（如果存在）
      pendingChanges.value.delete(selectedModuleId.value)
      // 重新加载当前角色权限，保持数据同步
      await handleRoleChange()
    } catch (error) {
      console.error('保存权限失败:', error)
      ElMessage.error(error.response?.data?.message || error.message || '权限保存失败')
    }
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