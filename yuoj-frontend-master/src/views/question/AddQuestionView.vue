<template>
  <div id="addQuestionView">
    <!-- 页面标题区域 -->
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <icon-edit v-if="!updatePage" />
          <icon-settings v-else />
          {{ updatePage ? "更新题目" : "创建题目" }}
        </h1>
        <p class="page-subtitle">
          {{ updatePage ? "修改题目信息" : "填写题目信息，创建新的编程题目" }}
        </p>

        <!-- AI生成题目按钮 (仅在创建页面显示) -->
        <div
          v-if="!updatePage"
          class="ai-generate-btn"
          style="margin-top: 15px"
        >
          <a-button type="primary" @click="showAiModal" class="custom-ai-btn">
            <template #icon>
              <icon-robot />
            </template>
            AI生成题目
          </a-button>
        </div>
      </div>
    </div>

    <!-- AI生成题目弹窗 -->
    <a-modal
      v-model:visible="aiModalVisible"
      title="AI生成题目"
      :footer="null"
      :mask-closable="false"
      width="500px"
      class="custom-ai-modal"
    >
      <a-spin :loading="aiLoading" dot>
        <div class="modal-content">
          <a-form :model="aiForm" layout="vertical" class="centered-form">
            <!-- 题目类型 -->
            <a-form-item label="题目类型" field="questionType" required>
              <a-select
                v-model="aiForm.questionType"
                placeholder="请选择题目类型"
                :options="languageOptions"
                size="large"
                style="width: 300px"
                show-search
                allow-clear
              />
            </a-form-item>

            <!-- 生成数量 -->
            <a-form-item label="生成数量" field="number" required>
              <a-input-number
                v-model="aiForm.number"
                :min="1"
                :max="10"
                placeholder="请输入生成数量(1-10)"
                size="large"
                style="width: 300px"
              />
            </a-form-item>
          </a-form>

          <!-- 操作按钮 - 居中显示 -->
          <div class="modal-footer centered-footer">
            <a-button
              size="large"
              @click="aiModalVisible = false"
              class="footer-btn cancel-btn"
            >
              取消
            </a-button>
            <a-button
              type="primary"
              size="large"
              :loading="aiLoading"
              @click="handleAiGenerate"
              class="footer-btn generate-btn"
            >
              <template #icon>
                <icon-robot v-if="!aiLoading" />
                <icon-loading v-else />
              </template>
              {{ aiLoading ? "正在生成..." : "开始生成" }}
            </a-button>
          </div>
        </div>
      </a-spin>
    </a-modal>

    <!-- 表单区域 -->
    <a-card class="form-card">
      <a-form
        :model="form"
        label-align="left"
        :label-col-props="{ span: 6 }"
        :wrapper-col-props="{ span: 18 }"
      >
        <!-- 基本信息 -->
        <div class="form-section">
          <h3 class="section-title">
            <icon-info-circle />
            基本信息
          </h3>
          <a-form-item field="title" label="题目标题">
            <a-input
              v-model="form.title"
              placeholder="请输入题目标题"
              :max-length="100"
              show-word-limit
              size="large"
            />
          </a-form-item>
          <a-form-item field="tags" label="题目标签">
            <a-input-tag
              v-model="form.tags"
              placeholder="请输入标签，按回车添加"
              allow-clear
              size="large"
            />
          </a-form-item>
        </div>

        <a-divider />

        <!-- 题目内容 -->
        <div class="form-section">
          <h3 class="section-title">
            <icon-file />
            题目内容
          </h3>
          <a-form-item field="content" label="题目描述">
            <div class="editor-wrapper">
              <MdEditor
                :value="form.content"
                :handle-change="onContentChange"
              />
            </div>
          </a-form-item>
        </div>

        <a-divider />

        <!-- 题目答案 -->
        <div class="form-section">
          <h3 class="section-title">
            <icon-check-circle />
            题目答案
          </h3>
          <a-form-item field="answer" label="参考答案">
            <div class="editor-wrapper">
              <MdEditor :value="form.answer" :handle-change="onAnswerChange" />
            </div>
          </a-form-item>
        </div>

        <a-divider />

        <!-- 判题配置 -->
        <div class="form-section">
          <h3 class="section-title">
            <icon-settings />
            判题配置
          </h3>
          <a-form-item
            label="资源限制"
            :content-flex="false"
            :merge-props="false"
          >
            <a-row :gutter="24">
              <a-col :span="8">
                <a-form-item field="judgeConfig.timeLimit" label="时间限制">
                  <a-input-number
                    v-model="form.judgeConfig.timeLimit"
                    placeholder="毫秒"
                    mode="button"
                    min="0"
                    size="large"
                    style="width: 100%"
                  >
                    <template #suffix>ms</template>
                  </a-input-number>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
                  <a-input-number
                    v-model="form.judgeConfig.memoryLimit"
                    placeholder="KB"
                    mode="button"
                    min="0"
                    size="large"
                    style="width: 100%"
                  >
                    <template #suffix>KB</template>
                  </a-input-number>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
                  <a-input-number
                    v-model="form.judgeConfig.stackLimit"
                    placeholder="KB"
                    mode="button"
                    min="0"
                    size="large"
                    style="width: 100%"
                  >
                    <template #suffix>KB</template>
                  </a-input-number>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form-item>
        </div>

        <a-divider />

        <!-- 测试用例配置 -->
        <div class="form-section">
          <div class="section-header">
            <h3 class="section-title">
              <icon-code />
              测试用例配置
            </h3>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus />
              </template>
              新增测试用例
            </a-button>
          </div>
          <a-form-item
            label="测试用例"
            :content-flex="false"
            :merge-props="false"
          >
            <div class="test-cases">
              <a-card
                v-for="(judgeCaseItem, index) of form.judgeCase"
                :key="index"
                class="test-case-card"
                :title="`测试用例 ${index + 1}`"
              >
                <template #extra>
                  <a-button
                    type="text"
                    status="danger"
                    size="small"
                    @click="handleDelete(index)"
                  >
                    <template #icon>
                      <icon-delete />
                    </template>
                    删除
                  </a-button>
                </template>
                <a-space direction="vertical" style="width: 100%">
                  <a-form-item
                    :field="`form.judgeCase[${index}].input`"
                    label="输入用例"
                    :key="index"
                  >
                    <a-textarea
                      v-model="judgeCaseItem.input"
                      placeholder="请输入测试输入用例"
                      :auto-size="{ minRows: 3, maxRows: 6 }"
                    />
                  </a-form-item>
                  <a-form-item
                    :field="`form.judgeCase[${index}].output`"
                    label="输出用例"
                    :key="index"
                  >
                    <a-textarea
                      v-model="judgeCaseItem.output"
                      placeholder="请输入测试输出用例"
                      :auto-size="{ minRows: 3, maxRows: 6 }"
                    />
                  </a-form-item>
                </a-space>
              </a-card>
            </div>
          </a-form-item>
        </div>

        <!-- 提交按钮 -->
        <div class="form-actions">
          <a-button size="large" @click="handleCancel">取消</a-button>
          <a-button
            type="primary"
            size="large"
            :loading="submitting"
            @click="doSubmit"
          >
            <template #icon>
              <icon-check v-if="!submitting" />
            </template>
            {{ updatePage ? "更新题目" : "创建题目" }}
          </a-button>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import MdEditor from "@/components/MdEditor.vue";
