package com.ishare.mall.api.restful;

import com.google.common.collect.Lists;
import com.ishare.mall.api.annotation.AccessToken;
import com.ishare.mall.api.form.pay.PayForm;
import com.ishare.mall.api.restful.base.BaseResource;
import com.ishare.mall.api.service.oauth.OAuthService;
import com.ishare.mall.api.utils.Servlets;
import com.ishare.mall.common.base.constant.uri.APPURIConstant;
import com.ishare.mall.common.base.dto.error.ErrorDTO;
import com.ishare.mall.common.base.dto.order.OrderDetailDTO;
import com.ishare.mall.common.base.dto.pay.AliPayDTO;
import com.ishare.mall.common.base.dto.pay.AliPayNotifyDTO;
import com.ishare.mall.common.base.enumeration.OrderState;
import com.ishare.mall.common.base.general.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by YinLin on 2015/8/24.
 * Description : 支付接口相关
 * Version 1.0
 */
@Controller
@RequestMapping("/payment")
public class PaymentResource extends BaseResource {

    private static final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    @Autowired
    private OAuthService oAuthService;
//    @Autowired
//    private AliPayService aliPayService;
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private OrderPayLogService orderPayLogService;
//    @Autowired
//    private ChannelService channelService;
//
    /**
     * 支付接口
     * @param model
     * @param payForm
     * @return
     */
    @AccessToken
    @RequestMapping(value = "/pay",method = {RequestMethod.POST, RequestMethod.GET}, produces = {"application/json"})
    public Object pay(Model model, @Valid PayForm payForm, BindingResult br, HttpServletResponse response) {
        if (br.hasErrors()) {
            log.debug(br.toString());
            for (ObjectError objectError : br.getAllErrors()) {
                log.debug(objectError.getDefaultMessage());
            }
            model.addAttribute("error", br.getAllErrors());
            ErrorDTO<List<String>> errorDTO = new ErrorDTO<>();
            errorDTO.setCode(HttpServletResponse.SC_ACCEPTED);
            List<String> errorList = Lists.newArrayList();
            errorDTO.setMessage(errorList);
            for (ObjectError error : br.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            Servlets.responseJson(response, errorDTO);
        }
        if (payForm.getToken() == null || !oAuthService.checkAccessToken(payForm.getToken())) {
           // 如果不存在/过期了，返回未验证错误，需重新验证
            OAuthResponse responseObject = null;
            try {
                responseObject = OAuthRSResponse
                .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .buildHeaderMessage();
            } catch (OAuthSystemException e) {
                e.printStackTrace();
            }
            Servlets.responseJson(response, responseObject);;
        }
        String id = payForm.getOrderId();
        ResponseEntity<Response> responseEntity;
        RestTemplate restTemplate = new RestTemplate();
        try {
            responseEntity = restTemplate.getForEntity(this.buildBizAppURI(APPURIConstant.Order.REQUEST_MAPPING, "/" + id), Response.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Response r = new Response();
            r.setMessage("订单不存在");
            r.setSuccess(false);
            r.setCode(404);
            return new ResponseEntity(r, HttpStatus.NOT_FOUND);
        }
        Response responseOrder = responseEntity.getBody();

        if (!responseOrder.isSuccess()) {
            Response r = new Response();
            r.setMessage("订单不存在");
            r.setSuccess(false);
            r.setCode(404);
            return new ResponseEntity(r, HttpStatus.NOT_FOUND);
        }

        OrderDetailDTO order = (OrderDetailDTO)responseOrder.getData();

        //订单不存在
        if (order == null) {
            ErrorDTO<String> errorDTO = new ErrorDTO<>();
            errorDTO.setCode(HttpServletResponse.SC_NOT_FOUND);
            errorDTO.setMessage("订单不存在");
            Servlets.responseHttpJson(response, new ResponseEntity(errorDTO, HttpStatus.valueOf(HttpServletResponse.SC_NOT_FOUND)));
        }
        //订单状态不是等待支付
        if (order.getState() != OrderState.WAIT_PAYMENT) {
            ErrorDTO<String> errorDTO = new ErrorDTO<>();
            errorDTO.setCode(HttpServletResponse.SC_BAD_REQUEST);
            errorDTO.setMessage("订单不是等待支付状态");
            Servlets.responseHttpJson(response, new ResponseEntity(errorDTO, HttpStatus.valueOf(HttpServletResponse.SC_BAD_REQUEST)));
        }

        AliPayDTO aliPayDTO = this.createAliPayDTO(payForm.getToken(), order);

        ResponseEntity<Response> responseHtml;
        try {
            responseHtml = restTemplate.postForEntity(this.buildBizAppURI(APPURIConstant.AliPay.REQUEST_MAPPING, APPURIConstant.AliPay.REQUEST_MAPPING_CREATE_PAY_HTML), aliPayDTO, Response.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Response err = new Response();
            err.setSuccess(false);
            err.setCode(500);
            err.setMessage("创建支付失败！");
            return new ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Response payHtml = responseHtml.getBody();

        if (!payHtml.isSuccess()) {
            Response err = new Response();
            err.setSuccess(false);
            err.setCode(500);
            err.setMessage("创建支付失败！");
            return new ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String payFormHtml = (String) payHtml.getData();
        model.addAttribute("returnContent", payFormHtml);
        log.debug(payFormHtml);
        return "pay/pay";
    }

    public String payPage() {
        return "";
    }
//
//    /**
//     * 阿里支付回调
//     * @return
//     */
//    @RequestMapping(value = "/pay/notify", method = RequestMethod.POST)
//    public Object payNotify(AliPayNotifyDTO notify, HttpServletRequest request) {
//        if(!aliPayService.verifyNotifyUrl(notify, null)) {
//            return null;
//        }
//        // 获取支付宝POST过来反馈信息
//        Map<String, String> params = new HashMap<String, String>();
//        Map requestParams = request.getParameterMap();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//            }
//            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//            params.put(name, valueStr);
//        }
//        //如果校验码为空
//        if (StringUtils.isBlank(notify.getSign())) {
//            log.info("notify from aliPay, sign is null, notify={}, params={}",
//                    new Object[]{notify, params});
//            return null;
//        }
//        if (aliPayService.verifyNotifySign(params, notify.getSign())) {
//            if (isPaySuccess(notify)) {
//                OrderPayLog payLog = orderPayLogService.findByOrderId(notify.getOut_trade_no());
//                if (payLog != null && payLog.getPayType().equals(PayType.NEW)) {
//                    payLog.setAmount(new BigDecimal(notify.getTotal_fee()));
//                    payLog.setTansId(notify.getTrade_no());
//                    payLog.setUpdateTime(new Date());
//                    log.warn("pay from pc and pay by blank amount payment={}", "支付宝");
//                    //更新支付log状态
//                    orderPayLogService.updateForProcess(payLog);
//                    //设置支付完成
//                    orderService.payComplete(notify.getOut_trade_no());
//                }
//            }
//        }
//        return null;
//    }
//
    /**
     * 创建支付DTO
     * @param token
     * @param order
     * @return
     */
    private AliPayDTO createAliPayDTO(String token, OrderDetailDTO order) {
        AliPayDTO aliPayDTO = new AliPayDTO();
        aliPayDTO.setAmount(new BigDecimal(order.getPayableFee()));
        aliPayDTO.setOrderID(order.getOrderId());
        aliPayDTO.setGoodsName(order.getOrderId());
        aliPayDTO.setReturnUrl("127.0.0.1");
        return  aliPayDTO;
    }

    /**
     * 简单验证支付是否成功
     * @param notify
     * @return
     */
    private boolean isPaySuccess(AliPayNotifyDTO notify) {
        return !StringUtils.isBlank(notify.getTrade_status()) && (notify.getTrade_status().equals("TRADE_FINISHED") || notify.getTrade_status().equals("TRADE_SUCCESS"));
    }


    public static Logger getLog() {
        return log;
    }
}
