<template>
  <div class="register-container">
    <div class="register-card">
      <h2 class="register-title">用户注册</h2>
      <a-form
        :model="form"
        @submit="handleSubmit"
        layout="vertical"
        class="register-form"
      >
        <a-form-item
          field="userAccount"
          label="账号"
          :rules="[
            { required: true, message: '请输入账号' },
            { minLength: 4, message: '账号长度不能少于4位' },
          ]"
        >
          <a-input
            v-model="form.userAccount"
            placeholder="请输入账号"
            size="large"
            class="input-with-icon"
          >
            <template #prefix>
              <icon-user />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item
          field="userName"
          label="用户名"
          :rules="[{ required: true, message: '请输入用户名' }]"
        >
          <a-input
            v-model="form.userName"
            placeholder="请输入用户名"
            size="large"
            class="input-with-icon"
          >
            <template #prefix>
              <icon-user />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item
          field="userPassword"
          label="密码"
          :rules="[
            { required: true, message: '请输入密码' },
            { minLength: 8, message: '密码长度不能少于8位' },
          ]"
        >
          <a-input-password
            v-model="form.userPassword"
            placeholder="请输入密码"
            size="large"
            class="input-with-icon"
            :default-visible="false"
          >
            <template #prefix>
              <icon-lock />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item
          field="checkPassword"
          label="确认密码"
          :rules="checkPasswordRules"
        >
          <a-input-password
            v-model="form.checkPassword"
            placeholder="请再次输入密码"
            size="large"
            class="input-with-icon"
            :default-visible="false"
          >
            <template #prefix>
              <icon-lock />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item field="userProfile" label="个人简介（选填）">
          <a-textarea
            v-model="form.userProfile"
            placeholder="请输入个人简介"
            :max-length="200"
            show-word-limit
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            :loading="loading"
            class="register-button"
            long
          >
            注册
          </a-button>
        </a-form-item>

        <a-form-item>
          <div class="login-link">
            <span>已有账号?</span>
            <a-link type="primary" @click="goToLogin">立即登录</a-link>
          </div>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { IconUser, IconLock } from "@arco-design/web-vue/es/icon";

/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userName: "",
  userProfile: "",
  userPassword: "",
  checkPassword: "",
  userAvatar: "",
} as UserRegisterRequest);

const router = useRouter();
const loading = ref(false);

// 确认密码验证规则
const checkPasswordRules = [
  { required: true, message: "请确认密码" },
  {
    validator: (value: string, callback: (error?: string) => void) => {
      if (value !== form.userPassword) {
        callback("两次输入的密码不一致");
      } else {
        callback();
      }
    },
  },
];

/**
 * 提交表单
 */
const handleSubmit = async () => {
  loading.value = true;
  try {
    const res = await UserControllerService.userRegisterUsingPost(form);
    // 注册成功，跳转到登录页
    if (res.code === 0) {
      message.success("注册成功，请登录");
      // 跳转到登录页面
      router.push({
        path: "/user/login",
        replace: true,
      });
    } else {
      message.error("注册失败，" + res.message);
    }
  } catch (error) {
    console.error("注册失败:", error);
    message.error("注册失败，请重试");
  } finally {
    loading.value = false;
  }
};

/**
 * 跳转到登录页面
 */
const goToLogin = () => {
  router.push({
    path: "/user/login",
    replace: true,
  });
};
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  background-image: radial-gradient(
      circle at 20% 50%,
      rgba(200, 200, 200, 0.1) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 80%,
      rgba(200, 200, 200, 0.1) 0%,
      transparent 50%
    ),
    linear-gradient(
      45deg,
      transparent 30%,
      rgba(200, 200, 200, 0.05) 50%,
      transparent 70%
    );
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.register-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: repeating-linear-gradient(
      0deg,
      transparent,
      transparent 2px,
      rgba(200, 200, 200, 0.03) 2px,
      rgba(200, 200, 200, 0.03) 4px
    ),
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 2px,
      rgba(200, 200, 200, 0.03) 2px,
      rgba(200, 200, 200, 0.03) 4px
    );
  pointer-events: none;
}

.register-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 1;
}

.register-title {
  text-align: center;
  font-size: 28px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 32px;
}

.register-form {
  width: 100%;
}

.register-form :deep(.arco-form-item) {
  margin-bottom: 20px;
}

.register-form :deep(.arco-form-item:last-child) {
  margin-bottom: 0;
}

.input-with-icon :deep(.arco-input) {
  padding-left: 40px;
}

.input-with-icon :deep(.arco-input-prefix) {
  color: #86909c;
}

.register-button {
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
}

.login-link {
  text-align: center;
  color: #86909c;
  font-size: 14px;
  width: 100%;
}

.login-link span {
  margin-right: 8px;
}

.register-form {
  width: 100%;
}

.register-form :deep(.arco-form-item) {
  margin-bottom: 20px;
}

.register-form :deep(.arco-form-item:last-child) {
  margin-bottom: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-card {
    padding: 32px 24px;
    margin: 20px;
  }

  .register-title {
    font-size: 24px;
    margin-bottom: 24px;
  }
}
</style>
