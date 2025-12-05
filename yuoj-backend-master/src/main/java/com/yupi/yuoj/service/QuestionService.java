package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.dto.post.PostQueryRequest;
import com.yupi.yuoj.model.dto.question.QuestionQueryRequest;
import com.yupi.yuoj.model.entity.Post;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 李鱼皮
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2023-08-07 20:58:00
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 从 ES 查询题目
     *
     * @param questionQueryRequest
     * @return
     */
    Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * AI 生成题目
     *
     * @param questionType 题目类型 例如 java
     * @param number       题目数量 例如 10
     * @param user         创建人
     * @return true or false
     */
    boolean aiGenerateQuestion(String questionType, int number, User user);
}