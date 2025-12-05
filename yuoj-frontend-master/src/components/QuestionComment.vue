<template>
  <div class="comment-section">
    <!-- 评论输入框 -->
    <div class="comment-input-area">
      <a-textarea
        v-model="commentContent"
        :placeholder="replyPlaceholder"
        :max-length="2000"
        show-word-limit
        :auto-size="{ minRows: 3, maxRows: 6 }"
        class="comment-textarea"
      />
      <div class="comment-actions">
        <a-button
          type="primary"
          :loading="submitting"
          @click="handleSubmitComment"
          :disabled="!commentContent.trim()"
        >
          评论
        </a-button>
        <a-button v-if="replyingTo" @click="cancelReply">取消</a-button>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-if="loading" class="loading">
        <a-spin />
      </div>
      <div v-else-if="comments.length === 0" class="empty-comment">
        <a-empty description="暂无评论，快来发表第一条评论吧~" />
      </div>
      <div v-else>
        <CommentItem
          v-for="comment in comments"
          :key="comment.id"
          :comment="comment"
          :question-id="questionId"
          @reply="handleReply"
          @delete="handleDelete"
          @thumb="handleThumb"
          @reload="loadComments"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { ref, computed, onMounted, watch } from "vue";
import message from "@arco-design/web-vue/es/message";
import axios from "axios";
import { OpenAPI } from "../../generated/core/OpenAPI";
import CommentItem from "./CommentItem.vue";

interface Props {
  questionId: number;
}

const props = defineProps<Props>();

interface QuestionCommentVO {
  id?: number;
  questionId?: number;
  content?: string;
  parentId?: number;
  replyUserId?: number;
  thumbNum?: number;
  userId?: number;
  createTime?: string;
  user?: {
    id?: number;
    userName?: string;
    userAvatar?: string;
  };
  replyUser?: {
    id?: number;
    userName?: string;
  };
  hasThumb?: boolean;
  children?: QuestionCommentVO[];
}

const comments = ref<QuestionCommentVO[]>([]);
const commentContent = ref("");
const loading = ref(false);
const submitting = ref(false);
const replyingTo = ref<{
  id: number;
  userName: string;
  parentId: number;
} | null>(null);

const replyPlaceholder = computed(() => {
  if (replyingTo.value) {
    return `回复 ${replyingTo.value.userName}:`;
  }
  return "善语结善缘,恶言伤人心...";
});

// 加载评论列表
const loadComments = async () => {
  if (!props.questionId) {
    return;
  }
  loading.value = true;
  try {
    const response = await axios.get(
      `${OpenAPI.BASE}/api/question_comment/list`,
      {
        params: { questionId: props.questionId },
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      comments.value = response.data.data || [];
    } else {
      message.error("加载评论失败: " + response.data.message);
    }
  } catch (error: any) {
    console.error("加载评论失败:", error);
    message.error("加载评论失败，请重试");
  } finally {
    loading.value = false;
  }
};

// 提交评论
const handleSubmitComment = async () => {
  if (!commentContent.value.trim()) {
    message.warning("请输入评论内容");
    return;
  }

  submitting.value = true;
  try {
    const requestData: any = {
      questionId: props.questionId,
      content: commentContent.value.trim(),
    };

    if (replyingTo.value) {
      // 如果是回复，parentId 应该是被回复评论的 id
      // 如果被回复的是顶级评论，parentId 就是该评论的 id
      // 如果被回复的是子评论，parentId 应该是该子评论的 parentId（保持在同一层级）
      const targetComment = findCommentById(replyingTo.value.parentId);
      if (targetComment) {
        // 如果被回复的评论是顶级评论（parentId === 0 或 undefined），则新回复的 parentId 就是该评论的 id
        // 如果被回复的评论是子评论，则新回复的 parentId 应该是该子评论的 parentId
        requestData.parentId =
          targetComment.parentId === 0 || !targetComment.parentId
            ? targetComment.id || 0
            : targetComment.parentId || targetComment.id || 0;
      } else {
        requestData.parentId = replyingTo.value.parentId || 0;
      }
      requestData.replyUserId = replyingTo.value.id || 0;
    }

    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_comment/add`,
      requestData,
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );

    if (response.data.code === 0) {
      message.success("评论成功");
      commentContent.value = "";
      replyingTo.value = null;
      await loadComments();
    } else {
      message.error("评论失败: " + response.data.message);
    }
  } catch (error: any) {
    console.error("评论失败:", error);
    message.error("评论失败，请重试");
  } finally {
    submitting.value = false;
  }
};

// 回复评论
const handleReply = (comment: QuestionCommentVO) => {
  replyingTo.value = {
    id: comment.userId || 0,
    userName: comment.user?.userName || "用户",
    parentId: comment.id || 0,
  };
  // 滚动到输入框
  setTimeout(() => {
    const textarea = document.querySelector(".comment-textarea textarea");
    if (textarea) {
      textarea.scrollIntoView({ behavior: "smooth", block: "center" });
      (textarea as HTMLTextAreaElement).focus();
    }
  }, 100);
};

// 取消回复
const cancelReply = () => {
  replyingTo.value = null;
};

// 删除评论
const handleDelete = async (commentId: number) => {
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_comment/delete`,
      { id: commentId },
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );

    if (response.data.code === 0) {
      message.success("删除成功");
      await loadComments();
    } else {
      message.error("删除失败: " + response.data.message);
    }
  } catch (error: any) {
    console.error("删除失败:", error);
    message.error("删除失败，请重试");
  }
};

// 点赞/取消点赞
const handleThumb = async (commentId: number) => {
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_comment/thumb`,
      null,
      {
        params: { commentId },
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );

    if (response.data.code === 0) {
      await loadComments();
    } else {
      message.error("操作失败: " + response.data.message);
    }
  } catch (error: any) {
    console.error("点赞失败:", error);
    message.error("操作失败，请重试");
  }
};

// 查找评论（递归查找）
const findCommentById = (id: number): QuestionCommentVO | null => {
  for (const comment of comments.value) {
    if (comment.id === id) {
      return comment;
    }
    if (comment.children) {
      for (const child of comment.children) {
        if (child.id === id) {
          return child;
        }
      }
    }
  }
  return null;
};

// 监听 questionId 变化
watch(
  () => props.questionId,
  () => {
    if (props.questionId) {
      loadComments();
    }
  },
  { immediate: true }
);

onMounted(() => {
  if (props.questionId) {
    loadComments();
  }
});
</script>

<style scoped>
.comment-section {
  width: 100%;
}

.comment-input-area {
  margin-bottom: 24px;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
}

.comment-textarea {
  margin-bottom: 12px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.comment-list {
  min-height: 200px;
}

.loading {
  display: flex;
  justify-content: center;
  padding: 40px;
}

.empty-comment {
  padding: 40px;
  text-align: center;
}
</style>
