package com.yupi.yuoj.model.dto.questioncomment;

import java.io.Serializable;
import lombok.Data;

/**
 * 题目评论添加请求
 *

 */
@Data
public class QuestionCommentAddRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}

