package be.civadis.store.business.common.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.civadis.store.base.domain.InvoiceProjectionGtw;
import be.civadis.commones.invoice.event.CreatedInvoiceEvent;
import be.civadis.commones.invoice.event.PaidInvoiceEvent;
import be.civadis.store.business.invoice.repository.InvoiceProjectionGtwExtRepository;


@Component
@ProcessingGroup("amqpEvents")
public class Cqrs {

    @Autowired
    private InvoiceProjectionGtwExtRepository invoiceRepo;
    
    
    @EventHandler
    public void handle(CreatedInvoiceEvent event) {
        System.out.println("Invoice created :: " + event.getAggregateId() + " :: " + event.getCode());
        // TODO : à compléter
        InvoiceProjectionGtw invoice = new InvoiceProjectionGtw();
        invoice.setCode(event.getCode());
        invoice.setDate(event.getDate());
        invoice.setDetails(event.getDetails());
        invoice.setAggregateId(event.getAggregateId());
        invoiceRepo.save(invoice);
    }

    @EventHandler
    public void handle(PaidInvoiceEvent event) {
        System.out.println("Invoice paid :: " + event.getAggregateId() + " :: " + event.getPaymentAmount());
        InvoiceProjectionGtw invoice = this.invoiceRepo.findByAggregateId(event.getAggregateId());
        if (invoice != null){
            invoice.setPaymentAmount(event.getPaymentAmount());
            invoiceRepo.save(invoice);
        }
    }

}