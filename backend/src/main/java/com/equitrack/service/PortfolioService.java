package com.equitrack.service;

import com.equitrack.client.AnalyticsClient;
import com.equitrack.dto.AnalyticsResponse;
import com.equitrack.dto.HoldingRequest;
import com.equitrack.dto.PortfolioRequest;
import com.equitrack.model.Holding;
import com.equitrack.model.Portfolio;
import com.equitrack.model.User;
import com.equitrack.repository.HoldingRepository;
import com.equitrack.repository.PortfolioRepository;
import com.equitrack.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;
    private final AnalyticsClient analyticsClient;

    public PortfolioService(PortfolioRepository portfolioRepository,
                             HoldingRepository holdingRepository,
                             UserRepository userRepository,
                             AnalyticsClient analyticsClient) {
        this.portfolioRepository = portfolioRepository;
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
        this.analyticsClient = analyticsClient;
    }

    public List<Portfolio> getPortfoliosForUser(Long userId) {
        return portfolioRepository.findByOwnerId(userId);
    }

    public Portfolio getPortfolio(Long portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio " + portfolioId + " not found"));
    }

    @Transactional
    public Portfolio createPortfolio(PortfolioRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User " + request.getOwnerId() + " not found"));
        Portfolio portfolio = new Portfolio(request.getName(), owner);
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Holding addHolding(Long portfolioId, HoldingRequest request) {
        Portfolio portfolio = getPortfolio(portfolioId);
        Holding holding = new Holding(
                portfolio,
                request.getSymbol().toUpperCase(),
                request.getSector(),
                request.getQuantity(),
                request.getCostBasis()
        );
        return holdingRepository.save(holding);
    }

    public List<Holding> getHoldings(Long portfolioId) {
        return holdingRepository.findByPortfolioId(portfolioId);
    }

    public AnalyticsResponse getAnalytics(Long portfolioId) {
        Portfolio portfolio = getPortfolio(portfolioId);
        return analyticsClient.computeAnalytics(portfolioId, portfolio.getHoldings());
    }
}
