package com.equitrack.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Lightweight snapshot of a market quote returned by the external
 * financial data provider (or served from Redis cache).
 */
public class MarketQuote implements Serializable {
    private String symbol;
    private BigDecimal price;
    private BigDecimal changePercent;
    private String asOf;

    public MarketQuote() {}

    public MarketQuote(String symbol, BigDecimal price, BigDecimal changePercent, String asOf) {
        this.symbol = symbol;
        this.price = price;
        this.changePercent = changePercent;
        this.asOf = asOf;
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }

    public String getAsOf() { return asOf; }
    public void setAsOf(String asOf) { this.asOf = asOf; }
}
