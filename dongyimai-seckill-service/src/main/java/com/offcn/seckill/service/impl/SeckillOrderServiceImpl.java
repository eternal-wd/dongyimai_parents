package com.offcn.seckill.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.offcn.mapper.TbSeckillOrderMapper;
import com.offcn.pojo.TbSeckillOrder;
import com.offcn.pojo.TbSeckillOrderExample;
import com.offcn.pojo.TbSeckillOrderExample.Criteria;
import com.offcn.seckill.service.SeckillOrderService;

import com.offcn.entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	@Autowired
	private TbSeckillOrderMapper seckill_orderMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillOrder> findAll() {
		return seckill_orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSeckillOrder> page=   (Page<TbSeckillOrder>) seckill_orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillOrder seckill_order) {
		seckill_orderMapper.insert(seckill_order);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillOrder seckill_order){
		seckill_orderMapper.updateByPrimaryKey(seckill_order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillOrder findOne(Long id){
		return seckill_orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			seckill_orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSeckillOrder seckill_order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSeckillOrderExample example=new TbSeckillOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(seckill_order!=null){			
						if(seckill_order.getUserId()!=null && seckill_order.getUserId().length()>0){
				criteria.andUserIdLike("%"+seckill_order.getUserId()+"%");
			}			if(seckill_order.getSellerId()!=null && seckill_order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+seckill_order.getSellerId()+"%");
			}			if(seckill_order.getStatus()!=null && seckill_order.getStatus().length()>0){
				criteria.andStatusLike("%"+seckill_order.getStatus()+"%");
			}			if(seckill_order.getReceiverAddress()!=null && seckill_order.getReceiverAddress().length()>0){
				criteria.andReceiverAddressLike("%"+seckill_order.getReceiverAddress()+"%");
			}			if(seckill_order.getReceiverMobile()!=null && seckill_order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+seckill_order.getReceiverMobile()+"%");
			}			if(seckill_order.getReceiver()!=null && seckill_order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+seckill_order.getReceiver()+"%");
			}			if(seckill_order.getTransactionId()!=null && seckill_order.getTransactionId().length()>0){
				criteria.andTransactionIdLike("%"+seckill_order.getTransactionId()+"%");
			}	
		}
		
		Page<TbSeckillOrder> page= (Page<TbSeckillOrder>)seckill_orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
