import java.util.HashMap; // 해시맵을 사용하기 위한 임포트
import java.util.Map; // 맵 인터페이스를 사용하기 위한 임포트

public class StockTrading { // 주식 가격과 보유 주식 수량, 현금 잔액을 저장하는 필드
    private Map<String, Double> stockPrices; // 주식 가격 저장
    private Map<String, Integer> portfolio; // 보유 주식 수량 저장
    private double cash; // 현금 잔액
    private String accountNumber; // 계좌 번호 추가

    // 생성자: 초기값 설정
    public StockTrading(String accountNumber) { // 계좌 번호 매개변수 추가
        this.accountNumber = accountNumber; // 계좌 번호 설정
        stockPrices = new HashMap<>(); // 주식 가격 저장을 위한 해시맵 초기화
        portfolio = new HashMap<>(); // 포트폴리오(보유 주식) 저장을 위한 해시맵 초기화
        cash = 100000.0; // 초기 현금 잔액 설정

        // 샘플 주식 가격 설정
        stockPrices.put("삼성", 150.0); // 삼성 주식 가격
        stockPrices.put("현대", 2500.0); // 현대 주식 가격
        stockPrices.put("LG", 300.0); // LG 주식 가격
    }

    // 주식 구매 메서드
    public boolean buyStock(String stockCode, int quantity) {
        // 유효한 주식 코드인지 확인
        if (!stockPrices.containsKey(stockCode)) {
            System.out.println("존재하지 않는 주식 코드입니다.");
            return false; // 유효하지 않은 경우 false 반환
        }

        double totalCost = stockPrices.get(stockCode) * quantity; // 총 비용 계산
        // 잔액이 충분한지 확인
        if (cash < totalCost) {
            System.out.println("잔액이 부족합니다.");
            return false; // 잔액이 부족한 경우 false 반환
        }

        // 잔액에서 총 비용을 차감하고 포트폴리오에 주식 수량을 추가
        cash -= totalCost; 
        portfolio.put(stockCode, portfolio.getOrDefault(stockCode, 0) + quantity); // 주식 수량 업데이트
        System.out.println(stockCode + " " + quantity + "주 매수 완료");
        return true; // 성공적으로 매수한 경우 true 반환
    }

    // 주식 판매 메서드
    public boolean sellStock(String stockCode, int quantity) {
        if (!portfolio.containsKey(stockCode) || portfolio.get(stockCode) < quantity) { 
            System.out.println("보유 주식이 부족합니다.");
            return false; // 보유 주식이 부족한 경우 false 반환
        }

        double totalEarning = stockPrices.get(stockCode) * quantity; // 총 수익 계산
        cash += totalEarning; // 잔액에 수익 추가
        portfolio.put(stockCode, portfolio.get(stockCode) - quantity); // 보유 주식 수량 업데이트
        if (portfolio.get(stockCode) == 0) {
            portfolio.remove(stockCode); // 잔여 수량이 0일 경우 삭제
        }
        System.out.println(stockCode + " " + quantity + "주 매도 완료");
        return true; // 성공적으로 매도한 경우 true 반환
    }

    // 계좌 잔액 조회 메서드
    public double getAccountBalance() {
        return cash; // 현재 잔액 반환
    }

    // 주식 수량 조회 메서드 추가
    public int getStockCount(String stockCode) {
        return portfolio.getOrDefault(stockCode, 0); // 보유 주식 수량 반환, 없으면 0 반환
    }

    // 테스트를 위한 메서드들
    public boolean testBuyStock() {
        double initialBalance = cash; // 초기 잔액 저장
        boolean result = buyStock("삼성", 10); // 삼성 주식으로 테스트
        return result && cash < initialBalance; // 매수 성공 및 잔액 변화 확인
    }

    public boolean testSellStock() {
        double initialBalance = cash; // 초기 잔액 저장
        buyStock("현대", 5); // 현대 주식 5주 구매
        boolean result = sellStock("현대", 2); // 현대 주식 2주 판매
        return result && cash > initialBalance; // 매도 성공 및 잔액 변화 확인
    }
}