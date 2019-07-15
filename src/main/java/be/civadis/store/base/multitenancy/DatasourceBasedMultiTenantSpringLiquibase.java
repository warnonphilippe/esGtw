package be.civadis.store.base.multitenancy;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.util.Map;

public class DatasourceBasedMultiTenantSpringLiquibase extends MultiTenantSpringLiquibase {

    private static final Logger log = LoggerFactory.getLogger(DatasourceBasedMultiTenantSpringLiquibase.class);

    @Autowired
    private DataSourceBasedMultiTenantConnectionProviderImpl multiTenantConnectionProvider;

    private ResourceLoader resourceLoader;

    public DatasourceBasedMultiTenantSpringLiquibase() {
        super();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.runOnAllDataSources();
    }

    private void runOnAllDataSources() throws LiquibaseException {
        Map<String, DataSource> dataSources = multiTenantConnectionProvider.getDataSources();
        for (String tenant: dataSources.keySet()) {
            log.info("Initializing Liquibase for data source {}", tenant);
            SpringLiquibase liquibase = getSpringLiquibase(dataSources.get(tenant));
            liquibase.afterPropertiesSet();
            log.info("Liquibase ran for data source {}", tenant);
        }
    }

    private SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(getChangeLog());
        liquibase.setChangeLogParameters(getParameters());
        liquibase.setContexts(getContexts());
        liquibase.setLabels(getLabels());
        liquibase.setDropFirst(isDropFirst());
        liquibase.setShouldRun(isShouldRun());
        liquibase.setRollbackFile(getRollbackFile());
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(getDefaultSchema());
        return liquibase;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
