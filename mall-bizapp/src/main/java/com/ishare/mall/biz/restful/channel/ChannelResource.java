package com.ishare.mall.biz.restful.channel;

import com.ishare.mall.common.base.constant.CommonConstant;
import com.ishare.mall.common.base.constant.uri.APPURIConstant;
import com.ishare.mall.common.base.constant.uri.ManageURIConstant;
import com.ishare.mall.common.base.dto.channel.ChannelDTO;
import com.ishare.mall.common.base.dto.channel.ChannelTokenResultDTO;
import com.ishare.mall.common.base.dto.page.PageDTO;
import com.ishare.mall.common.base.dto.validform.ValidformRespDTO;
import com.ishare.mall.common.base.exception.service.channel.ChannelServiceException;
import com.ishare.mall.common.base.general.Response;
import com.ishare.mall.core.model.information.Channel;
import com.ishare.mall.core.service.information.ChannelService;
import com.ishare.mall.core.utils.UuidUtils;
import com.ishare.mall.core.utils.mapper.MapperUtils;
import com.ishare.mall.core.utils.page.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YinLin on 2015/9/1.
 * Description : 渠道相关APP
 * Version 1.0
 */
@RestController
@RequestMapping(APPURIConstant.Channel.REQUEST_MAPPING)
public class ChannelResource {
    private static final Logger logger = LoggerFactory.getLogger(ChannelResource.class);
    @Autowired
    private ChannelService channelService;
    public static Logger getLog() {
        return logger;
    }
    /**
     * 通过appId获取订单
     * @param id
     * @return
     */
    @RequestMapping(value       = APPURIConstant.Channel.REQUEST_MAPPING_GET_BY_APP_ID,
                    method      = RequestMethod.GET,
                    headers     = "Accept=application/xml, application/json",
                    produces    = {"application/json"})
    public Response<ChannelTokenResultDTO> getByAppId(@NotEmpty @PathVariable("id")String id) {
        Channel channel = channelService.findByAppId(id);
        if (channel != null) {
            Response response = new Response();
            response.setCode(200);
            response.setData((ChannelTokenResultDTO)MapperUtils.map(channel, ChannelTokenResultDTO.class));
            return response;
        }
        throw new ChannelServiceException("未找到");
    }

    /**
     * 通过appSecret获取订单
     * @param secret
     * @return
     */
    @RequestMapping(value       = APPURIConstant.Channel.REQUEST_MAPPING_GET_BY_APP_SECRET,
                    method      = RequestMethod.GET,
                    headers     = "Accept=application/xml, application/json",
                    produces    = {"application/json"})
    public Response<ChannelTokenResultDTO> getByAppSecret(@NotEmpty @PathVariable("secret") String secret) {
        Channel channel = channelService.findByAppSecret(secret);
        if (channel != null) {
            Response response = new Response();
            response.setCode(200);
            response.setData((ChannelTokenResultDTO)MapperUtils.map(channel, ChannelTokenResultDTO.class));
            return response;
        }
        throw new ChannelServiceException("未找到");
    }
    
