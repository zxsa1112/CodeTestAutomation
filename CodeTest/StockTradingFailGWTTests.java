import java.util.HashMap;
import java.util.Map;

public class StockTradingFailGWTTests {
    private Map<String, Double> stockPrices; // 주식 가격 저장
    private Map<String, Integer> portfolio; // 보유 주식 수량 저장
    private double cash; // 현금 잔액

    public StockTradingFailGWTTests() {
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

    // 실패하는 테스트 메서드들
    public boolean testFailBuyStock() {
        // 잔액이 부족하여 주식 구매 실패
        cash = 0; // 잔액을 0으로 설정하여 구매 실패 유도
        boolean result = buyStock("123456", "AAPL", 10);
        return !result; // 구매가 실패해야 하므로 false가 반환되어야 함
    }

    public boolean testFailSellStock() {
        // 보유 주식이 부족하여 판매 실패
        boolean result = sellStock("123456", "MSFT", 5); // 보유 주식이 없음
        return !result; // 판매가 실패해야 하므로 false가 반환되어야 함
    }

    public boolean testFailGetAccountBalance() {
        double balance = getAccountBalance("123456");
        return balance != 100000.0; // 잔액이 100000.0이 아니어야 함 (실패 조건)
    }
}
