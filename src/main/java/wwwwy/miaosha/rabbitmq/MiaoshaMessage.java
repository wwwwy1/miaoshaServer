package wwwwy.miaosha.rabbitmq;

import lombok.Data;
import wwwwy.miaosha.domain.MiaoshaUser;

import java.io.Serializable;

@Data
public class MiaoshaMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private MiaoshaUser user;
	private long goodsId;
}
