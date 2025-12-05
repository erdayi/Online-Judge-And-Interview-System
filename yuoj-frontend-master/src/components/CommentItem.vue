<template>
  <div class="comment-item" :class="{ 'is-reply': comment.parentId > 0 }">
    <div class="comment-header">
      <a-avatar
        :size="32"
        :style="{ backgroundColor: '#3370ff', marginRight: '12px' }"
      >
        <img
          v-if="comment.user?.userAvatar"
          :src="comment.user.userAvatar"
          alt="avatar"
        />
        <icon-user v-else />
      </a-avatar>
      <div class="comment-info">
        <span class="comment-author">{{
          comment.user?.userName || "匿名用户"
        }}</span>
        <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
      </div>
    </div>

    <div class="comment-content">
      <template
        v-if="
          comment.replyUser && comment.replyUserId && comment.replyUserId > 0
        "
      >
        <span class="reply-tag">回复 {{ comment.replyUser.userName }}:</span>
      </template>
      <span>{{ comment.content }}</span>
    </div>

    <div class="comment-actions">
      <a-button
        type="text"
        size="small"
        @click="handleThumb"
        :class="{ 'thumb-active': comment.hasThumb }"
      >
        <template #icon>
          <icon-heart :fill="comment.hasThumb ? '#f53f3f' : 'none'" />
        </template>
        点赞
        <span v-if="comment.thumbNum > 0" class="thumb-count">
          {{ comment.thumbNum }}
        </span>
      </a-button>
      <a-button type="text" size="small" @click="handleReply">回复</a-button>
      <a-button
        v-if="canDelete"
        type="text"
        size="small"
        status="danger"
        @click="handleDelete"
      >
        <template #icon>
          <icon-delete />
        </template>
        删除
      </a-button>
    </div>

    <!-- 子评论 -->
    <div
      v-if="comment.children && comment.children.length > 0"
      class="comment-children"
    >
      <CommentItem
        v-for="child in comment.children"
        :key="child.id"
        :comment="child"
        :question-id="questionId"
        @reply="handleReply"
        @delete="handleDelete"
        @thumb="handleThumb"
        @reload="handleReload"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { computed } from "vue";
import { IconUser, IconHeart, IconDelete } from "@arco-design/web-vue/es/icon";
import { useStore } from "vuex";

interface Props {
  comment: any;
  questionId: number;
}

const props = defineProps<Props>();
const emit = defineEmits(["reply", "delete", "thumb", "reload"]);

const store = useStore();

// 判断是否可以删除（只有评论作者或管理员可以删除）
const canDelete = computed(() => {
  const loginUser = store.state.user?.loginUser;
  if (!loginUser) {
    return false;
  }
  return (
    props.comment.userId === loginUser.id || loginUser.userRole === "admin"
  );
});

// 格式化时间
const formatTime = (time: string) => {
  if (!time) return "";
  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const seconds = Math.floor(diff / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);

  if (days > 0) {
    return (
      date.toLocaleDateString("zh-CN") +
      " " +
      date.toLocaleTimeString("zh-CN", { hour: "2-digit", minute: "2-digit" })
    );
  } else if (hours > 0) {
    return `${hours}小时前`;
  } else if (minutes > 0) {
    return `${minutes}分钟前`;
  } else {
    return "刚刚";
  }
};

const handleReply = () => {
  emit("reply", props.comment);
};

const handleDelete = () => {
  emit("delete", props.comment.id);
};

const handleThumb = () => {
  emit("thumb", props.comment.id);
};

const handleReload = () => {
  emit("reload");
};
</script>

<style scoped>
.comment-item {
  padding: 16px;
  margin-bottom: 16px;
  background: #ffffff;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  transition: all 0.3s ease;
}

.comment-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comment-item.is-reply {
  margin-left: 48px;
  margin-top: 12px;
  background: #f9fafb;
  border-color: #e5e7eb;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.comment-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.comment-author {
  font-weight: 500;
  color: #1d2129;
  font-size: 14px;
}
.comment-time {
  font-size: 12px;
  color: #86909c;
}

.comment-content {
  margin-bottom: 12px;
  color: #1d2129;
  line-height: 1.6;
  word-break: break-word;
}

.reply-tag {
  color: #165dff;
  font-weight: 500;
  margin-right: 4px;
}

.comment-actions {
  display: flex;
  gap: 16px;
  align-items: center;
}

.comment-actions .arco-btn {
  padding: 0 8px;
  height: 28px;
  font-size: 13px;
}

.thumb-active {
  color: #f53f3f;
}

.thumb-count {
  margin-left: 4px;
  font-weight: 500;
}

.comment-children {
  margin-top: 16px;
  padding-left: 16px;
  border-left: 2px solid #e5e7eb;
}
</style>
