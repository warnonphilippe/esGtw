package be.civadis.store.base.multitenancy;

import java.util.Optional;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;

import be.civadis.tools.es.BaseCommand;

/**
 * MyCommandHandlerInterceptor
 */
public class MultitenantCommandHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        CommandMessage<?> command = unitOfWork.getMessage();
        BaseCommand bc = (BaseCommand) command.getPayload();
        TenantContext.setCurrentTenant(bc.getTenantId());
        return interceptorChain.proceed();
    }
}