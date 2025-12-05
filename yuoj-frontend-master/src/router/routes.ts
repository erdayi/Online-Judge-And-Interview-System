import { User } from "./../../generated/models/User";
import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import UserUpdateMyView from "@/views/user/UserUpdateMyView.vue";
import UserView from "@/views/user/UserView.vue";
import AddUserView from "@/views/user/AddUserView.vue";

import AdminView from "@/views/AdminView.vue";
import NoAuthView from "@/views/NoAuthView.vue";
import ACCESS_ENUM from "@/access/accessEnum";
import AddQuestionView from "@/views/question/AddQuestionView.vue";
import ManageQuestionView from "@/views/question/ManageQuestionView.vue";
import QuestionsView from "@/views/question/QuestionsView.vue";
import QuestionSubmitView from "@/views/question/QuestionSubmitView.vue";
import ViewQuestionView from "@/views/question/ViewQuestionView.vue";
import FavoritesView from "@/views/question/FavoritesView.vue";
import AddMockInterviewView from "@/views/mockInterview/AddMockInterviewView.vue";
import MyMockInterviewListView from "@/views/mockInterview/MyMockInterviewListView.vue";
import MockInterviewChatView from "@/views/mockInterview/MockInterviewChatView.vue";
import ManageMockInterviewView from "@/views/mockInterview/ManageMockInterviewView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "主页",
    component: HomeView,
  },
  {
    path: "/user",
    name: "用户",
    // component: UserLayout,
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: UserLoginView,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterView,
      },
    ],
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/questions",
    name: "浏览题目",
    component: QuestionsView,
  },
  {
    path: "/favorites",
    name: "我的收藏",
    component: FavoritesView,
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
  },
  // 添加模拟面试
  {
    path: "/add/mock-interview",
    name: "添加模拟面试",
    component: AddMockInterviewView,
    meta: { access: ACCESS_ENUM.USER },
  },

  // 我的模拟面试列表
  {
    path: "/mock-interview/my",
    name: "我的模拟面试",
    component: MyMockInterviewListView,
    meta: { access: ACCESS_ENUM.USER },
  },

  // 模拟面试聊天页
  {
    path: "/mock-interview/:id",
    name: "模拟面试详情",
    component: MockInterviewChatView,
    meta: { access: ACCESS_ENUM.USER, hideInMenu: true },
  },
  {
    path: "/question_submit",
    name: "浏览题目提交",
    component: QuestionSubmitView,
  },
  {
    path: "/view/question/:id",
    name: "在线做题",
    component: ViewQuestionView,
    props: true,
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/add/question",
    name: "创建题目",
    component: AddQuestionView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/update/question",
    name: "更新题目",
    component: AddQuestionView,
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/manage/question/",
    name: "管理题目",
    component: ManageQuestionView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/manage/user/",
    name: "管理用户",
    component: UserView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/manage/mock-interview/",
    name: "管理面试",
    component: ManageMockInterviewView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/update/user",
    name: "更新用户",
    component: AddUserView,
    meta: {
      access: ACCESS_ENUM.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/user/update/my",
    name: "个人中心",
    component: UserUpdateMyView,
    props: true,
    meta: {
      access: ACCESS_ENUM.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  // {
  //   path: "/hide",
  //   name: "隐藏页面",
  //   component: HomeView,
  //   meta: {
  //     hideInMenu: true,
  //   },
  // },
  {
    path: "/noAuth",
    name: "无权限",
    component: NoAuthView,
    meta: {
      hideInMenu: true,
    },
  },
  // {
  //   path: "/admin",
  //   name: "管理员可见",
  //   component: AdminView,
  //   meta: {
  //     access: ACCESS_ENUM.ADMIN,
  //   },
  // },
  // {
  //   path: "/about",
  //   name: "关于我的",
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
];
