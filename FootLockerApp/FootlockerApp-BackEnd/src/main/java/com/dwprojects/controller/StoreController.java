package com.dwprojects.controller;

import com.dwprojects.model.Stores;
import com.dwprojects.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "http://localhost:3000")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<Stores> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stores> getStoreById(@PathVariable int id) {
        return storeService.getStoreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/state/{state}")
    public List<Stores> getStoresByState(@PathVariable String state) {
        return storeService.getStoresByState(state);
    }

    @PostMapping
    public ResponseEntity<?> createStore(@Valid @RequestBody Stores store) {
        if (store.getState().length() != 2) {
            return ResponseEntity.badRequest().body("State code must be 2 characters");
        }
        return ResponseEntity.ok(storeService.saveStore(store));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stores> updateStore(@PathVariable int id, @Valid @RequestBody Stores store) {
        return storeService.getStoreById(id)
                .map(existingStore -> {
                    store.setStore_id(id);
                    return ResponseEntity.ok(storeService.saveStore(store));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable int id) {
        return storeService.getStoreById(id)
                .map(store -> {
                    storeService.deleteStore(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
