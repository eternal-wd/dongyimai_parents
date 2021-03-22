package com.offcn.cart.service;

import com.offcn.group.Cart;

import java.util.List;

/**
 * 购物车服务接口
 * @author Administrator
 *
 */
public interface CartService {

    /**
     * 添加商品到购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num );

    /**
     * 从redis中查询购物车
     *
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车保存到redis
     *
     * @param username
     * @param cartList
     */
    void saveCartListToRedis(String username, List<Cart> cartList);

    List<Cart> mergeCartList(List<Cart> cartList_redis, List<Cart> cartList_cookie);
}