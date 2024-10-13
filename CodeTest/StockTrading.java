import java.util.HashMap; // 해시맵을 사용하기 위한 임포트
import java.util.Map; // 맵 인터페이스를 사용하기 위한 임포트

public class StockTrading {
    private Map<String, Account> accounts; // 계좌 정보를 저장하는 맵
    private Map<String, Double> stockPrices; // 주식 가격 저장
    private double cash; // 초기 잔액

    // 생성자: 초기값 설정
    public StockTrading() {
        accounts = new HashMap<>(); // 계좌 정보를 저장하는 해시맵 초기화
        stockPrices = new HashMap<>(); // 주식 가격 저장을 위한 해시맵 초기화

        // 샘플 주식 가격 설정
        stockPrices.put("삼성", 150.0); // 삼성 주식 가격
        stockPrices.put("현대", 2500.0); // 현대 주식 가격
        stockPrices.put("LG", 300.0); // LG 주식 가격

        // 초기 계좌 생성
        cash = 100000.0; // 초기 잔액 설정
    }

    // 주식 구매 메서드
    public boolean buyStock(String accountNumber, String stockCode, int quantity) {
        Account account = accounts.get(accountNumber); // 계좌 정보 가져오기
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return false; // 유효하지 않은 경우 false 반환
        }

        // 유효한 주식 코드인지 확인
        if (!stockPrices.containsKey(stockCode)) {
            System.out.println("존재하지 않는 주식 코드입니다.");
            return false; // 유효하지 않은 경우 false 반환
        }

        double totalCost = stockPrices.get(stockCode) * quantity; // 총 비용 계산
        // 잔액이 충분한지 확인
        if (account.cash < totalCost) {
            System.out.println("잔액이 부족합니다.");
            return false; // 잔액이 부족한 경우 false 반환
        }

        // 잔액에서 총 비용을 차감하고 포트폴리오에 주식 수량을 추가
        account.cash -= totalCost; 
        account.portfolio.put(stockCode, account.portfolio.getOrDefault(stockCode, 0) + quantity); // 주식 수량 업데이트
        System.out.println(stockCode + " " + quantity + "주 매수 완료");
        return true; // 성공적으로 매수한 경우 true 반환
    }

    // 주식 판매 메서드
    public boolean sellStock(String accountNumber, String stockCode, int quantity) {
        Account account = accounts.get(accountNumber); // 계좌 정보 가져오기
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return false; // 유효하지 않은 경우 false 반환
        }

        // 보유 주식 수량 확인
        if (!account.portfolio.containsKey(stockCode) || account.portfolio.get(stockCode) < quantity) {
            System.out.println("보유 주식이 부족합니다.");
            return false; // 보유 주식이 부족한 경우 false 반환
        }

        double totalEarning = stockPrices.get(stockCode) * quantity; // 총 수익 계산
        account.cash += totalEarning; // 잔액에 수익 추가
        account.portfolio.put(stockCode, account.portfolio.get(stockCode) - quantity); // 보유 주식 수량 업데이트
        if (account.portfolio.get(stockCode) == 0) {
            account.portfolio.remove(stockCode); // 잔여 수량이 0일 경우 삭제
        }
        System.out.println(stockCode + " " + quantity + "주 매도 완료");
        return true; // 성공적으로 매도한 경우 true 반환
    }

    // 계좌 잔액 조회 메서드
    public double getAccountBalance(String accountNumber) {
        Account account = accounts.get(accountNumber); // 계좌 정보 가져오기
        return account != null ? account.cash : 0.0; // 현재 잔액 반환
    }

    // 포트폴리오 상태 출력 메서드
    public void displayPortfolio(String accountNumber) {
        Account account = accounts.get(accountNumber); // 계좌 정보 가져오기
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return; // 유효하지 않은 경우 종료
        }

        System.out.println("현재 포트폴리오:");
        for (Map.Entry<String, Integer> entry : account.portfolio.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "주"); // 보유 주식 출력
        }
        System.out.println("현금 잔액: $" + account.cash); // 잔액 출력
    }

    // 주식 수량 조회 메서드 추가
    public int getStockCount(String accountNumber, String stockCode) {
        Account account = accounts.get(accountNumber); // 계좌 정보 가져오기
        return account != null ? account.portfolio.getOrDefault(stockCode, 0) : 0; // 보유 주식 수량 반환, 없으면 0 반환
    }

    // 내부 클래스: 계좌 정보 저장
    private static class Account {
        private Map<String, Integer> portfolio; // 보유 주식 수량 저장
        private double cash; // 현금 잔액

        public Account(double initialCash) {
            this.cash = initialCash; // 초기 현금 잔액 설정
            this.portfolio = new HashMap<>(); // 포트폴리오 초기화
        }
    }
}