# EquiTrack

A full-stack portfolio management application that integrates external
financial APIs to aggregate real-time market data and track portfolio
performance.

## Architecture

```
Frontend (HTML/JS)  ──REST──▶  Spring Boot Backend  ──REST──▶  External Market Data API
                                     │        │
                                     │        └──REST──▶  Python/NumPy Analytics Service
                                     │
                                  MySQL (holdings, portfolios, users)
                                     │
                                  Redis (market data + analytics cache)
```

- **backend/** — Spring Boot REST API (Java 17, Maven). Handles portfolios,
  holdings, users, and proxies market data with Redis caching.
- **analytics-service/** — Python FastAPI microservice using NumPy to compute
  sector exposure, diversification score, max asset concentration, and
  historical volatility.
- **frontend/** — Static HTML/CSS/JS dashboard that calls the backend REST API.
- **db/** — MySQL init script.
- **docker-compose.yml** — Runs all five services (MySQL, Redis, analytics
  service, backend, frontend) together.

## Endpoints

```
GET  /api/portfolios?userId=1
GET  /api/portfolios/{id}
POST /api/portfolios
GET  /api/portfolios/{id}/holdings
POST /api/portfolios/{id}/holdings
GET  /api/portfolios/{id}/analytics
GET  /api/market-data/{symbol}
GET  /api/users/{id}
POST /api/users
```

## Running locally with Docker Compose

```bash
docker compose up --build
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Analytics service: http://localhost:8000
- MySQL: localhost:3306
- Redis: localhost:6379

## Running without Docker (for IDE development)

**Backend (IntelliJ):**
1. Open `backend/` as a Maven project in IntelliJ.
2. Start a local MySQL instance and Redis instance (or `docker compose up mysql redis`).
3. Run `EquiTrackApplication.java` (it reads `application.yml`, defaulting to
   `localhost:3306` / `localhost:6379`).

**Analytics service:**
```bash
cd analytics-service
python -m venv venv
source venv/bin/activate   # venv\Scripts\activate on Windows
pip install -r requirements.txt
uvicorn app:app --reload --port 8000
```

**Frontend:**
Just open `frontend/index.html` in a browser, or serve it with any static
server (e.g. `npx serve frontend`). It targets `http://localhost:8080/api`
by default (override via `window.EQUITRACK_API_BASE` in the browser console
or by editing `js/app.js`).

## Notes on the architecture

NumPy lives in its own Python microservice rather than inside the Java
backend — Spring Boot doesn't have a native NumPy equivalent, so portfolio
analytics (sector exposure, diversification, concentration, volatility) are
computed in Python and returned as JSON, then cached in Redis by the backend
under the `portfolioAnalytics` cache. Market data quotes are cached
separately under `marketData` with a short TTL, which is what cuts repeat
calls to the external market data API.
