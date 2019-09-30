package wwwwy.miaosha.vo;

import lombok.Data;
import wwwwy.miaosha.domain.Goods;

import java.util.Date;
@Data
public class GoodsVo extends Goods{
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
}
