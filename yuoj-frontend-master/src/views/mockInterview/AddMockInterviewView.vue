<!-- src/views/mockInterview/AddMockInterviewView.vue -->
<template>
  <div class="add-mock-interview">
    <!-- 页面标题区域（与列表页一致） -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title"><icon-edit /> 创建模拟面试</h1>
        <p class="page-subtitle">填写岗位信息，开始你的 AI 模拟面试</p>
      </div>
    </div>

    <!-- 表单卡片（风格对齐 search-card / list-card） -->
    <a-card class="form-card">
      <a-form :model="form" layout="vertical">
        <a-form-item label="应聘岗位" required>
          <a-input
            v-model="form.jobPosition"
            placeholder="如：Java后端工程师、前端开发、算法工程师"
            size="large"
            allow-clear
          />
        </a-form-item>

        <a-form-item label="工作经验" required>
          <a-textarea
            v-model="form.workExperience"
            placeholder="请描述你工作年限，例如：3年"
            :rows="4"
            size="large"
            allow-clear
          />
        </a-form-item>

        <a-form-item label="面试难度" required>
          <a-select
            v-model="form.difficulty"
            placeholder="请选择难度"
            size="large"
            allow-clear
          >
            <a-option value="简单">简单</a-option>
            <a-option value="中等">中等</a-option>
            <a-option value="困难">困难</a-option>
            <a-option value="地狱">地狱</a-option>
          </a-select>
        </a-form-item>

        <div class="form-actions">
          <a-button
            type="primary"
            @click="handleSubmit"
            :loading="loading"
            size="large"
            style="width: 100%; max-width: 320px"
          >
            创建面试
          </a-button>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import message from "@arco-design/web-vue/es/message";
import { IconEdit } from "@arco-design/web-vue/es/icon";
import { MockInterviewControllerService } from "../../../generated";

const router = useRouter();
const loading = ref(false);
const form = reactive({
  jobPosition: "",
  workExperience: "",
  difficulty: "",
});

const handleSubmit = async () => {
  if (
    !form.jobPosition.trim() ||
    !form.workExperience.trim() ||
    !form.difficulty
  ) {
    message.warning("请填写完整信息");
    return;
  }

  loading.value = true;
  try {
    const res = await MockInterviewControllerService.addMockInterviewUsingPost(
      form
    );
    if (res.code === 0) {
      message.success("模拟面试创建成功！");
      router.push("/mock-interview/my");
    } else {
      message.error(res.message || "创建失败，请重试");
    }
  } catch (err) {
    console.error("创建失败:", err);
    message.error("网络错误，请稍后重试");
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.add-mock-interview {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 复用列表页的 page-header 样式 */
.page-header {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 24px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02);
}

.header-content .page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

/* 表单卡片：与 search-card / list-card 风格统一 */
.form-card {
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  background: #ffffff;
  padding: 24px;
}

:deep(.arco-form-item-label) {
  font-weight: 600;
  color: #334155;
  margin-bottom: 8px;
  font-size: 14px;
}

.form-actions {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .page-header {
    padding: 24px;
    flex-direction: column;
    align-items: flex-start;
  }

  .page-title {
    font-size: 24px;
  }

  .form-card {
    padding: 16px;
  }

  .form-actions {
    margin-top: 20px;
  }
}
</style>
