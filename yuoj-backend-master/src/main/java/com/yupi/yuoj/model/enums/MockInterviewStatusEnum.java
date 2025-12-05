package com.yupi.yuoj.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 模拟面试状态枚举
 */
public enum MockInterviewStatusEnum {

    PENDING("待开始", 0),
    IN_PROGRESS("进行中", 1),
    FINISHED("已结束", 2);

    private final String text;

    private final Integer value;

    MockInterviewStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return 所有状态值的集合
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values())
                .map(MockInterviewStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     * @param value 状态值
     * @return 对应的枚举实例，无匹配时返回 null
     */
    public static MockInterviewStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MockInterviewStatusEnum anEnum : MockInterviewStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}