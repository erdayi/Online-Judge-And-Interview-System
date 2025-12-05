<template>
  <div class="user-profile">
    <!-- 个人信息卡片 -->
    <a-card title="个人信息" :bordered="false" class="profile-card">
      <a-form
        :model="form"
        label-align="left"
        auto-label-width
        @submit="handleSubmit"
      >
        <a-form-item label="用户头像">
          <a-upload
            :file-list="fileList"
            list-type="picture-card"
            :limit="1"
            :before-upload="beforeUpload"
            @preview="handlePreview"
            @change="handleAvatarChange"
            :custom-request="customUpload"
            accept="image/*"
          >
            <template #upload-button>
              <div v-if="fileList.length === 0">
                <icon-plus />
                <div style="margin-top: 8px">上传头像</div>
              </div>
            </template>
          </a-upload>
          <a-modal
            v-model:visible="previewVisible"
            :footer="null"
            title="预览头像"
          >
            <img alt="预览" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-form-item>

        <a-form-item label="用户昵称" required>
          <a-input
            v-model="form.userName"
            placeholder="请输入用户昵称"
            :max-length="20"
            show-word-limit
          />
        </a-form-item>

        <a-form-item label="个人简介">
          <a-textarea
            v-model="form.userProfile"
            placeholder="请输入个人简介"
            :rows="4"
            :max-length="200"
            show-word-limit
          />
        </a-form-item>

        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit" :loading="loading">
              保存修改
            </a-button>
            <a-button @click="resetForm">重置</a-button>
            <a-button @click="goBack">返回</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 🗓️ 全年签到日历图 -->
    <a-card
      title="全年签到记录"
      :bordered="false"
      class="profile-card"
      style="margin-top: 24px"
    >
      <div ref="chartRef" style="height: 320px; width: 100%"></div>
      <div
        style="
          text-align: center;
          color: #999;
          font-size: 12px;
          margin-top: 8px;
        "
      >
        深色表示已签到 · 点击可查看日期
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import message from "@arco-design/web-vue/es/message";
import { IconPlus } from "@arco-design/web-vue/es/icon";
import axios from "axios";
import * as echarts from "echarts";
import {
  UserControllerService,
  UserUpdateMyRequest,
  LoginUserVO,
} from "../../../generated";
import { OpenAPI } from "../../../generated/core/OpenAPI";

interface FormState {
  userName: string;
  userAvatar: string;
  userProfile: string;
}

const router = useRouter();
const store = useStore();
const loading = ref(false);
const previewVisible = ref(false);
const previewImage = ref("");
const fileList = ref<any[]>([]);
const uploading = ref(false);

// 表单数据
const form = reactive<FormState>({
  userName: "",
  userAvatar: "",
  userProfile: "",
});

// ECharts 相关
const chartRef = ref<HTMLElement | null>(null);
let myChart: echarts.ECharts | null = null;

// █████████████████████ 初始化用户信息 █████████████████████
const initFormData = async () => {
  try {
    const response = await UserControllerService.getLoginUserUsingGet();
    if (response.code === 0 && response.data) {
      const userInfo = response.data as LoginUserVO;
      form.userName = userInfo.userName || "";
      form.userAvatar = userInfo.userAvatar || "";
      form.userProfile = userInfo.userProfile || "";

      if (userInfo.userAvatar) {
        fileList.value = [
          {
            uid: "-1",
            name: "avatar",
            status: "done",
            url: userInfo.userAvatar,
          },
        ];
      } else {
        fileList.value = [];
      }
    } else {
      message.error("获取用户信息失败");
    }
  } catch (error) {
    console.error("获取用户信息失败:", error);
    message.error("获取用户信息失败");
  }
};

// █████████████████████ 头像相关逻辑 █████████████████████
const handlePreview = async (file: any) => {
  if (!file.url && !file.preview) {
    if (file.originFileObj) {
      file.preview = await getBase64(file.originFileObj);
    }
  }
  previewImage.value = file.url || file.preview || "";
  previewVisible.value = true;
};

