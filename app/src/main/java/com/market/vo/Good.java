package com.market.vo;

import java.io.Serializable;

public class Good implements Serializable {

    private Long id;

    private String goodName;

    private Integer num;

    private Long merchantId;

    public Good() {
    }

    public Good(String name, Integer num, Long sId) {
        this.goodName = name;
        this.num = num;
        this.merchantId = sId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
