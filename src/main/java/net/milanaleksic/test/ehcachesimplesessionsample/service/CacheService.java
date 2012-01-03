package net.milanaleksic.test.ehcachesimplesessionsample.service;

import java.io.Serializable;

public interface CacheService {

    public void put(String key, Serializable value, int expirationInSeconds) ;

	public <T> T get(String key) ;

	void remove(String key);

}
