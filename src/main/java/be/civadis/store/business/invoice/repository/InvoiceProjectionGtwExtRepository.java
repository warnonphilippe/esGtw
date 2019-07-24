package be.civadis.store.business.invoice.repository;

import be.civadis.store.base.domain.InvoiceProjectionGtw;
import be.civadis.store.base.repository.InvoiceProjectionGtwRepository;

/**
 * InvoiceProjectionExtrepository
 */
public interface InvoiceProjectionGtwExtRepository extends InvoiceProjectionGtwRepository {

    public InvoiceProjectionGtw findByAggregateId(String aggregateId);

    
}