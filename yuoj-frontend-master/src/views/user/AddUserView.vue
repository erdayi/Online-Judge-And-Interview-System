<template>
  <div id="addUserView">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <icon-edit v-if="!updatePage" />
          <icon-settings v-else />
          {{ updatePage ? "更新用户" : "创建用户" }}
        </h1>
        <p class="page-subtitle">
          {{ updatePage ? "修改用户信息" : "填写用户信息，创建新的用户" }}
        </p>
      </div>
    </div>

    <!-- 表单区域 -->
    <a-card class="form-card">
      <a-form
        :model="form"
        label-align="left"
        :label-col-props="{ span: 6 }"
        :wrapper-col-props="{ span: 18 }"
      >
        <!-- 基本信息 -->
        <div class="form-section">
          <h3 class="section-title">
            <icon-info-circle />
            基本信息
          </h3>
          <a-form-item field="userAccount" label="用户账号">
            <a-input
              v-model="form.userAccount"
              placeholder="请输入用户账号"
              :max-length="40"
              show-word-limit
              size="large"
            />
          </a-form-item>
          <a-form-item field="userName" label="用户名称">
            <a-input
              v-model="form.userName"
              placeholder="请输入用户名称"
              :max-length="40"
              show-word-limit
              size="large"
            />
          </a-form-item>
          <a-form-item field="userAvatar" label="用户头像">
            <div class="avatar-upload-container">
              <!-- 核心：复用个人信息编辑的 Arco Upload 组件逻辑 -->
              <a-upload
                :file-list="fileList"
                list-type="picture-card"
                :limit="1"
                :before-upload="beforeUpload"
                @preview="handlePreview"
                @change="handleAvatarChange"
                :custom-request="customUpload"
                accept="image/*"
                class="avatar-upload"
              >
                <template #upload-button>
                  <div v-if="fileList.length === 0">
                    <icon-plus />
                    <div style="margin-top: 8px">上传头像</div>
                  </div>
                </template>
              </a-upload>

              <!-- 头像预览弹窗 -->
              <a-modal
                v-model:visible="previewVisible"
                :footer="null"
                title="预览头像"
              >
                <img alt="预览" style="width: 100%" :src="previewImage" />
              </a-modal>

              <!-- 上传提示 -->
              <div class="upload-tips">
                <p>• 支持 JPG、PNG、SVG、WEBP 格式</p>
                <p>• 文件大小不超过 1MB（与个人信息保持一致）</p>
                <p>• 建议尺寸：200×200 像素</p>
              </div>
            </div>
          </a-form-item>
          <a-form-item field="userProfile" label="用户简介">
            <a-textarea
              v-model="form.userProfile"
              placeholder="请输入用户简介"
              :max-length="200"
              show-word-limit
              :auto-size="{ minRows: 3, maxRows: 5 }"
            />
          </a-form-item>
          <a-form-item field="userRole" label="用户角色">
            <a-select
              v-model="form.userRole"
              placeholder="请选择用户角色"
              allow-clear
            >
              <a-option value="user">普通用户</a-option>
              <a-option value="admin">管理员</a-option>
            </a-select>
          </a-form-item>
        </div>

        <!-- 提交按钮 -->
        <div class="form-actions">
          <a-button size="large" @click="handleCancel">取消</a-button>
          <a-button
            type="primary"
            size="large"
            :loading="submitting || uploading"
            @click="doSubmit"
            :disabled="!isFormValid"
          >
            <template #icon>
              <icon-check v-if="!submitting" />
            </template>
            {{ updatePage ? "更新用户" : "创建用户" }}
          </a-button>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { UserControllerService } from "../../../generated";
import { OpenAPI } from "../../../generated/core/OpenAPI";
import message from "@arco-design/web-vue/es/message";
import axios from "axios";
import {
  IconEdit,
  IconSettings,
  IconInfoCircle,
  IconCheck,
  IconPlus,
  IconEye,
} from "@arco-design/web-vue/es/icon";