import {
  QuestionControllerService,
  QuestionAIGenerateRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import {
  IconEdit,
  IconSettings,
  IconInfoCircle,
  IconFile,
  IconCheckCircle,
  IconCode,
  IconPlus,
  IconDelete,
  IconCheck,
  IconRobot,
} from "@arco-design/web-vue/es/icon";

const route = useRoute();
const router = useRouter();
// 如果页面地址包含 update，视为更新页面
const updatePage = route.path.includes("update");
const submitting = ref(false);

let form = ref({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
});

// AI生成题目相关状态
const aiModalVisible = ref(false);
const aiLoading = ref(false);
const aiForm = ref<QuestionAIGenerateRequest>({
  questionType: "java",
  number: 1,
});

// 支持的语言类型
const languageOptions = [
  { label: "Java", value: "java" },
  { label: "Python", value: "python" },
  { label: "C++", value: "cpp" },
  { label: "JavaScript", value: "javascript" },
  { label: "Go", value: "go" },
  { label: "TypeScript", value: "typescript" },
];

// 显示AI生成弹窗
const showAiModal = () => {
  aiModalVisible.value = true;
};

// 处理AI生成题目
const handleAiGenerate = async () => {
  if (!aiForm.value.questionType) {
    message.warning("请选择题目类型");
    return;
  }

  if (
    !aiForm.value.number ||
    aiForm.value.number < 1 ||
    aiForm.value.number > 10
  ) {
    message.warning("生成数量需在1-10之间");
    return;
  }

  aiLoading.value = true;

  try {
    const res = await QuestionControllerService.aiGenerateQuestionUsingPost({
      questionType: aiForm.value.questionType,
      number: aiForm.value.number,
    });

    if (res.code === 0) {
      message.success(
        `成功生成${aiForm.value.number}道${aiForm.value.questionType}题目！`
      );
      aiModalVisible.value = false;
      // 跳转到题目列表页面
      router.push("/questions");
    } else {
      message.error("生成失败：" + (res.message || "未知错误"));
    }
  } catch (error: any) {
    console.error("AI生成题目失败:", error);
    message.error("生成失败：" + (error.message || "请重试"));
  } finally {
    aiLoading.value = false;
  }
};
/**
 * 根据题目 id 获取老的数据
 */
const loadData = async () => {
  const id = route.query.id;
  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    // json 转 js 对象
    if (!form.value.judgeCase) {
      form.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      form.value.judgeCase = JSON.parse(form.value.judgeCase as any);
    }
    if (!form.value.judgeConfig) {
      form.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any);
    }
    if (!form.value.tags) {
      form.value.tags = [];
    } else {
      form.value.tags = JSON.parse(form.value.tags as any);
    }
  } else {
    message.error("加载失败，" + res.message);
  }
};

