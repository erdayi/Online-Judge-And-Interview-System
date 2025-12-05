package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.exception.ThrowUtils;
import com.yupi.yuoj.mapper.QuestionCommentMapper;
import com.yupi.yuoj.mapper.QuestionCommentThumbMapper;
import com.yupi.yuoj.model.dto.questioncomment.QuestionCommentAddRequest;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionComment;
import com.yupi.yuoj.model.entity.QuestionCommentThumb;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionCommentVO;
import com.yupi.yuoj.model.vo.UserVO;
import com.yupi.yuoj.service.QuestionCommentService;
import com.yupi.yuoj.service.QuestionCommentThumbService;
import com.yupi.yuoj.service.QuestionService;
import com.yupi.yuoj.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

/**
 * 题目评论服务实现
 *

 */
@Service
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment>
        implements QuestionCommentService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionCommentThumbService questionCommentThumbService;

    @Resource
    private QuestionCommentThumbMapper questionCommentThumbMapper;

    @Override
    public Long addQuestionComment(QuestionCommentAddRequest questionCommentAddRequest, User loginUser) {
        if (questionCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long questionId = questionCommentAddRequest.getQuestionId();
        String content = questionCommentAddRequest.getContent();
        Long parentId = questionCommentAddRequest.getParentId();
        Long replyUserId = questionCommentAddRequest.getReplyUserId();

        // 校验题目是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 校验内容 防止用户输入“只有空白字符”的内容
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论内容不能为空");
        }
        if (content.length() > 2000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论内容过长");
        }

        // 如果是回复，校验父评论是否存在
        if (parentId != null && parentId > 0) {
            QuestionComment parentComment = this.getById(parentId);
            if (parentComment == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "父评论不存在");
            }
            if (!parentComment.getQuestionId().equals(questionId)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "父评论不属于该题目");
            }
        }

        QuestionComment questionComment = new QuestionComment();
        questionComment.setQuestionId(questionId);
        questionComment.setContent(content.trim());
        questionComment.setParentId(parentId == null || parentId <= 0 ? 0L : parentId);
        questionComment.setReplyUserId(replyUserId == null || replyUserId <= 0 ? 0L : replyUserId);
        questionComment.setUserId(loginUser.getId());
        questionComment.setThumbNum(0);

        boolean result = this.save(questionComment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return questionComment.getId();
    }

    @Override
    public List<QuestionCommentVO> getCommentTree(Long questionId, HttpServletRequest request) {
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 查询所有评论
        QueryWrapper<QuestionComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId", questionId);
        queryWrapper.orderByAsc("createTime");
        List<QuestionComment> commentList = this.list(queryWrapper);

        if (CollectionUtils.isEmpty(commentList)) {
            return new ArrayList<>();
        }

        // 获取所有用户ID
        Set<Long> userIdSet = commentList.stream()
                .map(QuestionComment::getUserId)
                .collect(Collectors.toSet());
        commentList.stream()
                .filter(comment -> comment.getReplyUserId() != null && comment.getReplyUserId() > 0)
                .forEach(comment -> userIdSet.add(comment.getReplyUserId()));

        // 获取用户信息
        Map<Long, UserVO> userMap = userService.listByIds(new ArrayList<>(userIdSet))
                .stream()
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user, userVO);
                    return userVO;
                })
                .collect(Collectors.toMap(UserVO::getId, userVO -> userVO));

        // 获取当前用户点赞的评论ID集合
        Set<Long> thumbedCommentIdSet = new java.util.HashSet<>();
        try {
            User loginUser = userService.getLoginUser(request);
            if (loginUser != null) {
                List<Long> commentIdList = commentList.stream()
                        .map(QuestionComment::getId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(commentIdList)) {
                    QueryWrapper<QuestionCommentThumb> thumbQueryWrapper = new QueryWrapper<>();
                    thumbQueryWrapper.eq("userId", loginUser.getId());
                    thumbQueryWrapper.in("commentId", commentIdList);
                    List<QuestionCommentThumb> thumbList = questionCommentThumbMapper.selectList(thumbQueryWrapper);
                    thumbedCommentIdSet = thumbList.stream()
                            .map(QuestionCommentThumb::getCommentId)
                            .collect(Collectors.toSet());
                }
            }
        } catch (Exception e) {
            // 未登录，忽略
        }

        // ****  扁平的评论列表，转换成带嵌套结构的“评论树”
        // 转换为VO并构建树形结构
        List<QuestionCommentVO> commentVOList = commentList.stream()
                .map(QuestionCommentVO::objToVo)
                .collect(Collectors.toList());

        // 设置用户信息
        for (QuestionCommentVO commentVO : commentVOList) {
            commentVO.setUser(userMap.get(commentVO.getUserId()));
            if (commentVO.getReplyUserId() != null && commentVO.getReplyUserId() > 0) {
                commentVO.setReplyUser(userMap.get(commentVO.getReplyUserId()));
            }
            commentVO.setHasThumb(thumbedCommentIdSet.contains(commentVO.getId()));
            commentVO.setChildren(new ArrayList<>());
        }

        // 构建树形结构
        List<QuestionCommentVO> rootComments = new ArrayList<>();
        Map<Long, QuestionCommentVO> commentMap = commentVOList.stream()
                .collect(Collectors.toMap(QuestionCommentVO::getId, comment -> comment));

        for (QuestionCommentVO commentVO : commentVOList) {
            if (commentVO.getParentId() == null || commentVO.getParentId() == 0) {
                // 顶级评论
                rootComments.add(commentVO);
            } else {
                // 子评论
                QuestionCommentVO parentComment = commentMap.get(commentVO.getParentId());
                if (parentComment != null) {
                    parentComment.getChildren().add(commentVO);
                }
            }
        }

        return rootComments;
    }

    @Override
    public Boolean deleteComment(Long id, User loginUser) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QuestionComment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }

        // 只有评论作者或管理员可以删除
        if (!comment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 如果有子评论，不能删除（或者可以级联删除，这里选择不允许删除）
        QueryWrapper<QuestionComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentId", id);
        long childCount = this.count(queryWrapper);
        if (childCount > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该评论下有回复，无法删除");
        }

        return this.removeById(id);
    }
}