const route = useRoute();
const router = useRouter();
const updatePage = route.path.includes("update");
const submitting = ref(false);
const uploading = ref(false);
const previewVisible = ref(false);
const previewImage = ref("");
const fileList = ref<any[]>([]);

// 表单数据（简化URL处理，仅存储服务器返回的正式URL）
const form = ref({
  userAccount: "paidaxing",
  userName: "派大星",
  userProfile: "憨厚懒惰，海绵宝宝好友",
  userAvatar: "",
  userRole: "user",
});

// 计算属性：表单有效性（复用个人信息的简洁判断）
const isFormValid = computed(() => {
  return (
    form.value.userAccount?.trim() &&
    form.value.userName?.trim() &&
    (form.value.userAvatar || !updatePage)
  );
});

// 初始化数据：加载用户信息时同步文件列表
const loadData = async () => {
  const id = route.query.id;
  if (!id) return;
  try {
    const res = await UserControllerService.getUserByIdUsingGet(id as any);
    if (res.code === 0) {
      const userData = res.data || {};
      form.value = {
        userAccount: userData.userAccount || "",
        userName: userData.userName || "",
        userProfile: userData.userProfile || "",
        userAvatar: userData.userAvatar || "",
        userRole: userData.userRole || "user",
      };
      // 关键：同步已有头像到文件列表
      if (form.value.userAvatar) {
        fileList.value = [
          {
            uid: "-1",
            name: "avatar",
            status: "done",
            url: form.value.userAvatar,
          },
        ];
      }
      message.success("用户数据加载成功");
    } else {
      message.error("加载失败，" + res.message);
    }
  } catch (error) {
    message.error("加载用户数据失败");
  }
};

// 头像预览（复用个人信息逻辑）
const handlePreview = async (file: any) => {
  if (!file.url && !file.preview) {
    if (file.originFileObj) {
      file.preview = await getBase64(file.originFileObj);
    }
  }
  previewImage.value = file.url || file.preview || "";
  previewVisible.value = true;
};

// 上传前验证（与个人信息保持一致：1MB限制+统一格式）
const beforeUpload = (file: File): boolean => {
  const fileName = file.name || "";
  const fileSuffix = fileName
    .substring(fileName.lastIndexOf(".") + 1)
    .toLowerCase();
  const allowedSuffixes = ["jpeg", "jpg", "svg", "png", "webp"];

  if (!allowedSuffixes.includes(fileSuffix)) {
    message.error("文件类型错误，仅支持 JPG、PNG、SVG、WEBP 格式!");
    return false;
  }

  const ONE_M = 1024 * 1024;
  if (file.size > ONE_M) {
    message.error("文件大小不能超过 1MB!");
    return false;
  }
  return true;
};

// 头像变化处理（同步文件列表与表单头像URL）
const handleAvatarChange = (newFileList: any[]) => {
  fileList.value = newFileList;
  // 清空头像：文件列表为空时重置URL
  if (newFileList.length === 0) {
    form.value.userAvatar = "";
  } else {
    const lastFile = newFileList[newFileList.length - 1];
    // 仅使用服务器返回的正式URL（排除blob临时地址）
    if (
      lastFile.status === "done" &&
      lastFile.url &&
      !lastFile.url.startsWith("blob:")
    ) {
      form.value.userAvatar = lastFile.url;
    }
  }
};

