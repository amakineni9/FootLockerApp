package com.dwprojects.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dwprojects.model.User;
import com.dwprojects.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Cacheable("users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Cacheable(value = "users", key = "#id")
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
    
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
