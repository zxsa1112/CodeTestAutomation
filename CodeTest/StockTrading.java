import java.util.HashMap;
import java.util.Map;

public class StockTrading {
    private Map<String, Account> accounts; // 계좌 정보를 저장하는 맵
    private Map<String, Double> stockPrices; // 주식 가격 저장

    public StockTrading() {
        accounts = new HashMap<>();
        stockPrices = new HashMap<>();
        stockPrices.put("삼성", 150.0);
        stockPrices.put("현대", 2500.0);
        stockPrices.put("LG", 300.0);
        
        // 초기 계좌 생성
        accounts.put("123456", new Account(100000.0)); // 계좌 123456 생성
    }

    public boolean buyStock(String accountNumber, String stockCode, int quantity) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return false;
        }

        if (!stockPrices.containsKey(stockCode)) {
            System.out.println("존재하지 않는 주식 코드입니다.");
            return false;
        }

        double totalCost = stockPrices.get(stockCode) * quantity;
        if (account.cash < totalCost) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }

        account.cash -= totalCost;
        account.portfolio.put(stockCode, account.portfolio.getOrDefault(stockCode, 0) + quantity);
        System.out.println(stockCode + " " + quantity + "주 매수 완료");
        return true;
    }

    public boolean sellStock(String accountNumber, String stockCode, int quantity) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return false;
        }

        if (!account.portfolio.containsKey(stockCode) || account.portfolio.get(stockCode) < quantity) {
            System.out.println("보유 주식이 부족합니다.");
            return false;
        }

        double totalEarning = stockPrices.get(stockCode) * quantity;
        account.cash += totalEarning;
        account.portfolio.put(stockCode, account.portfolio.get(stockCode) - quantity);
        if (account.portfolio.get(stockCode) == 0) {
            account.portfolio.remove(stockCode); // 잔여 수량이 0일 경우 삭제
        }
        System.out.println(stockCode + " " + quantity + "주 매도 완료");
        return true;
    }

    public double getAccountBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.cash : 0.0;
    }

    public void displayPortfolio(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return;
        }

        System.out.println("현재 포트폴리오:");
        for (Map.Entry<String, Integer> entry : account.portfolio.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "주");
        }
        System.out.println("현금 잔액: $" + account.cash);
    }

    public int getStockCount(String accountNumber, String stockCode) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.portfolio.getOrDefault(stockCode, 0) : 0;
    }

    private static class Account {
        private Map<String, Integer> portfolio; // 보유 주식 수량 저장
        private double cash; // 현금 잔액

        public Account(double initialCash) {
            this.cash = initialCash;
            this.portfolio = new HashMap<>();
        }
    }
}