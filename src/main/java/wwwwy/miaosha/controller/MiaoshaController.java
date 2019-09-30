package wwwwy.miaosha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.domain.MiaoshaOrder;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.service.IMiaoshaGoodsService;
import wwwwy.miaosha.service.IMiaoshaOrderService;
import wwwwy.miaosha.service.IOrderInfoService;
import wwwwy.miaosha.vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

	@Autowired
	IMiaoshaGoodsService iMiaoshaGoodsService;
	@Autowired
	IGoodsService iGoodsService;
	@Autowired
	IOrderInfoService iOrderInfoService;
	@Autowired
	IMiaoshaOrderService iMiaoshaOrderService;

	@PostMapping("do_miaosha")
	@ResponseBody
	public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") Long goodsId){
		model.addAttribute("user",user);
		if (user==null){
			return Result.error(CodeMsg.SESSION_ERROR);
			//return "login";
		}
		GoodsVo goodsVo = iGoodsService.getGoodsVoByGoodsId(goodsId);
		if (goodsVo==null || goodsVo.getStockCount()<=0 ){
			model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
			return Result.error(CodeMsg.MIAO_SHA_OVER);
			//return "miaosha_fail";
		}
		QueryWrapper<MiaoshaOrder> wrapper=new QueryWrapper<>();
		wrapper.eq("goods_id",goodsId);
		wrapper.eq("user_id",user.getId());
		MiaoshaOrder one = iMiaoshaOrderService.getOne(wrapper);
		if (one!=null){
			model.addAttribute("errmsg",CodeMsg.REPEATE_MIAOSHA.getMsg());
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
			//return "miaosha_fail";
		}
		OrderInfo orderInfo = iMiaoshaGoodsService.miaosha(user, goodsVo);
		//model.addAttribute("orderInfo",orderInfo);
		//model.addAttribute("goods",goodsVo);
		return Result.success(orderInfo);
	}
}
