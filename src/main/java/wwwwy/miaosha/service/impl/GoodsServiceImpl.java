package wwwwy.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wwwwy.miaosha.domain.Goods;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.mapper.GoodsMapper;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Override
	public List<GoodsVo> getGoodsVoList(){
		return baseMapper.listGoodsVo();
	}

	@Override
	public GoodsVo getGoodsVoByGoodsId(Long id) {
		return baseMapper.getGoodsVoByGoodsId(id);
	}

	@Override
	public int reduceStock(MiaoshaGoods g) {

		return baseMapper.reduceStock(g);
	}
}
