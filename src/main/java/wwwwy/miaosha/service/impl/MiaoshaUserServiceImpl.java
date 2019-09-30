package wwwwy.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.exception.GlobalException;
import wwwwy.miaosha.mapper.MiaoshaUserMapper;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.service.IMiaoshaUserService;
import wwwwy.miaosha.util.MD5Util;
import wwwwy.miaosha.util.UUIDUtil;
import wwwwy.miaosha.vo.LoginVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-24
 */
@Service
public class MiaoshaUserServiceImpl extends ServiceImpl<MiaoshaUserMapper, MiaoshaUser> implements IMiaoshaUserService {

	@Autowired
	RedisTemplate<Object,Object> redisTemplate;

	public MiaoshaUser getByIdMe(Long id) {
		MiaoshaUser user = (MiaoshaUser)redisTemplate.opsForValue().get("id" + id);
		if (user==null){
			user = this.getById(id);
			redisTemplate.opsForValue().set("id"+id,user);
		}
		return user;
	}

	@Override
	public String login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo==null)throw new GlobalException(CodeMsg.SERVER_ERROR);
		MiaoshaUser user = getByIdMe(Long.parseLong(loginVo.getMobile()));
		if (user==null)throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDbPass(loginVo.getPassword(), saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		String token = UUIDUtil.uuid();
		addCookie(user,token,response);
		return token;
	}

	public  boolean updatePassword(long id,String password){
		MiaoshaUser user = getByIdMe(id);
		if (user==null){
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		MiaoshaUser newUser = new MiaoshaUser();
		newUser.setId(id);
		newUser.setPassword(MD5Util.formPassToDbPass(password,user.getSalt()));
		this.updateById(newUser);
		user.setPassword(newUser.getPassword());
		redisTemplate.delete("id"+id);
		redisTemplate.opsForValue().set("token",user);
		return true;
	}

	@Override
	public MiaoshaUser getByToken(HttpServletResponse response,String token) {
		if (StringUtils.isEmpty(token))
			return null;
		MiaoshaUser user = (MiaoshaUser) redisTemplate.opsForValue().get("uk" + token);
		if (user!=null) addCookie(user,token,response);
		return user;
	}
	private void addCookie(MiaoshaUser user,String token,HttpServletResponse response){
		redisTemplate.opsForValue().set("uk"+token,user,30,TimeUnit.MINUTES);
		Cookie cookie=new Cookie("token",token);
		cookie.setMaxAge(30*60*60);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
