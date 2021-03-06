package be.civadis.store.base.config;

import com.rabbitmq.client.Channel;

import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import be.civadis.store.base.config.axon.CivadisAMQPMessageSource;

/**
 * AxonConfiguration
 */
@Configuration
public class AxonConfiguration {

	private final Logger log = LoggerFactory.getLogger(AxonConfiguration.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	@Bean
	public SpringAMQPMessageSource queueMessageSource(AMQPMessageConverter messageConverter,
			ApplicationProperties applicationProperties) {
				
		return new CivadisAMQPMessageSource(messageConverter, applicationProperties){
			
			@RabbitListener(queues = AmqpConfiguration.AXON_QUEUE_PREFIX + "${spring.application.name}", concurrency = "1")
			@Override
			public void onMessage(Message message, Channel channel) {
				super.onMessage(message, channel);
			}
		};
	}
}