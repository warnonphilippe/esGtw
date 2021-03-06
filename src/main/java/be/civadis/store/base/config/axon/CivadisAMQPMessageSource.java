package be.civadis.store.base.config.axon;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import com.rabbitmq.client.Channel;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StringUtils;

import be.civadis.tools.es.BaseEvent;
import be.civadis.store.base.config.ApplicationProperties;

/**
 * CivadisAMQPMessageSource
 */
public class CivadisAMQPMessageSource extends SpringAMQPMessageSource {
    private List<Consumer<List<? extends EventMessage<?>>>> parentEventProcessors = new CopyOnWriteArrayList<>();
    private AMQPMessageConverter parentMessageConverter;
    private ApplicationProperties applicationProperties;

    public CivadisAMQPMessageSource(AMQPMessageConverter messageConverter,
            ApplicationProperties applicationProperties) {
        super(messageConverter);

        this.applicationProperties = applicationProperties;

        try {
            Field parentEventProcessorsField = SpringAMQPMessageSource.class.getDeclaredField("eventProcessors");
            parentEventProcessorsField.setAccessible(true);
            this.parentEventProcessors = (List<Consumer<List<? extends EventMessage<?>>>>) parentEventProcessorsField
                    .get(this);

            Field parentMessageConverterField = SpringAMQPMessageSource.class.getDeclaredField("messageConverter");
            parentMessageConverterField.setAccessible(true);
            this.parentMessageConverter = (AMQPMessageConverter) parentMessageConverterField.get(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        if (!parentEventProcessors.isEmpty()) {
            parentMessageConverter.readAMQPMessage(message.getBody(), message.getMessageProperties().getHeaders())
                    .ifPresent(event -> parentEventProcessors.forEach(ep -> {

                        if (event.getPayload() != null && (event.getPayload() instanceof BaseEvent)) {
                            BaseEvent baseEvent = (BaseEvent) event.getPayload();

                            // rechercher liste des tenants
                            List<String> tenantList = new ArrayList<>();
                            applicationProperties.getMultitenancy().getTenants().forEach(
                                tenant -> tenantList.add(tenant.getName().toLowerCase())
                            );

                            // ne laisser passer l'event que si son tenantId est dans la liste (ou all)
                            if (tenantList.contains(baseEvent.getTenantId().toLowerCase()) || "all".equalsIgnoreCase(baseEvent.getTenantId())){
                                ep.accept(Collections.singletonList(event));
                            } 
                            
                        } else {
                            // pas un BaseEvent, on laisse passer
                            ep.accept(Collections.singletonList(event));
                        }
                    }));
        }
    }
}