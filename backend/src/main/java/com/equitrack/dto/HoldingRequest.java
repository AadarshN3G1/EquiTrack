package com.equitrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class HoldingRequest {

    @NotBlank
    private String symbol;

    @NotBlank
    private String sector;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Positive
    private BigDecimal costBasis;

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getCostBasis() { return costBasis; }
    public void setCostBasis(BigDecimal costBasis) { this.costBasis = costBasis; }
}
