package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.mapper.DailyQuestionMapper;
import com.yupi.yuoj.model.entity.DailyQuestion;
import com.yupi.yuoj.service.DailyQuestionService;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * 每日题目服务实现
 *

 */
@Service
public class DailyQuestionServiceImpl extends ServiceImpl<DailyQuestionMapper, DailyQuestion>
        implements DailyQuestionService {

    @Override
    public Boolean setDailyQuestion(Long questionId, Date date) {
        QueryWrapper<DailyQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionDate", date);
        DailyQuestion existing = this.getOne(queryWrapper);

        if (existing != null) {
            existing.setQuestionId(questionId);
            return this.updateById(existing);
        } else {
            DailyQuestion dailyQuestion = new DailyQuestion();
            dailyQuestion.setQuestionId(questionId);
            dailyQuestion.setQuestionDate(date);
            return this.save(dailyQuestion);
        }
    }
}

