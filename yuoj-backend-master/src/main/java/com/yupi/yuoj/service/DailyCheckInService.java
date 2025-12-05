package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.DailyCheckIn;
import com.yupi.yuoj.model.entity.User;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * 每日打卡服务
 *

 */
public interface DailyCheckInService extends IService<DailyCheckIn> {

    /**
     * 打卡
     *
     * @param questionId 题目 id
     * @param request
     * @return
     */
    Boolean checkIn(Long questionId, HttpServletRequest request);

    /**
     * 获取用户打卡记录（按月）
     *
     * @param year 年份
     * @param month 月份
     * @param request
     * @return 日期 -> 打卡状态
     */
    Map<String, Integer> getCheckInRecord(Integer year, Integer month, HttpServletRequest request);

    /**
     * 获取今日题目
     *
     * @return 题目 id
     */
    Long getTodayQuestionId();

    /**
     * 补卡
     *
     * @param checkInDate 补卡日期
     * @param request
     * @return
     */
    Boolean makeupCheckIn(Date checkInDate, HttpServletRequest request);

    /**
     * 获取补卡券数量
     *
     * @param request
     * @return
     */
    Integer getMakeupCardCount(HttpServletRequest request);
}

