package wwwwy.miaosha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IMiaoshaUserService;
import wwwwy.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("login")
public class LoginController {
	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;
	@Autowired
	private IMiaoshaUserService iMiaoshaUserService;
	private static Logger log=LoggerFactory.getLogger(LoginController.class);
	@GetMapping("to_login")
	public String toLogin(){
		return "login";
	}
	@PostMapping("do_login")
	@ResponseBody
	public Result<Boolean> doLogin(@Valid LoginVo loginVo, HttpServletResponse response){
		log.info(loginVo.toString());
		iMiaoshaUserService.login(response,loginVo);
		return Result.success(true);
	}

}
