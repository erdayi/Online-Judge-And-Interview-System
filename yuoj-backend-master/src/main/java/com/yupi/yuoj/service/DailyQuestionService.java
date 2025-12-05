package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.DailyQuestion;

/**
 * 每日题目服务
 *

 */
public interface DailyQuestionService extends IService<DailyQuestion> {

    /**
     * 设置每日题目
     *
     * @param questionId 题目 id
     * @param date 日期
     * @return
     */
    Boolean setDailyQuestion(Long questionId, java.util.Date date);
}

