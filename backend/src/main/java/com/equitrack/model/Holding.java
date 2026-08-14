package com.equitrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    private Portfolio portfolio;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String sector;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "cost_basis", nullable = false)
    private BigDecimal costBasis;

    public Holding() {}

    public Holding(Portfolio portfolio, String symbol, String sector, BigDecimal quantity, BigDecimal costBasis) {
        this.portfolio = portfolio;
        this.symbol = symbol;
        this.sector = sector;
        this.quantity = quantity;
        this.costBasis = costBasis;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getCostBasis() { return costBasis; }
    public void setCostBasis(BigDecimal costBasis) { this.costBasis = costBasis; }
}
