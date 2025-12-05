<template>
  <div id="viewQuestionView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question.title">
              <template #extra>
                <a-space wrap>
                  <a-tag
                    v-for="(tag, index) of question.tags"
                    :key="index"
                    color="green"
                    >{{ tag }}
                  </a-tag>
                </a-space>
              </template>
              <div class="question-header">
                <div class="favour-button-wrapper">
                  <a-button
                    type="text"
                    :status="isFavourited ? 'success' : 'default'"
                    @click="handleFavour"
                    :loading="favourLoading"
                    class="favour-button"
                  >
                    <template #icon>
                      <icon-star
                        :fill="isFavourited ? '#ffffff' : 'none'"
                        :size="18"
                      />
                    </template>
                    {{ isFavourited ? "已收藏" : "收藏" }}
                  </a-button>
                </div>
              </div>
              <a-descriptions
                title="判题条件"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>
              <MdViewer :value="question.content || ''" />
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论区">
            <QuestionComment v-if="question?.id" :question-id="question.id" />
          </a-tab-pane>
          <a-tab-pane key="answer" title="答案">
            <pre>{{ question?.answer }}</pre>
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" layout="inline">
          <a-form-item
            field="language"
            label="编程语言"
            style="min-width: 240px"
          >
            <a-select
              v-model="form.language"
              :style="{ width: '320px' }"
              placeholder="选择编程语言"
            >
              <a-option>java</a-option>
              <a-option>cpp</a-option>
              <a-option>go</a-option>
              <a-option>html</a-option>
            </a-select>
          </a-form-item>
        </a-form>
        <CodeEditor
          :value="form.code as string"
          :language="form.language"
          :handle-change="changeCode"
        />
        <a-divider size="0" />
        <a-button type="primary" style="min-width: 200px" @click="doSubmit">
          提交代码
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect, withDefaults, defineProps } from "vue";
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import MdViewer from "@/components/MdViewer.vue";
import QuestionComment from "@/components/QuestionComment.vue";
import { IconStar } from "@arco-design/web-vue/es/icon";
import axios from "axios";
import { OpenAPI } from "../../../generated/core/OpenAPI";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionVO,
} from "../../../generated";
// ViewQuestionView.vue
import { UserControllerService } from "../../../generated"; // 确保路径正确

onMounted(async () => {
  await loadData();

  // 自动签到（仅执行一次，避免重复）
  try {
    const signInRes = await UserControllerService.addUserSignInUsingPost();
    if (signInRes.code === 0) {
      console.log("自动签到成功");
      // 可选：提示用户“已自动打卡”
    }
  } catch (error) {
    console.warn("自动签到失败（可能已签过或未登录）", error);
  }
});

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const question = ref<QuestionVO>();
const isFavourited = ref(false);
const favourLoading = ref(false);

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  if (res.code === 0) {
    question.value = res.data;
    // 检查是否已收藏
    checkFavourStatus();
  } else {
    message.error("加载失败，" + res.message);
  }
};

const checkFavourStatus = () => {
  // 直接从 QuestionVO 中获取收藏状态
  if (question.value?.hasFavour !== undefined) {
    isFavourited.value = question.value.hasFavour;
  } else {
    isFavourited.value = false;
  }
};

const handleFavour = async () => {
  if (!question.value?.id) {
    return;
  }
  favourLoading.value = true;
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_favour/`,
      {
        questionId: question.value.id,
      },
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      const result = response.data.data;
      isFavourited.value = result > 0;
      // 更新 QuestionVO 中的收藏状态
      if (question.value) {
        question.value.hasFavour = result > 0;
        // 更新收藏数
        if (question.value.favourNum !== undefined) {
          question.value.favourNum = (question.value.favourNum || 0) + result;
        }
      }
      message.success(result > 0 ? "收藏成功" : "取消收藏成功");
    } else {
      message.error("操作失败，" + response.data.message);
    }
  } catch (error: any) {
    console.error("收藏操作失败:", error);
    if (error.response?.status === 401) {
      message.error("请先登录");
    } else {
      message.error("操作失败，请重试");
    }
  } finally {
    favourLoading.value = false;
  }
};

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});

/**
 * 提交代码
 */
const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }

  const res = await QuestionControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    message.success("提交成功");
  } else {
    message.error("提交失败," + res.message);
  }
};

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const changeCode = (value: string) => {
  form.value.code = value;
};
</script>

<style>
#viewQuestionView {
  max-width: 1400px;
  margin: 0 auto;
}

#viewQuestionView .arco-space-horizontal .arco-space-item {
  margin-bottom: 0 !important;
}

.question-header {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.favour-button-wrapper {
  display: flex;
  justify-content: flex-start;
}

.favour-button {
  padding: 4px 12px;
  height: auto;
  font-size: 14px;
}

.favour-button:hover {
  background-color: #f7f8fa;
}
</style>
