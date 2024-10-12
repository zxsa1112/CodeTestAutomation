import java.util.HashMap;
import java.util.Map;

public class StockTrading {
    private Map<String, Double> stockPrices;
    private Map<String, Integer> portfolio;
    private double cash;

    public StockTrading() {
        stockPrices = new HashMap<>();
        portfolio = new HashMap<>();
        cash = 10000.0; // 초기 현금 설정
        
        // 샘플 주식 가격 설정
        stockPrices.put("AAPL", 150.0);
        stockPrices.put("GOOGL", 2500.0);
        stockPrices.put("MSFT", 300.0);
    }

    public boolean buyStock(String accountNumber, String stockCode, int quantity) {
        if (!stockPrices.containsKey(stockCode)) {
            System.out.println("존재하지 않는 주식 코드입니다.");
            return false;
        }

        double totalCost = stockPrices.get(stockCode) * quantity;
        if (cash < totalCost) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }

        cash -= totalCost;
        portfolio.put(stockCode, portfolio.getOrDefault(stockCode, 0) + quantity);
        System.out.println(stockCode + " " + quantity + "주 매수 완료");
        return true;
    }

    public boolean sellStock(String accountNumber, String stockCode, int quantity) {
        if (!portfolio.containsKey(stockCode) || portfolio.get(stockCode) < quantity) {
            System.out.println("보유 주식이 부족합니다.");
            return false;
        }

        double totalEarning = stockPrices.get(stockCode) * quantity;
        cash += totalEarning;
        portfolio.put(stockCode, portfolio.get(stockCode) - quantity);
        System.out.println(stockCode + " " + quantity + "주 매도 완료");
        return true;
    }

    public double getAccountBalance(String accountNumber) {
        return cash;
    }

    public void displayPortfolio() {
        System.out.println("현재 포트폴리오:");
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "주");
        }
        System.out.println("현금 잔액: $" + cash);
    }

    // 테스트를 위한 메서드들
    public boolean testBuyStock() {
        double initialBalance = cash;
        boolean result = buyStock("123456", "AAPL", 10);
        return result && cash < initialBalance;
    }

    public boolean testSellStock() {
        double initialBalance = cash; // 매도 전에 잔고 저장
        buyStock("123456", "GOOGL", 5); // GOOGL 주식 5주 매수
        boolean result = sellStock("123456", "GOOGL", 2); // GOOGL 주식 2주 매도
        double expectedBalance = initialBalance - (stockPrices.get("GOOGL") * 5) + (stockPrices.get("GOOGL") * 2);
        return result && cash == expectedBalance; // 잔고가 예상 잔고와 같은지 확인
    }    

    public double testGetAccountBalance() {
        return getAccountBalance("123456");
    }
}