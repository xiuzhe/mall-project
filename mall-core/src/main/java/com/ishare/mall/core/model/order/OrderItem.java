package com.ishare.mall.core.model.order;

import com.ishare.mall.common.base.enumeration.OrderItemSort;
import com.ishare.mall.common.base.enumeration.OrderItemState;
import com.ishare.mall.core.model.base.BaseEntity;

import javax.persistence.*;

import static com.ishare.mall.common.base.constant.DataBaseConstant.Table.TABLE_ORDER_ITEM_NAME;

/**
 * Created by YinLin on 2015/7/31.
 * Description:
 * Version 1.0
 */
@Entity
@Table(name = TABLE_ORDER_ITEM_NAME)
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /* 产品名称 */
    @Column(length = 50,nullable = false, name = "item_product_name")
    private String productName;
    /* 产品id */
    @Column(nullable = false, name = "item_product_id",length = 25)
    private Integer productId;
    /* 产品销售价 */
    @Column(nullable = false, name = "item_product_price",length = 8)
    private Float productPrice = 0f;
    /* 购买数量 */
    @Column(nullable = false, name = "item_product_amount",length = 3)
    private Integer amount = 1;
    /* 产品样式 */
    @Column(length = 30, nullable = true, name = "item_product_style_name")
    private String styleName;
    /* 产品样式ID */
    @Column(nullable = true, name = "item_product_style_id",length = 50)
    private Long styleId;
    /* 产品图片地址 */
    @Column(nullable = true, name = "item_product_image_url",length = 200)
    private String imageUrl;
    /* 产品状态 */
    @Enumerated(EnumType.STRING)
    @Column(length=16, nullable = true, name = "item_state")
    private OrderItemState state;
   
    /* 购买者*/
    @Column(length=16, nullable = true, name = "create_by")
    private String createBy;
    	
    /*退换货标记*/
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "item_exchange_or_back",length = 6)
    private OrderItemSort exchangeOrBack;
    
    /* 所属订单 */
    @ManyToOne(cascade= CascadeType.REFRESH, optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
    /**渠道Id**/
    @Column(length = 11, name="channel_id")
    private Integer channelId;
    public OrderItem(){}
    public OrderItem(String productName, Integer productId, Float productPrice,
                     Integer amount, String styleName, Long styleId) {
        this.productName = productName;
        this.productId = productId;
        this.productPrice = productPrice;
        this.amount = amount;
        this.styleName = styleName;
        this.styleId = styleId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Float getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getStyleName() {
        return styleName;
    }
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public OrderItemState getState() {
		return state;
	}
    
	public void setState(OrderItemState state) {
		this.state = state;
	}
	
	public OrderItemSort getExchangeOrBack() {
		return exchangeOrBack;
	}
	
	public void setExchangeOrBack(OrderItemSort exchangeOrBack) {
		this.exchangeOrBack = exchangeOrBack;
	}
	
	public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? super.hashCode() : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderItem other = (OrderItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productId=" + productId +
                ", productPrice=" + productPrice +
                ", amount=" + amount +
                ", styleName='" + styleName + '\'' +
                ", styleId=" + styleId +
                ", imageUrl='" + imageUrl + '\'' +
                ", state=" + state +
                ", createBy='" + createBy + '\'' +
                ", exchangeOrBack=" + exchangeOrBack +
                ", order=" + order +
                '}';
    }
}
