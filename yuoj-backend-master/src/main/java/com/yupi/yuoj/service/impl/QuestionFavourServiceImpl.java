package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.mapper.QuestionFavourMapper;
import com.yupi.yuoj.mapper.QuestionMapper;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionFavour;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.service.QuestionFavourService;
import com.yupi.yuoj.service.QuestionService;
import javax.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题目收藏服务实现
 *

 */
@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour>
        implements QuestionFavourService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 题目收藏
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    @Override
    public int doQuestionFavour(long questionId, User loginUser) {
        // 判断是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已收藏
        long userId = loginUser.getId();
        // 每个用户串行收藏
        QuestionFavourService questionFavourService = (QuestionFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionFavourService.doQuestionFavourInner(userId, questionId);
        }
    }

    @Override
    public Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
            long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourQuestionByPage((Page<Question>) page, favourUserId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionFavourInner(long userId, long questionId) {
        QuestionFavour questionFavour = new QuestionFavour();
        questionFavour.setUserId(userId);
        questionFavour.setQuestionId(questionId);
        QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>(questionFavour);
        QuestionFavour oldQuestionFavour = this.getOne(questionFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldQuestionFavour != null) {
            result = this.remove(questionFavourQueryWrapper);
            if (result) {
                // 题目收藏数 - 1
                Question question = questionMapper.selectById(questionId);
                if (question != null && question.getFavourNum() > 0) {
                    question.setFavourNum(question.getFavourNum() - 1);
                    result = questionMapper.updateById(question) > 0;
                }
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未收藏
            result = this.save(questionFavour);
            if (result) {
                // 题目收藏数 + 1
                Question question = questionMapper.selectById(questionId);
                if (question != null) {
                    question.setFavourNum((question.getFavourNum() == null ? 0 : question.getFavourNum()) + 1);
                    result = questionMapper.updateById(question) > 0;
                }
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}

