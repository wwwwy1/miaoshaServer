package wwwwy.miaosha.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wwwwy.miaosha.domain.MiaoshaOrder;
import wwwwy.miaosha.domain.MiaoshaUser;
import wwwwy.miaosha.domain.OrderInfo;
import wwwwy.miaosha.result.Result;
import wwwwy.miaosha.service.IGoodsService;
import wwwwy.miaosha.service.IMiaoshaGoodsService;
import wwwwy.miaosha.service.IMiaoshaOrderService;
import wwwwy.miaosha.service.IOrderInfoService;
import wwwwy.miaosha.vo.GoodsVo;


@Service
public class MQReceiver {
	private static Logger log=LoggerFactory.getLogger(MQReceiver.class);
	@Autowired
	IMiaoshaGoodsService iMiaoshaGoodsService;
	@Autowired
	IGoodsService iGoodsService;
	@Autowired
	IOrderInfoService iOrderInfoService;
	@Autowired
	IMiaoshaOrderService iMiaoshaOrderService;
	@RabbitListener(queues = MQConifg.MIAOSHA_QUEUE)
	public void receive(String message){
		log.info("receive message:" + message);
		MiaoshaMessage miaoshaMessage = JSON.toJavaObject(JSON.parseObject(message), MiaoshaMessage.class);
		MiaoshaUser user = miaoshaMessage.getUser();
		long goodsId = miaoshaMessage.getGoodsId();
		GoodsVo goodsVo = iGoodsService.getGoodsVoByGoodsId(goodsId);
		if (goodsVo.getStockCount()<=0 ){
			return;
			//return "miaosha_fail";
		}
		QueryWrapper<MiaoshaOrder> wrapper=new QueryWrapper<>();
		wrapper.eq("goods_id",goodsId);
		wrapper.eq("user_id",user.getId());
		MiaoshaOrder one = iMiaoshaOrderService.getOne(wrapper);
		if (one!=null){
			return;
			//return "miaosha_fail";
		}
		OrderInfo orderInfo = iMiaoshaGoodsService.miaosha(user, goodsVo);
		//model.addAttribute("orderInfo",orderInfo);
		//model.addAttribute("goods",goodsVo);
		//return Result.success(orderInfo);
	}

	/*@RabbitListener(queues = MQConifg.QUEUE)
	public void receive(String message){
		log.info("receive message:" + message);
	}

	@RabbitListener(queues = MQConifg.TOPIC_QUEUE1)
	public void receiveTopic1(String message){
		log.info("receive topic q1 message:" + message);
	}
	@RabbitListener(queues = MQConifg.TOPIC_QUEUE2)
	public void receiveTopic2(String message){
		log.info("receive topic q2 message:" + message);
	}

	@RabbitListener(queues = MQConifg.HEADERS_QUEUE1)
	public void receiveHeaders(byte[] message){
		log.info("receive headers message:" + new String(message));
	}
*/

}
