import java.util.*;

public class PortfolioService {
    private Map<String, User> users = new HashMap<>();
    public User createUser(String username) {
        User user = new User(username);
        users.put(username, user);
        return user;
    }
    public void addStock(String username, String symbol, int shares, double buyPrice) {
        User user = users.get(username);
        user.getPortfolio().add(new PortfolioItem(symbol, shares, buyPrice));
    }
    public User getUser(String username) {
        return users.get(username);
    }
}