<template>
  <div id="managerUserView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="id" label="用户ID" style="min-width: 240px">
        <a-input v-model="searchParams.id" placeholder="请输入" />
      </a-form-item>
      <a-form-item field="userRole" label="用户角色" style="min-width: 240px">
        <a-select
          v-model="searchParams.userRole"
          :style="{ width: '320px' }"
          placeholder="选择用户角色"
        >
          <a-option>user</a-option>
          <a-option>admin</a-option>
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
      <!-- 昵称插槽（添加点击跳转） -->
      <template #userName="{ record }">
        <!-- <a-link @click="toUserPage(record)"> -->
        {{ record.userName || record.userAccount || "匿名用户" }}
        <!-- </a-link> -->
      </template>

      <!-- 头像插槽（关键修正） -->
      <template #userAvatar="{ record }">
        <a-avatar :size="40" :style="{ backgroundColor: '#3370ff' }">
          <img
            v-if="record.userAvatar && isAvatarUrlValid(record.userAvatar)"
            :src="formatAvatarUrl(record.userAvatar)"
            alt="avatar"
            @error="handleAvatarError"
          />
          <icon-user v-else />
        </a-avatar>
      </template>

      <!-- 用户简介插槽 -->
      <template #userProfile="{ record }">
        <span :title="record.userProfile">
          {{ record.userProfile || "这个用户很懒，什么都没写~" }}
        </span>
      </template>

      <!-- 用户角色插槽 -->
      <template #userRole="{ record }">
        <a-tag :color="record.userRole === 'admin' ? 'red' : 'green'">
          {{ (record.userRole || "user").toUpperCase() }}
        </a-tag>
      </template>

      <!-- 创建时间插槽 -->
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>

      <!-- 更新时间插槽 -->
      <template #updateTime="{ record }">
        {{ moment(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>

      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)"> 修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { IconUser } from "@arco-design/web-vue/es/icon"; // 必须导入图标
import {
  User,
  UserControllerService,
  UserQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();
const router = useRouter();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref<UserQueryRequest>({
  id: undefined,
  userRole: undefined,
  pageSize: 10,
  current: 1,
});

// 头像URL验证和格式化
const isAvatarUrlValid = (url: string) => {
  return url && url.startsWith("http") && url.length > 10;
};

// 修正头像URL格式（处理换行等问题）
const formatAvatarUrl = (url: string) => {
  if (!url) return "";
  // 移除换行符和多余空格
  return url.replace(/\s+/g, "").replace(/\n/g, "");
};

// 头像加载失败处理
const handleAvatarError = (event: Event) => {
  const img = event.target as HTMLImageElement;
  img.style.display = "none";
  console.warn("头像加载失败:", img.src);
};

const loadData = async () => {
  const res = await UserControllerService.listUserByPageUsingPost({
    ...searchParams.value,
    sortField: "createTime",
    sortOrder: "descend",
  });
  if (res.code === 0) {
    console.log("用户数据:", res.data.records); // 调试用
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("加载失败，" + res.message);
  }
};

// 监听搜索条件变化
watchEffect(() => {
  loadData();
});

// 页面加载时获取数据
onMounted(() => {
  loadData();
});

// 表格列配置
const columns = [
  {
    title: "ID",
    dataIndex: "id",
    align: "center",
    width: 160,
  },
  {
    title: "账户",
    dataIndex: "userAccount",
    align: "center",
    width: 140,
  },
  {
    title: "昵称",
    slotName: "userName",
    align: "center",
    width: 120,
  },
  {
    title: "头像",
    slotName: "userAvatar",
    align: "center",
    width: 40,
  },
  {
    title: "简介",
    slotName: "userProfile",
    align: "center",
    width: 200,
  },
  {
    title: "角色",
    slotName: "userRole",
    align: "center",
    width: 60,
  },
  {
    title: "创建时间",
    slotName: "createTime",
    align: "center",
    width: 140,
  },
  {
    title: "更新时间",
    slotName: "updateTime",
    align: "center",
    width: 140,
  },
  {
    title: "操作",
    slotName: "optional",
    align: "center",
    width: 100,
  },
];

// 分页变化
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

// 跳转到用户详情
const toUserPage = (user: User) => {
  router.push({
    path: `/view/user/${user.id}`,
  });
};

// 搜索
const doSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
// 删除用户
const doDelete = async (user: User) => {
  const res = await UserControllerService.deleteUserUsingPost({
    id: user.id,
  });
  if (res.code === 0) {
    message.success("删除成功");
    loadData();
  } else {
    message.error("删除失败");
  }
};

const doUpdate = (user: User) => {
  router.push({
    path: "/update/user",
    query: {
      id: user.id,
    },
  });
};
</script>

<style scoped>
#managerUserView {
  max-width: 1580px;
  margin: 0 auto;
}

/* 表格样式 */
:deep(.arco-table-cell) {
  text-align: center;
}

:deep(.arco-table-th) {
  text-align: center;
}

:deep(.arco-table-td) {
  vertical-align: middle;
}

/* 头像样式 */
:deep(.arco-avatar img) {
  object-fit: cover;
}
</style>
