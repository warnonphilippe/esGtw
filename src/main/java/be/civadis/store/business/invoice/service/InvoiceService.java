package be.civadis.store.business.invoice.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import be.civadis.commones.invoice.command.CreateInvoiceCommand;
import be.civadis.commones.invoice.command.PayInvoiceCommand;

import be.civadis.commones.invoice.query.*;
import be.civadis.store.base.config.ApplicationProperties;
import be.civadis.store.base.domain.InvoiceProjectionGtw;
import be.civadis.store.base.multitenancy.TenantContext;


/**
 * InvoiceService
 */
@Service
public class InvoiceService {

    private final CommandGateway commandGateway;
    private final EventGateway eventGateway;
    private final QueryGateway queryGateway;
    private final ApplicationProperties applicationProperties;

    public InvoiceService(CommandGateway commandGateway, EventGateway eventGateway, QueryGateway queryGateway,
            ApplicationProperties applicationProperties) {
        this.commandGateway = commandGateway;
        this.eventGateway = eventGateway;
        this.queryGateway = queryGateway;
        this.applicationProperties = applicationProperties;
    }

    public String createInvoice(String id, String code, Instant date, String details){
       return commandGateway.sendAndWait(new CreateInvoiceCommand(id, TenantContext.getCurrentTenant(), code, date, details));
    }

    public String payInvoice(String id, Instant date, BigDecimal amount){
        return commandGateway.sendAndWait(new PayInvoiceCommand(id, TenantContext.getCurrentTenant(), date, amount));
    }

    public List<InvoiceProjectionGtw> findAllInvoices() {
        return queryGateway.query(new FindAllInvoices(TenantContext.getCurrentTenant()), 
            ResponseTypes.multipleInstancesOf(InvoiceProjectionGtw.class)).join();
    }

    


    
}