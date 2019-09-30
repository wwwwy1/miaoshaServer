package wwwwy.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class MQReceiver {
	private static Logger log=LoggerFactory.getLogger(MQReceiver.class);
	@RabbitListener(queues = MQConifg.QUEUE)
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


}
