package be.civadis.store.base.multitenancy;

import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.queryhandling.QueryMessage;

import be.civadis.tools.es.BaseQuery;;

/**
 * MultitenantQueryHandlerInterceptor
 */
public class MultitenantQueryHandlerInterceptor implements MessageHandlerInterceptor<QueryMessage<?, ?>> {

    @Override
    public Object handle(UnitOfWork<? extends QueryMessage<?, ?>> unitOfWork, 
                         InterceptorChain interceptorChain) throws Exception {

        QueryMessage<?, ?> msg = unitOfWork.getMessage();
        BaseQuery query = (BaseQuery) msg.getPayload();
        TenantContext.setCurrentTenant(query.getTenantId());
        return interceptorChain.proceed();
    }
    
}