// 自定义上传（复用个人信息的稳定逻辑，移除多端点重试）
const customUpload = async (option: any) => {
  const fileItem = option.fileItem || option;
  const file = fileItem.file || fileItem.originFile || fileItem;
  if (!file || !(file instanceof File)) {
    const errorMsg = "文件获取失败，请重试";
    message.error(errorMsg);
    option.onError?.(errorMsg);
    return;
  }

  uploading.value = true;
  const { onSuccess, onError, onProgress } = option;

  try {
    const formData = new FormData();
    formData.append("biz", "user_avatar");
    formData.append("file", file);
    // 仅更新页面需要传递userId
    if (updatePage && route.query.id) {
      formData.append("userId", route.query.id.toString());
    }

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
    if (res.code === 0 && res.data) {
      const avatarUrl = res.data;
      // 同步文件列表状态
      fileList.value = [
        {
          uid: fileItem.uid || Date.now().toString(),
          name: file.name || "avatar",
          status: "done",
          url: avatarUrl,
        },
      ];
      form.value.userAvatar = avatarUrl;
      onSuccess?.(avatarUrl);
      message.success("头像上传成功");
    } else {
      throw new Error(res.message || `上传失败（错误码: ${res.code}）`);
    }
  } catch (error: any) {
    console.error("上传异常:", error);
    let errorMessage = "上传失败，请重试";
    if (error.response) {
      const responseData = error.response.data;
      errorMessage =
        responseData?.message || `上传失败：${error.response.status}`;
    } else if (error.request) {
      errorMessage = "上传失败：网络错误，请检查网络连接";
    } else {
      errorMessage = error.message || "上传失败，请重试";
    }
    message.error(errorMessage);
    onError?.(errorMessage);
    // 上传失败清除无效文件项
    fileList.value = [];
    form.value.userAvatar = "";
  } finally {
    uploading.value = false;
  }
};

// 获取base64（用于预览）
const getBase64 = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });
};

// 表单提交（简化URL处理，直接使用表单存储的正式URL）
const doSubmit = async () => {
  if (!isFormValid.value) {
    message.warning("请填写完整的用户信息");
    return;
  }

  submitting.value = true;
  try {
    const submitData = {
      ...form.value,
      // 无需额外处理URL，表单中已存储服务器返回的有效地址
    };

    if (updatePage) {
      const res = await UserControllerService.updateUserUsingPost({
        ...submitData,
        id: route.query.id,
      });
      if (res.code === 0) {
        message.success("用户更新成功");
        router.push("/manage/user");
      } else {
        message.error("更新失败，" + res.message);
      }
    } else {
      const res = await UserControllerService.addUserUsingPost(submitData);
      if (res.code === 0) {
        message.success("用户创建成功");
        router.push("/user/manage");
      } else {
        message.error("创建失败，" + res.message);
      }
    }
  } catch (error: any) {
    console.error("提交失败:", error);
    message.error("提交失败，请重试");
  } finally {
    submitting.value = false;
  }
};

const handleCancel = () => {
  router.back();
};

onMounted(() => {
  if (updatePage) {
    loadData();
  } else if (form.value.userAvatar) {
    // 新用户预设头像同步到文件列表
    fileList.value = [
      {
        uid: "-1",
        name: "avatar",
        status: "done",
        url: form.value.userAvatar,
      },
    ];
  }
});
</script>

<style scoped>
/* 样式简化，复用个人信息的头像上传样式 */
.avatar-upload-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.avatar-upload {
  display: inline-block;
}

:deep(.arco-upload-list-picture-card .arco-upload-list-item) {
  width: 120px;
  height: 120px;
  margin-right: 8px;
}

:deep(.arco-upload-list-picture-card .arco-upload-trigger) {
  width: 120px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

:deep(.arco-upload-list-picture-card .arco-upload-trigger:hover) {
  border-color: #1677ff;
}

.upload-tips {
  font-size: 12px;
  color: #666;
  line-height: 1.8;
}

.form-actions {
  margin-top: 24px;
  display: flex;
  gap: 16px;
  justify-content: flex-end;
}

/* 保留原有基础样式 */
.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 14px;
  color: #666;
}

.form-card {
  max-width: 960px;
  margin: 0 auto;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.form-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 页面标题区域 */
.page-header {
  background: #f7f8fa;
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 24px;
  border: 1px solid #e5e7eb;
}

.header-content {
  text-align: center;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #1d2129;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.page-subtitle {
  font-size: 16px;
  color: #86909c;
  margin: 0;
}

#addUserView {
  max-width: 960px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>
