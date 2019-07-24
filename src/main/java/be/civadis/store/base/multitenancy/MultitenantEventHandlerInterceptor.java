package be.civadis.store.base.multitenancy;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;

import be.civadis.store.base.config.ApplicationProperties;
import be.civadis.tools.es.BaseEvent;

/**
 * MyEventHandlerInterceptor
 */
public class MultitenantEventHandlerInterceptor 
        implements MessageHandlerInterceptor<EventMessage<?>> {


    private final EventGateway eventGateway;
    private final ApplicationProperties applicationProperties;
        
    public MultitenantEventHandlerInterceptor(EventGateway eventGateway, ApplicationProperties applicationProperties){
        this.eventGateway = eventGateway;
        this.applicationProperties = applicationProperties;
    }

    /**
     * 
     * Dans contexteId, possible d'avoir un tenant ou "all" (trt aussi cas null, vide,…)
     * Si tenant, set du tenant dans TenantContext
     * Si all,
     *  on réenvoi un event par tenant
     */
    @Override
    public Object handle(UnitOfWork<? extends EventMessage<?>> unitOfWork, 
                         InterceptorChain interceptorChain) throws Exception {
        EventMessage<?> msg = unitOfWork.getMessage();
        BaseEvent event = (BaseEvent) msg.getPayload();
        String ctx = event.getTenantId();
        boolean proceed = false;
        
        if ("all".equalsIgnoreCase(ctx)){
            applicationProperties.getMultitenancy().getTenants().forEach(
                tenant -> {
                    // émettre un event de même type mais pour un autre tenantId
                    BaseEvent copyEvent = event.copy(event.getAggregateId(), tenant.getName());
                    eventGateway.publish(copyEvent);
                }
            );

        } else if (ctx != null && !ctx.isEmpty()){
            TenantContext.setCurrentTenant(ctx);
            proceed = true;
        }

        if (proceed){
            return interceptorChain.proceed();
        } else {
            return null;
        }
        
    }
}