const beforeUpload = (file: File): boolean => {
  const fileName = file.name || "";
  const fileSuffix = fileName
    .substring(fileName.lastIndexOf(".") + 1)
    .toLowerCase();
  const allowedSuffixes = ["jpeg", "jpg", "svg", "png", "webp"];
  if (!allowedSuffixes.includes(fileSuffix)) {
    message.error("仅支持 JPG、PNG、SVG、WEBP 格式!");
    return false;
  }

  const ONE_M = 1024 * 1024;
  if (file.size > ONE_M) {
    message.error("文件大小不能超过 1MB!");
    return false;
  }
  return true;
};

const handleAvatarChange = (newFileList: any[]) => {
  fileList.value = newFileList;
  if (newFileList.length === 0) {
    form.userAvatar = "";
  } else {
    const lastFile = newFileList[newFileList.length - 1];
    if (
      lastFile.status === "done" &&
      lastFile.url &&
      !lastFile.url.startsWith("blob:")
    ) {
      form.userAvatar = lastFile.url;
    }
  }
};

const customUpload = async (option: any) => {
  const fileItem = option.fileItem || option;
  const file = fileItem.file || fileItem.originFile || fileItem;

  if (!file || !(file instanceof File)) {
    message.error("文件获取失败");
    option.onError?.("文件获取失败");
    return;
  }

  uploading.value = true;
  const { onSuccess, onError, onProgress } = option;

  try {
    if (onProgress) onProgress(10);

    const formData = new FormData();
    formData.append("biz", "user_avatar");
    formData.append("file", file);

    const config = {
      method: "POST",
      url: `${OpenAPI.BASE}/api/file/upload`,
      data: formData,
      withCredentials: OpenAPI.WITH_CREDENTIALS,
      headers: {},
      onUploadProgress: (progressEvent: any) => {
        if (onProgress && progressEvent.total) {
          const percent = Math.round(
            (progressEvent.loaded * 90) / progressEvent.total
          );
          onProgress(percent);
        }
      },
    };

    const response = await axios.request(config);
    const res = response.data;

    if (onProgress) onProgress(100);

    let avatarUrl: string | null = null;
    if (res && typeof res === "object") {
      if (res.code === 0 && res.data) {
        avatarUrl = res.data;
      } else {
        throw new Error(res.message || "上传失败");
      }
    } else if (typeof res === "string") {
      avatarUrl = res;
    } else {
      throw new Error("响应格式错误");
    }

    if (avatarUrl) {
      form.userAvatar = avatarUrl;
      fileList.value = [
        {
          uid: fileItem.uid || Date.now().toString(),
          name: file.name || "avatar",
          status: "done",
          url: avatarUrl,
        },
      ];
      onSuccess?.(avatarUrl);
      message.success("头像上传成功");
    } else {
      throw new Error("未获取到图片URL");
    }
  } catch (error: any) {
    const msg = error.response?.data?.message || error.message || "上传失败";
    message.error(msg);
    onError?.(msg);
    // 清除临时 blob
    if (
      fileList.value.length > 0 &&
      fileList.value[0].url?.startsWith("blob:")
    ) {
      fileList.value = [];
      form.userAvatar = "";
    }
  } finally {
    uploading.value = false;
  }
};

const getBase64 = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });
};

// █████████████████████ 提交表单 █████████████████████
const handleSubmit = async () => {
  if (!form.userName?.trim()) {
    message.error("请输入用户昵称");
    return;
  }

  loading.value = true;
  try {
    const updateData: UserUpdateMyRequest = {
      userName: form.userName.trim(),
      userAvatar: form.userAvatar,
      userProfile: form.userProfile?.trim() || "",
    };

    const res = await UserControllerService.updateMyUserUsingPost(updateData);
    if (res.code === 0) {
      message.success("个人信息更新成功");
      await store.dispatch("user/getLoginUser");

      // ✅ 关键：如果今天刚签到（比如从题目页跳转过来），也更新 localStorage
      const today = new Date();
      const year = today.getFullYear();
      const dayOfYear = Math.floor(
        (today.getTime() - new Date(year, 0, 0).getTime()) /
          (1000 * 60 * 60 * 24)
      );
      localStorage.setItem(`sign_in_${year}_${dayOfYear}`, "1");
    } else {
      message.error("更新失败：" + res.message);
    }
  } catch (error: any) {
    console.error("更新失败:", error);
    message.error("更新失败，请重试");
  } finally {
    loading.value = false;
  }
};

