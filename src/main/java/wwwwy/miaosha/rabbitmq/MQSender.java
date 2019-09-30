package wwwwy.miaosha.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
	private static Logger log=LoggerFactory.getLogger(MQSender.class);

	@Autowired
	AmqpTemplate amqpTemplate;

	public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage){
		String msg = JSON.toJSONString(miaoshaMessage);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConifg.MIAOSHA_QUEUE,msg);
	}
	/*public void send(Object message){
		String msg = (String) message;
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConifg.QUEUE,msg);
	}

	public void sendTopic(Object message){
		String msg = (String) message;
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConifg.TOPIC_EXCHANGE,"topic.key1",msg+"1");
		amqpTemplate.convertAndSend(MQConifg.TOPIC_EXCHANGE,"topic.key2",msg+"2");
	}

	public void sendFanout(Object message){
		String msg = (String) message;
		log.info("send fanout message:"+msg);
		amqpTemplate.convertAndSend(MQConifg.FANOUT_EXCHANGE,"13",msg+"1");
		amqpTemplate.convertAndSend(MQConifg.FANOUT_EXCHANGE,"13",msg+"2");
	}

	public void sendHeaders(Object message){
		String msg = (String) message;
		log.info("send headers message:"+msg);
		MessageProperties properties=new MessageProperties();
		properties.setHeader("header1","value1");
		properties.setHeader("header2","value2");
		Message message1=new Message(msg.getBytes(),properties);
		amqpTemplate.convertAndSend(MQConifg.HEADERS_EXCHANGE,"",message1);
	}*/

}
