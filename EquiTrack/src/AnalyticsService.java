import java.util.*;

public class AnalyticsService {

    private StockApiClient api;
    private PriceCache cache;
    public AnalyticsService(StockApiClient api, PriceCache cache) {
        this.api = api;
        this.cache = cache;
    }
    public double getCurrentPrice(String symbol) {
        Double cached = cache.get(symbol);
        if (cached != null) return cached;
        double price = api.getPrice(symbol);
        cache.put(symbol, price);
        return price;
    }
    public double calculatePortfolioValue(User user) {
        double total = 0;
        for (PortfolioItem item : user.getPortfolio()) {
            double price = getCurrentPrice(item.getSymbol());
            total += price * item.getShares();
        }
        return total;
    }
    public double calculateProfitLoss(User user) {
        double profit = 0;
        for (PortfolioItem item : user.getPortfolio()) {
            double current = getCurrentPrice(item.getSymbol());
            double cost = item.getBuyPrice() * item.getShares();
            profit += (current * item.getShares()) - cost;
        }
        return profit;
    }
}