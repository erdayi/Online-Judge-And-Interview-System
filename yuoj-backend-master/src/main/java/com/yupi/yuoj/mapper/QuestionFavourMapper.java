package com.yupi.yuoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionFavour;
import org.apache.ibatis.annotations.Param;

/**
 * 题目收藏 Mapper
 *

 */
public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {

    /**
     * 分页查询用户收藏的题目
     *
     * @param page
     * @param favourUserId
     * @return
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<Question> listFavourQuestionByPage(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<Question> page,
            @Param("favourUserId") long favourUserId);
}

