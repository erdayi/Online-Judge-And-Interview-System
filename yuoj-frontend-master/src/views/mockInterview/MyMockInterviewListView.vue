<!-- src/views/mockInterview/MyMockInterviewListView.vue -->
<template>
  <div class="my-mock-interview-list">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title"><icon-history /> 我的模拟面试</h1>
        <p class="page-subtitle">查看和管理你的 AI 模拟面试记录</p>
      </div>
      <div class="header-actions">
        <a-button type="primary" size="large" @click="goToAdd">
          <template #icon><icon-plus /></template>
          新建面试
        </a-button>
      </div>
    </div>

    <!-- 搜索表单：紧凑一行，自动搜索 -->
    <a-card class="search-card" :bordered="false" style="margin-bottom: 24px">
      <a-form :model="searchForm" layout="vertical">
        <a-row :gutter="[12, 12]" wrap class="search-row">
          <a-col flex="200px">
            <a-form-item label="工作岗位">
              <a-input
                v-model="searchForm.jobPosition"
                placeholder="如：Java后端"
                allow-clear
                @input="handleSearch"
                @keyup.enter="handleSearch"
              />
            </a-form-item>
          </a-col>
          <a-col flex="180px">
            <a-form-item label="工作年限">
              <a-input
                v-model="searchForm.workExperience"
                placeholder="如：3年"
                allow-clear
                @input="handleSearch"
                @keyup.enter="handleSearch"
              />
            </a-form-item>
          </a-col>
          <a-col flex="160px">
            <a-form-item label="难度">
              <a-select
                v-model="searchForm.difficulty"
                placeholder="全部"
                allow-clear
                @change="handleSearch"
              >
                <a-option value="简单">简单</a-option>
                <a-option value="中等">中等</a-option>
                <a-option value="困难">困难</a-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col flex="160px">
            <a-form-item label="状态">
              <a-select
                v-model="searchForm.status"
                placeholder="全部"
                allow-clear
                @change="handleSearch"
              >
                <a-option :value="0">未开始</a-option>
                <a-option :value="1">进行中</a-option>
                <a-option :value="2">已结束</a-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col flex="none">
            <a-form-item label="&nbsp;">
              <a-button @click="handleReset">重置</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <!-- 列表卡片 -->
    <a-card class="list-card">
      <a-empty
        v-if="!loading && list.length === 0"
        description="暂无模拟面试记录"
      />
      <a-list
        v-else
        :loading="loading"
        item-layout="vertical"
        :bordered="false"
      >
        <a-list-item v-for="item in list" :key="item.id">
          <a-list-item-meta
            :title="item.jobPosition || '未填写岗位'"
            :description="formatDescription(item)"
          />
          <div class="item-footer">
            <div class="info-tags">
              <a-tag v-if="item.difficulty" color="arcoblue">{{
                item.difficulty
              }}</a-tag>
              <a-tag v-if="item.status === 0" color="gray">未开始</a-tag>
              <a-tag v-if="item.status === 1" color="blue">进行中</a-tag>
              <a-tag v-if="item.status === 2" color="green">已结束</a-tag>
            </div>
            <div class="action-buttons">
              <a-button type="primary" size="small" @click="goToChat(item.id)">
                {{ getStatusText(item.status) }}
              </a-button>
            </div>
          </div>
        </a-list-item>
      </a-list>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import message from "@arco-design/web-vue/es/message";
import dayjs from "dayjs"; // 如果你项目中没有 dayjs，请改用 new Date().toLocaleString() 或其他方式
import { IconHistory, IconPlus } from "@arco-design/web-vue/es/icon";
import {
  MockInterviewControllerService,
  type MockInterview,
} from "../../../generated";

const router = useRouter();
const loading = ref(false);
const list = ref<MockInterview[]>([]);

// 搜索条件
const searchForm = ref({
  jobPosition: "",
  workExperience: "",
  difficulty: "",
  status: null as number | null,
});

const goToAdd = () => {
  router.push("/add/mock-interview");
};

const goToChat = (id: number) => {
  router.push(`/mock-interview/${id}`);
};

const getStatusText = (status: number): string => {
  if (status === 0) return "进入房间";
  if (status === 1) return "继续面试";
  if (status === 2) return "查看回顾";
  return "进入房间";
};

// 格式化描述：工作经验 + 创建时间
const formatDescription = (item: MockInterview): string => {
  const exp = item.workExperience || "未填写";
  // 假设 createTime 是 ISO 字符串 或 时间戳（毫秒）
  let timeStr = "未知时间";
  if (item.createTime) {
    // 如果是时间戳（数字），需判断
    const time =
      typeof item.createTime === "number"
        ? dayjs(item.createTime).format("YYYY-MM-DD HH:mm")
        : dayjs(item.createTime).format("YYYY-MM-DD HH:mm");
    timeStr = time;
  }
  return `工作经验: ${exp}｜创建时间: ${timeStr}`;
};

const fetchList = async () => {
  loading.value = true;
  try {
    const params: Record<string, any> = {
      current: 1,
      pageSize: 10,
    };

    if (searchForm.value.jobPosition)
      params.jobPosition = searchForm.value.jobPosition;
    if (searchForm.value.workExperience)
      params.workExperience = searchForm.value.workExperience;
    if (searchForm.value.difficulty)
      params.difficulty = searchForm.value.difficulty;
    if (searchForm.value.status !== null)
      params.status = searchForm.value.status;

    const res =
      await MockInterviewControllerService.listMyMockInterviewVoByPageUsingPost(
        params
      );

    if (res.code === 0 && res.data?.records) {
      list.value = res.data.records.map((item) => ({
        ...item,
        id: Number(item.id),
        userId: Number(item.userId),
        status: Number(item.status),
      }));
    } else {
      throw new Error(res.message || "获取列表失败");
    }
  } catch (error: any) {
    console.error("获取列表失败:", error);
    message.error("获取模拟面试列表失败");
    list.value = [];
  } finally {
    loading.value = false;
  }
};

// 防抖可选，此处为简化直接调用
const handleSearch = () => {
  fetchList();
};

const handleReset = () => {
  searchForm.value = {
    jobPosition: "",
    workExperience: "",
    difficulty: "",
    status: null,
  };
  fetchList();
};

onMounted(() => {
  fetchList();
});
</script>

<style scoped>
.my-mock-interview-list {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 24px;
  border: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.header-actions {
  flex-shrink: 0;
}

.search-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}

.search-row {
  flex-wrap: wrap;
}

.list-card {
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  background: #ffffff;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f1f5f9;
}

.info-tags {
  display: flex;
  gap: 8px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  align-items: center;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 20px;
    padding: 24px;
  }

  .page-title {
    font-size: 24px;
  }

  .item-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .action-buttons {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>
