package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.mapper.QuestionCommentMapper;
import com.yupi.yuoj.mapper.QuestionCommentThumbMapper;
import com.yupi.yuoj.model.entity.QuestionComment;
import com.yupi.yuoj.model.entity.QuestionCommentThumb;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.service.QuestionCommentThumbService;
import javax.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 题目评论点赞服务实现
 *

 */
@Service
public class QuestionCommentThumbServiceImpl extends ServiceImpl<QuestionCommentThumbMapper, QuestionCommentThumb>
        implements QuestionCommentThumbService {

    @Resource
    private QuestionCommentMapper questionCommentMapper;

    /**
     * 点赞
     *
     * @param commentId
     * @param loginUser
     * @return
     */
    @Override
    public int doCommentThumb(long commentId, User loginUser) {
        // 判断评论是否存在
        QuestionComment comment = questionCommentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        // 每个用户串行点赞
        QuestionCommentThumbService commentThumbService = (QuestionCommentThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(loginUser.getId()).intern()) {
            return commentThumbService.doCommentThumbInner(loginUser.getId(), commentId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param commentId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doCommentThumbInner(long userId, long commentId) {
        QuestionCommentThumb commentThumb = new QuestionCommentThumb();
        commentThumb.setUserId(userId);
        commentThumb.setCommentId(commentId);
        QueryWrapper<QuestionCommentThumb> thumbQueryWrapper = new QueryWrapper<>(commentThumb);
        QuestionCommentThumb oldCommentThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldCommentThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                QuestionComment comment = questionCommentMapper.selectById(commentId);
                if (comment != null && comment.getThumbNum() > 0) {
                    comment.setThumbNum(comment.getThumbNum() - 1);
                    result = questionCommentMapper.updateById(comment) > 0;
                } else {
                    result = false;
                }
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(commentThumb);
            if (result) {
                // 点赞数 + 1
                QuestionComment comment = questionCommentMapper.selectById(commentId);
                if (comment != null) {
                    comment.setThumbNum(comment.getThumbNum() + 1);
                    result = questionCommentMapper.updateById(comment) > 0;
                }
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}

