<template>
  <div id="favoritesView">
    <a-card class="favorites-header-card">
      <div class="header-content">
        <div class="header-icon">
          <icon-star :size="48" :fill="'#ffb400'" />
        </div>
        <div class="header-info">
          <h2 class="header-title">我的收藏</h2>
          <p class="header-subtitle">{{ total }} 题</p>
        </div>
        <div class="header-actions">
          <a-button type="primary" @click="goToQuestions">
            <template #icon>
              <icon-plus />
            </template>
            添加题目
          </a-button>
        </div>
      </div>
    </a-card>

    <a-card class="favorites-content-card">
      <template #title>
        <div class="card-title-wrapper">
          <span>收藏列表</span>
          <a-button type="text" size="small" @click="refreshData">
            <template #icon>
              <icon-refresh />
            </template>
            刷新
          </a-button>
        </div>
      </template>
      <a-spin :loading="loading" style="width: 100%">
        <div v-if="favorites.length === 0 && !loading" class="empty-state">
          <icon-inbox class="empty-icon" />
          <p class="empty-text">暂无收藏</p>
          <p class="empty-hint">收藏的题目将显示在这里</p>
          <a-button type="primary" @click="goToQuestions">
            <template #icon>
              <icon-plus />
            </template>
            去收藏题目
          </a-button>
        </div>
        <a-list v-else :bordered="false">
          <a-list-item
            v-for="item in favorites"
            :key="item.id"
            class="favorite-item"
          >
            <a-list-item-meta>
              <template #title>
                <div class="item-header">
                  <a-tag color="blue" size="small">#{{ item.id }}</a-tag>
                  <a-button
                    type="text"
                    size="small"
                    status="danger"
                    @click="handleUnfavour(item)"
                    :loading="unfavourLoading === item.id"
                  >
                    <template #icon>
                      <icon-delete />
                    </template>
                    取消收藏
                  </a-button>
                </div>
                <a class="item-title" @click="goToQuestion(item.id)">
                  {{ item.title }}
                </a>
              </template>
              <template #description>
                <div class="item-tags">
                  <a-tag
                    v-for="(tag, index) in item.tags?.slice(0, 3)"
                    :key="index"
                    color="green"
                    size="small"
                  >
                    {{ tag }}
                  </a-tag>
                </div>
                <div class="item-footer">
                  <span class="item-stat">
                    <icon-eye />
                    {{ item.submitNum || 0 }} 提交
                  </span>
                  <span class="item-stat">
                    <icon-check-circle />
                    {{ item.acceptedNum || 0 }} 通过
                  </span>
                  <span v-if="item.favourTime" class="item-stat">
                    <icon-clock-circle />
                    收藏于 {{ formatTime(item.favourTime) }}
                  </span>
                </div>
              </template>
            </a-list-item-meta>
            <template #actions>
              <a-button type="primary" @click="goToQuestion(item.id)">
                开始做题
              </a-button>
            </template>
          </a-list-item>
        </a-list>
        <a-pagination
          v-if="total > 0"
          :total="total"
          :current="currentPage"
          :page-size="pageSize"
          show-total
          show-page-size
          @change="handlePageChange"
          @page-size-change="handlePageSizeChange"
          style="margin-top: 24px; text-align: right"
        />
      </a-spin>
    </a-card>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { OpenAPI } from "../../../generated/core/OpenAPI";
import message from "@arco-design/web-vue/es/message";
import {
  IconStar,
  IconInbox,
  IconPlus,
  IconRefresh,
  IconDelete,
  IconEye,
  IconCheckCircle,
  IconClockCircle,
} from "@arco-design/web-vue/es/icon";
import moment from "moment";

const router = useRouter();

const loading = ref(false);
const unfavourLoading = ref<number | null>(null);
const favorites = ref<any[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const loadFavorites = async () => {
  loading.value = true;
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_favour/my/list/page`,
      {
        pageSize: pageSize.value,
        current: currentPage.value,
      },
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      favorites.value = response.data.data?.records || [];
      total.value = response.data.data?.total || 0;
    } else {
      message.error("加载失败，" + response.data.message);
    }
  } catch (error: any) {
    console.error("加载收藏列表失败:", error);
    if (error.response?.status === 401) {
      message.error("请先登录");
      router.push("/user/login");
    } else {
      message.error("加载失败，请重试");
    }
  } finally {
    loading.value = false;
  }
};

const handleUnfavour = async (item: any) => {
  if (!item.id) {
    return;
  }
  unfavourLoading.value = item.id;
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/question_favour/`,
      {
        questionId: item.id,
      },
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      message.success("取消收藏成功");
      await loadFavorites();
    } else {
      message.error("操作失败，" + response.data.message);
    }
  } catch (error: any) {
    console.error("取消收藏失败:", error);
    message.error("操作失败，请重试");
  } finally {
    unfavourLoading.value = null;
  }
};

const goToQuestion = (questionId: number) => {
  router.push(`/view/question/${questionId}`);
};

const goToQuestions = () => {
  router.push("/questions");
};

const refreshData = () => {
  loadFavorites();
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  loadFavorites();
};

const handlePageSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  loadFavorites();
};

const formatTime = (time: string | Date | undefined) => {
  if (!time) {
    return "未知";
  }
  return moment(time).format("YYYY-MM-DD HH:mm");
};

onMounted(() => {
  loadFavorites();
});
</script>

<style scoped>
#favoritesView {
  max-width: 1200px;
  margin: 0 auto;
}

.favorites-header-card {
  margin-bottom: 24px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 24px;
}

.header-icon {
  flex-shrink: 0;
}

.header-info {
  flex: 1;
}

.header-title {
  font-size: 24px;
  font-weight: 600;
  color: #1d2129;
  margin: 0 0 8px 0;
}

.header-subtitle {
  font-size: 14px;
  color: #86909c;
  margin: 0;
}

.header-actions {
  flex-shrink: 0;
}

.favorites-content-card {
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.card-title-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  font-size: 64px;
  color: #c9cdd4;
  margin-bottom: 24px;
}

.empty-text {
  font-size: 18px;
  font-weight: 500;
  color: #1d2129;
  margin-bottom: 8px;
}

.empty-hint {
  font-size: 14px;
  color: #86909c;
  margin-bottom: 24px;
}

.favorite-item {
  padding: 16px 0;
  border-bottom: 1px solid #e5e7eb;
  transition: background-color 0.2s;
}

.favorite-item:hover {
  background-color: #f7f8fa;
}

.favorite-item:last-child {
  border-bottom: none;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-title {
  font-size: 16px;
  font-weight: 500;
  color: #1d2129;
  cursor: pointer;
  transition: color 0.2s;
  display: block;
  margin-bottom: 12px;
}

.item-title:hover {
  color: #3370ff;
}

.item-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.item-footer {
  display: flex;
  gap: 24px;
  font-size: 13px;
  color: #86909c;
}

.item-stat {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
