import java.net.*;
import java.io.*;

public class StockApiClient {
    private static final String API_KEY = "YOUR_API_KEY";
    public double getPrice(String symbol) {
        try {
            String urlStr = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + API_KEY;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            String json = sb.toString();
            String key = "\"c\":";
            int idx = json.indexOf(key);
            int start = idx + key.length();
            int end = json.indexOf(",", start);
            return Double.parseDouble(json.substring(start, end));

        } catch (Exception e) {
            return 0.0;
        }
    }
}