onMounted(() => {
  loadData();
});

const doSubmit = async () => {
  // 表单验证
  if (!form.value.title || form.value.title.trim() === "") {
    message.warning("请输入题目标题");
    return;
  }
  if (!form.value.content || form.value.content.trim() === "") {
    message.warning("请输入题目内容");
    return;
  }
  if (!form.value.answer || form.value.answer.trim() === "") {
    message.warning("请输入题目答案");
    return;
  }
  if (!form.value.judgeCase || form.value.judgeCase.length === 0) {
    message.warning("请至少添加一个测试用例");
    return;
  }

  submitting.value = true;
  try {
    // 区分更新还是创建
    if (updatePage) {
      const res = await QuestionControllerService.updateQuestionUsingPost(
        form.value
      );
      if (res.code === 0) {
        message.success("更新成功");
        router.push("/questions");
      } else {
        message.error("更新失败，" + res.message);
      }
    } else {
      const res = await QuestionControllerService.addQuestionUsingPost(
        form.value
      );
      if (res.code === 0) {
        message.success("创建成功");
        router.push("/questions");
      } else {
        message.error("创建失败，" + res.message);
      }
    }
  } catch (error: any) {
    console.error("提交失败:", error);
    message.error("提交失败，请重试");
  } finally {
    submitting.value = false;
  }
};

/**
 * 新增判题用例
 */
const handleAdd = () => {
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};

/**
 * 删除判题用例
 */
const handleDelete = (index: number) => {
  if (form.value.judgeCase.length <= 1) {
    message.warning("至少需要保留一个测试用例");
    return;
  }
  form.value.judgeCase.splice(index, 1);
};

const onContentChange = (value: string) => {
  form.value.content = value;
};

const onAnswerChange = (value: string) => {
  form.value.answer = value;
};

const handleCancel = () => {
  router.back();
};
</script>

<style scoped>
#addQuestionView {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面标题区域 */
.page-header {
  background: #f7f8fa;
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 24px;
  border: 1px solid #e5e7eb;
}

