import java.util.*;
public class Main {
    public static void main(String[] args) {

        StockApiClient api = new StockApiClient();
        PriceCache cache = new PriceCache();

        PortfolioService portfolioService = new PortfolioService();
        AnalyticsService analytics = new AnalyticsService(api, cache);
        FileStorage storage = new FileStorage();

        User user = portfolioService.createUser("john");

        portfolioService.addStock("john", "AAPL", 5, 170);
        portfolioService.addStock("john", "TSLA", 2, 240);

        System.out.println("Portfolio Value: " + analytics.calculatePortfolioValue(user));

        System.out.println("Profit/Loss: " + analytics.calculateProfitLoss(user));

        storage.save(user);
    }
}