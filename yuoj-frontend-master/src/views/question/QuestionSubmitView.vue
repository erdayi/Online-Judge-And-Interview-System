<template>
  <div id="questionSubmitView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 240px">
        <a-input v-model="searchParams.questionId" placeholder="请输入" />
      </a-form-item>
      <a-form-item field="language" label="编程语言" style="min-width: 240px">
        <a-select
          v-model="searchParams.language"
          :style="{ width: '320px' }"
          placeholder="选择编程语言"
        >
          <a-option>java</a-option>
          <a-option>cpp</a-option>
          <a-option>go</a-option>
          <a-option>html</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :ref="tableRef"
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #judgeInfo="{ record }">
        <div class="judge-info-tags">
          <template v-if="isJudgeInfoEmpty(record.judgeInfo)">
            <a-tag color="gray" size="small">
              {{ getEmptyJudgeInfoText(record.status) }}
            </a-tag>
          </template>
          <template v-else>
            <a-tag v-if="record.judgeInfo?.message" color="blue" size="small">
              信息: {{ record.judgeInfo.message }}
            </a-tag>
            <a-tag v-if="record.judgeInfo?.time" color="green" size="small">
              时间: {{ record.judgeInfo.time }}ms
            </a-tag>
            <a-tag v-if="record.judgeInfo?.memory" color="orange" size="small">
              内存: {{ record.judgeInfo.memory }}KB
            </a-tag>
          </template>
        </div>
      </template>
      <template #status="{ record }">
        <a-tag :color="getStatusColor(record.status)" size="small">
          {{ getStatusText(record.status) }}
        </a-tag>
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionSubmitQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: undefined,
  pageSize: 10,
  current: 1,
});

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
    {
      ...searchParams.value,
      sortField: "createTime",
      sortOrder: "descend",
    }
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("加载失败，" + res.message);
  }
};

/**
 * 判题状态对照关系
 * 0 - 待判题、1 - 判题中、2 - 成功、3 - 失败
 */
const statusConfig = {
  "0": { text: "待判题", color: "gray" },
  "1": { text: "判题中", color: "blue" },
  "2": { text: "成功", color: "green" },
  "3": { text: "失败", color: "red" },
};

/**
 * 判断判题信息是否为空（全为null或空对象）
 */
const isJudgeInfoEmpty = (judgeInfo: any) => {
  if (!judgeInfo) return true;
  if (typeof judgeInfo !== "object") return false;

  // 检查所有属性是否都为null或undefined
  const values = Object.values(judgeInfo);
  return values.every(
    (value) => value === null || value === undefined || value === ""
  );
};

/**
 * 根据状态获取空的判题信息提示文本
 */
const getEmptyJudgeInfoText = (status: string) => {
  const emptyTextMap: { [key: string]: string } = {
    "0": "⏳ 等待判题",
    "1": "🔍 判题中",
    "2": "✅ 判题完成",
    "3": "❌ 判题失败",
  };
  return emptyTextMap[status] || "📊 暂无判题信息";
};

/**
 * 获取状态对应的颜色
 */
const getStatusColor = (status: string) => {
  return statusConfig[status]?.color || "gray";
};

/**
 * 获取状态对应的文本
 */
const getStatusText = (status: string) => {
  return statusConfig[status]?.text || "未知状态";
};

/**
 * 监听 searchParams 变量，改变时触发页面的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const columns = [
  {
    title: "提交号",
    dataIndex: "id",
    align: "center",
  },
  {
    title: "编程语言",
    dataIndex: "language",
    align: "center",
  },
  {
    title: "判题信息",
    slotName: "judgeInfo",
    align: "center",
  },
  {
    title: "判题状态",
    slotName: "status",
    align: "center",
  },
  {
    title: "题目 id",
    dataIndex: "questionId",
    align: "center",
  },
  {
    title: "提交者 id",
    dataIndex: "userId",
    align: "center",
  },
  {
    title: "创建时间",
    slotName: "createTime",
    align: "center",
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const router = useRouter();

/**
 * 跳转到做题页面
 * @param question
 */
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

/**
 * 确认搜索，重新加载数据
 */
const doSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>

<style scoped>
#questionSubmitView {
  max-width: 1280px;
  margin: 0 auto;
}

.judge-info-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
}

.judge-info-tags .arco-tag {
  margin: 2px;
}

/* 表格内容居中对齐 */
:deep(.arco-table-cell) {
  text-align: center;
}

/* 表头居中对齐 */
:deep(.arco-table-th) {
  text-align: center;
}

/* 确保标签内容在单元格内居中 */
:deep(.arco-table-td) {
  vertical-align: middle;
}
</style>
