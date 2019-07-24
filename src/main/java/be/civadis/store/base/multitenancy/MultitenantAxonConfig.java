package be.civadis.store.base.multitenancy;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.DefaultEventGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.extensions.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.axonframework.serialization.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestOperations;

import be.civadis.store.base.config.ApplicationProperties;


/**
 * MultitenantAxonConfig
 */
@Configuration
 public class MultitenantAxonConfig {

    
     @Primary
     @Bean
     public CommandBusConnector springHttpCommandBusConnector(
                         @Qualifier("localSegment") CommandBus localSegment,
                         RestOperations restOperations,
                         Serializer serializer) {
         localSegment.registerHandlerInterceptor(new MultitenantCommandHandlerInterceptor());
         return SpringHttpCommandBusConnector.builder()
                                             .localCommandBus(localSegment)
                                             .restOperations(restOperations)
                                             .serializer(serializer)
                                             .build();
     }

    @Primary
    @Bean
    public EventGateway eventGateway(EventBus eventBus) {
        return DefaultEventGateway.builder().eventBus(eventBus).build();
    }
     

     @Autowired
     public void configure(EventProcessingConfigurer config, EventGateway eventGateway, ApplicationProperties applicationProperties) {	
         EventProcessingConfiguration conf;
         config.usingSubscribingEventProcessors();
         config.registerDefaultHandlerInterceptor(
            (configuration, name) -> new MultitenantEventHandlerInterceptor(eventGateway, applicationProperties));
         //config.registerHandlerInterceptor("amqpEvents",
         //    configuration -> new MultitenantEventHandlerInterceptor(eventGateway, applicationProperties));
     }
 
 }