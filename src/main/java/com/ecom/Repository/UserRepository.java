package com.ecom.Repository;

import java.util.Optional;

import com.ecom.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
  User findByUserName(String userName);
}

