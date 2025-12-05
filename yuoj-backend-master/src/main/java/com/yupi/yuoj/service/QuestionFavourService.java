package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionFavour;
import com.yupi.yuoj.model.entity.User;

/**
 * 题目收藏服务
 *

 */
public interface QuestionFavourService extends IService<QuestionFavour> {

    /**
     * 题目收藏
     *
     * @param questionId
     * @param loginUser
     * @return resultNum 收藏变化数
     */
    int doQuestionFavour(long questionId, User loginUser);

    /**
     * 分页获取用户收藏的题目
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
            long favourUserId);

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionFavourInner(long userId, long questionId);
}

