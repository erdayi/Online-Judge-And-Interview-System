package com.yupi.yuoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionAIGenerateRequest implements Serializable {
    /**
     * 题目类型 比如 JAVA
     */
    private String questionType;

    /**
     * 题目数量 比如 10
     */
    private int number = 1;
}
