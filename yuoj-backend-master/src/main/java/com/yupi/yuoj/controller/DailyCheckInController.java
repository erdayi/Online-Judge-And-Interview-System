package com.yupi.yuoj.controller;

import com.yupi.yuoj.common.BaseResponse;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.common.ResultUtils;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.service.DailyCheckInService;
import com.yupi.yuoj.service.UserService;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 每日打卡接口
 *

 */
@RestController
@RequestMapping("/daily_check_in")
@Slf4j
public class DailyCheckInController {

    @Resource
    private DailyCheckInService dailyCheckInService;

    @Resource
    private UserService userService;

    /**
     * 打卡
     *
     * @param questionId 题目 id
     * @param request
     * @return
     */
    @PostMapping("/check_in")
    public BaseResponse<Boolean> checkIn(@RequestParam Long questionId, HttpServletRequest request) {
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = dailyCheckInService.checkIn(questionId, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取打卡记录
     *
     * @param year 年份
     * @param month 月份
     * @param request
     * @return
     */
    @GetMapping("/record")
    public BaseResponse<Map<String, Integer>> getCheckInRecord(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            HttpServletRequest request) {
        if (year == null || month == null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            year = cal.get(java.util.Calendar.YEAR);
            month = cal.get(java.util.Calendar.MONTH) + 1;
        }
        Map<String, Integer> record = dailyCheckInService.getCheckInRecord(year, month, request);
        return ResultUtils.success(record);
    }

    /**
     * 获取今日题目
     *
     * @return
     */
    @GetMapping("/today_question")
    public BaseResponse<Long> getTodayQuestion() {
        Long questionId = dailyCheckInService.getTodayQuestionId();
        return ResultUtils.success(questionId);
    }

    /**
     * 补卡
     *
     * @param checkInDate 补卡日期
     * @param request
     * @return
     */
    @PostMapping("/makeup")
    public BaseResponse<Boolean> makeupCheckIn(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate,
            HttpServletRequest request) {
        if (checkInDate == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = dailyCheckInService.makeupCheckIn(checkInDate, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取补卡券数量
     *
     * @param request
     * @return
     */
    @GetMapping("/makeup_card_count")
    public BaseResponse<Integer> getMakeupCardCount(HttpServletRequest request) {
        Integer count = dailyCheckInService.getMakeupCardCount(request);
        return ResultUtils.success(count);
    }
}

