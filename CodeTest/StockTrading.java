import java.util.HashMap;
import java.util.Map;

public class StockTrading {
    private Map<String, Double> stockPrices; // 주식 가격 저장
    private Map<String, Integer> portfolio; // 보유 주식 수량 저장
    private double cash; // 현금 잔액

    public StockTrading() {
        stockPrices = new HashMap<>();
        portfolio = new HashMap<>();
        cash = 100000.0; // 초기 현금 설정

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
        if (portfolio.get(stockCode) == 0) {
            portfolio.remove(stockCode); // 잔여 수량이 0일 경우 삭제
        }
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

    // 주식 수량 조회 메서드 추가
    public int getStockCount(String accountNumber, String stockCode) {
        return portfolio.getOrDefault(stockCode, 0);
    }

    // 테스트를 위한 메서드들
    public boolean testBuyStock() {
        double initialBalance = cash;
        boolean result = buyStock("123456", "AAPL", 10);
        return result && cash < initialBalance;
    }

    public boolean testSellStock() {
        double initialBalance = cash;
        buyStock("123456", "GOOGL", 5);
        boolean result = sellStock("123456", "GOOGL", 2);
        return result && cash > initialBalance;
    }

    public double testGetAccountBalance() {
        return getAccountBalance("123456");
    }
}