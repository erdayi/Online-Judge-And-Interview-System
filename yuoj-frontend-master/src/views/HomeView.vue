<template>
  <div id="homeView">
    <!-- Hero 区域 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">欢迎来到 鱼 OJ</h1>
        <p class="hero-subtitle">专业的在线判题系统，提升你的编程能力</p>
        <div class="hero-actions">
          <a-button type="primary" size="large" @click="goToQuestions">
            <template #icon>
              <icon-code />
            </template>
            开始刷题
          </a-button>
          <a-button size="large" @click="goToSubmit">
            <template #icon>
              <icon-file />
            </template>
            查看提交
          </a-button>
        </div>
      </div>
    </div>

    <!-- 统计数据 -->
    <div class="stats-section">
      <a-row :gutter="24">
        <a-col :xs="12" :sm="12" :md="6" :lg="6">
          <a-card class="stat-card" hoverable>
            <div class="stat-content">
              <div class="stat-icon stat-icon-blue">
                <icon-book />
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalQuestions }}</div>
                <div class="stat-label">题目总数</div>
              </div>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="12" :sm="12" :md="6" :lg="6">
          <a-card class="stat-card" hoverable>
            <div class="stat-content">
              <div class="stat-icon stat-icon-green">
                <icon-user />
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalUsers }}</div>
                <div class="stat-label">注册用户</div>
              </div>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="12" :sm="12" :md="6" :lg="6">
          <a-card class="stat-card" hoverable>
            <div class="stat-content">
              <div class="stat-icon stat-icon-orange">
                <icon-send />
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalSubmits }}</div>
                <div class="stat-label">提交总数</div>
              </div>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="12" :sm="12" :md="6" :lg="6">
          <a-card class="stat-card" hoverable>
            <div class="stat-content">
              <div class="stat-icon stat-icon-purple">
                <icon-check-circle />
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.acceptRate }}%</div>
                <div class="stat-label">通过率</div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 功能特性 -->
    <div class="features-section">
      <h2 class="section-title">平台特色</h2>
      <a-row :gutter="24">
        <a-col :xs="24" :sm="12" :md="8" :lg="8">
          <a-card class="feature-card" hoverable>
            <template #cover>
              <div class="feature-icon-wrapper">
                <icon-code class="feature-icon" />
              </div>
            </template>
            <div class="feature-content">
              <h3 class="feature-title">在线编程</h3>
              <p class="feature-description">
                支持多种编程语言，实时编译运行，即时反馈结果
              </p>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="8" :lg="8">
          <a-card class="feature-card" hoverable>
            <template #cover>
              <div class="feature-icon-wrapper">
                <icon-bug class="feature-icon" />
              </div>
            </template>
            <div class="feature-content">
              <h3 class="feature-title">智能判题</h3>
              <p class="feature-description">
                自动化判题系统，快速准确评估代码正确性
              </p>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="8" :lg="8">
          <a-card class="feature-card" hoverable>
            <template #cover>
              <div class="feature-icon-wrapper">
                <icon-trophy class="feature-icon" />
              </div>
            </template>
            <div class="feature-content">
              <h3 class="feature-title">题目分类</h3>
              <p class="feature-description">
                丰富的题目分类，从基础到进阶，循序渐进
              </p>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 热门题目 -->
    <div class="hot-questions-section">
      <div class="section-header">
        <h2 class="section-title">热门题目</h2>
        <a-button type="text" @click="goToQuestions">
          查看更多
          <template #icon>
            <icon-arrow-right />
          </template>
        </a-button>
      </div>
      <a-spin :loading="loading" style="width: 100%">
        <a-row :gutter="24">
          <a-col
            v-for="question in hotQuestions"
            :key="question.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
          >
            <a-card
              class="question-card"
              hoverable
              @click="toQuestionPage(question)"
            >
              <div class="question-header">
                <a-tag color="blue">#{{ question.id }}</a-tag>
                <a-tag
                  v-for="(tag, index) in question.tags?.slice(0, 1)"
                  :key="index"
                  color="green"
                >
                  {{ tag }}
                </a-tag>
              </div>
              <h3 class="question-title">{{ question.title }}</h3>
              <div class="question-stats">
                <span class="stat-item">
                  <icon-thumb-up />
                  {{ question.acceptedNum || 0 }} 通过
                </span>
                <span class="stat-item">
                  <icon-eye />
                  {{ question.submitNum || 0 }} 提交
                </span>
              </div>
              <div class="question-footer">
                <a-button
                  type="primary"
                  size="small"
                  @click.stop="toQuestionPage(question)"
                >
                  开始做题
                </a-button>
                <span class="accept-rate">
                  通过率:
                  {{
                    question.submitNum
                      ? Math.round(
                          ((question.acceptedNum || 0) / question.submitNum) *
                            100
                        )
                      : 0
                  }}%
                </span>
              </div>
            </a-card>
          </a-col>
        </a-row>
        <a-empty
          v-if="!loading && hotQuestions.length === 0"
          description="暂无题目"
        />
      </a-spin>
    </div>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
  QuestionSubmitQueryRequest,
  UserControllerService,
  UserQueryRequest,
} from "../../generated";
import message from "@arco-design/web-vue/es/message";
import {
  IconCode,
  IconFile,
  IconBook,
  IconUser,
  IconSend,
  IconCheckCircle,
  IconBug,
  IconTrophy,
  IconArrowRight,
  IconThumbUp,
  IconEye,
} from "@arco-design/web-vue/es/icon";

