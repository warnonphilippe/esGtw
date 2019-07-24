package be.civadis.store.base.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import be.civadis.store.base.security.SecurityUtils;

import java.util.Optional;

/**
 * Indique à Hibernate le tenant courant
 */
 public class ContextBasedTenantIdentifierResolver implements CurrentTenantIdentifierResolver
 {
 
     private final Logger log = LoggerFactory.getLogger(ContextBasedTenantIdentifierResolver.class);
 
     @Override
     public String resolveCurrentTenantIdentifier() {
         String user = SecurityUtils.getCurrentUserLogin().orElse("notLogged");
         log.debug("*********************** user : " + user);
         String tenant = Optional.ofNullable(TenantContext.getCurrentTenant()).orElse("");
         log.debug("*********************** resolve tenant for connection : " + tenant);
         return tenant;
     }
 
     @Override
     public boolean validateExistingCurrentSessions() {
         return true;
     }
 }

