<template>
  <div class="left-sidebar">
    <a-menu
      :selected-keys="selectedKeys"
      :open-keys="openKeys"
      mode="vertical"
      @menu-item-click="handleMenuClick"
      @sub-menu-click="handleSubMenuClick"
    >
      <a-menu-item key="problems">
        <template #icon>
          <icon-list />
        </template>
        题目
      </a-menu-item>
      <a-menu-item key="study-plan">
        <template #icon>
          <icon-calendar />
        </template>
        学习计划
      </a-menu-item>
      <a-sub-menu key="my-lists">
        <template #icon>
          <icon-folder />
        </template>
        <template #title>我的题单</template>
        <a-menu-item key="favorites">
          <template #icon>
            <icon-star :fill="'#ffb400'" />
          </template>
          <span>我的收藏</span>
          <template #suffix>
            <icon-lock v-if="!isLoggedIn" />
          </template>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </div>
</template>

<script setup lang="ts">
/* eslint-disable no-undef */
import { ref, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import message from "@arco-design/web-vue/es/message";
import {
  IconList,
  IconCalendar,
  IconFolder,
  IconStar,
  IconLock,
} from "@arco-design/web-vue/es/icon";

const router = useRouter();
const route = useRoute();
const store = useStore();

const selectedKeys = ref<string[]>([]);
const openKeys = ref<string[]>(["my-lists"]);

const isLoggedIn = computed(() => {
  const user = store.state.user?.loginUser;
  return user && user.userName !== "未登录";
});

const handleMenuClick = (key: string) => {
  if (key === "problems") {
    router.push("/questions");
  } else if (key === "study-plan") {
    // 学习计划功能待实现
    console.log("学习计划");
  } else if (key === "favorites") {
    if (isLoggedIn.value) {
      router.push("/favorites");
    } else {
      message.warning("请先登录");
      router.push("/user/login");
    }
  }
};

const handleSubMenuClick = (key: string) => {
  // 处理子菜单点击
};
</script>

<style scoped>
.left-sidebar {
  width: 100%;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  padding: 8px 0;
}

:deep(.arco-menu) {
  border: none;
  background: transparent;
}

:deep(.arco-menu-item) {
  margin: 4px 8px;
  border-radius: 6px;
}

:deep(.arco-menu-item-selected) {
  background: #f2f3f5;
  color: #1d2129;
}

:deep(.arco-menu-item:hover) {
  background: #f7f8fa;
}

:deep(.arco-sub-menu-title) {
  margin: 4px 8px;
  border-radius: 6px;
}

:deep(.arco-sub-menu-title:hover) {
  background: #f7f8fa;
}
</style>
