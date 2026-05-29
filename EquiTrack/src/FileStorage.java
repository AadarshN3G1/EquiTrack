import java.io.*;
import java.util.*;

public class FileStorage {
    private final String FILE = "data.txt";
    public void save(User user) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (PortfolioItem item : user.getPortfolio()) {
                pw.println(
                        user.getUsername() + "," + item.getSymbol() + "," + item.getShares() + "," +
                                item.getBuyPrice()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}