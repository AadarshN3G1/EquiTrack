public class PortfolioItem {
    private String symbol;
    private int shares;
    private double buyPrice;
    public PortfolioItem(String symbol, int shares, double buyPrice) {
        this.symbol = symbol;
        this.shares = shares;
        this.buyPrice = buyPrice;
    }
    public String getSymbol() { return symbol; }
    public int getShares() { return shares; }
    public double getBuyPrice() { return buyPrice; }
}