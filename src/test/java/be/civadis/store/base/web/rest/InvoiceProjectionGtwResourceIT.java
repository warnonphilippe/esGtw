package be.civadis.store.base.web.rest;

import be.civadis.store.base.StoreApp;
import be.civadis.store.base.config.TestSecurityConfiguration;
import be.civadis.store.base.domain.InvoiceProjectionGtw;
import be.civadis.store.base.repository.InvoiceProjectionGtwRepository;
import be.civadis.store.base.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static be.civadis.store.base.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link InvoiceProjectionGtwResource} REST controller.
 */
@SpringBootTest(classes = {StoreApp.class, TestSecurityConfiguration.class})
public class InvoiceProjectionGtwResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_AGGREGATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_AGGREGATE_ID = "BBBBBBBBBB";

    @Autowired
    private InvoiceProjectionGtwRepository invoiceProjectionGtwRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInvoiceProjectionGtwMockMvc;

    private InvoiceProjectionGtw invoiceProjectionGtw;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceProjectionGtwResource invoiceProjectionGtwResource = new InvoiceProjectionGtwResource(invoiceProjectionGtwRepository);
        this.restInvoiceProjectionGtwMockMvc = MockMvcBuilders.standaloneSetup(invoiceProjectionGtwResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceProjectionGtw createEntity(EntityManager em) {
        InvoiceProjectionGtw invoiceProjectionGtw = new InvoiceProjectionGtw()
            .code(DEFAULT_CODE)
            .date(DEFAULT_DATE)
            .details(DEFAULT_DETAILS)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .aggregateId(DEFAULT_AGGREGATE_ID);
        return invoiceProjectionGtw;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceProjectionGtw createUpdatedEntity(EntityManager em) {
        InvoiceProjectionGtw invoiceProjectionGtw = new InvoiceProjectionGtw()
            .code(UPDATED_CODE)
            .date(UPDATED_DATE)
            .details(UPDATED_DETAILS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .aggregateId(UPDATED_AGGREGATE_ID);
        return invoiceProjectionGtw;
    }

    @BeforeEach
    public void initTest() {
        invoiceProjectionGtw = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceProjectionGtw() throws Exception {
        int databaseSizeBeforeCreate = invoiceProjectionGtwRepository.findAll().size();

        // Create the InvoiceProjectionGtw
        restInvoiceProjectionGtwMockMvc.perform(post("/api/invoice-projection-gtws")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceProjectionGtw)))
            .andExpect(status().isCreated());

        // Validate the InvoiceProjectionGtw in the database
        List<InvoiceProjectionGtw> invoiceProjectionGtwList = invoiceProjectionGtwRepository.findAll();
        assertThat(invoiceProjectionGtwList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceProjectionGtw testInvoiceProjectionGtw = invoiceProjectionGtwList.get(invoiceProjectionGtwList.size() - 1);
        assertThat(testInvoiceProjectionGtw.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInvoiceProjectionGtw.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInvoiceProjectionGtw.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testInvoiceProjectionGtw.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testInvoiceProjectionGtw.getPaymentAmount()).isEqualTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testInvoiceProjectionGtw.getAggregateId()).isEqualTo(DEFAULT_AGGREGATE_ID);
    }

    @Test
    @Transactional
    public void createInvoiceProjectionGtwWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceProjectionGtwRepository.findAll().size();

        // Create the InvoiceProjectionGtw with an existing ID
        invoiceProjectionGtw.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceProjectionGtwMockMvc.perform(post("/api/invoice-projection-gtws")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceProjectionGtw)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProjectionGtw in the database
        List<InvoiceProjectionGtw> invoiceProjectionGtwList = invoiceProjectionGtwRepository.findAll();
        assertThat(invoiceProjectionGtwList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceProjectionGtwRepository.findAll().size();
        // set the field null
        invoiceProjectionGtw.setCode(null);

        // Create the InvoiceProjectionGtw, which fails.

        restInvoiceProjectionGtwMockMvc.perform(post("/api/invoice-projection-gtws")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceProjectionGtw)))
            .andExpect(status().isBadRequest());

        List<InvoiceProjectionGtw> invoiceProjectionGtwList = invoiceProjectionGtwRepository.findAll();
        assertThat(invoiceProjectionGtwList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceProjectionGtws() throws Exception {
        // Initialize the database
        invoiceProjectionGtwRepository.saveAndFlush(invoiceProjectionGtw);

        // Get all the invoiceProjectionGtwList
        restInvoiceProjectionGtwMockMvc.perform(get("/api/invoice-projection-gtws?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceProjectionGtw.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].aggregateId").value(hasItem(DEFAULT_AGGREGATE_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getInvoiceProjectionGtw() throws Exception {
        // Initialize the database
        invoiceProjectionGtwRepository.saveAndFlush(invoiceProjectionGtw);

        // Get the invoiceProjectionGtw
        restInvoiceProjectionGtwMockMvc.perform(get("/api/invoice-projection-gtws/{id}", invoiceProjectionGtw.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceProjectionGtw.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.aggregateId").value(DEFAULT_AGGREGATE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoiceProjectionGtw() throws Exception {
        // Get the invoiceProjectionGtw
        restInvoiceProjectionGtwMockMvc.perform(get("/api/invoice-projection-gtws/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceProjectionGtw() throws Exception {
        // Initialize the database
        invoiceProjectionGtwRepository.saveAndFlush(invoiceProjectionGtw);

        int databaseSizeBeforeUpdate = invoiceProjectionGtwRepository.findAll().size();

        // Update the invoiceProjectionGtw
        InvoiceProjectionGtw updatedInvoiceProjectionGtw = invoiceProjectionGtwRepository.findById(invoiceProjectionGtw.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceProjectionGtw are not directly saved in db
        em.detach(updatedInvoiceProjectionGtw);
        updatedInvoiceProjectionGtw
            .code(UPDATED_CODE)
            .date(UPDATED_DATE)
            .details(UPDATED_DETAILS)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .aggregateId(UPDATED_AGGREGATE_ID);

        restInvoiceProjectionGtwMockMvc.perform(put("/api/invoice-projection-gtws")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceProjectionGtw)))
            .andExpect(status().isOk());

        // Validate the InvoiceProjectionGtw in the database
        List<InvoiceProjectionGtw> invoiceProjectionGtwList = invoiceProjectionGtwRepository.findAll();
        assertThat(invoiceProjectionGtwList).hasSize(databaseSizeBeforeUpdate);
        InvoiceProjectionGtw testInvoiceProjectionGtw = invoiceProjectionGtwList.get(invoiceProjectionGtwList.size() - 1);
        assertThat(testInvoiceProjectionGtw.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInvoiceProjectionGtw.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInvoiceProjectionGtw.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testInvoiceProjectionGtw.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testInvoiceProjectionGtw.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testInvoiceProjectionGtw.getAggregateId()).isEqualTo(UPDATED_AGGREGATE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceProjectionGtw() throws Exception {
        int databaseSizeBeforeUpdate = invoiceProjectionGtwRepository.findAll().size();

        // Create the InvoiceProjectionGtw

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceProjectionGtwMockMvc.perform(put("/api/invoice-projection-gtws")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceProjectionGtw)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceProjectionGtw in the database
        List<InvoiceProjectionGtw> invoiceProjectionGtwList = invoiceProjectionGtwRepository.findAll();
        assertThat(invoiceProjectionGtwList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceProjectionGtw() throws Exception {
        // Initialize the database
        invoiceProjectionGtwRepository.saveAndFlush(invoiceProjectionGtw);

        int databaseSizeBeforeDelete = invoiceProjectionGtwRepository.findAll().size();

        // Delete the invoiceProjectionGtw
        restInvoiceProjectionGtwMockMvc.perform(delete("/api/invoice-projection-gtws/{id}", invoiceProjectionGtw.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceProjectionGtw> invoiceProjectionGtwList = invoiceProjectionGtwRepository.findAll();
        assertThat(invoiceProjectionGtwList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceProjectionGtw.class);
        InvoiceProjectionGtw invoiceProjectionGtw1 = new InvoiceProjectionGtw();
        invoiceProjectionGtw1.setId(1L);
        InvoiceProjectionGtw invoiceProjectionGtw2 = new InvoiceProjectionGtw();
        invoiceProjectionGtw2.setId(invoiceProjectionGtw1.getId());
        assertThat(invoiceProjectionGtw1).isEqualTo(invoiceProjectionGtw2);
        invoiceProjectionGtw2.setId(2L);
        assertThat(invoiceProjectionGtw1).isNotEqualTo(invoiceProjectionGtw2);
        invoiceProjectionGtw1.setId(null);
        assertThat(invoiceProjectionGtw1).isNotEqualTo(invoiceProjectionGtw2);
    }
}