// █████████████████████ ECharts 日历图 █████████████████████
const loadYearSignInRecord = async (year: number) => {
  try {
    const res = await UserControllerService.getUserSignInRecordUsingPost(year);
    if (res.code === 0 && res.data) {
      renderCalendarChart(year, res.data);
    }
  } catch (error) {
    console.error("加载签到记录失败", error);
  }
};

const renderCalendarChart = (year: number, dayList: number[]) => {
  if (!chartRef.value) return;

  // 转换为 YYYY-MM-DD
  const dates = dayList.map((dayOfYear) => {
    const date = new Date(year, 0);
    date.setDate(dayOfYear);
    return date.toISOString().slice(0, 10);
  });

  const data = dates.map((date) => [date, 1]);

  if (myChart) {
    myChart.dispose();
  }
  myChart = echarts.init(chartRef.value);

  const option = {
    tooltip: {
      formatter: (params: any) => {
        const date = params.name;
        return `<div style="padding: 6px;">
        <div>${date}</div>
        <div style="color: #52c41a; margin-top: 4px;">✅ 已签到</div>
      </div>`;
      },
      backgroundColor: "rgba(0,0,0,0.7)",
      textStyle: { color: "#fff" },
      borderRadius: 4,
    },
    visualMap: {
      show: false, // 不显示图例色块（因为我们只有一种状态）
      min: 0,
      max: 1,
      calculable: true,
      orient: "horizontal",
      left: "center",
      bottom: 20,
      inRange: {
        color: ["#ebedf0", "#52c41a"], // 0: 未签到（浅灰），1: 已签到（绿色）
      },
    },
    calendar: {
      top: 30,
      left: 30,
      right: 20,
      bottom: 70, // 给 visualMap 留点空间（即使隐藏也建议留底边距）
      range: year,
      cellSize: ["auto", 22],
      itemStyle: {
        borderWidth: 1,
        borderColor: "#eee",
        borderRadius: 4,
      },
      dayLabel: {
        nameMap: "en",
        color: "#666",
        fontWeight: "normal",
      },
      monthLabel: {
        color: "#333",
        fontSize: 13,
        fontWeight: "bold",
      },
      yearLabel: { show: false },
    },
    series: {
      type: "heatmap",
      coordinateSystem: "calendar",
      data: data,
      itemStyle: {
        // 注意：实际颜色由 visualMap 控制，这里可不设 color
        borderRadius: 4,
      },
    },
  };
  myChart.setOption(option);
};

// █████████████████████ 其他方法 █████████████████████
const resetForm = () => {
  initFormData();
  message.info("表单已重置");
};

const goBack = () => {
  router.back();
};

// █████████████████████ 生命周期 █████████████████████
onMounted(() => {
  initFormData();
  const currentYear = new Date().getFullYear();
  loadYearSignInRecord(currentYear);
});

onUnmounted(() => {
  if (myChart) {
    myChart.dispose();
    myChart = null;
  }
});
</script>

<style scoped>
.user-profile {
  padding: 24px;
  background: #f5f5f5;
  min-height: calc(100vh - 200px);
}

.profile-card {
  max-width: 800px;
  margin: 0 auto 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.arco-upload-list-picture-card) {
  display: flex;
  flex-wrap: wrap;
}
:deep(.arco-upload-list-picture-card .arco-upload-list-item),
:deep(.arco-upload-list-picture-card .arco-upload-trigger) {
  width: 120px;
  height: 120px;
}
:deep(.arco-upload-list-picture-card .arco-upload-list-item-preview img) {
  object-fit: cover;
}
:deep(.arco-upload-list-picture-card .arco-upload-trigger:hover) {
  border-color: #1677ff;
}

@media (max-width: 768px) {
  .user-profile {
    padding: 16px;
  }
  .profile-card {
    border-radius: 4px;
  }
}
</style>
