package com.yupi.yuoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.mapper.MakeupCardMapper;
import com.yupi.yuoj.model.entity.MakeupCard;
import com.yupi.yuoj.service.MakeupCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 补卡券服务实现
 *

 */
@Service
public class MakeupCardServiceImpl extends ServiceImpl<MakeupCardMapper, MakeupCard>
        implements MakeupCardService {

    @Override
    public MakeupCard getOrCreateMakeupCard(Long userId) {
        QueryWrapper<MakeupCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        MakeupCard makeupCard = this.getOne(queryWrapper);

        if (makeupCard == null) {
            makeupCard = new MakeupCard();
            makeupCard.setUserId(userId);
            makeupCard.setCardCount(0);
            this.save(makeupCard);
        }
        return makeupCard;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean useMakeupCard(Long userId) {
        MakeupCard makeupCard = getOrCreateMakeupCard(userId);
        if (makeupCard.getCardCount() <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "补卡券不足");
        }
        makeupCard.setCardCount(makeupCard.getCardCount() - 1);
        return this.updateById(makeupCard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMakeupCard(Long userId, Integer count) {
        MakeupCard makeupCard = getOrCreateMakeupCard(userId);
        makeupCard.setCardCount(makeupCard.getCardCount() + count);
        return this.updateById(makeupCard);
    }
}

