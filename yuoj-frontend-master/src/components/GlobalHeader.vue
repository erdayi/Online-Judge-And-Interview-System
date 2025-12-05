<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="title-bar">
            <img class="logo" src="../assets/oj-logo.svg" />
            <div class="title">鱼 OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <a-dropdown trigger="hover" position="bl">
        <div class="user-info">
          <a-avatar
            :size="32"
            :style="{ backgroundColor: '#3370ff', marginRight: '8px' }"
          >
            <img v-if="userAvatar" alt="avatar" :src="userAvatar" />
            <icon-user v-else />
          </a-avatar>
          <span class="user-name">{{
            store.state.user?.loginUser?.userName ?? "未登录"
          }}</span>
          <icon-down class="dropdown-arrow" />
        </div>
        <template #content>
          <a-doption @click="handleUserCenter">
            <template #icon>
              <icon-user />
            </template>
            <template #default>个人中心</template>
          </a-doption>
          <a-doption @click="showLogoutConfirm" divided>
            <template #icon>
              <icon-poweroff />
            </template>
            <template #default>退出登录</template>
          </a-doption>
        </template>
      </a-dropdown>
    </a-col>
  </a-row>

  <!-- 独立的确认对话框 -->
  <a-modal
    v-model:visible="logoutModalVisible"
    title="确认"
    @ok="confirmLogout"
    @cancel="cancelLogout"
    simple
  >
    <div style="text-align: center">确定要退出登录吗？</div>
    <template #footer>
      <a-button @click="cancelLogout">取消</a-button>
      <a-button type="primary" status="danger" @click="confirmLogout"
        >确定</a-button
      >
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { routes } from "../router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import ACCESS_ENUM from "@/access/accessEnum";
import { Message, Modal } from "@arco-design/web-vue";

const router = useRouter();
const store = useStore();
const logoutModalVisible = ref(false);

// 计算用户头像，如果没有则返回 null（显示默认图标）
const userAvatar = computed(() => {
  const avatar = store.state.user?.loginUser?.userAvatar;
  return avatar && avatar.trim() ? avatar : null;
});

// 展示在菜单的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (
      !checkAccess(store.state.user.loginUser, item?.meta?.access as string)
    ) {
      return false;
    }
    return true;
  });
});

// 默认主页
const selectedKeys = ref(["/"]);

// 路由跳转后，更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

// 模拟登录用户数据
setTimeout(() => {
  store.dispatch("user/getLoginUser", {
    userName: "管理员",
    userRole: ACCESS_ENUM.ADMIN,
  });
}, 3000);

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

// 跳转到个人中心
const handleUserCenter = () => {
  router.push("/user/update/my");
};

// 显示退出确认对话框
const showLogoutConfirm = () => {
  logoutModalVisible.value = true;
};

// 确认退出登录
const confirmLogout = async () => {
  try {
    // 关闭对话框
    logoutModalVisible.value = false;

    // 调用退出登录接口
    await store.dispatch("user/logout");

    // 清除本地存储
    localStorage.removeItem("token");
    sessionStorage.clear();

    // 跳转到登录页
    router.push("/user/login");

    // 显示成功消息
    Message.success("退出登录成功");
  } catch (error) {
    console.error("退出登录失败:", error);
    Message.error("退出登录失败，请重试");
  }
};

// 取消退出登录
const cancelLogout = () => {
  logoutModalVisible.value = false;
  Message.info("已取消退出登录");
};
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: #444;
  margin-left: 16px;
  font-weight: 600;
  font-size: 18px;
}

.logo {
  height: 48px;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.user-name {
  margin: 0 8px;
  font-weight: 500;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-arrow {
  font-size: 12px;
  color: #999;
  transition: transform 0.3s ease;
}

.user-info:hover .dropdown-arrow {
  transform: rotate(180deg);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-name {
    display: none;
  }

  .user-info {
    padding: 8px;
  }
}

#globalHeader {
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: #fff;
}
</style>
