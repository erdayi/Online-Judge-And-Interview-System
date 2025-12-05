package com.yupi.yuoj.model.dto.mockinterview;

import lombok.Data;

import java.io.Serializable;


/**
 * 模拟面试消息记录
 *
 */
@Data
public class MockInterviewChatMessage implements Serializable {

    private static final long serialVersionUID = -5034145192980568L;

    /**
     * 对话角色
     */
    private String role;

    /**
     * 消息列表
     */
    private String message;


}
