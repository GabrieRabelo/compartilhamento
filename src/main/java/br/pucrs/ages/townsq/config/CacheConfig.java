package br.pucrs.ages.townsq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    @Autowired
    CacheManager cacheManager;

    @Bean(name = "springCM")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("banner");
    }

    @Scheduled(fixedRate = 200000) // ajustar o tempo
    @CacheEvict(value = {"banner"}, allEntries = true)
    public void evictAllcachesAtIntervals() {
        cacheManager.getCache("banner").clear();
    }
}