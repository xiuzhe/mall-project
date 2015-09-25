package com.ishare.mall.api.service.order.impl;

import com.ishare.mall.api.service.BaseService;
import com.ishare.mall.api.service.order.OrderService;
import com.ishare.mall.common.base.constant.uri.APPURIConstant;
import com.ishare.mall.common.base.dto.order.OrderDetailDTO;
import com.ishare.mall.common.base.exception.web.api.ApiLogicException;
import com.ishare.mall.common.base.general.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by YinLin on 2015/9/24.
 * Description : 客户端
 * Version 1.0
 */

@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public OrderDetailDTO findOne(String id) throws ApiLogicException {

        ResponseEntity<Response<OrderDetailDTO>> responseEntity;

        try {
            responseEntity = restTemplate.exchange(
                    this.buildBizAppURI(APPURIConstant.Order.REQUEST_MAPPING, APPURIConstant.Order.REQUEST_MAPPING_FIND_BY_ID),
                    HttpMethod.GET, null, new ParameterizedTypeReference<Response<OrderDetailDTO>>() {
                    }, id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiLogicException("订单未找到", HttpStatus.NOT_FOUND);
        }

        Response<OrderDetailDTO> response = responseEntity.getBody();

        if (!response.isSuccess() || response.getData() == null) {
            throw new ApiLogicException("订单未找到", HttpStatus.NOT_FOUND);
        }

        return response.getData();
    }
}
