package com.ishare.mall.crawler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "spider_base_page_data")
public class BasePageData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FetchSite fetchSite;

    //@OneToOne(cascade = CascadeType.ALL)
    //private FetchUrl fetchUrl;

    private String code;
    private String name;
    @Column(length = 1024)
    private String link;
    private String priceText;
    private String priceOriginText;
    private String stock;
    private String tag;
    private String datetimeText;//上架时间

    @Type(type = "yes_no")
    @Column(name = "is_self")
    private boolean self;//是否自营

    private String thirdPartyShopName;//第三方店铺名称

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "spider_base_page_data_attr", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> attributes = Maps.newHashMap();
    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    @CollectionTable(name = "spider_base_page_data_intro_image", joinColumns = @JoinColumn(name = "data_id"))
    @Column(name = "intro_image_url")
    private Set<String> introImages = Sets.newHashSet();
    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    @CollectionTable(name = "spider_base_page_data_photo", joinColumns = @JoinColumn(name = "data_id"))
    @Column(name = "photo_url")
    private Set<String> photos = Sets.newHashSet();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*
    public FetchUrl getFetchUrl() {
        return fetchUrl;
    }

    public void setFetchUrl(FetchUrl fetchUrl) {
        this.fetchUrl = fetchUrl;
    }
    */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public String getPriceOriginText() {
        return priceOriginText;
    }

    public void setPriceOriginText(String priceOriginText) {
        this.priceOriginText = priceOriginText;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDatetimeText() {
        return datetimeText;
    }

    public void setDatetimeText(String datetimeText) {
        this.datetimeText = datetimeText;
    }

    public boolean getSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getThirdPartyShopName() {
        return thirdPartyShopName;
    }

    public void setThirdPartyShopName(String thirdPartyShopName) {
        this.thirdPartyShopName = thirdPartyShopName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Set<String> getIntroImages() {
        return introImages;
    }

    public void setIntroImages(Set<String> introImages) {
        this.introImages = introImages;
    }

    public Set<String> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<String> photos) {
        this.photos = photos;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FetchSite getFetchSite() {
        return fetchSite;
    }

    public void setFetchSite(FetchSite fetchSite) {
        this.fetchSite = fetchSite;
    }

    @Override
    public String toString() {
        return "BasePageData{" +
                "id=" + id +
                ", fetchSite=" + fetchSite +
                //", fetchUrl=" + fetchUrl +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", priceText='" + priceText + '\'' +
                ", priceOriginText='" + priceOriginText + '\'' +
                ", stock='" + stock + '\'' +
                ", tag='" + tag + '\'' +
                ", datetimeText='" + datetimeText + '\'' +
                ", self=" + self +
                ", thirdPartyShopName='" + thirdPartyShopName + '\'' +
                ", updateTime=" + updateTime +
                ", attributes=" + attributes.size() +
                ", introImages=" + introImages.size() +
                ", photos=" + photos.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasePageData that = (BasePageData) o;

        if (!code.equals(that.code)) return false;
        return link.equals(that.link);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + link.hashCode();
        return result;
    }
}
