package wwwwy.miaosha.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wwwwy.miaosha.access.AccessLimit;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.domain.MiaoshaOrder;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.rabbitmq.MQSender;
import wwwwy.miaosha.rabbitmq.MiaoshaMessage;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.service.IMiaoshaGoodsService;
import wwwwy.miaosha.service.IMiaoshaOrderService;
import wwwwy.miaosha.service.IOrderInfoService;
import wwwwy.miaosha.util.MD5Util;
import wwwwy.miaosha.util.UUIDUtil;
import wwwwy.miaosha.vo.GoodsVo;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

	@Autowired
	IMiaoshaGoodsService iMiaoshaGoodsService;
	@Autowired
	IGoodsService iGoodsService;
	@Autowired
	IOrderInfoService iOrderInfoService;
	@Autowired
	IMiaoshaOrderService iMiaoshaOrderService;
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Autowired
	MQSender mqSender;
	private Map<Long,Boolean> goodsMap= new HashMap<>();
	@PostMapping("/{path}/do_miaosha")
	@ResponseBody
	public Result<Integer> doMiaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") Long goodsId
	,@PathVariable("path") String path){
		model.addAttribute("user",user);
		if (user==null){
			return Result.error(CodeMsg.SESSION_ERROR);
			//return "login";
		}
		String o = (String)redisTemplate.opsForValue().get("path" + user.getId() + "_" + goodsId);
		if (!o.equals(path))return Result.error(CodeMsg.REQUEST_ILLEGAL);
		//利用map进行标记，减少redis访问
		if (goodsMap.get(goodsId)) return Result.error(CodeMsg.MIAO_SHA_OVER); ;
		//预减库存
		long stock = Long.parseLong(redisTemplate.opsForValue().get("goods"+goodsId).toString());
		if (--stock<0){
			goodsMap.put(goodsId,true);
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		QueryWrapper<MiaoshaOrder> wrapper=new QueryWrapper<>();
		wrapper.eq("goods_id",goodsId);
		wrapper.eq("user_id",user.getId());
		MiaoshaOrder order = iMiaoshaOrderService.getOne(wrapper);
		if (order!=null){
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		// 入队
		MiaoshaMessage message = new MiaoshaMessage();
		message.setUser(user);
		message.setGoodsId(goodsId);
		mqSender.sendMiaoshaMessage(message);
		return Result.success(0);

		/*GoodsVo goodsVo = iGoodsService.getGoodsVoByGoodsId(goodsId);
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
		return Result.success(orderInfo);*/
	}
	/*
	* 系统初始化
	* */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsVoList = iGoodsService.getGoodsVoList();
		if (goodsVoList == null){
			return;
		}
		for (GoodsVo goodsVo : goodsVoList) {
			redisTemplate.opsForValue().set("goods"+goodsVo.getId(),goodsVo.getStockCount());
			goodsMap.put(goodsVo.getId(),false);
		}
	}
	//order：success
	//-1：秒杀
	//0：排队中
	@GetMapping("/result")
	@ResponseBody
	public Result<Long> miaoshaResult(Model model,MiaoshaUser user,@RequestParam("goodsId")long goodsId){
		model.addAttribute("user",user);
		if (user==null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		QueryWrapper<MiaoshaOrder> wq=new QueryWrapper<>();
		wq.eq("user_id",user.getId());
		wq.eq("goods_id",goodsId);
		MiaoshaOrder miaoshaOrder = iMiaoshaOrderService.getOne(wq);
		if (miaoshaOrder==null){
			return Result.success(goodsId);
		}else {
			if (iMiaoshaGoodsService.getGoodsOver(goodsId)){
				return Result.success(-1L);
			}else {
				return Result.success(0L);
			}
		}
	}
	@AccessLimit(secounds=5,maxCount=5,needLogin=true)
	@GetMapping("/path")
	@ResponseBody
	public Result<String> getMiaosha(HttpServletRequest request,MiaoshaUser user, @RequestParam("goodsId")long goodsId){
		if (user==null)return Result.error(CodeMsg.SESSION_ERROR);
		//查询访问次数，接口限制流量
		/*String requestURI = request.getRequestURI();
		Integer num = (Integer) redisTemplate.opsForValue().get("access" + requestURI + "_" + user.getId());
		if (num==null){
			redisTemplate.opsForValue().set("access" + requestURI + "_" + user.getId(),1);
		}else if (num<5){
			redisTemplate.opsForValue().set("access" + requestURI + "_" + user.getId(),num+1);
		}else {
			return Result.error(CodeMsg.ACCESS_LIMIT);
		}*/
		String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
		redisTemplate.opsForValue().set("path"+user.getId()+"_"+goodsId,str);
		return Result.success(str);
	}
}
