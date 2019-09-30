package wwwwy.miaosha.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.result.Result;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-24
 */
@Controller
@RequestMapping("miaosha_user")
public class MiaoshaUserController {
	private static Logger log=LoggerFactory.getLogger(LoginController.class);

	@GetMapping("info")
	@ResponseBody
	public Result<MiaoshaUser> getUserInfo(MiaoshaUser user){
		if (user==null) return Result.error(null);
		log.info(user.toString());
		return Result.success(user);
	}
}
