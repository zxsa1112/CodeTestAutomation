import org.junit.jupiter.api.DynamicTest; // 동적 테스트를 생성하기 위한 클래스
import org.junit.jupiter.api.TestFactory; // 테스트 팩토리를 만들기 위한 클래스
import org.junit.jupiter.api.Assertions; // 테스트 결과를 확인하는 데 필요한 클래스
import java.util.ArrayList; // 동적 테스트를 저장할 리스트를 위한 클래스
import java.util.Collection; // 여러 객체를 모아서 저장할 수 있는 컬렉션 클래스
import java.lang.reflect.Method; // 리플렉션을 사용하여 메소드 정보를 가져오기 위한 클래스
import java.io.File; // 파일 작업을 위한 클래스

public class GWTTests {
    private StockTrading stockTrading; // StockTrading 인스턴스를 클래스 필드로 선언

    // 생성자에서 StockTrading 인스턴스 초기화
    public GWTTests() {
        stockTrading = new StockTrading();
    }

    // 동적 테스트를 만드는 메소드
    @TestFactory
    Collection<DynamicTest> testDynamicGWT() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        // 주식 거래 테스트 추가
        dynamicTests.addAll(createGWTTestsForStockTrading());

        return dynamicTests; // 생성된 동적 테스트 리스트 반환
    }

    private Collection<DynamicTest> createGWTTestsForStockTrading() {
        Collection<DynamicTest> tests = new ArrayList<>();

        // 삼성 주식을 구매하는 테스트
        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, buyStock이 호출되면, 성공해야 한다", () -> {
            double initialBalance = stockTrading.getAccountBalance("123456");
            boolean result = stockTrading.buyStock("123456", "삼성", 10);
            Assertions.assertTrue(result, "주식을 사는 것이 성공해야 합니다.");
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") < initialBalance, "주식을 구매한 후 잔액이 줄어야 합니다.");
        }));

        // 현대 주식을 판매하는 테스트
        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서 주식을 보유한 상태에서, sellStock이 호출되면, 성공해야 한다", () -> {
            stockTrading.buyStock("123456", "현대", 5);
            double initialBalance = stockTrading.getAccountBalance("123456");
            boolean result = stockTrading.sellStock("123456", "현대", 2);
            Assertions.assertTrue(result, "주식을 파는 것이 성공해야 합니다.");
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") > initialBalance, "주식을 판매한 후 잔액이 증가해야 합니다.");
            int remainingStocks = stockTrading.getStockCount("123456", "현대");
            Assertions.assertEquals(3, remainingStocks, "2주를 판매한 후 남은 주식 수는 3이어야 합니다.");
        }));

        // LG 주식의 초기 잔액을 확인하는 테스트
tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, getAccountBalance가 호출되면, 올바른 잔액을 반환해야 한다", () -> {
    // 주식을 구매하기 전에 잔액을 조회
    double balance = stockTrading.getAccountBalance("123456");
    Assertions.assertEquals(100000.0, balance, "초기 잔액은 100,000달러여야 합니다.");

    // 주식을 사서 잔액 확인
    stockTrading.buyStock("123456", "삼성", 10); // 삼성 주식 구매
    double newBalance = stockTrading.getAccountBalance("123456");
    double expectedBalanceAfterPurchase = 100000.0 - (150.0 * 10); // 150.0은 삼성 주식 가격
    Assertions.assertEquals(expectedBalanceAfterPurchase, newBalance, "주식을 구매한 후 잔액이 줄어야 합니다.");
    
    System.out.println("LG 주식 초기 잔액: " + balance);
}));


        return tests; // 모든 테스트 목록을 반환합니다.
    }
}