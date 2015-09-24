package com.ishare.mall.api.service.order;

import com.ishare.mall.common.base.dto.order.OrderDetailDTO;
import com.ishare.mall.common.base.exception.web.api.ApiLogicException;

/**
 * Created by YinLin on 2015/9/24.
 * Description : 客户端 order Service
 * Version 1.0
 */
public interface OrderService {
    OrderDetailDTO findOne(String id) throws ApiLogicException;
}
