<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <h2>用户注册</h2>
      </template>
      
      <el-steps :active="step" finish-status="success" align-center>
        <el-step title="验证身份证" />
        <el-step title="填写信息" />
      </el-steps>

      <div v-if="step === 0" class="step-content">
        <el-form :model="verifyForm" :rules="verifyRules" ref="verifyFormRef" label-width="100px">
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="verifyForm.idCard" placeholder="请输入18位身份证号" maxlength="18" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleVerify" :loading="verifying">验证</el-button>
            <el-button @click="$router.push('/login')">返回登录</el-button>
          </el-form-item>
        </el-form>
        
        <el-alert v-if="verifyResult" :type="verifyResult.type" :title="verifyResult.message" :closable="false" />
      </div>

      <div v-if="step === 1" class="step-content">
        <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="100px">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="registerForm.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="registerForm.idCard" disabled />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="registerForm.phone" placeholder="请输入手机号" maxlength="11" />
          </el-form-item>
          <el-form-item label="岗位" prop="postId">
            <el-select v-model="registerForm.postId" placeholder="请选择岗位" style="width: 100%">
              <el-option v-for="item in postOptions" :key="item.id" :label="item.title" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="片区" prop="areaId">
            <el-select v-model="registerForm.areaId" placeholder="请选择片区" style="width: 100%">
              <el-option v-for="item in areaOptions" :key="item.id" :label="item.title" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="性别">
            <el-input v-model="registerForm.gender" disabled />
          </el-form-item>
          <el-form-item label="出生日期">
            <el-input v-model="registerForm.birthDate" disabled />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleRegister" :loading="registering">提交注册</el-button>
            <el-button @click="step = 0">上一步</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { verifyIdCard, register } from '../api/user'
import { getOptionList } from '../api/option'
import { parseIdCard } from '../utils/idCard'

const router = useRouter()
const step = ref(0)
const verifying = ref(false)
const registering = ref(false)
const verifyFormRef = ref(null)
const registerFormRef = ref(null)
const verifyResult = ref(null)
const postOptions = ref([])
const areaOptions = ref([])

const verifyForm = reactive({
  idCard: ''
})

const registerForm = reactive({
  name: '',
  idCard: '',
  phone: '',
  postId: '',
  areaId: '',
  gender: '',
  birthDate: ''
})

const verifyRules = {
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { len: 18, message: '身份证号必须为18位', trigger: 'blur' }
  ]
}

const registerRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  postId: [{ required: true, message: '请选择岗位', trigger: 'change' }],
  areaId: [{ required: true, message: '请选择片区', trigger: 'change' }]
}

const loadOptions = async () => {
  try {
    const [postRes, areaRes] = await Promise.all([
      getOptionList('post'),
      getOptionList('area')
    ])
    postOptions.value = postRes.data || []
    areaOptions.value = areaRes.data || []
  } catch (error) {
    console.error('加载选项失败', error)
  }
}

const handleVerify = async () => {
  await verifyFormRef.value.validate(async (valid) => {
    if (valid) {
      verifying.value = true
      try {
        const res = await verifyIdCard(verifyForm.idCard)
        if (res.data && res.data.username) {
          verifyResult.value = {
            type: 'warning',
            message: `该身份证号已注册，账号：${res.data.username}，姓名：${res.data.name}`
          }
        } else {
          const parsed = parseIdCard(verifyForm.idCard)
          if (parsed) {
            registerForm.idCard = verifyForm.idCard
            registerForm.gender = parsed.gender
            registerForm.birthDate = parsed.birthDate
            step.value = 1
            verifyResult.value = null
          } else {
            verifyResult.value = {
              type: 'error',
              message: '身份证号格式不正确'
            }
          }
        }
      } catch (error) {
        verifyResult.value = {
          type: 'error',
          message: error.message || '验证失败'
        }
      } finally {
        verifying.value = false
      }
    }
  })
}

const handleRegister = async () => {
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      registering.value = true
      try {
        await register(registerForm)
        ElMessage.success('注册成功，等待管理员审核')
        router.push('/login')
      } catch (error) {
        ElMessage.error(error.message || '注册失败')
      } finally {
        registering.value = false
      }
    }
  })
}

onMounted(() => {
  loadOptions()
})
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  width: 600px;
}

.register-card h2 {
  text-align: center;
  margin: 0;
}

.step-content {
  margin-top: 30px;
}
</style>

