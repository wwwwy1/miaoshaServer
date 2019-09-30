package wwwwy.miaosha.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wwwwy.miaosha.domain.User;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IUserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-24
 */
@RestController
@RequestMapping("/user")

public class UserController {
	@Autowired
	private IUserService iUserService;
	@GetMapping("/get/{id}")
	public Result<String> getById(@PathVariable("id") Integer id){
		User byId = iUserService.getById(id);
		return Result.success(byId.getName());
	}
	@GetMapping("/add")
	public Object addId(User user){
		boolean save = iUserService.save(user);
		return save;
	}
}
