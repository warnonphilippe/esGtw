package be.civadis.store.business.invoice.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * InvoiceDto
 */
public class InvoiceDto {

    private String code;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant date;
    private String details;

    public InvoiceDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    

    

    
}