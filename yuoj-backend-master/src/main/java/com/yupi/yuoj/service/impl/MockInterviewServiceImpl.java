package com.yupi.yuoj.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.constant.CommonConstant;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.exception.ThrowUtils;
import com.yupi.yuoj.manager.AiManager;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewAddRequest;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewChatMessage;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewEventRequest;
import com.yupi.yuoj.model.dto.mockinterview.MockInterviewQueryRequest;
import com.yupi.yuoj.model.entity.MockInterview;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.enums.MockInterviewEventEnum;
import com.yupi.yuoj.model.enums.MockInterviewStatusEnum;
import com.yupi.yuoj.service.MockInterviewService;
import com.yupi.yuoj.mapper.MockInterviewMapper;
import com.yupi.yuoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 * @description 针对表【mock_interview(模拟面试)】的数据库操作Service实现
 * @createDate 2025-11-20 08:28:53
 */
@Service
public class MockInterviewServiceImpl extends ServiceImpl<MockInterviewMapper, MockInterview>
        implements MockInterviewService {

    @Resource
    AiManager aiManager;

    /**
     * 创建模拟面试
     *
     * @param mockInterviewAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long createMockInterview(MockInterviewAddRequest mockInterviewAddRequest, User loginUser) {
        // 1.参数校验
        if (mockInterviewAddRequest == null || loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String workExperience = mockInterviewAddRequest.getWorkExperience();
        String jobPosition = mockInterviewAddRequest.getJobPosition();
        String difficulty = mockInterviewAddRequest.getDifficulty();
        ThrowUtils.throwIf(StrUtil.hasBlank(workExperience, jobPosition, difficulty), ErrorCode.PARAMS_ERROR, "参数错误");
        // 2.封装插入到数据库中的对象
        MockInterview mockInterview = new MockInterview();
        mockInterview.setWorkExperience(workExperience);
        mockInterview.setJobPosition(jobPosition);
        mockInterview.setDifficulty(difficulty);
        mockInterview.setStatus(MockInterviewStatusEnum.PENDING.getValue());
        mockInterview.setUserId(loginUser.getId());
        // 3.插入数据库（主键回写 插入到数据库中 对应id会自动回填mockInterview对象中）
        boolean result = this.save(mockInterview);
        ThrowUtils.throwIf(!result, ErrorCode.PARAMS_ERROR, "创建失败");
        // 4.返回id
        return mockInterview.getId();
    }

    @Override
    public QueryWrapper<MockInterview> getQueryWrapper(MockInterviewQueryRequest mockInterviewQueryRequest) {
        QueryWrapper<MockInterview> queryWrapper = new QueryWrapper<>();
        if (mockInterviewQueryRequest == null) {
            // 默认按创建时间降序
            queryWrapper.orderByDesc("createTime");
            return queryWrapper;
        }

        Long id = mockInterviewQueryRequest.getId();
        String workExperience = mockInterviewQueryRequest.getWorkExperience();
        String jobPosition = mockInterviewQueryRequest.getJobPosition();
        String difficulty = mockInterviewQueryRequest.getDifficulty();
        Integer status = mockInterviewQueryRequest.getStatus();
        Long userId = mockInterviewQueryRequest.getUserId();
        String sortField = mockInterviewQueryRequest.getSortField();
        String sortOrder = mockInterviewQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(workExperience), "workExperience", workExperience);
        queryWrapper.like(StringUtils.isNotBlank(jobPosition), "jobPosition", jobPosition);
        queryWrapper.like(StringUtils.isNotBlank(difficulty), "difficulty", difficulty);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);

        // 处理排序：如果前端指定了有效排序字段，则使用；否则默认按 createTime 降序
        if (StringUtils.isNotBlank(sortField) && SqlUtils.validSortField(sortField)) {
            if (CommonConstant.SORT_ORDER_ASC.equals(sortOrder)) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        } else {
            // 默认排序：按创建时间降序
            queryWrapper.orderByDesc("createTime");
        }
        return queryWrapper;
    }

    /**
     * 处理模拟面试事件
     *
     * @param mockInterviewEventRequest
     * @param loginUser
     * @return
     */
    @Override
    public String handleMockInterview(MockInterviewEventRequest mockInterviewEventRequest, User loginUser) {
        Long id = mockInterviewEventRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，房间不存在");
        }
        MockInterview mockInterview = this.getById(id);
        ThrowUtils.throwIf(mockInterview == null, ErrorCode.PARAMS_ERROR, "参数错误，模拟面试未创建");
        // 如果不是本人创建的模拟面试 直接报错
        if (!mockInterview.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"暂无权限查看其他人面试房间");
        }
        String event = mockInterviewEventRequest.getEvent();
        MockInterviewEventEnum eventEnum = MockInterviewEventEnum.getEnumByValue(event);

        // 区分事件
        switch (eventEnum) {
            case START:
                return handleChatStartEvent(mockInterview);

            case CHAT:
                return handleChatEvent(mockInterviewEventRequest, mockInterview);

            case END:
                return handleChatEndEvent(mockInterview);

            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");

        }
    }

    /**
     * 处理 AI 对话开始事件
     *
     * @param mockInterview
     * @return
     */
    private String handleChatStartEvent(MockInterview mockInterview) {
        // -- 处理开始事件
        // 用户进入模拟面试 发送开始事件 修改模拟面试状态为已开始 AI 需要给出对应的回复
        // 定义 AI 的prompt
        String systemPrompt = String.format("你是一位严厉的程序员面试官，我是候选人，来应聘 %s 的 %s 岗位，面试难度为 %s。请你向我依次提出问题（最多 20 个问题），我也会依次回复。在这期间请完全保持真人面试官的口吻，比如适当引导学员、或者表达出你对学员回答的态度。\n" +
                "必须满足如下要求：\n" +
                "1. 当学员回复 “开始” 时，你要正式开始面试\n" +
                "2. 当学员表示希望 “结束面试” 时，你要结束面试\n" +
                "3. 此外，当你觉得这场面试可以结束时（比如候选人回答结果较差、不满足工作年限的招聘需求、或者候选人态度不礼貌），必须主动提出面试结束，不用继续询问更多问题了。并且要在回复中包含字符串【面试结束】\n" +
                "4. 面试结束后，应该给出候选人整场面试的表现和总结。\n", mockInterview.getWorkExperience(), mockInterview.getJobPosition(), mockInterview.getDifficulty());

        // 构造消息列表
        String userPrompt = "开始";
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();
        messages.add(systemMessage);
        messages.add(userMessage);
        // 调用 AI 获取结果
        String answer = aiManager.doChat(messages);
        ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(answer).build();
        messages.add(assistantMessage);
        // 保存消息记录 更新状态
        List<MockInterviewChatMessage> chatMessageList = transformFromChatMessage(messages);
        String jsonStr = JSONUtil.toJsonStr(chatMessageList);
        // 操作数据库进行更新
        MockInterview updateMockInterview = new MockInterview();
        updateMockInterview.setStatus(MockInterviewStatusEnum.IN_PROGRESS.getValue());
        updateMockInterview.setId(mockInterview.getId());
        updateMockInterview.setMessages(jsonStr);
        boolean result = this.updateById(updateMockInterview);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return answer;
    }

    /**
     * 处理 AI 对话事件
     *
     * @param mockInterviewEventRequest
     * @param mockInterview
     * @return
     */
    private String handleChatEvent(MockInterviewEventRequest mockInterviewEventRequest, MockInterview mockInterview) {
        // -- 处理对话事件
        // 用户可以和 AI 面试官发送消息 发送消息事件 携带上要发送的消息内容 AI需要给出对应的回复

        // 构造消息列表 获取之前的历史记录
        String message = mockInterviewEventRequest.getMessage();
        String historyMessage = mockInterview.getMessages();
        List<MockInterviewChatMessage> historyMessageList = JSONUtil.parseArray(historyMessage).toList(MockInterviewChatMessage.class);
        final List<ChatMessage> chatMessages = transformToChatMessage(historyMessageList);
        final ChatMessage chatUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();
        chatMessages.add(chatUserMessage);

        // 调用 AI 获取结果
        String chatAnswer = aiManager.doChat(chatMessages);
        ChatMessage chatAssistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(chatAnswer).build();
        chatMessages.add(chatAssistantMessage);
        // 保存消息记录 更新状态
        List<MockInterviewChatMessage> mockInterviewChatMessage = transformFromChatMessage(chatMessages);
        String newJsonStr = JSONUtil.toJsonStr(mockInterviewChatMessage);
        MockInterview newUpdateMockInterview = new MockInterview();
        // 如果 AI 主动结束面试 更改状态
        if (chatAnswer.contains("【面试结束】")) {
            newUpdateMockInterview.setStatus(MockInterviewStatusEnum.FINISHED.getValue());
        }
        newUpdateMockInterview.setId(mockInterview.getId());
        newUpdateMockInterview.setMessages(newJsonStr);
        boolean newResult = this.updateById(newUpdateMockInterview);
        ThrowUtils.throwIf(!newResult, ErrorCode.SYSTEM_ERROR, "更新失败");
        return chatAnswer;
    }

    /**
     * 处理 AI 对话结束事件
     *
     * @param mockInterview
     * @return
     */
    private String handleChatEndEvent(MockInterview mockInterview) {
        // -- 处理结束事件
        // 退出模拟面试 发送退出事件 AI 给出面试的复盘总结 修改状态为已结束

        // 构造消息列表 获取之前的历史记录
        String historyMessage = mockInterview.getMessages();
        List<MockInterviewChatMessage> historyMessageList = JSONUtil.parseArray(historyMessage).toList(MockInterviewChatMessage.class);
        final List<ChatMessage> chatMessages = transformToChatMessage(historyMessageList);
        // 构造用户结束消息
        String endUserPrompt = "结束";
        final ChatMessage endUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(endUserPrompt).build();
        chatMessages.add(endUserMessage);

        // 调用 AI 获取结果
        String endAnswer = aiManager.doChat(chatMessages);
        ChatMessage endAssistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(endAnswer).build();
        chatMessages.add(endAssistantMessage);

        // 保存消息记录 更新状态
        List<MockInterviewChatMessage> mockInterviewChatMessage = transformFromChatMessage(chatMessages);
        String newJsonStr = JSONUtil.toJsonStr(mockInterviewChatMessage);
        MockInterview newUpdateMockInterview = new MockInterview();
        newUpdateMockInterview.setStatus(MockInterviewStatusEnum.FINISHED.getValue());
        newUpdateMockInterview.setId(mockInterview.getId());
        newUpdateMockInterview.setMessages(newJsonStr);
        boolean newResult = this.updateById(newUpdateMockInterview);
        ThrowUtils.throwIf(!newResult, ErrorCode.SYSTEM_ERROR, "更新失败");
        return endAnswer;
    }

    /**
     * 消息记录对象转换
     *
     * @param chatMessageList
     * @return
     */
    List<MockInterviewChatMessage> transformFromChatMessage(List<ChatMessage> chatMessageList) {
        return chatMessageList.stream().map(chatMessage -> {
            MockInterviewChatMessage mockInterviewChatMessage = new MockInterviewChatMessage();
            mockInterviewChatMessage.setRole(chatMessage.getRole().value());
            mockInterviewChatMessage.setMessage(chatMessage.getContent().toString());
            return mockInterviewChatMessage;
        }).collect(Collectors.toList());
    }

    /**
     * 消息记录对象转换
     *
     * @param chatMessageList
     * @return
     */
    List<ChatMessage> transformToChatMessage(List<MockInterviewChatMessage> chatMessageList) {
        return chatMessageList.stream().map(chatMessage -> {
            ChatMessage tempChatMessage = ChatMessage.builder().role(ChatMessageRole.valueOf(StringUtils.upperCase(chatMessage.getRole()))).content(chatMessage.getMessage()).build();
            return tempChatMessage;
        }).collect(Collectors.toList());
    }
}




