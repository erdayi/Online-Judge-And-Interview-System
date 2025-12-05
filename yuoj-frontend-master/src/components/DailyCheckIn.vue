<template>
  <a-card class="daily-check-in-card" :bordered="false">
    <!-- 标题和导航 -->
    <div class="card-header">
      <h3 class="card-title">每日1题</h3>
      <div class="month-nav">
        <a-button type="text" size="mini" @click="prevMonth">
          <template #icon>
            <icon-left />
          </template>
        </a-button>
        <span class="month-label">{{ currentMonthLabel }}</span>
        <a-button type="text" size="mini" @click="nextMonth">
          <template #icon>
            <icon-right />
          </template>
        </a-button>
      </div>
    </div>

    <!-- 月份标识 -->
    <div class="month-badge">
      {{ monthBadgeText }}
    </div>

    <!-- 日历 -->
    <div class="calendar">
      <div class="weekdays">
        <span v-for="day in weekdays" :key="day" class="weekday">{{
          day
        }}</span>
      </div>
      <div class="calendar-grid">
        <div
          v-for="(date, index) in calendarDates"
          :key="index"
          class="calendar-day"
          :class="{
            'is-today': isToday(date),
            'is-checked': isChecked(date),
            'is-missed': isMissed(date),
            'is-future': isFuture(date),
          }"
        >
          <span class="day-number">{{ date.getDate() }}</span>
          <span v-if="isMissed(date)" class="missed-dot"></span>
        </div>
      </div>
    </div>

    <!-- Plus 会员挑战 -->
    <div class="challenge-section">
      <div class="challenge-header">
        <span class="challenge-title">Plus 会员挑战</span>
        <a-tooltip content="连续打卡可获得奖励">
          <icon-question-circle class="info-icon" />
        </a-tooltip>
        <span class="challenge-remaining">剩余{{ remainingDays }}天</span>
      </div>
      <div class="week-indicators">
        <span
          v-for="(week, index) in weeks"
          :key="index"
          class="week-indicator"
          :class="{ active: week.active }"
        >
          W{{ index + 1 }}
        </span>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="card-footer">
      <div class="makeup-card-info" @click="handleMakeupCard">
        <icon-check-circle class="makeup-icon" />
        <span>补卡券 {{ makeupCardCount }} 兑换</span>
      </div>
      <a-button type="text" size="mini" @click="showRules">活动规则</a-button>
    </div>

    <!-- 打卡按钮（如果今天未打卡） -->
    <div v-if="!isTodayChecked && todayQuestionId" class="check-in-button">
      <a-button type="primary" block @click="handleCheckIn">
        <template #icon>
          <icon-check />
        </template>
        立即打卡
      </a-button>
    </div>
  </a-card>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { ref, computed, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { OpenAPI } from "../../generated/core/OpenAPI";
import message from "@arco-design/web-vue/es/message";
import {
  IconLeft,
  IconRight,
  IconQuestionCircle,
  IconCheckCircle,
  IconCheck,
} from "@arco-design/web-vue/es/icon";

const router = useRouter();

const currentDate = ref(new Date());
const checkInRecords = ref<Record<string, number>>({});
const makeupCardCount = ref(0);
const todayQuestionId = ref<number | null>(null);
const loading = ref(false);
const checkingIn = ref(false);

const weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

const currentMonthLabel = computed(() => {
  const year = currentDate.value.getFullYear();
  const month = currentDate.value.getMonth() + 1;
  return `${year}年${month}月`;
});

const monthBadgeText = computed(() => {
  const month = currentDate.value.getMonth() + 1;
  const monthNames = [
    "JAN",
    "FEB",
    "MAR",
    "APR",
    "MAY",
    "JUN",
    "JUL",
    "AUG",
    "SEP",
    "OCT",
    "NOV",
    "DEC",
  ];
  return `${month} ${monthNames[month - 1]}`;
});

const calendarDates = computed(() => {
  const year = currentDate.value.getFullYear();
  const month = currentDate.value.getMonth();
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const startDate = new Date(firstDay);
  startDate.setDate(startDate.getDate() - startDate.getDay());

  const dates: Date[] = [];
  const current = new Date(startDate);
  const endDate = new Date(lastDay);
  endDate.setDate(endDate.getDate() + (6 - endDate.getDay()));

  while (current <= endDate) {
    dates.push(new Date(current));
    current.setDate(current.getDate() + 1);
  }

  return dates;
});

const weeks = computed(() => {
  const today = new Date();
  const currentWeek = Math.floor((today.getDate() - 1) / 7) + 1;
  return Array.from({ length: 6 }, (_, i) => ({
    active: i + 1 === currentWeek,
  }));
});

const remainingDays = computed(() => {
  const today = new Date();
  const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
  return lastDay.getDate() - today.getDate();
});

const formatDate = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

const isToday = (date: Date): boolean => {
  const today = new Date();
  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth() &&
    date.getDate() === today.getDate()
  );
};

const isChecked = (date: Date): boolean => {
  const dateStr = formatDate(date);
  return checkInRecords.value[dateStr] === 1;
};

const isMissed = (date: Date): boolean => {
  const today = new Date();
  if (date > today) {
    return false;
  }
  const dateStr = formatDate(date);
  const record = checkInRecords.value[dateStr];
  return !isToday(date) && record !== 1 && date < today;
};

const isFuture = (date: Date): boolean => {
  const today = new Date();
  return date > today;
};

const isTodayChecked = computed(() => {
  const today = new Date();
  const dateStr = formatDate(today);
  return checkInRecords.value[dateStr] === 1;
});

