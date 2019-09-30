package wwwwy.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.mapper.OrderInfoMapper;
import wwwwy.miaosha.service.IOrderInfoService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

}
