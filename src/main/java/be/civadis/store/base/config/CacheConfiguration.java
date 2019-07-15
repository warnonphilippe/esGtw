package be.civadis.store.base.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, be.civadis.store.base.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, be.civadis.store.base.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, be.civadis.store.base.domain.User.class.getName());
            createCache(cm, be.civadis.store.base.domain.Authority.class.getName());
            createCache(cm, be.civadis.store.base.domain.User.class.getName() + ".authorities");
            createCache(cm, be.civadis.store.base.domain.Product.class.getName());
            createCache(cm, be.civadis.store.base.domain.ProductCategory.class.getName());
            createCache(cm, be.civadis.store.base.domain.ProductCategory.class.getName() + ".products");
            createCache(cm, be.civadis.store.base.domain.Customer.class.getName());
            createCache(cm, be.civadis.store.base.domain.Customer.class.getName() + ".orders");
            createCache(cm, be.civadis.store.base.domain.ProductOrder.class.getName());
            createCache(cm, be.civadis.store.base.domain.ProductOrder.class.getName() + ".orderItems");
            createCache(cm, be.civadis.store.base.domain.OrderItem.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
