package be.civadis.store.base.multitenancy;

import com.zaxxer.hikari.HikariDataSource;
import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.liquibase.AsyncSpringLiquibase;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.civadis.store.base.config.ApplicationProperties;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
public class MultitenantDatabaseConfig {

    private final Logger log = LoggerFactory.getLogger(MultitenantDatabaseConfig.class);

    private final Environment env;
    private JpaProperties jpaProperties;
    private ApplicationProperties applicationProperties;

    public MultitenantDatabaseConfig(Environment env, JpaProperties jpaProperties, ApplicationProperties applicationProperties) {
        this.env = env;
        this.jpaProperties = jpaProperties;
        this.applicationProperties = applicationProperties;
    }

    //hibernate

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Conditional(MultiSchemasCondition.class)
    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver(){
        return new MyCurrentTenantIdentifierResolver();
    }

    @Bean(name = "multiTenantConnectionProvider")
    public DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProvider() {
        HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();

        applicationProperties.getMultitenancy().getTenants().stream().forEach(tc -> {
            DataSource ds = DataSourceBuilder
                .create()
                .username(tc.getUsername())
                .password(tc.getPassword())
                .url(tc.getUrl())
                .build();

            if (ds instanceof HikariDataSource){
                ((HikariDataSource) ds).setAutoCommit(tc.getHikari().isAutoCommit());
                ((HikariDataSource) ds).setDataSourceProperties(tc.getHikari().getDataSourceProperties());
            }

            dataSources.put(tc.getName(), ds);
        });

        log.warn("datasources : " + dataSources.size());
        log.warn(applicationProperties.getMultitenancy().getDefaultTenant().toString());

        return new DataSourceBasedMultiTenantConnectionProviderImpl(applicationProperties.getMultitenancy().getDefaultTenant().getName(), dataSources);
    }

    @Bean
    @DependsOn("multiTenantConnectionProvider")
    public DataSource dataSource() {
        return dataSourceBasedMultiTenantConnectionProvider().getDefaultDataSource();
    }

    @Conditional(MultiSchemasCondition.class)
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, 
                                                                       HibernateProperties hibernateProperties, JpaProperties jpaProperties,
                                                                       MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                       CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
        
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings().ddlAuto(() -> {return "none";}));
        
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("be.civadis");
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(properties);

        return em;
    }

}

