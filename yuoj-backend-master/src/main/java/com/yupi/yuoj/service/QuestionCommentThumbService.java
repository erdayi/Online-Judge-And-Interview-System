package com.yupi.yuoj.service;

import com.yupi.yuoj.model.entity.QuestionCommentThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.User;

/**
 * 题目评论点赞服务
 *

 */
public interface QuestionCommentThumbService extends IService<QuestionCommentThumb> {

    /**
     * 点赞
     *
     * @param commentId
     * @param loginUser
     * @return
     */
    int doCommentThumb(long commentId, User loginUser);

    /**
     * 评论点赞（内部服务）
     *
     * @param userId
     * @param commentId
     * @return
     */
    int doCommentThumbInner(long userId, long commentId);
}

