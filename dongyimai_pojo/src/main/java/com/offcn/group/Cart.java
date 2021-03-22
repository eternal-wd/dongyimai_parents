package com.offcn.group;

import com.offcn.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    // 缓存这个对象时,此句就一定要加,不然序列化和反序列化时随机生成的id对应不上,会报错
    // 本次要将对象放到redis中缓存,所以要加
    // 建议养成习惯,只要序列化就添加此句(id值随意但不要重复)
    // 若开始忘记添加，需要删除缓存中的对象，如redis中的字段，再重新运行
    private static final long serialVersionUID = 4125096758372084309L;

    private String sellerId;//商家ID
    private String sellerName;//商家名称
    private List<TbOrderItem> orderItemList;//购物车明细

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
