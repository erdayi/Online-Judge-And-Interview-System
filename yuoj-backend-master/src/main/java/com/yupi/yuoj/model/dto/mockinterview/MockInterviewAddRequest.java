package com.yupi.yuoj.model.dto.mockinterview;

import com.yupi.yuoj.model.dto.question.JudgeCase;
import com.yupi.yuoj.model.dto.question.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *

 */
@Data
public class MockInterviewAddRequest implements Serializable {

    /**
     * 工作年限
     */
    private String workExperience;

    /**
     * 工作岗位
     */
    private String jobPosition;

    /**
     * 面试难度
     */
    private String difficulty;

    private static final long serialVersionUID = 1L;
}