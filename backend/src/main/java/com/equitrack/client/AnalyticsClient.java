package com.equitrack.client;

import com.equitrack.dto.AnalyticsResponse;
import com.equitrack.model.Holding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Talks to the standalone Python/NumPy analytics service which
 * computes sector exposure, diversification, asset concentration
 * and historical volatility for a portfolio's holdings.
 *
 * Keeping the numerical work in Python (NumPy) rather than the Java
 * backend lets each layer do what it's best at: Spring Boot handles
 * the API/data layer, Python handles the vectorized math.
 */
@Component
public class AnalyticsClient {

    private final RestClient restClient;

    @Value("${equitrack.analytics-service.base-url:http://localhost:8000}")
    private String analyticsBaseUrl;

    public AnalyticsClient() {
        this.restClient = RestClient.create();
    }

    @Cacheable(cacheNames = "portfolioAnalytics", key = "#portfolioId")
    public AnalyticsResponse computeAnalytics(Long portfolioId, List<Holding> holdings) {
        List<Map<String, Object>> payloadHoldings = holdings.stream()
                .map(h -> Map.<String, Object>of(
                        "symbol", h.getSymbol(),
                        "sector", h.getSector(),
                        "quantity", h.getQuantity(),
                        "costBasis", h.getCostBasis()
                ))
                .collect(Collectors.toList());

        Map<String, Object> requestBody = Map.of(
                "portfolioId", portfolioId,
                "holdings", payloadHoldings
        );

        return restClient.post()
                .uri(analyticsBaseUrl + "/analyze")
                .body(requestBody)
                .retrieve()
                .body(AnalyticsResponse.class);
    }
}
