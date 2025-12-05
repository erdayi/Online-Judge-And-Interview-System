package com.yupi.yuoj.controller;

import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.DeleteRequest;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.dto.questioncomment.QuestionCommentAddRequest;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionCommentVO;
import com.yupi.yuoj.service.QuestionCommentService;
import com.yupi.yuoj.service.QuestionCommentThumbService;
import com.yupi.yuoj.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 题目评论接口
 *

 */
@RestController
@RequestMapping("/question_comment")
@Slf4j
public class QuestionCommentController {

    @Resource
    private QuestionCommentService questionCommentService;

    @Resource
    private QuestionCommentThumbService questionCommentThumbService;

    @Resource
    private UserService userService;

    /**
     * 添加评论
     *
     * @param questionCommentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addComment(@RequestBody QuestionCommentAddRequest questionCommentAddRequest,
            HttpServletRequest request) {
        if (questionCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long commentId = questionCommentService.addQuestionComment(questionCommentAddRequest, loginUser);
        return ResultUtils.success(commentId);
    }

    /**
     * 获取评论列表（树形结构）
     *
     * @param questionId
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<QuestionCommentVO>> getCommentList(@RequestParam Long questionId,
            HttpServletRequest request) {
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<QuestionCommentVO> commentList = questionCommentService.getCommentTree(questionId, request);
        return ResultUtils.success(commentList);
    }

    /**
     * 删除评论
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest,
            HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Boolean result = questionCommentService.deleteComment(deleteRequest.getId(), loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 点赞 / 取消点赞
     *
     * @param commentId
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/thumb")
    public BaseResponse<Integer> doThumb(@RequestParam Long commentId, HttpServletRequest request) {
        if (commentId == null || commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        int result = questionCommentThumbService.doCommentThumb(commentId, loginUser);
        return ResultUtils.success(result);
    }
}

