package be.civadis.store.base.multitenancy;

import be.civadis.store.base.config.ApplicationProperties;

import io.github.jhipster.config.JHipsterConstants;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfig.class);
    private final Environment env;
    private JpaProperties jpaProperties;
    private ApplicationProperties applicationProperties;

    public LiquibaseConfig(Environment env, JpaProperties jpaProperties, ApplicationProperties applicationProperties) {
        this.env = env;
        this.jpaProperties = jpaProperties;
        this.applicationProperties = applicationProperties;
    }

    @Primary
    @Bean("liquibase")
    public SpringLiquibase liquibase(@Qualifier("dataSource") DataSource dataSource,
                                         LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());

        //active liquibase sur le schéma public seulement pour les tests
        if (liquibaseProperties.getContexts().contentEquals("test")){
            if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
                liquibase.setShouldRun(false);
            } else {
                liquibase.setShouldRun(liquibaseProperties.isEnabled());
                log.debug("Configuring Liquibase");
            }
        } else {
            liquibase.setShouldRun(false);
        }

        return liquibase;
    }

    @Conditional(MultiSchemasCondition.class)
    @Bean("tenantLiquibase")
    public DatasourceBasedMultiTenantSpringLiquibase liquibaseMultiTenant() {
        DatasourceBasedMultiTenantSpringLiquibase liquibase = new DatasourceBasedMultiTenantSpringLiquibase();
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts("development, production");
        if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(true);
            log.debug("Configuring Liquibase");
        }
        liquibase.setShouldRun(true);
        return liquibase;
    }

}
