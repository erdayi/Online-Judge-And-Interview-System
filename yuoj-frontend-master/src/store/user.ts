// initial state
import { StoreOptions } from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import { UserControllerService } from "../../generated";

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      userName: "未登录",
    },
  }),
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // 从远程请求获取登录信息
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("updateUser", res.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
      }
    },
    async logout({ commit }) {
      try {
        // 调用后端退出登录接口，清除服务端 session
        await UserControllerService.userLogoutUsingPost();
        console.log("后端退出登录成功");
      } catch (error) {
        console.error("调用后端退出登录接口失败:", error);
        // 即使后端调用失败，也继续执行前端清理
      } finally {
        // 清除前端用户状态
        commit("updateUser", {
          userName: "未登录",
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
      }
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
