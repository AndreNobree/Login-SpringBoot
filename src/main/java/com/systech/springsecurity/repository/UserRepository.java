package com.systech.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.systech.springsecurity.entities.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);
}
