package com.equitrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PortfolioRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long ownerId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
