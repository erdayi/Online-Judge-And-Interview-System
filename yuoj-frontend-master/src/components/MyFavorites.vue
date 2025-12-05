<template>
  <a-card class="favorites-card" :bordered="false">
    <template #title>
      <div class="card-title-wrapper">
        <icon-star class="title-icon" />
        <span>我的收藏</span>
      </div>
    </template>
    <div class="favorites-content">
      <a-spin :loading="loading" style="width: 100%">
        <div v-if="favorites.length === 0" class="empty-state">
          <icon-inbox class="empty-icon" />
          <p class="empty-text">暂无收藏</p>
          <p class="empty-hint">收藏的题目将显示在这里</p>
        </div>
        <div v-else class="favorites-list">
          <div
            v-for="question in favorites"
            :key="question.id"
            class="favorite-item"
            @click="goToQuestion(question.id)"
          >
            <div class="item-header">
              <a-tag color="blue" size="small">#{{ question.id }}</a-tag>
              <a-tag
                v-for="(tag, index) in question.tags?.slice(0, 2)"
                :key="index"
                color="green"
                size="small"
              >
                {{ tag }}
              </a-tag>
            </div>
            <div class="item-title">{{ question.title }}</div>
            <div class="item-footer">
              <span class="item-stat">
                <icon-eye />
                {{ question.submitNum || 0 }} 提交
              </span>
              <span class="item-stat">
                <icon-check-circle />
                {{ question.acceptedNum || 0 }} 通过
              </span>
            </div>
          </div>
        </div>
      </a-spin>
    </div>
    <template #actions>
      <a-button type="text" size="small" @click="viewAll">
        查看全部
        <template #icon>
          <icon-arrow-right />
        </template>
      </a-button>
    </template>
  </a-card>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { OpenAPI } from "../../generated/core/OpenAPI";
import {
  IconStar,
  IconInbox,
  IconArrowRight,
  IconEye,
  IconCheckCircle,
} from "@arco-design/web-vue/es/icon";

const router = useRouter();

const loading = ref(false);
const favorites = ref<any[]>([]);

const loadFavorites = async () => {
  loading.value = true;
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_favour/my/list/page`,
      {
        pageSize: 5,
        current: 1,
      },
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      favorites.value = response.data.data?.records || [];
    }
  } catch (error: any) {
    // 未登录或加载失败，静默处理
    console.warn("加载收藏列表失败:", error);
  } finally {
    loading.value = false;
  }
};

const goToQuestion = (questionId: number) => {
  router.push(`/view/question/${questionId}`);
};

const viewAll = () => {
  router.push("/questions?favour=true");
};

onMounted(() => {
  loadFavorites();
});
</script>

<style scoped>
.favorites-card {
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.card-title-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
}

.title-icon {
  color: #ffb400;
  font-size: 18px;
}

.favorites-content {
  min-height: 200px;
  max-height: 600px;
  overflow-y: auto;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
}

.empty-icon {
  font-size: 48px;
  color: #c9cdd4;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
  color: #1d2129;
  margin-bottom: 8px;
}

.empty-hint {
  font-size: 12px;
  color: #86909c;
}

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.favorite-item {
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.favorite-item:hover {
  border-color: #3370ff;
  box-shadow: 0 2px 8px rgba(51, 112, 255, 0.1);
  transform: translateY(-2px);
}

.item-header {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #1d2129;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.item-footer {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #86909c;
}

.item-stat {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
