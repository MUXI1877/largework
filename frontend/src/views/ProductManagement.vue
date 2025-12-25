<template>
  <div class="product-management">
    <el-tabs v-model="activeTab">
      <!-- 单体设备管理 -->
      <el-tab-pane label="单体设备管理" name="single">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>单体设备管理</span>
              <div>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'add' }"
                  type="primary" @click="handleAdd('SINGLE')">新增</el-button>
                <el-button type="success" @click="handleImport('SINGLE')">Excel导入</el-button>
                <el-button type="success" @click="handleExport('SINGLE')">导出</el-button>
              </div>
            </div>
          </template>

          <el-table :data="singleList" border style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="model" label="型号" />
            <el-table-column prop="dimensions" label="尺寸" />
            <el-table-column prop="weight" label="重量" />
            <el-table-column prop="deliveryCycle" label="交货周期" />
            <el-table-column label="附件">
              <template #default="scope">
                <el-tag v-if="getAttachmentCount(scope.row.id) > 0" type="success">
                  {{ getAttachmentCount(scope.row.id) }} 个附件
                </el-tag>
                <span v-else>无附件</span>
              </template>
            </el-table-column>
            <el-table-column prop="remarks" label="备注" />
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'update' }"
                  type="warning" size="small" @click="handleEdit(scope.row)">修改</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'update' }"
                  type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 备品备件管理 -->
      <el-tab-pane label="备品备件管理" name="spare">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>备品备件管理</span>
              <div>
                <el-button type="primary" @click="handleAdd('SPARE')">新增</el-button>
                <el-button type="success" @click="handleImport('SPARE')">Excel导入</el-button>
                <el-button type="success" @click="handleExport('SPARE')">导出</el-button>
              </div>
            </div>
          </template>

          <el-table :data="spareList" border style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="model" label="型号" />
            <el-table-column prop="dimensions" label="尺寸" />
            <el-table-column prop="weight" label="重量" />
            <el-table-column prop="deliveryCycle" label="交货周期" />
            <el-table-column label="附件">
              <template #default="scope">
                <el-tag v-if="getAttachmentCount(scope.row.id) > 0" type="success">
                  {{ getAttachmentCount(scope.row.id) }} 个附件
                </el-tag>
                <span v-else>无附件</span>
              </template>
            </el-table-column>
            <el-table-column prop="remarks" label="备注" />
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'update' }"
                  type="warning" size="small" @click="handleEdit(scope.row)">修改</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'update' }"
                  type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 设备成套管理 -->
      <el-tab-pane label="设备成套管理" name="complete">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>设备成套管理</span>
              <div>
                <el-button type="primary" @click="handleAdd('COMPLETE')">新增</el-button>
                <el-button type="success" @click="handleExport('COMPLETE')">导出</el-button>
              </div>
            </div>
          </template>

          <el-table :data="completeList" border style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="model" label="型号" />
            <el-table-column prop="dimensions" label="尺寸" />
            <el-table-column prop="weight" label="重量" />
            <el-table-column prop="deliveryCycle" label="交货周期" />
            <el-table-column label="附件">
              <template #default="scope">
                <el-tag v-if="getAttachmentCount(scope.row.id) > 0" type="success">
                  {{ getAttachmentCount(scope.row.id) }} 个附件
                </el-tag>
                <span v-else>无附件</span>
              </template>
            </el-table-column>
            <el-table-column prop="remarks" label="备注" />
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'update' }"
                  type="warning" size="small" @click="handleEdit(scope.row)">修改</el-button>
                <el-button 
                  v-permission="{ moduleId: 'm010', action: 'update' }"
                  type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 产品编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="formData.name" />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="formData.model" />
        </el-form-item>
        <el-form-item label="参数">
          <el-input v-model="formData.parameters" type="textarea" :rows="3" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格">
              <el-input-number v-model="formData.price" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重量">
              <el-input v-model="formData.weight" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="尺寸">
          <el-input v-model="formData.dimensions" />
        </el-form-item>
        <el-form-item label="交货周期">
          <el-input v-model="formData.deliveryCycle" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 产品详细信息对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="产品详细信息"
      width="1000px"
    >
      <el-tabs v-model="detailActiveTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="名称">{{ currentProduct.name }}</el-descriptions-item>
            <el-descriptions-item label="型号">{{ currentProduct.model }}</el-descriptions-item>
            <el-descriptions-item label="参数" :span="2">{{ currentProduct.parameters }}</el-descriptions-item>
            <el-descriptions-item label="价格">{{ currentProduct.price }}</el-descriptions-item>
            <el-descriptions-item label="重量">{{ currentProduct.weight }}</el-descriptions-item>
            <el-descriptions-item label="尺寸">{{ currentProduct.dimensions }}</el-descriptions-item>
            <el-descriptions-item label="交货周期">{{ currentProduct.deliveryCycle }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ currentProduct.remarks }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="附件管理" name="attachments">
          <div style="margin-bottom: 10px">
            <el-upload
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="{ productId: currentProduct.id }"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
            >
              <el-button type="primary">上传附件</el-button>
            </el-upload>
          </div>
          <el-table :data="attachmentList" border>
            <el-table-column prop="fileName" label="文件名" />
            <el-table-column prop="fileSize" label="文件大小">
              <template #default="scope">
                {{ formatFileSize(scope.row.fileSize) }}
              </template>
            </el-table-column>
            <el-table-column prop="fileType" label="文件类型" />
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button type="danger" size="small" @click="handleDeleteAttachment(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- Excel导入对话框 -->
    <el-dialog
      v-model="importDialogVisible"
      title="Excel导入"
      width="500px"
    >
      <el-upload
        :auto-upload="false"
        :on-change="handleFileChange"
        :before-upload="beforeUpload"
        accept=".xlsx,.xls"
        :limit="1"
        ref="uploadRef"
      >
        <el-button type="primary">选择Excel文件</el-button>
        <template #tip>
          <div class="el-upload__tip">只能上传Excel文件，格式：名称、型号、参数、价格、重量、尺寸、交货周期、备注</div>
        </template>
      </el-upload>
      <div style="margin-top: 20px">
        <el-button type="primary" @click="handleConfirmImport" :disabled="!selectedFile">确认导入</el-button>
        <el-button @click="importDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { canRead, loadPermissions } from '../utils/permission'
