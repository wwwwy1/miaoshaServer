package wwwwy.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wwwwy.miaosha.domain.Goods;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
public interface IGoodsService extends IService<Goods> {
	List<GoodsVo> getGoodsVoList();
	GoodsVo getGoodsVoByGoodsId(Long id);
	int reduceStock(MiaoshaGoods g);
}
