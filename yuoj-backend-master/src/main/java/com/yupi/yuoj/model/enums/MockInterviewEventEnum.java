package com.yupi.yuoj.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 模拟面试事件枚举
 */
public enum MockInterviewEventEnum {

    START("开始", "start"),
    CHAT("聊天", "chat"),
    END("结束", "end");

    private final String text;

    private final String value;

    MockInterviewEventEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 所有状态值的集合
     */
    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(MockInterviewEventEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 状态值
     * @return 对应的枚举实例，无匹配时返回 null
     */
    public static MockInterviewEventEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MockInterviewEventEnum anEnum : MockInterviewEventEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}