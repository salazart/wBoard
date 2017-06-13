package ua.sgkhmja.wboard.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(ua.sgkhmja.wboard.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.AdminTools.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.Board.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.OwnerTools.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.ReaderTools.class.getName(), jcacheConfiguration);
            cm.createCache(ua.sgkhmja.wboard.domain.WriterTools.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
