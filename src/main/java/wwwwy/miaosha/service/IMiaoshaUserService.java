package wwwwy.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.lang3.StringUtils;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-24
 */
public interface IMiaoshaUserService extends IService<MiaoshaUser> {

	boolean login(HttpServletResponse response,LoginVo loginVo);
	MiaoshaUser getByToken(HttpServletResponse response,String token);
}
