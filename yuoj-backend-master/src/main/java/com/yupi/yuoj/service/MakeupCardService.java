package com.yupi.yuoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yuoj.model.entity.MakeupCard;

/**
 * 补卡券服务
 *

 */
public interface MakeupCardService extends IService<MakeupCard> {

    /**
     * 获取或创建用户的补卡券记录
     *
     * @param userId 用户 id
     * @return
     */
    MakeupCard getOrCreateMakeupCard(Long userId);

    /**
     * 使用补卡券
     *
     * @param userId 用户 id
     * @return 是否成功
     */
    Boolean useMakeupCard(Long userId);

    /**
     * 增加补卡券
     *
     * @param userId 用户 id
     * @param count 数量
     * @return
     */
    Boolean addMakeupCard(Long userId, Integer count);
}