.header-content {
  text-align: center;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1d2129;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.page-subtitle {
  font-size: 16px;
  color: #86909c;
  margin: 0;
}

/* 表单卡片 */
.form-card {
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

/* 表单区域 */
.form-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header .section-title {
  margin-bottom: 0;
}

/* 编辑器包装器 */
.editor-wrapper {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.editor-wrapper:hover {
  border-color: #3370ff;
}

/* 测试用例区域 */
.test-cases {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.test-case-card {
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  transition: all 0.3s ease;
}

.test-case-card:hover {
  border-color: #3370ff;
  box-shadow: 0 4px 12px rgba(51, 112, 255, 0.1);
}

/* 表单操作按钮 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

/* 分割线样式 */
:deep(.arco-divider) {
  margin: 32px 0;
  border-color: #e5e7eb;
}

/* 表单项样式优化 */
:deep(.arco-form-item-label) {
  font-weight: 500;
  color: #1d2129;
}

:deep(.arco-input-wrapper),
:deep(.arco-textarea-wrapper) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.arco-input-wrapper:hover),
:deep(.arco-textarea-wrapper:hover) {
  border-color: #3370ff;
}

:deep(.arco-input-wrapper.arco-input-focus),
:deep(.arco-textarea-wrapper.arco-textarea-focus) {
  border-color: #3370ff;
  box-shadow: 0 0 0 2px rgba(51, 112, 255, 0.1);
}

/* 标签输入样式 */
:deep(.arco-input-tag) {
  border-radius: 8px;
}

/* 数字输入框样式 */
:deep(.arco-input-number) {
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    padding: 24px;
  }

  .page-title {
    font-size: 24px;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .form-actions .arco-btn {
    width: 100%;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .section-header .arco-btn {
    width: 100%;
  }
}
</style>
<style scoped>
/* 自定义模态框样式 */
.custom-ai-modal {
  border-radius: 16px !important;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15) !important;
}

.custom-ai-modal .arco-modal-header {
  padding: 24px !important;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-bottom: 1px solid var(--color-neutral-3) !important;
}

.custom-ai-modal .arco-modal-body {
  padding: 24px !important;
}

/* 模态框内容区域 - 居中对齐 */
.modal-content {
  margin-left: 70px;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 300px; /* 限制最大宽度，避免在大屏幕上过宽 */
}

/* 居中表单 */
.centered-form {
  width: 300px;
}

/* 表单项样式 */
.modal-content .arco-form-item {
  margin-bottom: 16px;
}

.modal-content .arco-form-item-label {
  font-weight: 500;
  color: var(--color-text-1);
}

/* 警告提示样式 */
.alert-content {
  font-size: 14px;
  color: var(--color-text-2);
  line-height: 1.5;
  display: block; /* 确保span正确换行 */
}

.highlight {
  font-weight: 600;
  color: var(--color-primary);
}

/* 居中按钮容器 */
.centered-footer {
  display: flex;
  justify-content: center; /* 按钮组水平居中 */
  gap: 20px;
  width: 300px;
  margin-top: 16px;
  padding-top: 20px;
  border-top: 1px dashed var(--color-neutral-3);
}

/* 按钮样式 */
.footer-btn {
  border-radius: 12px;
  font-weight: 500;
  transition: all 0.2s ease;
  padding: 0 24px;
}

.cancel-btn {
  background: var(--color-neutral-1);
  border: 1px solid var(--color-neutral-3);
  color: var(--color-text-1);
}

.cancel-btn:hover {
  background: var(--color-neutral-2);
  border-color: var(--color-neutral-4);
}

.generate-btn {
  background: linear-gradient(135deg, #6e8efb, #a77cd1);
  border: none;
  color: white;
  box-shadow: 0 4px 12px rgba(110, 142, 251, 0.3);
}

.generate-btn:hover {
  box-shadow: 0 6px 16px rgba(110, 142, 251, 0.4);
  transform: translateY(-1px);
}

.generate-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(110, 142, 251, 0.2);
}

/* 选择器和输入框样式 */
.modal-content .arco-select,
.modal-content .arco-input-number {
  border-radius: 12px;
  border: 1px solid var(--color-neutral-3);
  transition: border-color 0.2s ease;
}

.modal-content .arco-select:hover,
.modal-content .arco-input-number:hover {
  border-color: var(--color-primary-light-3);
}

/* 响应式调整 */
@media (max-width: 576px) {
  .custom-ai-modal {
    width: 95%;
    margin: 0 auto;
  }

  .centered-footer {
    flex-direction: column;
    gap: 12px;
  }

  .footer-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
