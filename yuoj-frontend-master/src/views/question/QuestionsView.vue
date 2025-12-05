<template>
  <div id="questionsView">
    <!-- 合并后的搜索表单：聚合搜索 + 名称 + 标签 -->
    <a-form :model="formModel" layout="inline">
      <!-- 聚合搜索 -->
      <a-form-item label="聚合搜索" style="min-width: 240px">
        <a-input
          v-model="aggregateSearchText"
          placeholder="Elasticsearch"
          allow-clear
          @press-enter="handleSubmit"
        />
      </a-form-item>

      <!-- 名称 -->
      <a-form-item field="title" label="名称" style="min-width: 240px">
        <a-input v-model="searchParams.title" placeholder="请输入名称" />
      </a-form-item>

      <!-- 标签 -->
      <a-form-item field="tags" label="标签" style="min-width: 240px">
        <a-input-tag v-model="searchParams.tags" placeholder="请输入标签" />
      </a-form-item>

      <!-- 提交按钮 -->
      <a-form-item>
        <a-button type="primary" @click="handleSubmit">提交</a-button>
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
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green">
            {{ tag }}
          </a-tag>
        </a-space>
      </template>

      <template #acceptedRate="{ record }">
        {{
          (() => {
            const submit = record.submitNum ?? 0;
            const accepted = record.acceptedNum ?? 0;
            const rate =
              submit > 0 ? ((accepted / submit) * 100).toFixed(1) : "0";
            return `${rate}% (${accepted}/${submit})`;
          })()
        }}
      </template>

      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>

      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="toQuestionPage(record)">
            做题
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter, useRoute } from "vue-router";
import moment from "moment";
import axios from "axios";
import { OpenAPI } from "../../../generated/core/OpenAPI";

const tableRef = ref();
const router = useRouter();
const route = useRoute();

const dataList = ref<Question[]>([]);
const total = ref<number>(0);

// 普通搜索参数（title + tags）
const searchParams = ref<QuestionQueryRequest>({
  title: "",
  tags: [],
  pageSize: 8,
  current: 1,
});

// 聚合搜索文本（独立于 searchParams）
const aggregateSearchText = ref<string>("");

// 是否处于聚合搜索模式
const isAggregateSearchMode = ref<boolean>(false);

// 是否是收藏页
const isFavourPage = ref<boolean>(false);

/**
 * 加载数据
 */
const loadData = async () => {
  if (isFavourPage.value) {
    // 收藏页逻辑不变
    try {
      const response = await axios.post(
        `${OpenAPI.BASE}/api/question_favour/my/list/page`,
        searchParams.value,
        { withCredentials: OpenAPI.WITH_CREDENTIALS }
      );
      if (response.data.code === 0) {
        dataList.value = response.data.data?.records || [];
        total.value = response.data.data?.total || 0;
      } else {
        message.error("加载失败，" + response.data.message);
      }
    } catch (error: any) {
      console.error("加载收藏列表失败:", error);
      message.error("加载失败，请先登录");
    }
    return;
  }

  // 非收藏页：判断是否聚合搜索
  let res;
  if (isAggregateSearchMode.value && aggregateSearchText.value.trim()) {
    // 聚合搜索：调用 ES 接口
    const esParams: QuestionQueryRequest = {
      searchText: aggregateSearchText.value.trim(),
      pageSize: searchParams.value.pageSize,
      current: searchParams.value.current,
    };
    res = await QuestionControllerService.searchQuestionVoByPageUsingPost(
      esParams
    );
  } else {
    // 普通搜索（title/tags）
    res = await QuestionControllerService.listQuestionVoByPageUsingPost(
      searchParams.value
    );
  }

  if (res?.code === 0) {
    // 数据清洗：防止 null
    dataList.value = (res.data.records || []).map((item) => ({
      ...item,
      submitNum: item.submitNum ?? 0,
      acceptedNum: item.acceptedNum ?? 0,
    }));
    total.value = res.data.total ?? 0;
  } else {
    message.error("加载失败，" + (res?.message || "未知错误"));
  }
};

/**
 * 分页变化
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 统一提交处理：优先聚合搜索
 */
const handleSubmit = () => {
  const hasAggregate = !!aggregateSearchText.value.trim();
  isAggregateSearchMode.value = hasAggregate;

  // 重置分页
  searchParams.value.current = 1;

  // 如果是聚合搜索，清空 title/tags（可选，避免混淆）
  if (hasAggregate) {
    searchParams.value.title = "";
    searchParams.value.tags = [];
  }

  loadData();
};

/**
 * 监听参数变化
 */
watchEffect(() => {
  if (isFavourPage.value !== undefined) {
    loadData();
  }
});

/**
 * 初始化
 */
onMounted(() => {
  const favour = route.query.favour;
  isFavourPage.value = favour === "true" || favour === true;
  loadData();
});

/**
 * 跳转题目页
 */
const toQuestionPage = (question: Question) => {
  router.push({ path: `/view/question/${question.id}` });
};

// 表格列
const columns = [
  { title: "题号", dataIndex: "id" },
  { title: "题目名称", dataIndex: "title" },
  { title: "标签", slotName: "tags" },
  { title: "通过率", slotName: "acceptedRate" },
  { title: "创建时间", slotName: "createTime" },
  { slotName: "optional" },
];

// 表单 model（仅用于 a-form 绑定，实际逻辑靠独立变量）
const formModel = ref({
  aggregateSearchText: aggregateSearchText,
});
</script>

<style scoped>
#questionsView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
