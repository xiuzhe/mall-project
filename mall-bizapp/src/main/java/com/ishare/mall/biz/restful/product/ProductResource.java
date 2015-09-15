package com.ishare.mall.biz.restful.product;

import com.ishare.mall.common.base.dto.page.PageDTO;
import com.ishare.mall.common.base.dto.product.ProductListDTO;
import com.ishare.mall.core.utils.mapper.MapperUtils;
import com.ishare.mall.core.utils.page.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishare.mall.common.base.constant.uri.APPURIConstant;
import com.ishare.mall.common.base.dto.product.ProductDetailDTO;
import com.ishare.mall.core.model.information.Brand;
import com.ishare.mall.core.model.information.Channel;
import com.ishare.mall.core.model.member.Member;
import com.ishare.mall.core.model.product.Product;
import com.ishare.mall.core.model.product.ProductType;
import com.ishare.mall.core.service.product.ProductService;
import com.ishare.mall.core.status.Gender;
import com.ishare.mall.core.status.MemberType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YinLin on 2015/9/1.
 * Description :
 * Version 1.0
 */
@RestController
@RequestMapping(APPURIConstant.Product.REQUEST_MAPPING)
public class ProductResource {
	
    private static final Logger log = LoggerFactory.getLogger(ProductResource.class);
    @Autowired
    private ProductService productService;

    public static Logger getLog() {
        return log;
    }



    @RequestMapping(value = APPURIConstant.Product.REQUEST_MAPPING_SAVE, method = RequestMethod.POST,
            headers = "Accept=application/xml, application/json",
            produces = {"application/json", "application/xml"},
            consumes = {"application/json", "application/xml"})
    public ProductDetailDTO saveProduct(@RequestBody ProductDetailDTO productDetailDTO){
            Product product = new Product();
            BeanUtils.copyProperties(productDetailDTO, product);
        			Brand brand = new Brand();
        			brand.setId(productDetailDTO.getBrandId());
        			Member member = new Member();
        			member.setAccount(productDetailDTO.getCreateByAccount());
        			member.setMemberType(MemberType.MEMBER);
        			member.setSex(Gender.MAN);
        			Channel channel = new Channel();
        			channel.setId(productDetailDTO.getChannelId());
        			member.setChannel(channel);
        			ProductType productType = new ProductType();
        			productType.setId(productDetailDTO.getTypeId());
        			productType.setCode("1001001001");
        			productType.setName("衬衫");
        			productType.setNote("非常好");
        			productType.setLevel(3);
        			product.setBrand(brand);
        			product.setCreateBy(member);
        			product.setChannel(channel);
        			product.setType(productType);
        			
        			try {
						productService.saveProduct(product);
					} catch (Exception e) {
						e.printStackTrace();
					}
		return productDetailDTO;
    }

	/**
	 * 根据商品ID 查询商品详细信息
	 * @param productDetailDTO
	 * @return
	 */
	@RequestMapping(value = APPURIConstant.Product.REQUEST_MAPPING_FIND_ID, method = RequestMethod.POST,
			headers = "Accept=application/xml, application/json",
			produces = {"application/json", "application/xml"},
			consumes = {"application/json", "application/xml"})
	public ProductDetailDTO findByID(@RequestBody ProductDetailDTO productDetailDTO){
		Product product = productService.findOne(productDetailDTO.getId());
		if(product == null || !product.getVisible()){
			return productDetailDTO;
		}
		return (ProductDetailDTO)MapperUtils.map(product, ProductDetailDTO.class);
	}

	/**
	 * 根据商品的code查询详细信息
	 * @param productDetailDTO
	 * @return
	 */
	@RequestMapping(value = APPURIConstant.Product.REQUEST_MAPPING_FIND_CODE, method = RequestMethod.POST,
			headers = "Accept=application/xml, application/json",
			produces = {"application/json", "application/xml"},
			consumes = {"application/json", "application/xml"})
	public ProductDetailDTO findByCode(@RequestBody ProductDetailDTO productDetailDTO){
		Product product = productService.findByCode(productDetailDTO.getCode());
		if(product == null || !product.getVisible()){
			return productDetailDTO;
		}
		return (ProductDetailDTO)MapperUtils.map(product,ProductDetailDTO.class);
	}

	/**
	 * 根据请求page 和 page size 返回数据
	 * @param productListDTO
	 * @return
	 */
	@RequestMapping(value = APPURIConstant.Product.REQUEST_BY_PARAM, method = RequestMethod.POST,
			headers = "Accept=application/xml, application/json",
			produces = {"application/json", "application/xml"},
			consumes = {"application/json", "application/xml"})
	public ProductListDTO findByParam(@RequestBody ProductListDTO productListDTO){
		List<ProductListDTO> list = new ArrayList<ProductListDTO>();
		Integer offset = productListDTO.getOffset();
		Integer limit = productListDTO.getLimit();
		PageRequest pageRequest = new PageRequest(offset - 1 < 0 ? 0 : offset - 1, limit <= 0 ? 15 : limit, Sort.Direction.DESC, "id");
		Page<Product> result = null;
		if(productListDTO.getMap() != null && !productListDTO.getMap().isEmpty()){
			result = productService.search(productListDTO.getMap(), pageRequest);
		}else {
			result = productService.search(null, pageRequest);
		}

		PageDTO pageDTO = new PageDTO();
		if(result != null && result.getSize() > 0 && result.getContent() != null && result.getContent().size() >0){
			List<Product> productList = result.getContent();
			for (Product product:productList){
				ProductListDTO productDTO = new ProductListDTO();
				BeanUtils.copyProperties(product,productDTO);
				list.add(productDTO);
			}
			pageDTO.setContent(list);
			pageDTO.setTotalPages(result.getTotalPages());
			pageDTO.setTotalElements(result.getTotalElements());
			productListDTO.setPageDTO(pageDTO);
		}
		return productListDTO;
	}
}
