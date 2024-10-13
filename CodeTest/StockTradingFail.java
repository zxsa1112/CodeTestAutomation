import java.util.HashMap;
import java.util.Map;

public class StockTradingFail {
    private Map<String, Double> stockPrices; // 주식 가격 저장
    private Map<String, Integer> portfolio; // 보유 주식 수량 저장
    private double cash; // 현금 잔액

    public StockTradingFail() {
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

    // 실패하는 테스트 메서드들
    public boolean testFailBuyStock() {
        // 잔액이 부족하여 주식 구매 실패 유도
        cash = 0; // 잔액을 0으로 설정
        boolean result = buyStock("123456", "AAPL", 10); // 잔액 부족으로 실패
        return !result; // 실패해야 하므로 false가 반환되어야 함
    }

    public boolean testFailSellStock() {
        // 보유 주식이 부족하여 판매 실패 유도
        boolean result = sellStock("123456", "AAPL", 5); // 보유 주식 없음
        return !result; // 실패해야 하므로 false가 반환되어야 함
    }

    public boolean testFailGetAccountBalance() {
        // 잔액이 100000.0이 아니어야 함 (실패 조건)
        double initialBalance = cash;
        cash = 99999.0; // 고의로 잔액 변경
        boolean result = getAccountBalance("123456") != initialBalance; 
        return result; // 잔액이 다르면 true 반환
    }
}