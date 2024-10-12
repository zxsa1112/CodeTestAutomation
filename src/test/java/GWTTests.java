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

    private Collection<DynamicTest> createGWTTestsForStockTrading() { // 메소드 위치 수정
        Collection<DynamicTest> tests = new ArrayList<>();
        
        tests.add(DynamicTest.dynamicTest("Given a StockTrading instance, When buyStock is called, Then it should succeed", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();

            // When
            boolean result = stockTrading.buyStock("123456", "AAPL", 10);

            // Then
            Assertions.assertTrue(result, "Buy stock should succeed");
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") < 10000.0, "Cash should be reduced after buying");
        }));

        tests.add(DynamicTest.dynamicTest("Given a StockTrading instance with stocks, When sellStock is called, Then it should succeed", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();
            stockTrading.buyStock("123456", "GOOGL", 5); // Buy stock first

            // When
            boolean result = stockTrading.sellStock("123456", "GOOGL", 2);

            // Then
            Assertions.assertTrue(result, "Sell stock should succeed");
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") > 10000.0, "Cash should be increased after selling");
        }));

        tests.add(DynamicTest.dynamicTest("Given a StockTrading instance, When getAccountBalance is called, Then it should return the correct balance", () -> {
            // Given
            StockTrading stockTrading = new StockTrading();

            // When
            double balance = stockTrading.getAccountBalance("123456");

            // Then
            Assertions.assertEquals(10000.0, balance, "Initial balance should be $10,000");
        }));

        return tests;
    }
}