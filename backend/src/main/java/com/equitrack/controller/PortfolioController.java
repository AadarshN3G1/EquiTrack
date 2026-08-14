package com.equitrack.controller;

import com.equitrack.dto.AnalyticsResponse;
import com.equitrack.dto.HoldingRequest;
import com.equitrack.dto.PortfolioRequest;
import com.equitrack.model.Holding;
import com.equitrack.model.Portfolio;
import com.equitrack.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "${equitrack.frontend.origin:http://localhost:3000}")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // GET /api/portfolios?userId=123
    @GetMapping
    public List<Portfolio> listPortfolios(@RequestParam Long userId) {
        return portfolioService.getPortfoliosForUser(userId);
    }

    // GET /api/portfolios/45
    @GetMapping("/{portfolioId}")
    public Portfolio getPortfolio(@PathVariable Long portfolioId) {
        return portfolioService.getPortfolio(portfolioId);
    }

    // POST /api/portfolios
    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@Valid @RequestBody PortfolioRequest request) {
        Portfolio created = portfolioService.createPortfolio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/portfolios/45/holdings
    @GetMapping("/{portfolioId}/holdings")
    public List<Holding> getHoldings(@PathVariable Long portfolioId) {
        return portfolioService.getHoldings(portfolioId);
    }

    // POST /api/portfolios/45/holdings
    @PostMapping("/{portfolioId}/holdings")
    public ResponseEntity<Holding> addHolding(@PathVariable Long portfolioId,
                                               @Valid @RequestBody HoldingRequest request) {
        Holding holding = portfolioService.addHolding(portfolioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(holding);
    }

    // GET /api/portfolios/45/analytics
    @GetMapping("/{portfolioId}/analytics")
    public AnalyticsResponse getAnalytics(@PathVariable Long portfolioId) {
        return portfolioService.getAnalytics(portfolioId);
    }
}
