package wwwwy.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.domain.MiaoshaOrder;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.mapper.MiaoshaGoodsMapper;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.service.IMiaoshaGoodsService;
import wwwwy.miaosha.service.IMiaoshaOrderService;
import wwwwy.miaosha.service.IOrderInfoService;
import wwwwy.miaosha.vo.GoodsVo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
@Service
public class MiaoshaGoodsServiceImpl extends ServiceImpl<MiaoshaGoodsMapper, MiaoshaGoods> implements IMiaoshaGoodsService {

	@Autowired
	IGoodsService iGoodsService;
	@Autowired
	IOrderInfoService iOrderInfoService;
	@Autowired
	IMiaoshaOrderService iMiaoshaOrderService;
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Override
	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
		MiaoshaGoods miaoshaGoods=new MiaoshaGoods();
		miaoshaGoods.setGoodsId(goodsVo.getId());
		int i = iGoodsService.reduceStock(miaoshaGoods);
		if (i>0){
			OrderInfo orderInfo=new OrderInfo();
			orderInfo.setGoodsId(goodsVo.getId());
			orderInfo.setDeliveryAddrId(0L);
			orderInfo.setGoodsCount(1);
			orderInfo.setGoodsName(goodsVo.getGoodsName());
			orderInfo.setGoodsPrice(new BigDecimal(goodsVo.getMiaoshaPrice()));
			orderInfo.setOrderChannel(1);
			orderInfo.setStatus(0);
			orderInfo.setUserId(user.getId());
			iOrderInfoService.save(orderInfo);
			MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
			miaoshaOrder.setGoodsId(goodsVo.getId());
			miaoshaOrder.setOrderId(orderInfo.getId());
			miaoshaOrder.setUserId(user.getId());
			iMiaoshaOrderService.save(miaoshaOrder);
			return orderInfo;
		}else {
			setGoodsOver(goodsVo.getId());
			return null;
		}
	}
	@Override
	public void setGoodsOver(Long goodsId) {
		redisTemplate.opsForValue().set("msgood"+goodsId,"true");
	}
	@Override
	public boolean getGoodsOver(Long goodsId){
		Object o = redisTemplate.opsForValue().get("msgoods" + goodsId);
		return o==null?true:false;
	}
}
