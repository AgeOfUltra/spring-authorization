package com.authorization.springauthorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheInspectionService {
    @Autowired
    CacheManager manager;

    public void printCacheContents(String name){
        Cache cache = manager.getCache(name);
        if(cache!=null){
            System.out.println("Cache Content");
            System.out.println(Objects.requireNonNull(cache.getNativeCache()).toString());
        }else{
            System.out.println("Cache Not Found with name: "+name);
        }
    }

}
