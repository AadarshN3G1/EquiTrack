package com.equitrack.repository;

import com.equitrack.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByOwnerId(Long ownerId);
}
