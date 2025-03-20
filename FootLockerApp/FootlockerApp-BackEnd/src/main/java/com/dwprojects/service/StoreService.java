package com.dwprojects.service;

import com.dwprojects.model.Stores;
import com.dwprojects.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    
    @Cacheable("stores")
    public List<Stores> getAllStores() {
        return storeRepository.findAll();
    }
    
    @Cacheable(value = "stores", key = "#id")
    public Optional<Stores> getStoreById(int id) {
        return storeRepository.findById(id);
    }
    
    @Cacheable(value = "stores", key = "'state:' + #state")
    public List<Stores> getStoresByState(String state) {
        return storeRepository.findByState(state.toUpperCase());
    }
    
    @CacheEvict(value = "stores", allEntries = true)
    public Stores saveStore(Stores store) {
        store.setState(store.getState().toUpperCase());
        return storeRepository.save(store);
    }
    
    @CacheEvict(value = "stores", allEntries = true)
    public void deleteStore(int id) {
        storeRepository.deleteById(id);
    }
}
