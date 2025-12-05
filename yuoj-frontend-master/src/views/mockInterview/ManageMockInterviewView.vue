<template>
  <div id="manageMockInterviewView">
    <a-table
      :ref="tableRef"
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
      }"
      @page-change="onPageChange"
    >
      <!-- 创建时间插槽 -->
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>

      <!-- 状态插槽 -->
      <template #status="{ record }">
        <a-tag :color="getStatusColor(record.status)" size="small">
          {{ getStatusText(record.status) }}
        </a-tag>
      </template>

      <!-- 操作插槽 -->
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" size="small" @click="doView(record)">
            查看
          </a-button>
          <a-button status="danger" size="small" @click="doDelete(record)">
            删除
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  MockInterview,
  MockInterviewControllerService,
  type Page_MockInterview_,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();
const dataList = ref<MockInterview[]>([]);
const total = ref(0);
const searchParams = ref({
  current: 1,
  pageSize: 10,
});

// 状态枚举定义
const STATUS = {
  PENDING: { text: "待开始", value: 0, color: "orange" },
  IN_PROGRESS: { text: "进行中", value: 1, color: "blue" },
  FINISHED: { text: "已结束", value: 2, color: "green" },
};

// 获取状态文本
const getStatusText = (status: number) => {
  switch (status) {
    case STATUS.PENDING.value:
      return STATUS.PENDING.text;
    case STATUS.IN_PROGRESS.value:
      return STATUS.IN_PROGRESS.text;
    case STATUS.FINISHED.value:
      return STATUS.FINISHED.text;
    default:
      return "未知状态";
  }
};

// 获取状态颜色
const getStatusColor = (status: number) => {
  switch (status) {
    case STATUS.PENDING.value:
      return STATUS.PENDING.color;
    case STATUS.IN_PROGRESS.value:
      return STATUS.IN_PROGRESS.color;
    case STATUS.FINISHED.value:
      return STATUS.FINISHED.color;
    default:
      return "gray";
  }
};

// 加载数据
const loadData = async () => {
  try {
    const res =
      await MockInterviewControllerService.listMockInterviewByPageUsingPost(
        searchParams.value
      );
    if (res.code === 0 && res.data) {
      const pageData = res.data as Page_MockInterview_;
      dataList.value = pageData.records || [];
      total.value = pageData.total || 0;
    } else {
      throw new Error(res.message || "加载失败");
    }
  } catch (err: any) {
    console.error("加载模拟面试列表失败:", err);
    message.error("加载失败：" + (err.message || "请重试"));
    dataList.value = [];
    total.value = 0;
  }
};

watchEffect(() => {
  loadData();
});

onMounted(() => {
  loadData();
});

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const doDelete = async (record: MockInterview) => {
  if (!record.id) return;
  try {
    const res =
      await MockInterviewControllerService.deleteMockInterviewUsingPost({
        id: record.id,
      });
    if (res.code === 0) {
      message.success("删除成功");
      loadData();
    } else {
      message.error(res.message || "删除失败");
    }
  } catch (err) {
    message.error("删除异常，请重试");
  }
};

const router = useRouter();
const doView = (record: MockInterview) => {
  if (!record.id) return;
  router.push({
    path: `/mock-interview/${record.id}`,
  });
};

// 表格列定义 —— 所有列居中
const columns = [
  {
    title: "ID",
    dataIndex: "id",
    width: 120,
    align: "center",
  },
  {
    title: "岗位",
    dataIndex: "jobPosition",
    width: 120,
    align: "center",
  },
  {
    title: "工作经验",
    dataIndex: "workExperience",
    width: 120,
    align: "center",
  },
  {
    title: "难度",
    dataIndex: "difficulty",
    width: 100,
    align: "center",
  },
  {
    title: "状态",
    slotName: "status",
    width: 100,
    align: "center",
    // 状态列标题加粗
    headerCellStyle: { fontWeight: "bold" },
  },
  {
    title: "用户ID",
    dataIndex: "userId",
    width: 140,
    align: "center",
  },
  {
    title: "创建时间",
    slotName: "createTime",
    width: 160,
    align: "center",
  },
  {
    title: "操作",
    slotName: "optional",
    width: 160,
    fixed: "right",
    align: "center",
  },
];
</script>

<style scoped>
/* 表格居中容器 */
#manageMockInterviewView {
  max-width: 1580px;
  margin: 0 auto;
  padding: 20px;
}

/* 所有单元格内容居中 */
:deep(.arco-table-cell) {
  text-align: center;
}

:deep(.arco-table-th),
:deep(.arco-table-td) {
  vertical-align: middle;
}

/* 状态列标题加粗 */
:deep(.arco-table-th[data-col-key="status"]) {
  font-weight: bold;
}
</style>
