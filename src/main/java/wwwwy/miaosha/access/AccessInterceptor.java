package wwwwy.miaosha.access;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IMiaoshaUserService;
import wwwwy.miaosha.util.MD5Util;
import wwwwy.miaosha.util.UUIDUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	IMiaoshaUserService iMiaoshaUserService;
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod){
			MiaoshaUser user = getUser(request, response);
			UserContext.setUser(user);

			HandlerMethod hm = (HandlerMethod)handler;
			AccessLimit accessLimit=hm.getMethodAnnotation(AccessLimit.class);
			if (accessLimit==null)	return true;
			int secounds = accessLimit.secounds();
			boolean needLogin = accessLimit.needLogin();
			int maxCount = accessLimit.maxCount();
			String key = request.getRequestURI();
			if (needLogin && user==null){
				render(response,CodeMsg.SESSION_ERROR);
				return false;
			}
			key=user==null?key:key+user.getId();
			Integer num = (Integer) redisTemplate.opsForValue().get("access" + key);
			if (num==null){
				redisTemplate.opsForValue().set("access" + key,1,secounds,TimeUnit.SECONDS);
			}else if (num<maxCount){
				redisTemplate.opsForValue().set("access" + key,num+1);
			}else {
				render(response,CodeMsg.ACCESS_LIMIT);
				return false;
			}
		}
		return true;
	}
	private void render(HttpServletResponse response,CodeMsg cm) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = response.getOutputStream();
		String str = JSON.toJSONString(Result.error(cm));
		outputStream.write(str.getBytes("UTF-8"));
		outputStream.flush();
		outputStream.close();
	}
	private MiaoshaUser getUser(HttpServletRequest request,HttpServletResponse response){
		String paramToken = request.getParameter("token");
		String cookieToken = getCookieValue(request,"token");
		if (StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)) return null;
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return iMiaoshaUserService.getByToken(response,token);
	}
	private String getCookieValue(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
		if (cookies==null)return null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName))return cookie.getValue();
		}
		return null;
	}
}
