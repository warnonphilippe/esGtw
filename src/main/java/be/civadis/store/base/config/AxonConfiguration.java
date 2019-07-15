package be.civadis.store.base.config;

import com.rabbitmq.client.Channel;

import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import be.civadis.tools.es.BaseEvent;
import be.civadis.store.base.config.axon.CivadisAMQPMessageSource;

/**
 * AxonConfiguration
 */
@Configuration
public class AxonConfiguration {
	@Autowired
	private ApplicationProperties applicationProperties;

	@Bean
	public SpringAMQPMessageSource queueMessageSource(AMQPMessageConverter messageConverter,
			ApplicationProperties applicationProperties) {
		return new CivadisAMQPMessageSource(messageConverter, applicationProperties){
			
			//@RabbitListener(bindings = @QueueBinding(
            //    value = @org.springframework.amqp.rabbit.annotation.Queue,
            //    exchange = @org.springframework.amqp.rabbit.annotation.Exchange(value ="axonMessageExchange",type = ExchangeTypes.FANOUT)), concurrency ="1")
			@RabbitListener(queues = "axonMessageExchangeQueue")
			@Override
			public void onMessage(Message message, Channel channel) {
				super.onMessage(message, channel);
			}
		};
	}
}