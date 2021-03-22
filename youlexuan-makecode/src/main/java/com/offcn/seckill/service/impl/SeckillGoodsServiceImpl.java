package com.offcn.seckill.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.offcn.mapper.TbSeckillGoodsMapper;
import com.offcn.pojo.TbSeckillGoods;
import com.offcn.pojo.TbSeckillGoodsExample;
import com.offcn.pojo.TbSeckillGoodsExample.Criteria;
import com.offcn.seckill.service.SeckillGoodsService;

import com.offcn.entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	@Autowired
	private TbSeckillGoodsMapper seckill_goodsMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSeckillGoods> findAll() {
		return seckill_goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSeckillGoods> page=   (Page<TbSeckillGoods>) seckill_goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbSeckillGoods seckill_goods) {
		seckill_goodsMapper.insert(seckill_goods);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSeckillGoods seckill_goods){
		seckill_goodsMapper.updateByPrimaryKey(seckill_goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillGoods findOne(Long id){
		return seckill_goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			seckill_goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSeckillGoods seckill_goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSeckillGoodsExample example=new TbSeckillGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(seckill_goods!=null){			
						if(seckill_goods.getTitle()!=null && seckill_goods.getTitle().length()>0){
				criteria.andTitleLike("%"+seckill_goods.getTitle()+"%");
			}			if(seckill_goods.getSmallPic()!=null && seckill_goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+seckill_goods.getSmallPic()+"%");
			}			if(seckill_goods.getSellerId()!=null && seckill_goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+seckill_goods.getSellerId()+"%");
			}			if(seckill_goods.getStatus()!=null && seckill_goods.getStatus().length()>0){
				criteria.andStatusLike("%"+seckill_goods.getStatus()+"%");
			}			if(seckill_goods.getIntroduction()!=null && seckill_goods.getIntroduction().length()>0){
				criteria.andIntroductionLike("%"+seckill_goods.getIntroduction()+"%");
			}	
		}
		
		Page<TbSeckillGoods> page= (Page<TbSeckillGoods>)seckill_goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
