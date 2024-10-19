import java.io.File; // 파일 작업을 위해 필요한 클래스
import java.lang.reflect.Method; // 메소드 정보를 가져오기 위한 클래스
import java.util.ArrayList; // 동적 테스트를 저장할 리스트 클래스
import java.util.Collection; // 객체를 모아 저장하는 클래스

import org.junit.jupiter.api.Assertions; // 테스트 결과를 검증하기 위한 클래스
import org.junit.jupiter.api.DynamicTest; // 동적 테스트를 생성하는 클래스
import org.junit.jupiter.api.TestFactory; // 테스트를 동적으로 생성하는 클래스

public class GWTTests {

    @TestFactory    // 동적 테스트를 만드는 메소드
    Collection<DynamicTest> testDynamicGWT() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();   // 동적 테스트를 저장할 리스트 생성

        File workspace = new File(System.getProperty("user.dir"));  // 현재 작업 디렉토리를 가져온다
        File codeTestDirectory = new File(workspace, "CodeTest");   // CodeTest 폴더 경로 생성

        File[] javaFiles = codeTestDirectory.listFiles((dir, name) -> name.endsWith(".java")); // CodeTest 폴더 내의 .java 파일 찾기

        if (javaFiles != null) {    // 찾은 Java 파일에 대해 테스트 생성
            for (File file : javaFiles) {
                String className = file.getName().replace(".java", ""); // 파일 이름에서 .java를 제거하여 클래스 이름을 만든다
                dynamicTests.addAll(createTestsForClass(className));    // 해당 클래스에 대한 테스트를 추가
            }
        }

        dynamicTests.addAll(createGWTTestsForStockTrading());   // 주식 거래 관련 테스트 추가

        return dynamicTests;    // 생성된 동적 테스트를 반환
    }

    private Collection<DynamicTest> createTestsForClass(String className) { // 주어진 클래스에 대한 테스트를 생성하는 메소드
        Collection<DynamicTest> tests = new ArrayList<>();  // 테스트를 저장할 리스트 생성

        try {
            Class<?> testClass = Class.forName(className);  // 클래스 정보를 가져온다
            Object testInstance = testClass.getDeclaredConstructor().newInstance(); // 클래스의 인스턴스를 생성

            for (Method method : testClass.getDeclaredMethods()) {  // 클래스의 모든 메소드를 확인
                if (method.getName().startsWith("test")) {  // 메소드 이름이 "test"로 시작하면
                    tests.add(createDynamicTest(testInstance, method)); // 동적 테스트를 추가
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing class " + className + ": " + e.getMessage());  // 에러 메시지 출력
            tests.add(DynamicTest.dynamicTest("Error in " + className, 
                () -> Assertions.fail("Error: " + e.getMessage())));    // 에러 테스트 추가
        }

        return tests;   // 생성된 테스트를 반환
    }

    // 동적 테스트를 생성하는 메소드
    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) {
        return DynamicTest.dynamicTest("test " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName(), () -> {
            System.out.println("Given: Setting up for " + testMethod.getName());
            System.out.println("When: Executing " + testMethod.getName());

            Object result = testMethod.invoke(testInstance);
            
            System.out.println("Then: Verifying the result of " + testMethod.getName());
            
            if (result instanceof Boolean) {
                Assertions.assertTrue((Boolean) result, "Test " + testMethod.getName() + " failed");
            } else if (result instanceof Double) {
                Assertions.assertTrue((Double) result > 0, "Test " + testMethod.getName() + " failed");
            } else {
                Assertions.assertNotNull(result, "Test " + testMethod.getName() + " returned null");
            }
        });
    }

    // 주식 거래 시스템에 대한 GWT 테스트를 생성하는 메소드
    private Collection<DynamicTest> createGWTTestsForStockTrading() {
        Collection<DynamicTest> tests = new ArrayList<>();

        // 삼성 주식을 구매하는 테스트
        tests.add(DynamicTest.dynamicTest("삼성 주식을 10주 구매할 때 성공해야 한다", () -> {
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템의 새로운 인스턴스를 만듭니다.
            double initialBalance = stockTrading.getAccountBalance("123456"); // 초기 잔액을 가져옵니다.
            
            boolean result = stockTrading.buyStock("123456", "삼성", 10); // '123456' 계좌로 '삼성' 주식 10주를 구매합니다.

            Assertions.assertTrue(result, "주식 구매가 성공해야 합니다."); // 주식 구매가 성공했는지 확인합니다.
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") < initialBalance, "주식을 구매한 후 잔액이 줄어야 합니다."); // 주식을 구매한 후 잔액이 감소했는지 확인합니다.
        }));

        // 현대 주식을 판매하는 테스트
        tests.add(DynamicTest.dynamicTest("현대 주식을 2주 판매할 때 성공해야 한다", () -> {
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템의 새로운 인스턴스를 만듭니다.
            stockTrading.buyStock("123456", "현대", 5); // '현대' 주식 5주를 '123456' 계좌로 구매합니다.
            double initialBalance = stockTrading.getAccountBalance("123456"); // 초기 잔액을 가져옵니다.

            boolean result = stockTrading.sellStock("123456", "현대", 2); // '123456' 계좌로 '현대' 주식 2주를 판매합니다.

            Assertions.assertTrue(result, "주식 판매가 성공해야 합니다."); // 주식 판매가 성공했는지 확인합니다.
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") > initialBalance, "주식을 판매한 후 잔액이 증가해야 합니다."); // 주식을 판매한 후 잔액이 증가했는지 확인합니다.
            
            int remainingStocks = stockTrading.getStockCount("123456", "현대"); // '123456' 계좌에서 남은 '현대' 주식 수를 확인합니다.
            Assertions.assertEquals(3, remainingStocks, "2주를 판매한 후 남은 주식 수는 3이어야 합니다."); // 2주를 판매한 후 남은 주식 수가 3인지 확인합니다.
        }));

        // LG 주식의 초기 잔액을 확인하는 테스트
        tests.add(DynamicTest.dynamicTest("초기 잔액이 100,000달러여야 한다", () -> {
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템의 새로운 인스턴스를 만듭니다.
            double balance = stockTrading.getAccountBalance("123456"); // '123456' 계좌의 잔액을 조회합니다.

            Assertions.assertEquals(100000.0, balance, "초기 잔액은 100,000달러여야 합니다."); // 초기 잔액이 100,000달러인지 확인합니다.
        }));

        return tests; // 모든 테스트 목록을 반환합니다.
    }
}