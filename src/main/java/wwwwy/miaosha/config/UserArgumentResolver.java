package wwwwy.miaosha.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wwwwy.miaosha.access.UserContext;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.service.IMiaoshaUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
	@Autowired
	IMiaoshaUserService iMiaoshaUserService;
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		Class<?> clazz = methodParameter.getParameterType();
		return clazz==MiaoshaUser.class;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		/*HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		String paramToken = request.getParameter("token");
		String cookieToken = getCookieValue(request,"token");
		if (StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)) return null;
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return iMiaoshaUserService.getByToken(response,token);*/
		return UserContext.getUser();
	}
	/*private String getCookieValue(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
		if (cookies==null)return null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName))return cookie.getValue();
		}
		return null;
	}*/
}
