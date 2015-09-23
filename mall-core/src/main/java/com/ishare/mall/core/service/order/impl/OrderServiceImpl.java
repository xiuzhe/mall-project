package com.ishare.mall.core.service.order.impl;

import com.ishare.mall.common.base.dto.order.ExchangeDTO;
import com.ishare.mall.common.base.dto.order.OrderDeliverDTO;
import com.ishare.mall.common.base.dto.order.OrderDetailDTO;
import com.ishare.mall.common.base.dto.order.OrderItemDetailDTO;
import com.ishare.mall.common.base.enumeration.OrderItemState;
import com.ishare.mall.common.base.enumeration.OrderState;
import com.ishare.mall.core.exception.OrderServiceException;
import com.ishare.mall.core.model.information.Channel;
import com.ishare.mall.core.model.member.Member;
import com.ishare.mall.core.model.order.GeneratedOrderId;
import com.ishare.mall.core.model.order.Order;
import com.ishare.mall.core.model.order.OrderDeliverInfo;
import com.ishare.mall.core.model.order.OrderItem;
import com.ishare.mall.core.model.product.Product;
import com.ishare.mall.core.model.product.ProductStyle;
import com.ishare.mall.core.repository.deliver.DeliverRepository;
import com.ishare.mall.core.repository.information.OrderItemRepository;
import com.ishare.mall.core.repository.order.GeneratedOrderIdRepository;
import com.ishare.mall.core.repository.order.OrderRepository;
import com.ishare.mall.core.repository.product.ProductRepository;
import com.ishare.mall.core.repository.product.ProductStyleRepository;
import com.ishare.mall.core.service.information.ChannelService;
import com.ishare.mall.core.service.member.MemberService;
import com.ishare.mall.core.service.order.OrderService;
import com.ishare.mall.core.utils.filter.DynamicSpecifications;
import com.ishare.mall.core.utils.filter.SearchFilter;
import com.ishare.mall.core.utils.mapper.MapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private GeneratedOrderIdRepository generatedOrderIdRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductStyleRepository styleRepository;
	@Autowired
	private OrderItemRepository itemRepository;
	@Autowired
	private DeliverRepository deliverRepository;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ChannelService channelService;

	@Override
	public Order findOne(String id) {
		return orderRepository.findOne(id);
	}
	@Override
	public Page<Order> search(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters == null ? null : filters.values(), Order.class);
		Page<Order> page = orderRepository.findAll(spec, pageRequest);
		return page;
    }

	public List<Order> findTotalSales() {

		return orderRepository.findAll();
	}
	
	@Override
	public List<Order> findByCreateBy(String createBy) {
	    List<Order> order = orderRepository.findByCreateBy(createBy);
	    if (order == null || order.size() == 0) return null;
	    return order;
	}
	
	@Override
	public Order createNewOrder(Order order) {
		Date current=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String date=sdf.format(current);
		GeneratedOrderId generatedOrderId = generatedOrderIdRepository.findOne(date);
		if(null == generatedOrderId){
			GeneratedOrderId go = new GeneratedOrderId();
			go.setId(date);
			go.setOrderId(1);
			generatedOrderIdRepository.save(go);
			String orderIdStr = String.format("%06d", go.getOrderId());     
			order.setOrderId(date + orderIdStr);
			return orderRepository.save(order);
		}
		generatedOrderId.setOrderId(generatedOrderId.getOrderId()+1);
		generatedOrderIdRepository.save(generatedOrderId);
		String orderIdStr = String.format("%06d",generatedOrderId.getOrderId()+1);
		order.setOrderId(date + orderIdStr);
		return orderRepository.save(order);
		
	}

	@Override
	public Order payComplete(String orderId) {
		Order order = orderRepository.findOne(orderId);
		if (order != null) {
			order.setState(OrderState.WAIT_DELIVER);
			order = orderRepository.save(order);
		}
		return order;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public OrderDetailDTO create(ExchangeDTO exchangeDTO) {
		return initProcessor(exchangeDTO);
	}

	@Override
	public Order confirm(String id) {
		return null;
	}

	@Override
	public Order cancel(String id) {
		return null;
	}

	@Override
	public Page<Order> findByChannelId(Integer channelId,
			PageRequest pageRequest) {
		Page<Order> page = orderRepository.findByChannelId(channelId, pageRequest);
		return page;
	}

	//订单生成流程
	private OrderDetailDTO initProcessor(ExchangeDTO exchangeDTO) {
		Order order = new Order();
		OrderDetailDTO detailDTO = new OrderDetailDTO();
		Channel channel = channelService.findByAppId(exchangeDTO.getClientId());
		Member buyer = memberService.findByAccount(exchangeDTO.getAccount());
		String orderId = this.nextOrderId();
		log.debug("ID : " + orderId);
		order.setOrderId(orderId);
		order.setCreateTime(new Date());
		order.setChannel(channel);
		List<OrderItem> orderItems = this.initItemProcessor(order, exchangeDTO);
		Float total = 0f;
		//费用计算
		for (OrderItem orderItem : orderItems) {
			total += orderItem.getAmount() * orderItem.getProductPrice();
		}
		order.setProductTotalPrice(total);
		//商品费用
		//TODO 运费
		Float transFee = 0f;
		//总计
		order.setTotalPrice(total + transFee);
		//实际支付
		order.setPayableFee(total + transFee);
		//支付方式
		order.setPaymentWay(exchangeDTO.getPaymentWay());
		//等待支付状态 TODO 如果这里的支付方式可选货到付款之后需要变动
		order.setState(OrderState.WAIT_PAYMENT);
		//是否支付
		order.setPaymentState(false);
		//保存 返回
		//修改商品库存
		order.setCreateBy(buyer);
		//收货人
		OrderDeliverInfo orderDeliverInfo = this.initDeliverProcessor(order, exchangeDTO);
		try {
			order.setOrderDeliverInfo(orderDeliverInfo);
			orderRepository.save(order);
			itemRepository.save(orderItems);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new OrderServiceException("订单保存失败");
		}
		//设置收货人信息
		detailDTO.setDeliver((OrderDeliverDTO) MapperUtils.map(orderDeliverInfo, OrderDeliverDTO.class));
		//设置订单项
		detailDTO.setItems((Set<OrderItemDetailDTO>) MapperUtils.mapAsSet(orderItems, OrderItemDetailDTO.class));
		return detailDTO;
	}

	/**
	 * 初始化订单收货地址
	 * @param order
	 * @param exchangeDTO
	 * @return
	 */
	private OrderDeliverInfo initDeliverProcessor(Order order, ExchangeDTO exchangeDTO) {
		OrderDeliverInfo orderDeliverInfo = new OrderDeliverInfo();
		//orderDeliverInfo.setOrder(order);
		orderDeliverInfo.setCountry(exchangeDTO.getCountry());
		orderDeliverInfo.setProvince(exchangeDTO.getProvince());
		orderDeliverInfo.setCity(exchangeDTO.getCity());
		orderDeliverInfo.setDistrict(exchangeDTO.getDistrict());
		orderDeliverInfo.setDetail(exchangeDTO.getDetail());
		orderDeliverInfo.setMobile(exchangeDTO.getMobile());
		orderDeliverInfo.setEmail(exchangeDTO.getEmail());
		orderDeliverInfo.setRecipients(exchangeDTO.getRecipients());
		orderDeliverInfo.setTel(exchangeDTO.getTel());
		orderDeliverInfo.setRequirement(exchangeDTO.getRequirement());
		//设置快递方式
		orderDeliverInfo.setDeliverWay(exchangeDTO.getDeliverWay());
		return orderDeliverInfo;
	}

	/**
	 * 初始化订单项
	 * @param order
	 * @param exchangeDTO
	 * @return
	 */
	private List<OrderItem> initItemProcessor(Order order, ExchangeDTO exchangeDTO) {

		List<OrderItem> orderItems = new ArrayList<>();
		// TODO 暂时单个商品
		Product product = productRepository.findOne(exchangeDTO.getProductId());
		ProductStyle style = styleRepository.findOne(exchangeDTO.getStyleId());

		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		//设置样式相关
		orderItem.setStyleId(exchangeDTO.getStyleId());
		if (style != null) {
			orderItem.setStyleName(style.getName());
			orderItem.setImageUrl(style.getImageUrl());
		}
		//设置商品相关
		orderItem.setAmount(exchangeDTO.getAmount());
		orderItem.setProductId(product.getId());
		orderItem.setProductName(product.getName());
		orderItem.setState(OrderItemState.COMPLETE_VERIFY);
		//销售价格
		orderItem.setProductPrice(product.getSellPrice());

		orderItems.add(orderItem);

		return orderItems;
	}

	//获取下一个订单号
	private String nextOrderId() {
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(current);
		GeneratedOrderId generatedOrderId = generatedOrderIdRepository.findOne(date);
		if(null == generatedOrderId){
			GeneratedOrderId go = new GeneratedOrderId();
			go.setId(date);
			go.setOrderId(1);
			generatedOrderIdRepository.save(go);
			return date + String.format("%06d", go.getOrderId());
		}
		generatedOrderId.setOrderId(generatedOrderId.getOrderId() + 1);
		generatedOrderIdRepository.save(generatedOrderId);
		System.out.println("date : " + date);
		System.out.println("orderID : " + date + String.format("%06d", generatedOrderId.getOrderId()));
		return date + String.format("%06d", generatedOrderId.getOrderId());
	}
}
