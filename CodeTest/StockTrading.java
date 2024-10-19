import java.util.HashMap; // 해시맵을 사용하기 위한 임포트
import java.util.Map; // 맵 인터페이스를 사용하기 위한 임포트

public class StockTrading {
    private static final double INITIAL_CASH = 100000.0; // 초기 현금 잔액
    private Map<String, Double> stockPrices; // 주식의 가격을 저장하는 해시맵
    private Map<String, Integer> portfolio; // 사용자가 보유한 주식 수량을 저장하는 해시맵
    private double cash; // 사용자의 현금 잔액

    // 생성자: 초기값 설정
    public StockTrading() {
        stockPrices = new HashMap<>(); // 주식 가격을 저장하기 위한 해시맵 초기화
        portfolio = new HashMap<>(); // 보유 주식을 저장하기 위한 해시맵 초기화
        cash = INITIAL_CASH; // 초기 현금 잔액 설정

        // 샘플 주식 가격 설정
        initializeStockPrices();
    }

    private void initializeStockPrices() {
        stockPrices.put("삼성", 150.0); // 삼성 주식의 가격
        stockPrices.put("현대", 2500.0); // 현대 주식의 가격
        stockPrices.put("LG", 300.0); // LG 주식의 가격
    }

    // 주식 구매 메서드
    public boolean buyStock(String stockCode, int quantity) {
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

    // 주식 판매 메서드
    public boolean sellStock(String stockCode, int quantity) {
        if (!portfolio.containsKey(stockCode) || portfolio.get(stockCode) < quantity) {
            System.out.println("보유 주식이 부족합니다.");
            return false; 
        }

        double totalEarning = stockPrices.get(stockCode) * quantity; 
        cash += totalEarning; 
        portfolio.put(stockCode, portfolio.get(stockCode) - quantity); 
        if (portfolio.get(stockCode) == 0) {
            portfolio.remove(stockCode); 
        }
        System.out.println(stockCode + " " + quantity + "주 매도 완료"); 
        return true; 
    }

    // 계좌 잔액 조회 메서드
    public double getAccountBalance() {
        return cash; 
    }

    // 포트폴리오 상태 출력 메서드
    public void displayPortfolio() {
        System.out.println("현재 포트폴리오:");
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "주"); 
        }
        System.out.println("현금 잔액: ₩" + cash); 
    }

    // 주식 수량 조회 메서드
    public int getStockCount(String stockCode) {
        return portfolio.getOrDefault(stockCode, 0); 
    }

    // 테스트를 위한 메서드들
    public boolean testBuyStock() {
        double initialBalance = cash; 
        boolean result = buyStock("삼성", 10); 
        return result && cash < initialBalance; 
    }

    public boolean testSellStock() {
        double initialBalance = cash; 
        buyStock("현대", 5); 
        boolean result = sellStock("현대", 2); 
        return result && cash > initialBalance; 
    }

    public double testGetAccountBalance() {
        return getAccountBalance(); 
    }
}