package com.equitrack.repository;

import com.equitrack.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findByPortfolioId(Long portfolioId);
}
