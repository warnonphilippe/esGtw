package be.civadis.store.base.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final long serialVersionUID = 1L;

    private String defaultTenant;

    private Map<String, DataSource> map;

    public DataSourceBasedMultiTenantConnectionProviderImpl(String defaultTenant, Map<String, DataSource> map) {
        super();
        this.defaultTenant = defaultTenant;
        this.map = map;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(defaultTenant);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        DataSource ds = map.get(tenantIdentifier);
        return (ds != null) ? ds : selectAnyDataSource();
    }

    public DataSource getDefaultDataSource() {
        return map.get(defaultTenant);
    }

    public Map<String, DataSource> getDataSources(){
        return Collections.unmodifiableMap(map);
    }

}
