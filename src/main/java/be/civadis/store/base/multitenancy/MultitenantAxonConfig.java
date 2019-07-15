package be.civadis.store.base.multitenancy;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.extensions.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.axonframework.serialization.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestOperations;


/**
 * MultitenantAxonConfig
 */
public class MultitenantAxonConfig {

    @Primary
	@Bean
    public CommandBusConnector springHttpCommandBusConnector(
                        @Qualifier("customLocalSegment") CommandBus localSegment,
                        RestOperations restOperations,
                        Serializer serializer) {
        return SpringHttpCommandBusConnector.builder()
                                            .localCommandBus(localSegment)
                                            .restOperations(restOperations)
                                            .serializer(serializer)
                                            .build();
	}
	
	@Primary
	@Bean
    @Qualifier("customLocalSegment")
    public CommandBus customLocalSegment(MultitenantCommandHandlerInterceptor multitenantCommandHandlerInterceptor) {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        commandBus.registerHandlerInterceptor(new MultitenantCommandHandlerInterceptor());
        return commandBus;
	}
	
	@Autowired
	public void configure(EventProcessingConfigurer config) {	
		config.usingSubscribingEventProcessors();
		config.registerHandlerInterceptor("multitenant-interceptor",
			configuration -> new MultitenantEventHandlerInterceptor());
	}

	/*
    @Primary
    @Bean
    @Qualifier("localSegment")
    public CommandBus localSegment(MultitenantCommandHandlerInterceptor multitenantCommandHandlerInterceptor) {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        commandBus.registerHandlerInterceptor(multitenantCommandHandlerInterceptor);
        return commandBus;
    }
*/


}