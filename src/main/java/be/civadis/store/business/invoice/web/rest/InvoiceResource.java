package be.civadis.store.business.invoice.web.rest;

import be.civadis.commones.invoice.event.PaidInvoiceEvent;

import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import be.civadis.store.base.domain.InvoiceProjectionGtw;
import be.civadis.store.business.invoice.dto.InvoiceDto;
import be.civadis.store.business.invoice.dto.PayInvoiceDto;
import be.civadis.store.business.invoice.repository.InvoiceProjectionGtwExtRepository;
import be.civadis.store.business.invoice.service.InvoiceService;

/**
 * REST controller for managing {@link be.civadis.invoice.base.domain.Invoice}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoiceInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceService invoiceService;
    private final InvoiceProjectionGtwExtRepository invoiceRepository;
    private final EventGateway eventGateway;
    private final QueryGateway queryGateway;
    
    public InvoiceResource(InvoiceService invoiceService, InvoiceProjectionGtwExtRepository invoiceRepository, EventGateway eventGateway, QueryGateway queryGateway) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.eventGateway = eventGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/invoices")
    public ResponseEntity createInvoice(@RequestBody InvoiceDto invoice) throws URISyntaxException {
        String id = UUID.randomUUID().toString();
        // TODO : voir pour deserialize des instant
        invoiceService.createInvoice(id, invoice.getCode(), Instant.now(), invoice.getDetails());
        return ResponseEntity.ok(id);
    }

    @PutMapping("/invoices/{id}/pay")
    public ResponseEntity payInvoice(@PathVariable String id, @RequestBody PayInvoiceDto invoice) throws URISyntaxException {
        // TODO : voir pour deserialize des instant
        invoiceService.payInvoice(id, Instant.now(), invoice.getPaymentAmount());
        return ResponseEntity.ok(id);
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceProjectionGtw>> getAllInvoiceProjections() {
        //TenantContext.setCurrentTenant("jhipster2");
        List<InvoiceProjectionGtw> list = invoiceService.findAllInvoices();
        return ResponseEntity.ok().body(list);
        
    }

    /**
     * Hello
     * @return
     */
    @GetMapping("/invoices/hello")
    public ResponseEntity<List<InvoiceProjectionGtw>> hello() {
        List<InvoiceProjectionGtw> list = new ArrayList<>();
        list.add(new InvoiceProjectionGtw()
            .aggregateId("1")
            .code("Invoice Test 1")
            .details("Dummy Invoice for test"));
        list.add(new InvoiceProjectionGtw()
            .aggregateId("2")
            .code("Invoice Test 2")
            .details("Dummy Invoice for test"));
        return ResponseEntity.ok().body(list);
    }

    /**
     * Simule l'envoi un event à tous les tenants
     * Attention, dans un projet réel, 
     *  - on devrait passer par une commande dans un contexte mono-tenant qui enverrait un event 'all' 
     *  - des eventHandler d'applications multi-tenant écouteraient ensuite cet event
     * Dans cet appli du test multitenant, les commandes doivent être associées à un tenant
     * On envoie dans directement un event 'all' sans apsser par une commande, ce que l'on ne doit pas faire dans une vraie app !
     */
    @PostMapping("/invoices/all")
    public ResponseEntity testAll() {
        List<InvoiceProjectionGtw> invoices = this.invoiceRepository.findAll();
        if (invoices != null && !invoices.isEmpty()){
            PaidInvoiceEvent event = new PaidInvoiceEvent(invoices.get(0).getAggregateId(), "all", Instant.now(), new BigDecimal(1000.0));
            eventGateway.publish(event);
        }
        return ResponseEntity.ok(true);
    }

}
