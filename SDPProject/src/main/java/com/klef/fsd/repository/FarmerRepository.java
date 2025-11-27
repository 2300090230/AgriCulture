package com.klef.fsd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.fsd.model.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Integer> {
    Farmer findByUsernameAndPassword(String username, String password);
    List<Farmer> findByStatus(String status);
    
    Optional<Farmer> findByEmail(String email);
    Farmer findByResetToken(String resetToken);

}

