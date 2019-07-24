package be.civadis.store.business.invoice.event;
import java.math.BigDecimal;
import java.time.Instant;

import be.civadis.tools.es.BaseEvent;

/**
 * PayInvoiceEvent
 */
public class PaidInvoiceEvent extends BaseEvent{

    private Instant paymentDate;
    private BigDecimal paymentAmount;

    public PaidInvoiceEvent(String aggregateId, String contexteId, Instant paymentDate, BigDecimal paymentAmount) {
        super(aggregateId, contexteId);
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
    }

    public PaidInvoiceEvent copy(String aggregateId, String contexteId){
        return new PaidInvoiceEvent(aggregateId, contexteId, this.paymentDate, this.paymentAmount);
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    
}