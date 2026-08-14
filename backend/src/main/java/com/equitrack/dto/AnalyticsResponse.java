package com.equitrack.dto;

import java.util.Map;

/**
 * Mirrors the JSON payload returned by the Python/NumPy analytics
 * microservice for a single portfolio.
 */
public class AnalyticsResponse {
    private Long portfolioId;
    private Map<String, Double> sectorExposure;
    private double diversificationScore;
    private double maxAssetConcentration;
    private double historicalVolatility;

    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

    public Map<String, Double> getSectorExposure() { return sectorExposure; }
    public void setSectorExposure(Map<String, Double> sectorExposure) { this.sectorExposure = sectorExposure; }

    public double getDiversificationScore() { return diversificationScore; }
    public void setDiversificationScore(double diversificationScore) { this.diversificationScore = diversificationScore; }

    public double getMaxAssetConcentration() { return maxAssetConcentration; }
    public void setMaxAssetConcentration(double maxAssetConcentration) { this.maxAssetConcentration = maxAssetConcentration; }

    public double getHistoricalVolatility() { return historicalVolatility; }
    public void setHistoricalVolatility(double historicalVolatility) { this.historicalVolatility = historicalVolatility; }
}
