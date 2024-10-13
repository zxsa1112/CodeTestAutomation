import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Method;
import java.io.File;

public class GWTTests {

    @TestFactory
    Collection<DynamicTest> testDynamicGWT() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        // GitHub 워크스페이스 경로 가져오기 (로컬 테스트시 현재 디렉토리 사용)
        String workspacePath = System.getenv("GITHUB_WORKSPACE");
        if (workspacePath == null) {
            workspacePath = System.getProperty("user.dir");
        }

        File workspace = new File(workspacePath);
        File[] javaFiles = workspace.listFiles((dir, name) -> name.endsWith(".java") && !name.equals("GWTTests.java"));

        if (javaFiles != null) {
            for (File file : javaFiles) {
                String className = file.getName().replace(".java", "");
                dynamicTests.addAll(createTestsForClass(className));
            }
        }

        dynamicTests.addAll(createGWTTestsForStockTrading()); // GWT 테스트 추가
        dynamicTests.addAll(createFailingTests()); // 실패하는 테스트 추가

        return dynamicTests;
    }

    private Collection<DynamicTest> createTestsForClass(String className) {
        Collection<DynamicTest> tests = new ArrayList<>();

        try {
            Class<?> testClass = Class.forName(className);
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.getName().startsWith("test")) {
                    tests.add(createDynamicTest(testInstance, method));
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing class " + className + ": " + e.getMessage());
            tests.add(DynamicTest.dynamicTest("Error in " + className, () -> Assertions.fail("Error: " + e.getMessage())));
        }

        return tests;
    }

    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) {
        return DynamicTest.dynamicTest("test " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName(), () -> {
            System.out.println("Given: Setting up for " + testMethod.getName());
            System.out.println("When: Executing " + testMethod.getName());
            
            Object result = testMethod.invoke(testInstance);
            
            System.out.println("Then: Verifying the result of " + testMethod.getName());
            if (result instanceof Boolean) {
                Assertions.assertTrue((Boolean) result, "Test " + testMethod.getName() + " failed");
                System.out.println("Result: " + result + " (Expected: true)");
            } else if (result instanceof Double) {
                Assertions.assertTrue((Double) result > 0, "Test " + testMethod.getName() + " failed");
                System.out.println("Result: " + result + " (Expected: > 0)");
            } else {
                System.out.println("Result: " + result + " (No assertion performed)");
                Assertions.assertNotNull(result, "Test " + testMethod.getName() + " returned null");
            }
        });
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

    // 실패하는 테스트를 추가하는 메서드
    private Collection<DynamicTest> createFailingTests() {
        Collection<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, 잔액이 부족하여 buyStock이 실패해야 한다", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();
            stockTrading.buyStock("123456", "AAPL", 1000); // 잔액을 소진하도록 큰 수량 구매
        
            // When
            boolean result = stockTrading.buyStock("123456", "AAPL", 10); // 잔액 부족 상태에서 구매 시도
        
            // Then
            Assertions.assertFalse(result, "주식을 사는 것이 실패해야 합니다."); // 실패해야 하므로 false가 반환되어야 함
        }));        

        return tests;
    }
}