    /**
     * 通过account查询出channel是否存在
     * @return Channel 返回的数据对象
     */
    @RequestMapping(value 	= APPURIConstant.Channel.REQUEST_MAPPING_FIND_VALID_BY_NAME, method = RequestMethod.POST,
            				headers 	= "Accept=application/xml, application/json",
        							produces = {"application/json", "application/xml"},
        							consumes = {"application/json", "application/xml"})
    public ValidformRespDTO findValidByAccount(@RequestBody ChannelTokenResultDTO channelRegisterDTO) {
        Channel channel = channelService.findByName(channelRegisterDTO.getName());
        ValidformRespDTO validformRespDTO = new ValidformRespDTO();
        if(null != channel){
		        validformRespDTO.setInfo(CommonConstant.ValidForm.VALIDFORM_FAIL_INFO);
		        validformRespDTO.setStatus(CommonConstant.ValidForm.VALIDFORM_FAIL_STATUS);
	    			}else{
		        validformRespDTO.setInfo(CommonConstant.ValidForm.VALIDFORM_SUCCESS_INFO);
		        validformRespDTO.setStatus(CommonConstant.ValidForm.VALIDFORM_SUCCESS_STATUS);
        				}
         return validformRespDTO;
    	}

    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_GET_CHANNEL_PAGE,method = RequestMethod.POST,
                    headers = "Accept=application/xml, application/json",
                    produces = {"application/json"},
                    consumes = {"application/json"})
    public Response<PageDTO<ChannelDTO>> getChannelPage(@RequestBody ChannelDTO channelDTO){
        Response<PageDTO<ChannelDTO>> response = new Response<PageDTO<ChannelDTO>>();
        int offset = channelDTO.getOffset();
        int limit = channelDTO.getLimit();
        PageRequest pageRequest = new PageRequest(offset - 1 < 0 ? 0 : offset - 1, limit <= 0 ? 15 : limit, Sort.Direction.DESC, "id");
        try{
            Page<Channel> pageChannel = channelService.getChannelpage(pageRequest);
            PageDTO<ChannelDTO> pageChnnelDTO = PageUtils.mapper(pageChannel, ChannelDTO.class);
            response.setData(pageChnnelDTO);
        }catch (ChannelServiceException e){
            logger.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_FIND_BY_ID,method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Response<ChannelDTO> findByChannelId(@RequestBody ChannelDTO channelDTO){
        Channel channel = channelService.findOne(channelDTO.getId());
        BeanUtils.copyProperties(channel,channelDTO);
        Response<ChannelDTO> response = new Response<ChannelDTO>();
        response.setData(channelDTO);
        return response;
    }

    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_UPDATE_CHANNEL_STATUS,method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Response<ChannelDTO> updateChannelStatus(@RequestBody ChannelDTO channelDTO){
        Channel channel = channelService.findOne(channelDTO.getId());
        channel.setVisible(channelDTO.getVisible());
        channelService.save(channel);
        return null;
    }

    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_SAVE_CHANNEL,method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Response saveChannel(@RequestBody ChannelDTO channelDTO) throws Exception{
        Response response = new Response();
        try{
            Channel channel = new Channel();
            BeanUtils.copyProperties(channelDTO,channel);
            channel.setCountry("中国");
            String area[] = channelDTO.getCity().split(",");
            channel.setProvince(area[0]);
            if(area.length > 1){
                channel.setCity(area[1]);
            }
            if(area.length > 2){
                channel.setDistrict(area[2]);
            }
            UuidUtils uu = new UuidUtils();
            String appId = uu.App_Id();
            channel.setAppId(appId);
            channel.setAppSecret(uu.App_screct(String.valueOf(new Date().getTime()), appId));
            channel.setVisible(true);
            channelService.save(channel);
        }catch (Exception e){
            response.setSuccess(false);
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_UPDATE_CHANNEL,method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Response updateChannel(@RequestBody ChannelDTO channelDTO) throws Exception{
        try{
            Channel channel = channelService.findOne(channelDTO.getChannelId());
            channel.setName(channelDTO.getName());
            channel.setPhone(channelDTO.getPhone());
            channel.setLinkName(channelDTO.getLinkName());
            channel.setLinkPhone(channelDTO.getLinkPhone());
            channel.setBusinessScale(channelDTO.getBusinessScale());
            channel.setCode(channelDTO.getCode());
            channel.setIndustry(channelDTO.getIndustry());
            channel.setCountry("中国");
            channel.setDetail(channelDTO.getDetail());
            String area[] = channelDTO.getCity().split(",");
            channel.setProvince(area[0]);
            if(area.length > 1){
                channel.setCity(area[1]);
            }
            if(area.length > 2){
                channel.setDistrict(area[2]);
            }
            channelService.save(channel);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }
    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_FIND_BY_PARAM,method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Response<PageDTO<ChannelDTO>> findBySearchCondition(@RequestBody ChannelDTO channelDTO){
        Response<PageDTO<ChannelDTO>> response = new Response<PageDTO<ChannelDTO>>();
        String name = "%" + channelDTO.getName() + "%";
        String phone = "%" + channelDTO.getPhone() + "%";
        String industry = "%" + channelDTO.getIndustry() + "%";
        int offset = channelDTO.getOffset();
        int limit = channelDTO.getLimit();
        Page<Channel> page = null;
        PageRequest pageRequest = new PageRequest(offset - 1 < 0 ? 0 : offset - 1, limit <= 0 ? 15 : limit, Sort.Direction.DESC, "id");
        try{
            if(StringUtils.isEmpty(channelDTO.getName())){
                page = channelService.getChannelpage(pageRequest);
            }else {
                page = channelService.getChannelpage(pageRequest,name,phone,industry);
            }
            PageDTO<ChannelDTO> pageChnnelDTO = PageUtils.mapper(page, ChannelDTO.class);
            response.setData(pageChnnelDTO);
        }catch (ChannelServiceException e){
            logger.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }


    /**
     * 查询本周的新增的渠道
     */
    @RequestMapping(value = ManageURIConstant.Channel.REQUEST_MAPPING_FindThisWeek, method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Response findAllThisWeek(@RequestBody ChannelDTO channelDTO) {
        List<ChannelDTO> listChannel = new ArrayList<ChannelDTO>();
        Integer offset = channelDTO.getOffset();
        Integer limit = channelDTO.getLimit();
        PageDTO<ChannelDTO> pageDTO = new PageDTO<ChannelDTO>();
        Response<PageDTO<ChannelDTO>> response = new Response();
        Page<Channel> result = null;
        try {
            PageRequest pageRequest = new PageRequest(offset - 1 < 0 ? 0 : offset - 1, limit <= 0 ? 15 : limit, Sort.Direction.DESC, "id");
            // ChannelServiceImpl ChannelServiceimpl = new ChannelServiceImpl();
            result = channelService.findAllThisWeek(pageRequest);
            if (result != null && result.getContent() != null && result.getContent().size() > 0) {
                List<ChannelDTO> list = (List<ChannelDTO>)MapperUtils.mapAsList(result.getContent(),ChannelDTO.class);
                pageDTO.setContent(list);
                pageDTO.setTotalPages(result.getTotalPages());
                pageDTO.setITotalDisplayRecords(result.getTotalElements());
                pageDTO.setITotalRecords(result.getTotalElements());
                pageDTO.setLimit(limit);
                pageDTO.setOffset(offset);
                response.setData(pageDTO);
            } else {
                pageDTO.setContent(listChannel);
                pageDTO.setTotalPages(0);
                pageDTO.setITotalDisplayRecords(0L);
                pageDTO.setITotalRecords(0L);
                response.setData(pageDTO);
            }
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return response;
        }

    }

    /**
     * 查询本周新增渠道的数量
     * @return
     */
    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_THISWEEK_COUNT, method = RequestMethod.GET,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"})
    public Response findThisWeekCount(){
        logger.debug("findAll start");
        Long count = channelService.findThisWeekCount();
        Response response = new Response();
        response.setData(count);
        return response;
    }

    /**
     * 查询Channel数量
     * @return
     */
    @RequestMapping(value = APPURIConstant.Channel.REQUEST_MAPPING_COUNT, method = RequestMethod.GET,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json"})
    public Response findCount(){
        logger.debug("findAll start");
        Long count = channelService.findCount();
        Response response = new Response();
        response.setData(count);
        return response;
    }

}
