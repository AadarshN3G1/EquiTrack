"""
EquiTrack Analytics Service
----------------------------
A small FastAPI microservice that performs the numerical portfolio
analytics for EquiTrack using NumPy:

  * Sector exposure       - % of portfolio market value per sector
  * Diversification score - Herfindahl-based diversification measure
  * Max asset concentration - largest single-position weight
  * Historical volatility - annualized stdev of simulated daily returns

The Spring Boot backend calls POST /analyze with a portfolio's
holdings; this service returns the computed metrics as JSON, which
Spring then caches in Redis for a configurable TTL.
"""

from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Dict
import numpy as np

app = FastAPI(title="EquiTrack Analytics Service")


class HoldingIn(BaseModel):
    symbol: str
    sector: str
    quantity: float
    costBasis: float


class AnalyticsRequest(BaseModel):
    portfolioId: int
    holdings: List[HoldingIn]


class AnalyticsResponse(BaseModel):
    portfolioId: int
    sectorExposure: Dict[str, float]
    diversificationScore: float
    maxAssetConcentration: float
    historicalVolatility: float


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/analyze", response_model=AnalyticsResponse)
def analyze(request: AnalyticsRequest):
    holdings = request.holdings

    if not holdings:
        return AnalyticsResponse(
            portfolioId=request.portfolioId,
            sectorExposure={},
            diversificationScore=0.0,
            maxAssetConcentration=0.0,
            historicalVolatility=0.0,
        )

    market_values = np.array([h.quantity * h.costBasis for h in holdings], dtype=float)
    sectors = np.array([h.sector for h in holdings])
    total_value = market_values.sum()

    weights = market_values / total_value if total_value > 0 else np.zeros_like(market_values)

    # --- Sector exposure: sum of weights grouped by sector ---
    sector_exposure: Dict[str, float] = {}
    for sector in np.unique(sectors):
        sector_mask = sectors == sector
        sector_exposure[str(sector)] = round(float(weights[sector_mask].sum()) * 100, 2)

    # --- Diversification score (1 - Herfindahl-Hirschman Index) ---
    # HHI close to 1 means concentrated in one asset; close to 0 means
    # evenly spread. We report diversification as 1 - HHI so higher is better.
    hhi = float(np.sum(weights ** 2))
    diversification_score = round((1 - hhi) * 100, 2)

    # --- Max single-asset concentration ---
    max_concentration = round(float(weights.max()) * 100, 2) if len(weights) else 0.0

    # --- Historical volatility ---
    # In production this would pull real daily price history per symbol
    # from the market-data service. Here we simulate a return series per
    # holding (seeded by symbol) purely to demonstrate the NumPy pipeline
    # for annualized volatility of the weighted portfolio.
    rng = np.random.default_rng(seed=abs(hash(tuple(h.symbol for h in holdings))) % (2**32))
    trading_days = 252
    simulated_daily_returns = rng.normal(loc=0.0004, scale=0.015, size=(len(holdings), trading_days))
    portfolio_daily_returns = weights @ simulated_daily_returns
    daily_vol = float(np.std(portfolio_daily_returns, ddof=1))
    annualized_vol = round(daily_vol * np.sqrt(trading_days) * 100, 2)

    return AnalyticsResponse(
        portfolioId=request.portfolioId,
        sectorExposure=sector_exposure,
        diversificationScore=diversification_score,
        maxAssetConcentration=max_concentration,
        historicalVolatility=annualized_vol,
    )
