package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.mapper.DailyCheckInMapper;
import com.yupi.yuoj.model.entity.DailyCheckIn;
import com.yupi.yuoj.model.entity.MakeupCard;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.service.DailyCheckInService;
import com.yupi.yuoj.service.MakeupCardService;
import com.yupi.yuoj.service.UserService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 每日打卡服务实现
 *

 */
@Service
@Slf4j
public class DailyCheckInServiceImpl extends ServiceImpl<DailyCheckInMapper, DailyCheckIn>
        implements DailyCheckInService {

    @Resource
    private UserService userService;

    @Resource
    private MakeupCardService makeupCardService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkIn(Long questionId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Date today = new Date();
        // 只取日期部分，忽略时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayDate = cal.getTime();

        // 检查今天是否已打卡
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.apply("DATE(checkInDate) = DATE({0})", todayDate);
        DailyCheckIn existing = this.getOne(queryWrapper);

        if (existing != null && existing.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "今日已打卡");
        }

        if (existing != null) {
            existing.setStatus(1);
            existing.setQuestionId(questionId);
            return this.updateById(existing);
        } else {
            DailyCheckIn checkIn = new DailyCheckIn();
            checkIn.setUserId(loginUser.getId());
            checkIn.setQuestionId(questionId);
            checkIn.setCheckInDate(todayDate);
            checkIn.setStatus(1);
            return this.save(checkIn);
        }
    }

    @Override
    public Map<String, Integer> getCheckInRecord(Integer year, Integer month, HttpServletRequest request) {
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser == null) {
            return new HashMap<>();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        Date startDate = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = cal.getTime();

        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.ge("checkInDate", startDate);
        queryWrapper.le("checkInDate", endDate);
        List<DailyCheckIn> checkIns = this.list(queryWrapper);

        Map<String, Integer> result = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (DailyCheckIn checkIn : checkIns) {
            if (checkIn.getCheckInDate() != null) {
                String dateStr = sdf.format(checkIn.getCheckInDate());
                result.put(dateStr, checkIn.getStatus());
            }
        }
        return result;
    }

    @Resource
    private com.yupi.yuoj.service.DailyQuestionService dailyQuestionService;

    @Resource
    private com.yupi.yuoj.service.QuestionService questionService;

    @Override
    public Long getTodayQuestionId() {
        // 根据日期生成一个种子，确保同一天返回相同的题目
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long seed = cal.getTimeInMillis();
        
        // 获取题目总数
        QueryWrapper<com.yupi.yuoj.model.entity.Question> countWrapper = new QueryWrapper<>();
        countWrapper.eq("isDelete", 0);
        long total = questionService.count(countWrapper);
        
        if (total == 0) {
            return null;
        }
        
        // 使用日期作为种子随机选择题目
        Random random = new Random(seed);
        int offset = (int) (random.nextDouble() * total);
        
        QueryWrapper<com.yupi.yuoj.model.entity.Question> qWrapper = new QueryWrapper<>();
        qWrapper.eq("isDelete", 0);
        qWrapper.last("LIMIT 1 OFFSET " + offset);
        com.yupi.yuoj.model.entity.Question question = questionService.getOne(qWrapper);
        return question != null ? question.getId() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean makeupCheckIn(Date checkInDate, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);

        // 检查补卡券
        MakeupCard makeupCard = makeupCardService.getOrCreateMakeupCard(loginUser.getId());
        if (makeupCard.getCardCount() <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "补卡券不足");
        }

        // 只取日期部分
        Calendar cal = Calendar.getInstance();
        cal.setTime(checkInDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date checkInDateOnly = cal.getTime();

        // 检查是否已打卡
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.apply("DATE(checkInDate) = DATE({0})", checkInDateOnly);
        DailyCheckIn existing = this.getOne(queryWrapper);

        if (existing != null && existing.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该日期已打卡");
        }

        // 使用补卡券
        makeupCardService.useMakeupCard(loginUser.getId());

        // 创建打卡记录
        if (existing != null) {
            existing.setStatus(1);
            return this.updateById(existing);
        } else {
            DailyCheckIn checkIn = new DailyCheckIn();
            checkIn.setUserId(loginUser.getId());
            checkIn.setQuestionId(getTodayQuestionId());
            checkIn.setCheckInDate(checkInDateOnly);
            checkIn.setStatus(1);
            return this.save(checkIn);
        }
    }

    @Override
    public Integer getMakeupCardCount(HttpServletRequest request) {
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser == null) {
            return 0;
        }
        MakeupCard makeupCard = makeupCardService.getOrCreateMakeupCard(loginUser.getId());
        return makeupCard.getCardCount();
    }
}

