<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <a-row :gutter="24">
          <!-- 左侧导航栏：左右两侧统一宽度（占 2 份） -->
          <a-col :xs="0" :sm="0" :md="3" :lg="3" :xl="3">
            <div class="left-sidebar">
              <LeftSidebar />
            </div>
          </a-col>

          <!-- 中间主内容区：占剩余宽度（24 - 2 - 2 = 20 份） -->
          <a-col :xs="24" :sm="24" :lg="18" :xl="18">
            <div class="main-content">
              <router-view />
            </div>
          </a-col>

          <!-- 右侧打卡栏：与左侧等宽（占 2 份） -->
          <a-col :xs="0" :sm="0" :md="3" :lg="3" :xl="3">
            <div class="right-sidebar">
              <DailyCheckIn />
            </div>
          </a-col>
        </a-row>
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="" target="_blank"> OJ在线判题面试系统 </a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#basicLayout .header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}

#basicLayout .content {
  background: linear-gradient(to right, #fefefe, #fff);
  margin-bottom: 16px;
  padding: 20px;
}

.left-sidebar {
  position: sticky;
  top: 20px;
  height: fit-content;
}

.main-content {
  min-height: calc(100vh - 200px);
}

.right-sidebar {
  position: sticky;
  top: 20px;
  height: fit-content;
}

#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 368px) {
  .left-sidebar,
  .right-sidebar {
    position: static;
    margin-top: 24px;
  }
}
</style>
<script>
import GlobalHeader from "@/components/GlobalHeader";
import DailyCheckIn from "@/components/DailyCheckIn.vue";
import LeftSidebar from "@/components/LeftSidebar.vue";

export default {
  components: { GlobalHeader, DailyCheckIn, LeftSidebar },
};
</script>
