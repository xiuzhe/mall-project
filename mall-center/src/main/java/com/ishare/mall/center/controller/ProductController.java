package com.ishare.mall.center.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.ishare.mall.center.controller.base.BaseController;
import com.ishare.mall.center.form.product.AddProductForm;
import com.ishare.mall.common.base.constant.uri.APPURIConstant;
import com.ishare.mall.common.base.constant.uri.CenterURIConstant;
import com.ishare.mall.common.base.constant.view.CenterViewConstant;
import com.ishare.mall.common.base.dto.product.ProductTypeDTO;


/**
 * Created by liaochenglei on 2015/8/13.
 * Description :
 * Version 1.0
 */
@Controller
@RequestMapping(value = APPURIConstant.Product.REQUEST_MAPPING)
public class ProductController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    public static Logger getLog() {
        return log;
    }
    
    @RequestMapping(value = "/addProduct")
    public String addProduct(@ModelAttribute("productAttribute") AddProductForm apf) {
        return CenterViewConstant.Product.ADD_PRODUCT;
    }
    @RequestMapping(value = "/allType", produces = {"application/json"})
    @ResponseBody
    public ProductTypeDTO getType() {
    	ResponseEntity<ProductTypeDTO> resultDTO = null;
		ProductTypeDTO productTypeDTO = new ProductTypeDTO();
		RestTemplate restTemplate = new RestTemplate();
		resultDTO = restTemplate.getForEntity(this.buildBizAppURI(APPURIConstant.ProductType.REQUEST_MAPPING, APPURIConstant.ProductType.REQUEST_MAPPING_FIND_FIRST_LEVEL), ProductTypeDTO.class);
		ProductTypeDTO productTypeDTOResult =  resultDTO.getBody();
		return productTypeDTOResult;
    }
}