package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewAddRequest;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewEventRequest;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewQueryRequest;
import com.yupi.yuoj.model.dto.question.QuestionQueryRequest;
import com.yupi.yuoj.model.entity.MockInterview;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.User;

/**
 * @author admin
 * @description 针对表【mock_interview(模拟面试)】的数据库操作Service
 * @createDate 2025-11-20 08:28:53
 */
public interface MockInterviewService extends IService<MockInterview> {

    /**
     * 创建模拟面试
     *
     * @param mockInterviewAddRequest
     * @param loginUser
     * @return
     */
    long createMockInterview(MockInterviewAddRequest mockInterviewAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param mockInterviewQueryRequest
     * @return
     */
    QueryWrapper<MockInterview> getQueryWrapper(MockInterviewQueryRequest mockInterviewQueryRequest);

    /**
     * 处理模拟面试事件
     * @param mockInterviewEventRequest
     * @param loginUser
     * @return AI 给出的回复
     */
    String handleMockInterview(MockInterviewEventRequest mockInterviewEventRequest, User loginUser);

}
