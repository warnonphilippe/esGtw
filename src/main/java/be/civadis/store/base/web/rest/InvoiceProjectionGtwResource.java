package be.civadis.store.base.web.rest;

import be.civadis.store.base.domain.InvoiceProjectionGtw;
import be.civadis.store.base.repository.InvoiceProjectionGtwRepository;
import be.civadis.store.base.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link be.civadis.store.base.domain.InvoiceProjectionGtw}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceProjectionGtwResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceProjectionGtwResource.class);

    private static final String ENTITY_NAME = "invoiceProjectionGtw";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceProjectionGtwRepository invoiceProjectionGtwRepository;

    public InvoiceProjectionGtwResource(InvoiceProjectionGtwRepository invoiceProjectionGtwRepository) {
        this.invoiceProjectionGtwRepository = invoiceProjectionGtwRepository;
    }

    /**
     * {@code POST  /invoice-projection-gtws} : Create a new invoiceProjectionGtw.
     *
     * @param invoiceProjectionGtw the invoiceProjectionGtw to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceProjectionGtw, or with status {@code 400 (Bad Request)} if the invoiceProjectionGtw has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-projection-gtws")
    public ResponseEntity<InvoiceProjectionGtw> createInvoiceProjectionGtw(@Valid @RequestBody InvoiceProjectionGtw invoiceProjectionGtw) throws URISyntaxException {
        log.debug("REST request to save InvoiceProjectionGtw : {}", invoiceProjectionGtw);
        if (invoiceProjectionGtw.getId() != null) {
            throw new BadRequestAlertException("A new invoiceProjectionGtw cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceProjectionGtw result = invoiceProjectionGtwRepository.save(invoiceProjectionGtw);
        return ResponseEntity.created(new URI("/api/invoice-projection-gtws/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-projection-gtws} : Updates an existing invoiceProjectionGtw.
     *
     * @param invoiceProjectionGtw the invoiceProjectionGtw to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceProjectionGtw,
     * or with status {@code 400 (Bad Request)} if the invoiceProjectionGtw is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceProjectionGtw couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-projection-gtws")
    public ResponseEntity<InvoiceProjectionGtw> updateInvoiceProjectionGtw(@Valid @RequestBody InvoiceProjectionGtw invoiceProjectionGtw) throws URISyntaxException {
        log.debug("REST request to update InvoiceProjectionGtw : {}", invoiceProjectionGtw);
        if (invoiceProjectionGtw.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceProjectionGtw result = invoiceProjectionGtwRepository.save(invoiceProjectionGtw);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceProjectionGtw.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /invoice-projection-gtws} : get all the invoiceProjectionGtws.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceProjectionGtws in body.
     */
    @GetMapping("/invoice-projection-gtws")
    public ResponseEntity<List<InvoiceProjectionGtw>> getAllInvoiceProjectionGtws(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of InvoiceProjectionGtws");
        Page<InvoiceProjectionGtw> page = invoiceProjectionGtwRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-projection-gtws/:id} : get the "id" invoiceProjectionGtw.
     *
     * @param id the id of the invoiceProjectionGtw to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceProjectionGtw, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-projection-gtws/{id}")
    public ResponseEntity<InvoiceProjectionGtw> getInvoiceProjectionGtw(@PathVariable Long id) {
        log.debug("REST request to get InvoiceProjectionGtw : {}", id);
        Optional<InvoiceProjectionGtw> invoiceProjectionGtw = invoiceProjectionGtwRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invoiceProjectionGtw);
    }

    /**
     * {@code DELETE  /invoice-projection-gtws/:id} : delete the "id" invoiceProjectionGtw.
     *
     * @param id the id of the invoiceProjectionGtw to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-projection-gtws/{id}")
    public ResponseEntity<Void> deleteInvoiceProjectionGtw(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceProjectionGtw : {}", id);
        invoiceProjectionGtwRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