import {
  getProductList,
  saveProduct,
  deleteProduct,
  importProducts,
  exportProducts,
  getProductAttachments,
  deleteProductAttachment,
  uploadProductAttachment
} from '../api/product'
import { getToken } from '../utils/auth'

const activeTab = ref('single')
const loading = ref(false)
const singleList = ref([])
const spareList = ref([])
const completeList = ref([])
const attachmentMap = ref({})

const dialogVisible = ref(false)
const dialogTitle = ref('新增产品')
const formRef = ref(null)
const formData = reactive({
  id: '',
  name: '',
  model: '',
  parameters: '',
  price: 0,
  weight: '',
  dimensions: '',
  deliveryCycle: '',
  productType: '',
  remarks: ''
})

const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }]
}

const detailDialogVisible = ref(false)
const detailActiveTab = ref('basic')
const currentProduct = ref({})
const attachmentList = ref([])

const importDialogVisible = ref(false)
const currentProductType = ref('')
const selectedFile = ref(null)
const uploadRef = ref(null)

const uploadUrl = computed(() => '/api/product/attachment/upload')
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${getToken()}`
}))

const loadData = async () => {
  loading.value = true
  try {
    const [singleRes, spareRes, completeRes] = await Promise.all([
      getProductList('SINGLE'),
      getProductList('SPARE'),
      getProductList('COMPLETE')
    ])
    singleList.value = singleRes.data || []
    spareList.value = spareRes.data || []
    completeList.value = completeRes.data || []
    
    // 加载所有产品的附件数量
    await loadAllAttachments()
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadAllAttachments = async () => {
  const allProducts = [...singleList.value, ...spareList.value, ...completeList.value]
  for (const product of allProducts) {
    try {
      const res = await getProductAttachments(product.id)
      attachmentMap.value[product.id] = res.data || []
    } catch (error) {
      attachmentMap.value[product.id] = []
    }
  }
}

const getAttachmentCount = (productId) => {
  return attachmentMap.value[productId]?.length || 0
}

const handleAdd = (productType) => {
  dialogTitle.value = '新增产品'
  currentProductType.value = productType
  Object.assign(formData, {
    id: '',
    name: '',
    model: '',
    parameters: '',
    price: 0,
    weight: '',
    dimensions: '',
    deliveryCycle: '',
    productType: productType,
    remarks: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '修改产品'
  currentProductType.value = row.productType
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveProduct(formData)
        ElMessage.success('保存成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(error.message || '保存失败')
      }
    }
  })
}

const handleDelete = async (row) => {
  ElMessageBox.confirm('确定要删除该产品吗？删除后关联的附件也将被删除。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteProduct(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const handleView = async (row) => {
  currentProduct.value = row
  detailDialogVisible.value = true
  detailActiveTab.value = 'basic'
  
  try {
    const res = await getProductAttachments(row.id)
    attachmentList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载附件失败')
  }
}

const handleImport = (productType) => {
  currentProductType.value = productType
  selectedFile.value = null
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
  importDialogVisible.value = true
}

const handleFileChange = (file) => {
  selectedFile.value = file.raw
}

const handleConfirmImport = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择Excel文件')
    return
  }
  
  try {
    const res = await importProducts(selectedFile.value, currentProductType.value)
    if (res.code === 200) {
      ElMessage.success(res.message || '导入成功')
      importDialogVisible.value = false
      selectedFile.value = null
      if (uploadRef.value) {
        uploadRef.value.clearFiles()
      }
      loadData()
    } else {
      ElMessage.error(res.message || '导入失败')
    }
  } catch (error) {
    ElMessage.error(error.message || '导入失败')
  }
}

const handleExport = async (productType) => {
  try {
    const response = await exportProducts(productType)
    // 创建下载链接
    const blob = new Blob([response], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    const filename = `产品列表_${productType || '全部'}_${new Date().getTime()}.xlsx`
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + (error.message || '未知错误'))
  }
}

const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                  file.type === 'application/vnd.ms-excel' ||
                  file.name.endsWith('.xlsx') ||
                  file.name.endsWith('.xls')
  if (!isExcel) {
    ElMessage.error('只能上传Excel文件')
    return false
  }
  return false // 阻止自动上传
}

const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    ElMessage.success('上传成功')
    handleView(currentProduct.value)
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

const handleUploadError = () => {
  ElMessage.error('上传失败')
}

const handleDeleteAttachment = async (row) => {
  ElMessageBox.confirm('确定要删除该附件吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteProductAttachment(row.id)
      ElMessage.success('删除成功')
      const res = await getProductAttachments(currentProduct.value.id)
      attachmentList.value = res.data || []
      loadData() // 重新加载以更新附件数量
    } catch (error) {
      ElMessage.error(error.message || '删除失败')
    }
  })
}

const formatFileSize = (bytes) => {
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(async () => {
  await loadPermissions()
  if (canRead('m010')) {
    loadData()
  } else {
    ElMessage.warning('您没有查看产品管理的权限')
  }
})
</script>

<style scoped>
.product-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

