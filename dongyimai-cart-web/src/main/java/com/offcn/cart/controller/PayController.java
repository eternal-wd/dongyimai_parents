package com.offcn.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.entity.Result;
import com.offcn.order.service.OrderService;
import com.offcn.pay.service.AliPayService;
import com.offcn.pojo.TbPayLog;
import com.offcn.util.IdWorker;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付控制层
 *
 * @author 吴迪
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private AliPayService aliPayService;
    @Reference(timeout = 3000)
    private OrderService orderService;
    /**
     * 生成二维码
     *
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative(){
        IdWorker idWorker = new IdWorker();
        // 订单号通过分布式ID生成器生成，金额暂时写死，后续开发我们再对接业务系统得到订单号和金额
//        return aliPayService.createNative(idWorker.nextId() + "", "2");
        /*实现思路：调用获取支付日志对象的方法，得到订单号和金额*/
        //获取当前用户
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //到redis查询支付日志
        TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        //判断支付日志存在
        if (payLog != null){
            System.out.println("redis中查到日志内容:" + payLog);// TbPayLog中添加toString方法
            System.out.println(aliPayService);
            return aliPayService.createNative(payLog.getOutTradeNo(), ((double)payLog.getTotalFee()) / 100 + "");

        }else {
            System.out.println("redis中未查到日志内容");
            return new HashMap();
        }
    }
    /**
     * 查询支付状态
     *
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        int x = 0;
        while (true){
            //调用查询接口
            Map<String,String> map = null;
            try {
                map = aliPayService.queryPayStatus(out_trade_no);
            }catch (Exception e1){
                /*e.printStackTrace();*/
                System.out.println("调用查询服务出错");
            }
            if (map == null){//出错
                result = new Result(false,"支付出错");
                break;
            }
            if (map.get("tradestatus")!=null&&map.get("tradestatus").equals("TRADE_SUCCESS")){//如果成功
                result = new Result(true, "支付成功");
                //修改订单状态
                orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));
                break;
            }
            if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_CLOSED")) {
                result = new Result(true, "未付款交易超时关闭，或支付完成后全额退款");
                break;
            }
            if (map.get("tradestatus") != null && map.get("tradestatus").equals("TRADE_FINISHED")) {//如果成功
                result = new Result(true, "交易结束，不可退款");
                break;
            }
            try {
                Thread.sleep(3000);//间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
            x++;
            if (x >= 100) {
                result = new Result(false, "二维码超时");
                break;
            }
        }
        return result;
        }
}
