package be.civadis.store.base.multitenancy;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.DefaultEventGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.extensions.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.axonframework.queryhandling.DefaultQueryGateway;
import org.axonframework.queryhandling.LoggingQueryInvocationErrorHandler;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryInvocationErrorHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.queryhandling.SimpleQueryBus;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestOperations;

import be.civadis.store.base.config.ApplicationProperties;


/**
 * MultitenantAxonConfig
 */
@Configuration
 public class MultitenantAxonConfig {

     @Conditional(MultiSchemasCondition.class)
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
     

    @Conditional(MultiSchemasCondition.class)
     @Autowired
     public void configure(EventProcessingConfigurer config, EventGateway eventGateway, ApplicationProperties applicationProperties) {	
         config.usingSubscribingEventProcessors();
         config.registerDefaultHandlerInterceptor(
            (configuration, name) -> new MultitenantEventHandlerInterceptor(eventGateway, applicationProperties));
         //config.registerHandlerInterceptor("amqpEvents",
         //    configuration -> new MultitenantEventHandlerInterceptor(eventGateway, applicationProperties));
     }

     // Le Builder de SimpleQueryBus ajoute TransactionManagingInterceptor au début de la liste des handlers
    // TransactionManagingInterceptor passe alors avant MultitenantQueryHandlerInterceptor et crée une transaction
    // Donc le TenantContext garni par MultitenantQueryHandlerInterceptor n'est pas utilisé car connexion déjà initialisée !
    // On remplace le SimpleQueryBus par un MultitenantQueryBus qui ajoute MultitenantQueryHandlerInterceptor en 1er
    // On doit aussi définir un QueryGateway
    @Conditional(MultiSchemasCondition.class)
    @Primary
    @Bean
    public QueryGateway queryGateway(@Qualifier("localSegment") QueryBus queryBus, TransactionManager transactionManager) {
        queryBus.registerHandlerInterceptor(new MultitenantQueryHandlerInterceptor());
        queryBus.registerHandlerInterceptor(new TransactionManagingInterceptor<>(transactionManager));
        return DefaultQueryGateway.builder().queryBus(queryBus).build();
    }

    @Conditional(MultiSchemasCondition.class)
    @Primary
    @Qualifier("localSegment")
    @Bean
    public SimpleQueryBus queryBus(AxonConfiguration axonConfiguration, TransactionManager transactionManager) {
        return SimpleQueryBus.builder()
                             .messageMonitor(axonConfiguration.messageMonitor(QueryBus.class, "queryBus"))
                             .transactionManager(NoTransactionManager.INSTANCE) //désactive ajout auto de la transaction, on l'ajoute plus tard
                             .errorHandler(axonConfiguration.getComponent(
                                     QueryInvocationErrorHandler.class,
                                     () -> LoggingQueryInvocationErrorHandler.builder().build()
                             ))
                             .queryUpdateEmitter(axonConfiguration.getComponent(QueryUpdateEmitter.class))
                             .build();

    }
 
 }