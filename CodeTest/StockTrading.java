import java.util.HashMap; // 해시맵을 사용하기 위한 임포트
import java.util.Map; // 맵 인터페이스를 사용하기 위한 임포트

public class StockTrading { // 주식 가격과 보유 주식 수량, 현금 잔액을 저장하는 필드
    private Map<String, Double> stockPrices; // 주식의 가격을 저장하는 해시맵
    private Map<String, Integer> portfolio; // 사용자가 보유한 주식 수량을 저장하는 해시맵
    private double cash; // 사용자의 현금 잔액

    // 생성자: 초기값 설정
    public StockTrading() {
        stockPrices = new HashMap<>(); // 주식 가격을 저장하기 위한 해시맵 초기화
        portfolio = new HashMap<>(); // 보유 주식을 저장하기 위한 해시맵 초기화
        cash = 100000.0; // 초기 현금 잔액을 100,000으로 설정

        // 샘플 주식 가격 설정
        stockPrices.put("삼성", 150.0); // 삼성 주식의 가격
        stockPrices.put("현대", 2500.0); // 현대 주식의 가격
        stockPrices.put("LG", 300.0); // LG 주식의 가격
    }

    // 주식 구매 메서드
    public boolean buyStock(String stockCode, int quantity) {
        if (!stockPrices.containsKey(stockCode)) {  // 주식 코드가 유효한지 확인
            System.out.println("존재하지 않는 주식 코드입니다.");   // 잘못된 주식 코드일 경우 메시지 출력
            return false; // 유효하지 않은 경우 false 반환
        }

        double totalCost = stockPrices.get(stockCode) * quantity; // 총 구매 비용 계산
        if (cash < totalCost) { // 잔액이 충분한지 확인
            System.out.println("잔액이 부족합니다.");   // 잔액이 부족할 경우 메시지 출력
            return false; // 잔액이 부족한 경우 false 반환
        }

        // 잔액에서 총 비용을 차감하고 포트폴리오에 주식 수량 추가
        cash -= totalCost;   // 현금 잔액에서 총 구매 비용 차감
        portfolio.put(stockCode, portfolio.getOrDefault(stockCode, 0) + quantity); // 보유 주식 수량 업데이트
        System.out.println(stockCode + " " + quantity + "주 매수 완료");    // 매수 완료 메시지 출력
        return true; // 성공적으로 매수한 경우 true 반환
    }

    // 주식 판매 메서드
    public boolean sellStock(String stockCode, int quantity) {
        if (!portfolio.containsKey(stockCode) || portfolio.get(stockCode) < quantity) { // 보유 주식 수량 확인
            System.out.println("보유 주식이 부족합니다.");  // 보유 주식이 부족할 경우 메시지 출력
            return false; // 보유 주식이 부족한 경우 false 반환
        }

        double totalEarning = stockPrices.get(stockCode) * quantity; // 총 판매 수익 계산
        cash += totalEarning; // 잔액에 판매 수익 추가
        portfolio.put(stockCode, portfolio.get(stockCode) - quantity); // 보유 주식 수량 업데이트
        if (portfolio.get(stockCode) == 0) {
            portfolio.remove(stockCode); // 남은 주식 수가 0일 경우 포트폴리오에서 삭제
        }
        System.out.println(stockCode + " " + quantity + "주 매도 완료");    // 매도 완료 메시지 출력
        return true; // 성공적으로 매도한 경우 true 반환
    }

    // 계좌 잔액 조회 메서드
    public double getAccountBalance() {
        return cash; // 현재 현금 잔액 반환
    }

    // 포트폴리오 상태 출력 메서드
    public void displayPortfolio() {
        System.out.println("현재 포트폴리오:"); // 포트폴리오 출력 시작 메시지
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "주"); // 각 주식의 보유 수량 출력
        }
        System.out.println("현금 잔액: ₩" + cash); // 현재 잔액 출력
    }

    // 주식 수량 조회 메서드 추가
    public int getStockCount(String stockCode) {
        return portfolio.getOrDefault(stockCode, 0); // 보유 주식 수량 반환, 없으면 0 반환
    }

    // 테스트를 위한 메서드들
    public boolean testBuyStock() {
        double initialBalance = cash; // 초기 잔액 저장
        boolean result = buyStock("삼성", 10); // 삼성 주식 10주 구매 시도
        return result && cash < initialBalance; // 매수 성공 및 잔액 변화 확인
    }

    public boolean testSellStock() {
        double initialBalance = cash; // 초기 잔액 저장
        buyStock("현대", 5); // 현대 주식 5주 구매
        boolean result = sellStock("현대", 2); // 현대 주식 2주 판매 시도
        return result && cash > initialBalance; // 매도 성공 및 잔액 변화 확인
    }

    public double testGetAccountBalance() {
        return getAccountBalace(); // 잔액 조회 테스트
    }
}