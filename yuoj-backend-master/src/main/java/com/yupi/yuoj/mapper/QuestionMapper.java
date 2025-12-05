package com.yupi.yuoj.mapper;

import com.yupi.yuoj.model.entity.Post;
import com.yupi.yuoj.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author 李鱼皮
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2023-08-07 20:58:00
* @Entity com.yupi.yuoj.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询题目列表（包括已被删除的数据） es 与 mysql 保证完全一致 逻辑删除同步 默认会加 is_delete = 0
     */
    @Select("select * from question where updateTime >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);

}




