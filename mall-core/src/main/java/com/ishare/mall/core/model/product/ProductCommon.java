package com.ishare.mall.core.model.product;

import static com.ishare.mall.common.base.constant.DataBaseConstant.Table.TABLE_PRODUCT_COMMON;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.ishare.mall.core.model.base.BaseEntity;
import com.ishare.mall.core.model.information.Brand;
import com.ishare.mall.core.model.information.Channel;
import com.ishare.mall.core.model.information.Origin;
import com.ishare.mall.core.model.member.Member;

/**
 * Created by liaochenglei on 2015/11/16.
 * Description: 商品信息
 * Version 1.0
 */
@Table(name = TABLE_PRODUCT_COMMON)
@Entity
public class ProductCommon extends BaseEntity {
    @Id@Column( name = "common_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer commonId;
 
    public Integer getCommonId() {
		return commonId;
	}

	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}

	@Column(name = "product_code",length = 15)
    private String code;
    //物品名字
    @Column(name = "product_name",length = 512)
    private String name;
    //物品类型 便于search 10010010001
    @Column(name = "product_type_code",length = 15)
    private String typeCode;
    //物品
    @Column(name = "product_model",length = 25)
    private String model;
    //商品进价
    @Column(name = "product_base_price",length = 8)
    private Float basePrice;
    //市场价格
    @Column(name = "product_market_price",length = 8)
    private Float marketPrice;
    //卖出价格
    @Column(name = "product_sell_price",length = 8)
    private Float sellPrice;
    //商品重量 单位（g）
    @Column(name = "product_weight",length = 6)
    private Integer weight;
    //描述
    @Column(name = "product_description",length = 150)
    private String description;
    //购买须知
    @Column(name = "product_buy_explain",length = 150)
    private String buyExplain;
    //是否可见
    @Column(name = "product_visible",length = 5)
    private Boolean visible = true;
    //点击人气
    @Column(name = "product_click_count",length = 7)
    private Integer clickCount = 1;
    //卖出人气
    @Column(name = "product_sell_count",length = 7)
    private Integer sellCount = 0;
    //是否推荐
    @Column(name = "product_commend",length = 5)
    private Boolean commend = Boolean.FALSE;
    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "product_create_time",length = 20)
    private Date createTime;
    //更新时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "product_update_time",length = 20)
    private Date updateTime;
    //默认的图片地址 冗余
    @Column(name = "default_img_url",length = 200)
    private String defaultImageUrl;
    //创建者
    @ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by_member_id")
    private Member createBy;
    //更新者
    @JsonIgnore
    @ManyToOne(cascade= CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by_member_id")
    private Member updateBy;

    @JsonIgnore
    @ManyToOne(cascade= CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_brand_id")
    private Brand brand;//品牌

    @JsonIgnore
    @ManyToOne(cascade= CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_channel_id")
    private Channel channel;

    @JsonIgnore
    @ManyToOne(cascade= CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id")
    private ProductType type;
    
         //是否自营
    @Column(name = "is_self")
    private Boolean self;
  //属性组的名称
    @Column(name ="attribute_Group_Name")
    private String arrtibuteGroupName;
    
    //包含的属性
    @Column(name ="attribute_name")
    private String attributeName;
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Float basePrice) {
		this.basePrice = basePrice;
	}

	public Float getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Float marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Float getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBuyExplain() {
		return buyExplain;
	}

	public void setBuyExplain(String buyExplain) {
		this.buyExplain = buyExplain;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	public Boolean getCommend() {
		return commend;
	}

	public void setCommend(Boolean commend) {
		this.commend = commend;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDefaultImageUrl() {
		return defaultImageUrl;
	}

	public void setDefaultImageUrl(String defaultImageUrl) {
		this.defaultImageUrl = defaultImageUrl;
	}

	public Member getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Member createBy) {
		this.createBy = createBy;
	}

	public Member getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Member updateBy) {
		this.updateBy = updateBy;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public Boolean getSelf() {
		return self;
	}

	public void setSelf(Boolean self) {
		this.self = self;
	}

	public String getArrtibuteGroupName() {
		return arrtibuteGroupName;
	}

	public void setArrtibuteGroupName(String arrtibuteGroupName) {
		this.arrtibuteGroupName = arrtibuteGroupName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCommon product = (ProductCommon) o;

        return commonId.equals(product.commonId);

    }

    @Override
    public int hashCode() {
        return commonId.hashCode();
    }

}
