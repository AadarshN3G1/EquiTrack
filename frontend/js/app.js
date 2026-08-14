// EquiTrack Dashboard
// Talks to the Spring Boot REST API (see backend/src/main/java/com/equitrack/controller)

const API_BASE = window.EQUITRACK_API_BASE || "http://localhost:8080/api";
const DEMO_USER_ID = 1; // Swap for real auth/session handling later

let activePortfolioId = null;

const el = {
  userPill: document.getElementById("userPill"),
  portfolioList: document.getElementById("portfolioList"),
  newPortfolioBtn: document.getElementById("newPortfolioBtn"),
  addHoldingBtn: document.getElementById("addHoldingBtn"),
  activePortfolioName: document.getElementById("activePortfolioName"),
  diversificationValue: document.getElementById("diversificationValue"),
  concentrationValue: document.getElementById("concentrationValue"),
  volatilityValue: document.getElementById("volatilityValue"),
  sectorExposure: document.getElementById("sectorExposure"),
  holdingsBody: document.getElementById("holdingsBody"),
  holdingDialog: document.getElementById("holdingDialog"),
  holdingForm: document.getElementById("holdingForm"),
  cancelHoldingBtn: document.getElementById("cancelHoldingBtn"),
};

async function api(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.message || `Request failed: ${res.status}`);
  }
  return res.status === 204 ? null : res.json();
}

async function loadUser() {
  try {
    const user = await api(`/users/${DEMO_USER_ID}`);
    el.userPill.textContent = user.displayName || user.email;
  } catch {
    el.userPill.textContent = "Guest";
  }
}

async function loadPortfolios() {
  const portfolios = await api(`/portfolios?userId=${DEMO_USER_ID}`);
  el.portfolioList.innerHTML = "";

  portfolios.forEach((p) => {
    const li = document.createElement("li");
    li.textContent = p.name;
    li.dataset.id = p.id;
    li.addEventListener("click", () => selectPortfolio(p.id, p.name));
    el.portfolioList.appendChild(li);
  });

  if (portfolios.length > 0 && !activePortfolioId) {
    selectPortfolio(portfolios[0].id, portfolios[0].name);
  }
}

async function selectPortfolio(id, name) {
  activePortfolioId = id;
  el.activePortfolioName.textContent = name;
  el.addHoldingBtn.disabled = false;

  [...el.portfolioList.children].forEach((li) => {
    li.classList.toggle("active", Number(li.dataset.id) === id);
  });

  await Promise.all([loadHoldings(id), loadAnalytics(id)]);
}

async function loadHoldings(portfolioId) {
  const holdings = await api(`/portfolios/${portfolioId}/holdings`);
  el.holdingsBody.innerHTML = "";

  for (const h of holdings) {
    let quote = { price: "--", changePercent: 0 };
    try {
      quote = await api(`/market-data/${h.symbol}`);
    } catch {
      // market data unavailable, keep placeholder
    }

    const tr = document.createElement("tr");
    const changeClass = quote.changePercent >= 0 ? "change-positive" : "change-negative";
    tr.innerHTML = `
      <td>${h.symbol}</td>
      <td>${h.sector}</td>
      <td>${h.quantity}</td>
      <td>$${Number(h.costBasis).toFixed(2)}</td>
      <td>$${Number(quote.price).toFixed(2)}</td>
      <td class="${changeClass}">${Number(quote.changePercent).toFixed(2)}%</td>
    `;
    el.holdingsBody.appendChild(tr);
  }
}

async function loadAnalytics(portfolioId) {
  try {
    const analytics = await api(`/portfolios/${portfolioId}/analytics`);
    el.diversificationValue.textContent = `${analytics.diversificationScore}%`;
    el.concentrationValue.textContent = `${analytics.maxAssetConcentration}%`;
    el.volatilityValue.textContent = `${analytics.historicalVolatility}%`;

    el.sectorExposure.innerHTML = "";
    Object.entries(analytics.sectorExposure).forEach(([sector, pct]) => {
      const row = document.createElement("div");
      row.className = "sector-bar-row";
      row.innerHTML = `
        <span>${sector}</span>
        <div class="sector-bar-track"><div class="sector-bar-fill" style="width:${pct}%"></div></div>
        <span>${pct}%</span>
      `;
      el.sectorExposure.appendChild(row);
    });
  } catch (err) {
    console.warn("Analytics unavailable:", err.message);
  }
}

el.newPortfolioBtn.addEventListener("click", async () => {
  const name = prompt("Portfolio name:");
  if (!name) return;
  const created = await api("/portfolios", {
    method: "POST",
    body: JSON.stringify({ name, ownerId: DEMO_USER_ID }),
  });
  await loadPortfolios();
  selectPortfolio(created.id, created.name);
});

el.addHoldingBtn.addEventListener("click", () => el.holdingDialog.showModal());
el.cancelHoldingBtn.addEventListener("click", () => el.holdingDialog.close());

el.holdingForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const payload = {
    symbol: document.getElementById("symbolInput").value.toUpperCase(),
    sector: document.getElementById("sectorInput").value,
    quantity: Number(document.getElementById("quantityInput").value),
    costBasis: Number(document.getElementById("costBasisInput").value),
  };

  await api(`/portfolios/${activePortfolioId}/holdings`, {
    method: "POST",
    body: JSON.stringify(payload),
  });

  el.holdingDialog.close();
  el.holdingForm.reset();
  await Promise.all([loadHoldings(activePortfolioId), loadAnalytics(activePortfolioId)]);
});

(async function init() {
  await loadUser();
  await loadPortfolios();
})();
