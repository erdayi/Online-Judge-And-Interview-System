package com.yupi.yuoj.model.vo;

import com.yupi.yuoj.model.entity.QuestionComment;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 题目评论视图
 *

 */
@Data
public class QuestionCommentVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论 id（用于回复，0 表示顶级评论）
     */
    private Long parentId;

    /**
     * 回复的用户 id（用于回复，0 表示顶级评论）
     */
    private Long replyUserId;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人信息
     */
    private UserVO user;

    /**
     * 回复的用户信息
     */
    private UserVO replyUser;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    /**
     * 子评论列表
     */
    private List<QuestionCommentVO> children;

    /**
     * 对象转包装类
     *
     * @param questionComment
     * @return
     */
    public static QuestionCommentVO objToVo(QuestionComment questionComment) {
        if (questionComment == null) {
            return null;
        }
        QuestionCommentVO questionCommentVO = new QuestionCommentVO();
        BeanUtils.copyProperties(questionComment, questionCommentVO);
        return questionCommentVO;
    }

    private static final long serialVersionUID = 1L;
}

