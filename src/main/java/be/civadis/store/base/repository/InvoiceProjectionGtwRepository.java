package be.civadis.store.base.repository;

import be.civadis.store.base.domain.InvoiceProjectionGtw;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvoiceProjectionGtw entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceProjectionGtwRepository extends JpaRepository<InvoiceProjectionGtw, Long> {

}
