package com.equitrack.controller;

import com.equitrack.dto.MarketQuote;
import com.equitrack.service.MarketDataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market-data")
@CrossOrigin(origins = "${equitrack.frontend.origin:http://localhost:3000}")
public class MarketDataController {

    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    // GET /api/market-data/AAPL  -> served from Redis cache when possible
    @GetMapping("/{symbol}")
    public MarketQuote getQuote(@PathVariable String symbol) {
        return marketDataService.getQuote(symbol.toUpperCase());
    }
}
