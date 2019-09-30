package wwwwy.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.domain.MiaoshaOrder;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.vo.GoodsVo;
import wwwwy.miaosha.vo.LoginVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
public interface IMiaoshaGoodsService extends IService<MiaoshaGoods> {
	OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo);
	void setGoodsOver(Long goodsId);
	boolean getGoodsOver(Long goodsId);
}
