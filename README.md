# EquiTrack

A full-stack portfolio management application that integrates external financial APIs to aggregate real-time market data and track portfolio performance.

## Architecture

```text
Frontend (HTML/JS)  ──REST──▶  Spring Boot Backend  ──REST──▶  External Market Data API
                                     │        │
                                     │        └──REST──▶  Python/NumPy Analytics Service
                                     │
                                  MySQL (holdings, portfolios, users)
                                     │
                                  Redis (market data + analytics cache)