const router = useRouter();

const loading = ref(false);
const statsLoading = ref(false);
const hotQuestions = ref<Question[]>([]);
const stats = ref({
  totalQuestions: 0,
  totalUsers: 0,
  totalSubmits: 0,
  acceptRate: 0,
});

// 加载统计数据
const loadStats = async () => {
  statsLoading.value = true;
  try {
    // 获取题目总数
    const questionRes =
      await QuestionControllerService.listQuestionVoByPageUsingPost({
        pageSize: 1,
        current: 1,
      } as QuestionQueryRequest);
    if (questionRes.code === 0) {
      stats.value.totalQuestions = questionRes.data?.total || 0;
    }

    // 获取用户总数
    try {
      const userRes = await UserControllerService.listUserVoByPageUsingPost({
        pageSize: 1,
        current: 1,
      } as UserQueryRequest);
      if (userRes.code === 0) {
        stats.value.totalUsers = userRes.data?.total || 0;
      }
    } catch (error: any) {
      // 如果获取用户列表失败（可能是权限问题），则跳过
      console.warn("获取用户总数失败:", error);
    }

    // 获取提交总数
    const submitRes =
      await QuestionControllerService.listQuestionSubmitByPageUsingPost({
        pageSize: 1,
        current: 1,
      } as QuestionSubmitQueryRequest);
    if (submitRes.code === 0) {
      stats.value.totalSubmits = submitRes.data?.total || 0;
    }

    // 计算通过率：获取所有题目，计算总通过数/总提交数
    const allQuestionsRes =
      await QuestionControllerService.listQuestionVoByPageUsingPost({
        pageSize: 1000,
        current: 1,
      } as QuestionQueryRequest);
    if (allQuestionsRes.code === 0) {
      const questions = allQuestionsRes.data?.records || [];
      let totalSubmits = 0;
      let totalAccepted = 0;
      questions.forEach((q: Question) => {
        totalSubmits += q.submitNum || 0;
        totalAccepted += q.acceptedNum || 0;
      });
      stats.value.acceptRate =
        totalSubmits > 0 ? Math.round((totalAccepted / totalSubmits) * 100) : 0;
    }
  } catch (error: any) {
    console.error("加载统计数据失败:", error);
    message.error("加载统计数据失败，请重试");
  } finally {
    statsLoading.value = false;
  }
};

// 加载热门题目
const loadHotQuestions = async () => {
  loading.value = true;
  try {
    const res = await QuestionControllerService.listQuestionVoByPageUsingPost({
      pageSize: 8,
      current: 1,
      sortField: "submitNum",
      sortOrder: "descend",
    } as QuestionQueryRequest);
    if (res.code === 0) {
      hotQuestions.value = res.data?.records || [];
    } else {
      message.error("加载失败，" + res.message);
    }
  } catch (error: any) {
    console.error("加载热门题目失败:", error);
    message.error("加载失败，请重试");
  } finally {
    loading.value = false;
  }
};

const goToQuestions = () => {
  router.push("/questions");
};

const goToSubmit = () => {
  router.push("/question_submit");
};

const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

onMounted(() => {
  loadStats();
  loadHotQuestions();
});
</script>

<style scoped>
#homeView {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Hero 区域 */
.hero-section {
  position: relative;
  padding: 80px 0;
  text-align: center;
  background: #f7f8fa;
  border-radius: 16px;
  margin-bottom: 48px;
  border: 1px solid #e5e7eb;
}

.hero-content {
  position: relative;
  z-index: 2;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 16px;
  color: #1d2129;
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 32px;
  color: #86909c;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

/* 统计数据 */
.stats-section {
  margin-bottom: 48px;
}

.stat-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid #e5e7eb;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.stat-icon-blue {
  background: #3370ff;
}

.stat-icon-green {
  background: #00b42a;
}

.stat-icon-orange {
  background: #ff7d00;
}

.stat-icon-purple {
  background: #722ed1;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1d2129;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #86909c;
}

/* 功能特性 */
.features-section {
  margin-bottom: 48px;
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 24px;
  text-align: center;
}

.feature-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  height: 100%;
  border: 1px solid #e5e7eb;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.feature-icon-wrapper {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7f8fa;
}

.feature-icon {
  font-size: 48px;
  color: #3370ff;
}

.feature-content {
  padding: 16px;
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 8px;
}

.feature-description {
  font-size: 14px;
  color: #86909c;
  line-height: 1.6;
  margin: 0;
}

/* 热门题目 */
.hot-questions-section {
  margin-bottom: 48px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.question-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
  height: 100%;
  margin-bottom: 24px;
  border: 1px solid #e5e7eb;
}

.question-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.question-header {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.question-title {
  font-size: 18px;
  font-weight: 600;
  color: #1d2129;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

.question-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #86909c;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.question-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.accept-rate {
  font-size: 12px;
  color: #86909c;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 32px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .hero-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .stat-value {
    font-size: 24px;
  }

  .section-title {
    font-size: 24px;
  }
}
</style>
