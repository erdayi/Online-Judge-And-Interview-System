<!-- src/views/mockInterview/MockInterviewChatView.vue -->
<template>
  <div style="max-width: 900px; margin: 0 auto; padding: 20px">
    <h2>模拟面试 #{{ interviewId }}</h2>
    <!-- 消息区域 -->
    <div
      class="messages"
      style="
        height: 60vh;
        overflow-y: auto;
        border: 1px solid #eee;
        padding: 16px;
        margin: 20px 0;
        border-radius: 8px;
        display: flex;
        flex-direction: column;
      "
    >
      <div v-if="loading" style="text-align: center; color: #999">
        加载中...
      </div>
      <div
        v-else-if="displayedMessages.length === 0"
        style="text-align: center; color: #999"
      >
        点击下方"开始面试"以启动对话
      </div>
      <div
        v-for="(msg, index) in displayedMessages"
        :key="index"
        class="message-row"
        :class="{ 'from-user': isUserMessage(msg) }"
      >
        <!-- AI 面试官头像（左侧） -->
        <img
          v-if="!isUserMessage(msg)"
          :src="aiAvatar"
          alt="AI"
          class="avatar"
        />
        <!-- 用户头像占位符（右侧） -->
        <div v-else class="avatar-placeholder"></div>

        <div class="message-bubble" :class="getMessageBubbleClass(msg)">
          <strong v-if="!isUserMessage(msg)">面试官：</strong>
          <strong v-if="isUserMessage(msg)">你：</strong>
          {{ msg.message }}
        </div>

        <!-- 用户头像（右侧） -->
        <img
          v-if="isUserMessage(msg)"
          :src="currentUserAvatar || defaultUserAvatar"
          alt="你"
          class="avatar"
        />
        <!-- AI 占位符（左侧） -->
        <div v-else class="avatar-placeholder"></div>
      </div>

      <!-- AI 加载动画 -->
      <div v-if="aiLoading" class="message-row">
        <img :src="aiAvatar" alt="AI" class="avatar" />
        <div class="message-bubble assistant">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
        <div class="avatar-placeholder"></div>
      </div>
    </div>
    <!-- 操作按钮 -->
    <div style="text-align: center; margin: 20px 0">
      <a-button
        type="primary"
        @click="handleStart"
        :disabled="isStarted || isEnded || loading"
        style="margin-right: 10px"
      >
        开始面试
      </a-button>
      <a-button
        status="danger"
        @click="handleEnd"
        :disabled="!isStarted || isEnded || loading"
      >
        结束面试
      </a-button>
    </div>
    <!-- 输入框 -->
    <div style="display: flex; gap: 10px">
      <a-input
        v-model="inputText"
        placeholder="请输入你的回答..."
        :disabled="!isStarted || isEnded || aiLoading"
        @press-enter="handleSend"
        style="flex: 1"
      />
      <a-button
        type="primary"
        @click="handleSend"
        :disabled="!inputText.trim() || !isStarted || isEnded || aiLoading"
      >
        发送
      </a-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import message from "@arco-design/web-vue/es/message";
import {
  MockInterviewControllerService,
  UserControllerService,
  BaseResponse_LoginUserVO_,
} from "../../../generated";

// --- 头像相关 ---
// 导入本地图片（使用 const 确保不变）
import aiLogo from "@/assets/ai.png";

// 默认头像
const defaultUserAvatar = "https://arco.design/assets/logo.svg";
const aiAvatar = aiLogo;

// 用户头像（通过 API 获取）
const currentUserAvatar = ref<string | undefined>(undefined);

// --- 事件与状态 ---
const EVENTS = {
  START: "start",
  CHAT: "chat",
  END: "end",
};

const route = useRoute();
const interviewId = Number(route.params.id);
const loading = ref(false);
const interviewData = ref<any>(null);
const inputText = ref("");
const aiLoading = ref(false);

// 原始消息（含 system）
const rawMessages = computed(() => {
  try {
    return interviewData.value?.messages
      ? JSON.parse(interviewData.value.messages)
      : [];
  } catch {
    return [];
  }
});

