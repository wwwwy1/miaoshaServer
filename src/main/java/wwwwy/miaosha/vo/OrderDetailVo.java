package wwwwy.miaosha.vo;

import lombok.Data;
import wwwwy.miaosha.domain.OrderInfo;

@Data
public class OrderDetailVo {
	private OrderInfo orderInfo;
	private GoodsVo goodsVo;
}
