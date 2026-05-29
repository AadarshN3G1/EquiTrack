import java.util.*;

public class User {
    private String username;
    private List<PortfolioItem> portfolio;
    public User(String username) {
        this.username = username;
        this.portfolio = new ArrayList<>();
    }
    public String getUsername() { return username; }
    public List<PortfolioItem> getPortfolio() { return portfolio; }
}