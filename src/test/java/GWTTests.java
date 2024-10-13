import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collection;

public class GWTTests {

    @TestFactory
    Collection<DynamicTest> testDynamicGWT() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        // 기존 성공하는 테스트
        dynamicTests.addAll(createGWTTestsForStockTrading());

        // 실패하는 테스트 추가
        dynamicTests.add(DynamicTest.dynamicTest("잔액 부족으로 buyStock이 실패해야 한다", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();
            stockTrading.buyStock("123456", "AAPL", 1000); // 잔액 소진

            // When
            boolean result = stockTrading.buyStock("123456", "GOOGL", 10); // 잔액 부족으로 구매 시도

            // Then
            Assertions.assertFalse(result, "주식을 사는 것이 실패해야 합니다."); // 실패해야 하므로 false가 반환되어야 함
        }));

        dynamicTests.add(DynamicTest.dynamicTest("보유 주식 부족으로 sellStock이 실패해야 한다", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();

            // When
            boolean result = stockTrading.sellStock("123456", "MSFT", 5); // 보유 주식이 없음

            // Then
            Assertions.assertFalse(result, "보유 주식이 부족하여 판매가 실패해야 합니다."); // 실패해야 하므로 false가 반환되어야 함
        }));

        return dynamicTests;
    }

    private Collection<DynamicTest> createGWTTestsForStockTrading() {
        Collection<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, buyStock이 호출되면, 성공해야 한다", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();
            double initialBalance = stockTrading.getAccountBalance("123456");

            // When
            boolean result = stockTrading.buyStock("123456", "AAPL", 10);

            // Then
            Assertions.assertTrue(result, "주식을 사는 것이 성공해야 합니다.");
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") < initialBalance, "주식을 구매한 후 잔액이 줄어야 합니다.");
        }));

        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서 주식을 보유한 상태에서, sellStock이 호출되면, 성공해야 한다", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();
            stockTrading.buyStock("123456", "GOOGL", 5); // 주식을 먼저 구매
            double initialBalance = stockTrading.getAccountBalance("123456");

            // When
            boolean result = stockTrading.sellStock("123456", "GOOGL", 2);

            // Then
            Assertions.assertTrue(result, "주식을 파는 것이 성공해야 합니다.");
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") > initialBalance, "주식을 판매한 후 잔액이 증가해야 합니다.");

            // 주식 수가 올바르게 감소했는지 확인
            int remainingStocks = stockTrading.getStockCount("123456", "GOOGL");
            Assertions.assertEquals(3, remainingStocks, "2주를 판매한 후 남은 주식 수는 3이어야 합니다.");
        }));

        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, getAccountBalance가 호출되면, 올바른 잔액을 반환해야 한다", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();

            // When
            double balance = stockTrading.getAccountBalance("123456");

            // Then
            Assertions.assertEquals(100000.0, balance, "초기 잔액은 100,000달러여야 합니다.");
        }));

        return tests;
    }
}