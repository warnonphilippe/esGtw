package be.civadis.store.business.invoice.event;

import java.time.Instant;

import be.civadis.tools.es.BaseEvent;

/**
 * CreateInvoiceEvent
 */
public class CreatedInvoiceEvent extends BaseEvent {

    private String code;
    private Instant date;
    private String details;

    public CreatedInvoiceEvent(String aggregateId, String contexteId, String code, Instant date, String details) {
        super(aggregateId, contexteId);
        this.code = code;
        this.date = date;
        this.details = details;
    }
    
    public CreatedInvoiceEvent copy(String aggregateId, String contexteId){
        return new CreatedInvoiceEvent(aggregateId, contexteId, this.code, this.date, this.details);
    }

    public String getDetails() {
        return details;
    }

    public Instant getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }

    
}