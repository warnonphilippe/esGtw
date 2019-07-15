package be.civadis.store.base.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AmqpConfiguration
 */
@Configuration
public class AmqpConfiguration {

	@Bean
	public Queue axonMessageExchangeQueue() {
		return QueueBuilder.durable("axonMessageExchangeQueue").build();
	}

	@Bean
	public Exchange axonMessageExchange() {
		return ExchangeBuilder.fanoutExchange("axonMessageExchange").build();
	}

	@Bean
	public Binding binding() {
		return BindingBuilder.bind(axonMessageExchangeQueue()).to(axonMessageExchange()).with("*").noargs();
	}

	@Autowired
	private void configure(AmqpAdmin amqpAdmin) {
		amqpAdmin.declareQueue(axonMessageExchangeQueue());
		amqpAdmin.declareExchange(axonMessageExchange());
		amqpAdmin.declareBinding(binding());
	}
}