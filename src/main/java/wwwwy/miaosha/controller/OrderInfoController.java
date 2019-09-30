package wwwwy.miaosha.controller;


import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wwwwy.miaosha.domain.Goods;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.service.IMiaoshaUserService;
import wwwwy.miaosha.service.IOrderInfoService;
import wwwwy.miaosha.vo.GoodsVo;
import wwwwy.miaosha.vo.OrderDetailVo;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
@Controller
@RequestMapping("/order")
public class OrderInfoController {
	@Autowired
	IMiaoshaUserService iMiaoshaUserService;
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Autowired
	IOrderInfoService iOrderInfoService;
	@Autowired
	IGoodsService iGoodsService;


	@GetMapping("/detail")
	@ResponseBody
	public Result<OrderDetailVo> info(MiaoshaUser user, @RequestParam("orderId")Long orderId){
		if (user==null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		OrderInfo orderInfo = iOrderInfoService.getById(orderId);
		if (orderInfo==null){
			return Result.error(CodeMsg.ORDER_NOT_EXIST);
		}
		GoodsVo goodsVo = iGoodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
		OrderDetailVo orderDetailVo=new OrderDetailVo();
		orderDetailVo.setGoodsVo(goodsVo);
		orderDetailVo.setOrderInfo(orderInfo);
		return Result.success(orderDetailVo);
	}


}
