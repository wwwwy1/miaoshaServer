package wwwwy.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wwwwy.miaosha.domain.User;
import wwwwy.miaosha.rabbitmq.MQSender;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.util.UserUtil;

import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/demo")
public class SampleController {
	@Autowired
	MQSender sender;
	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;
	@GetMapping("/thymeleaf")
	public String thymeleaf(Model model){
		model.addAttribute("name","wwwwy");
		return "hello";
	}
	@GetMapping("/redis/get")
	@ResponseBody
	public Object getReids(String key){
		User sd=new User();
		sd.setId(123);
		sd.setName("df");
		redisTemplate.opsForValue().set(key,sd,30,TimeUnit.MINUTES);
		Object o = redisTemplate.opsForValue().get(key);
		sd=(User)o;
		System.out.println(sd);
		return o;
	}
	/*@GetMapping("/mq")
	@ResponseBody
	public Result<String> mq(){
		sender.send("hello");
		return Result.success("Hello world");
	}
	@GetMapping("/mq/topic")
	@ResponseBody
	public Result<String> mqTopic(){
		sender.sendTopic("hello,ttttopic");
		return Result.success("Hello world");
	}

	@GetMapping("/mq/fanout")
	@ResponseBody
	public Result<String> mqFanout(){
		sender.sendFanout("hello,fanout");
		return Result.success("Hello world");
	}
	@GetMapping("/mq/headers")
	@ResponseBody
	public Result<String> mqHeaders(){
		sender.sendHeaders("hello,headers");
		return Result.success("Hello world");
	}*/

	@GetMapping("fff")
	@ResponseBody
	public Object tsssss() throws Exception {
		UserUtil.createUser();
		return "ffssdfs";
	}
}
