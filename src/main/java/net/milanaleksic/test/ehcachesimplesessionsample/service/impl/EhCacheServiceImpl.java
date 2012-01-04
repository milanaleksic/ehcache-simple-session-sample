package net.milanaleksic.test.ehcachesimplesessionsample.service.impl;

import net.milanaleksic.test.ehcachesimplesessionsample.service.CacheService;
import net.sf.ehcache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;

public class EhCacheServiceImpl implements CacheService, ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(EhCacheServiceImpl.class);

    private Cache applicationCache;

    private String cacheName;

    private CacheManager cacheManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (log.isInfoEnabled())
            log.info("Using cache name {} as application cache", cacheName);
        applicationCache = cacheManager.getCache(cacheName);
        if (applicationCache == null) {
            log.warn("Cache name {} could not be found. Will use default cache", cacheName);
            cacheManager.addCacheIfAbsent(cacheName);
            applicationCache = cacheManager.getCache(cacheName);
        }
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public void put(String key, Serializable value, int expirationInSeconds) {
        Element element = new Element(key, value);
        if (expirationInSeconds == 0)
            element.setEternal(true);
        else
            element.setTimeToLive(expirationInSeconds);
        applicationCache.put(element);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Element element = applicationCache.get(key);
        if (element == null)
            return null;
        return (T) element.getValue();
    }

    @Override
    public void remove(String key) {
        applicationCache.remove(key);
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
