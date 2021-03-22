package com.offcn.cart.controller;

//实现思路：
//（1）从cookie中取出购物车
//（2）向购物车添加商品
//（3）将购物车存入cookie

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.offcn.cart.service.CartService;
import com.offcn.entity.Result;
import com.offcn.group.Cart;
import com.offcn.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout=6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;
    /**
     * 购物车列表
     * @param request
     * @return
     */

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        //得到登录人账号，判断当前是否有人登录
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户："+username);

        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString == null || cartListString.equals("")){
            cartListString="[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString,Cart.class);
//        return cartList_cookie;
        if (username.equals("anonymousUser")){
            //读取本地购物车
            return cartList_cookie;
        }else {//如果已登录
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);//从redis中提取

            if (cartList_cookie.size() > 0) {//如果本地存在购物车
                //合并购物车
                cartList_redis = cartService.mergeCartList(cartList_redis, cartList_cookie);
                //清除本地cookie的数据
                CookieUtil.deleteCookie(request, response, "cartList");
                //将合并后的数据存入redis
                cartService.saveCartListToRedis(username, cartList_redis);
            }

            return cartList_redis;
        }
    }

    /**
     * 添加商品到购物车
     * @param request
     * @param response
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9105",allowCredentials="true")//allowCredentials="true"  可以缺省
    public Result addGoodsToCartList(Long itemId,Integer num) {
        /*
            1.Access-Control-Allow-Origin字段:
            Access-Control-Allow-Origin是HTML5中定义的一种解决资源跨域的策略
            是通过服务器端返回带有Access-Control-Allow-Origin标识的Response header，用来解决资源的跨域权限问题
            使用方法，在response添加 Access-Control-Allow-Origin
            例如:Access-Control-Allow-Origin:www.google.com
            也可以设置为 * 表示该资源谁都可以用

            2.Access-Control-Allow-Credentials字段
            CORS请求默认不发送Cookie和HTTP认证信息。如果要把Cookie发到服务器，
            一方面要服务器同意，指定Access-Control-Allow-Credentials字段。
            另一方面，开发者必须在AJAX请求中打开withCredentials属性。
            否则，即使服务器同意发送Cookie，浏览器也不会发送。或者，服务器要求设置Cookie，浏览器也不会处理。
         */
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
//        response.setHeader("Access-Control-Allow-Credentials", "true");



        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户：" + username);

        try {
            List<Cart> cartList = findCartList();//获取购物车列表
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
//            CookieUtil.setCookie(request,response, "cartList", JSON.toJSONString(cartList),3600*24,"UTF-8");
            if (username.equals("anonymousUser")) { //如果是未登录，保存到cookie
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                System.out.println("向cookie存入数据");
            } else {//如果是已登录，保存到redis
                cartService.saveCartListToRedis(username, cartList);
            }

            return new Result(true, "添加成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
}
