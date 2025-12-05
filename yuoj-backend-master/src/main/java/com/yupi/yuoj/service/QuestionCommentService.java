package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.dto.questioncomment.QuestionCommentAddRequest;
import com.yupi.yuoj.model.entity.QuestionComment;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionCommentVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目评论服务
 *

 */
public interface QuestionCommentService extends IService<QuestionComment> {

    /**
     * 添加评论
     *
     * @param questionCommentAddRequest
     * @param loginUser
     * @return
     */
    Long addQuestionComment(QuestionCommentAddRequest questionCommentAddRequest, User loginUser);

    /**
     * 获取评论列表（树形结构）
     *
     * @param questionId
     * @param request
     * @return
     */
    List<QuestionCommentVO> getCommentTree(Long questionId, HttpServletRequest request);

    /**
     * 删除评论
     *
     * @param id
     * @param loginUser
     * @return
     */
    Boolean deleteComment(Long id, User loginUser);
}