const prevMonth = () => {
  const newDate = new Date(currentDate.value);
  newDate.setMonth(newDate.getMonth() - 1);
  currentDate.value = newDate;
  loadCheckInRecord();
};

const nextMonth = () => {
  const newDate = new Date(currentDate.value);
  newDate.setMonth(newDate.getMonth() + 1);
  currentDate.value = newDate;
  loadCheckInRecord();
};

const loadCheckInRecord = async () => {
  loading.value = true;
  try {
    const year = currentDate.value.getFullYear();
    const month = currentDate.value.getMonth() + 1;
    const response = await axios.get(
      `${OpenAPI.BASE}/api/daily_check_in/record`,
      {
        params: { year, month },
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      checkInRecords.value = response.data.data || {};
    }
  } catch (error: any) {
    console.error("加载打卡记录失败:", error);
  } finally {
    loading.value = false;
  }
};

const loadMakeupCardCount = async () => {
  try {
    const response = await axios.get(
      `${OpenAPI.BASE}/api/daily_check_in/makeup_card_count`,
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      makeupCardCount.value = response.data.data || 0;
    }
  } catch (error: any) {
    console.error("加载补卡券数量失败:", error);
  }
};

const loadTodayQuestion = async () => {
  try {
    const response = await axios.get(
      `${OpenAPI.BASE}/api/daily_check_in/today_question`,
      {
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      todayQuestionId.value = response.data.data;
    }
  } catch (error: any) {
    console.error("加载今日题目失败:", error);
  }
};

const handleCheckIn = async () => {
  if (!todayQuestionId.value) {
    message.warning("今日题目未设置");
    return;
  }
  checkingIn.value = true;
  try {
    const response = await axios.post(
      `${OpenAPI.BASE}/api/daily_check_in/check_in`,
      null,
      {
        params: { questionId: todayQuestionId.value },
        withCredentials: OpenAPI.WITH_CREDENTIALS,
      }
    );
    if (response.data.code === 0) {
      message.success("打卡成功！");
      await loadCheckInRecord();
      await loadMakeupCardCount();
      // 跳转到题目页面
      router.push(`/view/question/${todayQuestionId.value}`);
    } else {
      message.error("打卡失败：" + response.data.message);
    }
  } catch (error: any) {
    console.error("打卡失败:", error);
    message.error("打卡失败，请重试");
  } finally {
    checkingIn.value = false;
  }
};

const handleMakeupCard = () => {
  if (makeupCardCount.value <= 0) {
    message.warning("补卡券不足");
    return;
  }
  message.info("补卡功能开发中，请稍后");
};

const showRules = () => {
  message.info("活动规则：每日完成一道题目即可打卡，连续打卡可获得奖励");
};

watch(
  () => currentDate.value,
  () => {
    loadCheckInRecord();
  }
);

onMounted(() => {
  loadCheckInRecord();
  loadMakeupCardCount();
  loadTodayQuestion();
});
</script>

<style scoped>
.daily-check-in-card {
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  position: relative;
  overflow: visible;
  width: 100%;
  max-width: 320px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1d2129;
  margin: 0;
}

.month-nav {
  display: flex;
  align-items: center;
  gap: 8px;
}

.month-label {
  padding: 4px 12px;
  background: #f7f8fa;
  border-radius: 4px;
  font-size: 12px;
  color: #86909c;
}

.month-badge {
  position: absolute;
  top: -8px;
  right: 16px;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  color: #86909c;
  transform: rotate(-5deg);
}

.calendar {
  margin-bottom: 20px;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
  margin-bottom: 8px;
}

.weekday {
  text-align: center;
  font-size: 12px;
  color: #86909c;
  padding: 4px 0;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  position: relative;
  cursor: pointer;
  transition: all 0.2s ease;
}

.calendar-day:hover {
  background: #f7f8fa;
}

.day-number {
  font-size: 14px;
  color: #86909c;
}

.calendar-day.is-today {
  background: #00b42a;
  border-radius: 50%;
}

.calendar-day.is-today .day-number {
  color: white;
  font-weight: 600;
}

.calendar-day.is-checked {
  background: #e8f5e9;
}

.calendar-day.is-checked .day-number {
  color: #00b42a;
  font-weight: 600;
}

.calendar-day.is-missed {
  position: relative;
}

.missed-dot {
  position: absolute;
  bottom: 4px;
  width: 4px;
  height: 4px;
  background: #f53f3f;
  border-radius: 50%;
}

.calendar-day.is-future .day-number {
  color: #c9cdd4;
}

.challenge-section {
  background: #fff7e6;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 16px;
}

.challenge-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.challenge-title {
  font-size: 14px;
  font-weight: 600;
  color: #ff7d00;
  flex: 1;
}

.info-icon {
  color: #86909c;
  cursor: help;
}

.challenge-remaining {
  font-size: 12px;
  color: #86909c;
}

.week-indicators {
  display: flex;
  gap: 8px;
}

.week-indicator {
  flex: 1;
  text-align: center;
  padding: 6px 0;
  border-radius: 4px;
  font-size: 12px;
  color: #86909c;
  background: transparent;
}

.week-indicator.active {
  background: #ff7d00;
  color: white;
  font-weight: 600;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #f0f9ff;
  border-radius: 8px;
  margin-top: 16px;
}

.makeup-card-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #00b42a;
}

.makeup-icon {
  font-size: 16px;
}

.makeup-card-info {
  cursor: pointer;
}

.makeup-card-info:hover {
  opacity: 0.8;
}

.check-in-button {
  margin-top: 16px;
}
</style>
