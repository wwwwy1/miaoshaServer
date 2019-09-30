package wwwwy.miaosha.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.service.IMiaoshaUserService;
import wwwwy.miaosha.vo.GoodsDetailVo;
import wwwwy.miaosha.vo.GoodsVo;
import wwwwy.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {
	@Autowired
	IMiaoshaUserService iMiaoshaUserService;
	@Autowired
	IGoodsService iGoodsService;
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	@GetMapping(value = "to_list",produces = "text/html")
	@ResponseBody
	public String toLogin(Model model, MiaoshaUser user, HttpServletResponse response, HttpServletRequest request){
		String html = (String) redisTemplate.opsForValue().get("goodsList");
		if (!StringUtils.isEmpty(html)){
			return html;
		}
		model.addAttribute("user",user);
		List<GoodsVo> goodsVoList = iGoodsService.getGoodsVoList();
		model.addAttribute("goodsList",goodsVoList);
		WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list",webContext);
		if (!StringUtils.isEmpty(html)){
			redisTemplate.opsForValue().set("goodsList",html);
		}
		return html;
	}
	@GetMapping(value = "to_detail2/{goodsId}",produces = "text/html")
	@ResponseBody
	public String detail2(Model model, MiaoshaUser user, @PathVariable("goodsId")Long goodsId , HttpServletResponse response, HttpServletRequest request){
		String html = (String) redisTemplate.opsForValue().get("goodsDetail"+goodsId);
		if (!StringUtils.isEmpty(html)){
			return html;
		}
		model.addAttribute("user",user);
		GoodsVo goods=iGoodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods",goods);
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long now =System.currentTimeMillis();
		int miaoshaStatus = 0;
		int remainSeconds =0;
		if (now<startTime){
			miaoshaStatus = 0;
			remainSeconds =	(int)(startTime - now)/1000;
		}else if (now>endTime){
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else {
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		model.addAttribute("miaoshaStatus",miaoshaStatus);
		model.addAttribute("remainSeconds",remainSeconds);
		WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
		if (!StringUtils.isEmpty(html)){
			redisTemplate.opsForValue().set("goodsDetail"+goodsId,html);
		}
		return html;
	}
	@GetMapping(value = "detail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> detail(MiaoshaUser user, @PathVariable("goodsId") Long goodsId){
		//model.addAttribute("user",user);
		GoodsVo goods=iGoodsService.getGoodsVoByGoodsId(goodsId);
		//model.addAttribute("goods",goods);
		long startTime = goods.getStartDate().getTime();
		long endTime = goods.getEndDate().getTime();
		long now =System.currentTimeMillis();
		int miaoshaStatus = 0;
		int remainSeconds =0;
		if (now<startTime){
			miaoshaStatus = 0;
			remainSeconds =	(int)(startTime - now)/1000;
		}else if (now>endTime){
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else {
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		GoodsDetailVo goodsDetailVo=new GoodsDetailVo();
		goodsDetailVo.setGoods(goods);
		goodsDetailVo.setUser(user);
		goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
		goodsDetailVo.setRemainSeconds(remainSeconds);
		return Result.success(goodsDetailVo);
	}
}