// 过滤掉 role === 'system' 的消息
const displayedMessages = computed(() => {
  return rawMessages.value.filter((msg: any) => msg.role !== "system");
});

// 状态判断
const getStatus = () => {
  const s = interviewData.value?.status;
  return s != null ? Number(s) : -1;
};
const isStarted = computed(() => getStatus() === 1);
const isEnded = computed(() => getStatus() === 2);

// 消息类型判断辅助函数
const isUserMessage = (msg: any) => {
  return msg.role === "user";
};

const getMessageBubbleClass = (msg: any) => {
  return isUserMessage(msg) ? "user" : "assistant";
};

// 加载面试数据
const loadInterview = async () => {
  loading.value = true;
  try {
    const res =
      await MockInterviewControllerService.getMockInterviewByIdUsingGet(
        interviewId
      );
    if (res.code === 0 && res.data) {
      interviewData.value = {
        ...res.data,
        status: Number(res.data.status),
      };

      // 调试：打印消息角色，帮助诊断问题
      console.log("原始消息:", rawMessages.value);
      console.log("显示消息:", displayedMessages.value);
    }
  } catch (err) {
    message.error("加载失败");
  } finally {
    loading.value = false;
  }
};

// 获取当前登录用户信息（用于头像）
const loadCurrentUser = async () => {
  try {
    const res: BaseResponse_LoginUserVO_ =
      await UserControllerService.getLoginUserUsingGet();
    if (res.code === 0 && res.data) {
      currentUserAvatar.value = res.data.userAvatar;
    }
  } catch (err) {
    console.warn("Failed to load user info:", err);
  }
};

// 通用发送方法
const sendEvent = async (event: string, msg?: string) => {
  aiLoading.value = true;
  try {
    const payload: any = { id: interviewId, event };
    if (msg !== undefined) payload.message = msg;
    const res =
      await MockInterviewControllerService.handleMockInterviewEventUsingPost(
        payload
      );
    if (res.code !== 0) {
      throw new Error(res.message || "操作失败");
    }
    await loadInterview();
  } catch (err: any) {
    message.error(err.message || "请求失败");
  } finally {
    aiLoading.value = false;
  }
};

// 操作处理
const handleStart = () => {
  sendEvent(EVENTS.START);
};

const handleEnd = () => {
  sendEvent(EVENTS.END);
};

const handleSend = async () => {
  if (!inputText.value.trim()) return;

  const userMessage = inputText.value.trim();
  const userMsgObj = {
    role: "user",
    message: userMessage,
  };

  const tempMsg = [...rawMessages.value, userMsgObj];
  interviewData.value = {
    ...interviewData.value,
    messages: JSON.stringify(tempMsg),
  };

  const originalText = inputText.value;
  inputText.value = "";

  try {
    await sendEvent(EVENTS.CHAT, userMessage);
  } catch (err) {
    message.error("发送失败");
    inputText.value = originalText;
    await loadInterview();
  }
};

onMounted(async () => {
  await loadInterview();
  await loadCurrentUser();
});
</script>

<style scoped>
.messages {
  display: flex;
  flex-direction: column;
}

.message-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.message-row.from-user {
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  margin: 0 8px;
  background-color: #f0f0f0;
}

.avatar-placeholder {
  width: 36px;
  height: 36px;
  margin: 0 8px;
}

.message-bubble {
  padding: 10px 14px;
  border-radius: 12px;
  word-break: break-word;
  max-width: calc(100% - 92px);
}

/* 修改CSS类名，兼容 interviewer 和 assistant */
.message-bubble.assistant,
.message-bubble.interviewer {
  background-color: #f0f9ff;
  border: 1px solid #bae7ff;
}

.message-bubble.user {
  background-color: #f9f0ff;
  border: 1px solid #d9b6ff;
  text-align: left;
}

/* 打字动画 */
.typing-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 20px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background-color: #999;
  border-radius: 50%;
  display: inline-block;
  margin: 0 2px;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typing {
  0%,
  80%,
  100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1.2);
    opacity: 1;
  }
}
</style>
