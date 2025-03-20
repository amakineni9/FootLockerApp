package com.dwprojects.repository;

import com.dwprojects.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    public List<User> findAll();
    Optional<User> findByEmailAndPassword(String email, String password);
}
