package com.yupi.yuoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yuoj.annotation.AuthCheck;
import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.DeleteRequest;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.constant.UserConstant;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.exception.ThrowUtils;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewAddRequest;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewEventRequest;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewQueryRequest;
import com.yupi.yuoj.model.entity.MockInterview;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.service.MockInterviewService;
import com.yupi.yuoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 模拟面试接口
 *
 * @author admin
 */
@RestController
@RequestMapping("/mockInterview")
@Slf4j
public class MockInterviewController {

    @Resource
    private MockInterviewService mockInterviewService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建模拟面试
     *
     * @param mockInterviewAddRequest 模拟面试添加请求
     * @param request                 请求对象
     * @return 新创建的模拟面试ID
     */
    @PostMapping("/add")
    public BaseResponse<Long> addMockInterview(@RequestBody MockInterviewAddRequest mockInterviewAddRequest, HttpServletRequest request) {
        if (mockInterviewAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long mockInterviewId = mockInterviewService.createMockInterview(mockInterviewAddRequest, loginUser);
        return ResultUtils.success(mockInterviewId);
    }

    /**
     * 删除模拟面试
     *
     * @param deleteRequest 删除请求
     * @param request       请求对象
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMockInterview(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 检查是否存在
        MockInterview mockInterview = mockInterviewService.getById(id);
        ThrowUtils.throwIf(mockInterview == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!mockInterview.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = mockInterviewService.removeById(id);
        return ResultUtils.success(result);
    }


    /**
     * 根据ID获取模拟面试
     *
     * @param id      模拟面试ID
     * @param request 请求对象
     * @return 模拟面试信息
     */
    @GetMapping("/get")
    public BaseResponse<MockInterview> getMockInterviewById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        MockInterview mockInterview = mockInterviewService.getById(id);
        ThrowUtils.throwIf(mockInterview == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(mockInterview);
    }


    /**
     * 分页获取当前用户的模拟面试列表
     *
     * @param mockInterviewQueryRequest 模拟面试查询请求
     * @param request                   请求对象
     * @return 分页的模拟面试VO列表
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<MockInterview>> listMyMockInterviewVOByPage(@RequestBody MockInterviewQueryRequest mockInterviewQueryRequest,
                                                                         HttpServletRequest request) {
        ThrowUtils.throwIf(mockInterviewQueryRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        mockInterviewQueryRequest.setUserId(loginUser.getId());
        long current = mockInterviewQueryRequest.getCurrent();
        long size = mockInterviewQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MockInterview> mockInterviewPage = mockInterviewService.page(
                new Page<>(current, size),
                mockInterviewService.getQueryWrapper(mockInterviewQueryRequest)
        );
        return ResultUtils.success(mockInterviewPage);
    }

    /**
     * 分页获取模拟面试列表（仅管理员）
     *
     * @param mockInterviewQueryRequest 模拟面试查询请求
     * @param request                   请求对象
     * @return 分页的模拟面试列表
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<MockInterview>> listMockInterviewByPage(@RequestBody MockInterviewQueryRequest mockInterviewQueryRequest,
                                                                     HttpServletRequest request) {
        long current = mockInterviewQueryRequest.getCurrent();
        long size = mockInterviewQueryRequest.getPageSize();
        Page<MockInterview> mockInterviewPage = mockInterviewService.page(
                new Page<>(current, size),
                mockInterviewService.getQueryWrapper(mockInterviewQueryRequest)
        );
        return ResultUtils.success(mockInterviewPage);
    }

    /**
     * 处理模拟面试事件
     *
     * @param mockInterviewEventRequest 模拟面试查询请求
     * @param request                   请求对象
     * @return 分页的模拟面试列表
     */
    @PostMapping("/handleEvent")
    public BaseResponse<String> handleMockInterviewEvent(@RequestBody MockInterviewEventRequest mockInterviewEventRequest,
                                                         HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 调用 Service 处理模拟面试事件
        String aiResponse = mockInterviewService.handleMockInterview(mockInterviewEventRequest, loginUser);
        // 返回 AI 的回复
        return ResultUtils.success(aiResponse);
    }

}