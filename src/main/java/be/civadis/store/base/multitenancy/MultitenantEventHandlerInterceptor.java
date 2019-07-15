package be.civadis.store.base.multitenancy;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;

/**
 * MyEventHandlerInterceptor
 */
public class MultitenantEventHandlerInterceptor 
        implements MessageHandlerInterceptor<EventMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends EventMessage<?>> unitOfWork, 
                         InterceptorChain interceptorChain) throws Exception {
        EventMessage<?> event = unitOfWork.getMessage();
        return interceptorChain.proceed();
    }
}