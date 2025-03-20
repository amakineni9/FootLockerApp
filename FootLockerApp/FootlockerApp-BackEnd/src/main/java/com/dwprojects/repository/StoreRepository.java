package com.dwprojects.repository;

import com.dwprojects.model.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Stores, Integer> {
    List<Stores> findByState(String state);
}
