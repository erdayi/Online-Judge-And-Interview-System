<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">用户登录</h2>
      <a-form
        :model="form"
        @submit="handleSubmit"
        layout="vertical"
        class="login-form"
      >
        <a-form-item
          field="userAccount"
          label="账号"
          :rules="[{ required: true, message: '请输入账号' }]"
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
          field="userPassword"
          label="密码"
          :rules="[{ required: true, message: '请输入密码' }]"
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

        <a-form-item>
          <div class="form-options">
            <a-checkbox v-model="rememberMe">记住我</a-checkbox>
            <a-link type="primary" @click="handleForgotPassword">
              忘记密码?
            </a-link>
          </div>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            :loading="loading"
            class="login-button"
            long
          >
            登录
          </a-button>
        </a-form-item>

        <a-form-item>
          <div class="register-link">
            <span>还没有账号?</span>
            <a-link type="primary" @click="goToRegister">立即注册</a-link>
          </div>
        </a-form-item>

        <a-divider class="divider">
          <span class="divider-text">或通过其他方式登录</span>
        </a-divider>

        <a-form-item>
          <div class="social-login">
            <a-button
              class="social-button"
              shape="circle"
              @click="handleGithubLogin"
            >
              <template #icon>
                <icon-github />
              </template>
            </a-button>
            <a-button
              class="social-button"
              shape="circle"
              @click="handleOtherLogin"
            >
              <template #icon>
                <icon-wechat />
              </template>
            </a-button>
          </div>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import {
  IconUser,
  IconLock,
  IconGithub,
  IconNotification,
} from "@arco-design/web-vue/es/icon";

/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

const router = useRouter();
const store = useStore();
const loading = ref(false);
const rememberMe = ref(false);

/**
 * 提交表单
 */
const handleSubmit = async () => {
  loading.value = true;
  try {
    const res = await UserControllerService.userLoginUsingPost(form);
    // 登录成功，跳转到主页
    if (res.code === 0) {
      // 如果勾选了记住我，保存账号到本地存储
      if (rememberMe.value) {
        localStorage.setItem("rememberedAccount", form.userAccount || "");
      } else {
        localStorage.removeItem("rememberedAccount");
      }

      await store.dispatch("user/getLoginUser");
      message.success("登录成功");
      router.push({
        path: "/",
        replace: true,
      });
    } else {
      message.error("登录失败，" + res.message);
    }
  } catch (error) {
    console.error("登录失败:", error);
    message.error("登录失败，请重试");
  } finally {
    loading.value = false;
  }
};

/**
 * 跳转到注册页面
 */
const goToRegister = () => {
  router.push({
    path: "/user/register",
    replace: true,
  });
};

/**
 * 忘记密码
 */
const handleForgotPassword = () => {
  message.info("忘记密码功能开发中...");
};

/**
 * GitHub 登录
 */
const handleGithubLogin = () => {
  message.info("GitHub 登录功能开发中...");
};

/**
 * 其他登录方式
 */
const handleOtherLogin = () => {
  message.info("其他登录方式开发中...");
};

// 页面加载时，如果有记住的账号，自动填充
const rememberedAccount = localStorage.getItem("rememberedAccount");
if (rememberedAccount) {
  form.userAccount = rememberedAccount || "";
  rememberMe.value = true;
}
</script>

<style scoped>
.login-container {
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

.login-container::before {
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

.login-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 1;
}

.login-title {
  text-align: center;
  font-size: 28px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 32px;
}

.login-form {
  width: 100%;
}

.login-form :deep(.arco-form-item) {
  margin-bottom: 20px;
}

.login-form :deep(.arco-form-item:last-child) {
  margin-bottom: 0;
}

.input-with-icon :deep(.arco-input) {
  padding-left: 40px;
}

.input-with-icon :deep(.arco-input-prefix) {
  color: #86909c;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.login-button {
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
}

.register-link {
  text-align: center;
  color: #86909c;
  font-size: 14px;
  width: 100%;
}

.register-link span {
  margin-right: 8px;
}

.divider {
  margin: 24px 0;
}

.divider-text {
  color: #86909c;
  font-size: 12px;
  padding: 0 16px;
  background: #ffffff;
}

.social-login {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  width: 100%;
}

.social-button {
  width: 48px;
  height: 48px;
  background: #1d2129;
  border: none;
  color: #ffffff;
  transition: all 0.3s ease;
}

.social-button:hover {
  background: #2d3139;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.social-button :deep(.arco-icon) {
  font-size: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    padding: 32px 24px;
    margin: 20px;
  }

  .login-title {
    font-size: 24px;
    margin-bottom: 24px;
  }
}
</style>
