import java.io.File; // 파일 작업을 위한 클래스
import java.lang.reflect.Method; // 리플렉션을 위한 클래스
import java.util.ArrayList; // 동적 테스트 결과를 저장할 리스트
import java.util.Collection; // 동적 테스트를 저장할 컬렉션

import org.junit.jupiter.api.Assertions; // 단언을 위한 클래스
import org.junit.jupiter.api.DynamicTest; // 동적 테스트 생성을 위한 클래스
import org.junit.jupiter.api.TestFactory; // JUnit5 테스트 팩토리 클래스

public class GWTTests {

    @TestFactory    // 동적 테스트를 생성하는 메소드
    Collection<DynamicTest> testDynamicGWT() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();   // 동적 테스트를 저장할 리스트 초기화

        File workspace = new File(System.getProperty("user.dir"));  // 현재 디렉토리에서 작업 디렉토리 설정

        File[] javaFiles = workspace.listFiles((dir, name) -> name.endsWith(".java") && !name.equals("GWTTests.java")); // .java 파일을 검색하되 GWTTests.java는 제외

        if (javaFiles != null) {    // 검색된 Java 파일에 대해 테스트 생성
            for (File file : javaFiles) {
                String className = file.getName().replace(".java", ""); // 파일 이름에서 .java 제거
                dynamicTests.addAll(createTestsForClass(className));    // 각 클래스에 대한 테스트 추가
            }
        }

        dynamicTests.addAll(createGWTTestsForStockTrading());   // 주식 거래 테스트 추가

        return dynamicTests;    // 생성된 테스트 목록 반환
    }

    
    private Collection<DynamicTest> createTestsForClass(String className) { // 주어진 클래스에 대한 테스트를 생성하는 메소드
        Collection<DynamicTest> tests = new ArrayList<>();

        try {
            Class<?> testClass = Class.forName(className);  // 클래스를 로드하고 인스턴스 생성
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {  // 클래스 내 모든 메소드 탐색
                
                if (method.getName().startsWith("test")) {  // 메소드 이름이 "test"로 시작하는 경우
                    tests.add(createDynamicTest(testInstance, method)); // 동적 테스트 생성 및 추가
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing class " + className + ": " + e.getMessage());  // 에러 발생 시 오류 메시지를 출력하고 실패한 테스트 추가
            tests.add(DynamicTest.dynamicTest("Error in " + className, 
                () -> Assertions.fail("Error: " + e.getMessage())));
        }

        return tests;   // 생성된 테스트 컬렉션 반환
    }

    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) { // 동적 테스트를 생성하는 메소드
        return DynamicTest.dynamicTest("test " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName(), () -> {
            System.out.println("Given: Setting up for " + testMethod.getName());
            System.out.println("When: Executing " + testMethod.getName());

            Object result = testMethod.invoke(testInstance);    // 테스트 메소드 실행
            
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

    private Collection<DynamicTest> createGWTTestsForStockTrading() {   // 주식 거래 시스템에 대한 GWT 테스트를 생성하는 메소드
        Collection<DynamicTest> tests = new ArrayList<>();

        // 삼성 주식 구매 테스트
        tests.add(DynamicTest.dynamicTest("삼성 주식을 10주 구매할 때 성공해야 한다", () -> {
            StockTrading stockTrading = new StockTrading();     // 주식 거래 시스템 인스턴스 생성
            double initialBalance = stockTrading.getAccountBalance("123456"); // 초기 잔액 조회
    
            // 삼성 주식 10주 구매 시도
            boolean result = stockTrading.buyStock("123456", "삼성", 10);

            // 주식 구매 성공 여부 및 잔액 검증
            Assertions.assertTrue(result, "주식 구매가 성공해야 합니다.");
            double currentBalance = stockTrading.getAccountBalance("123456"); // 현재 잔액 조회
            System.out.println("매수 후 현재 잔액: ₩" + currentBalance);
            Assertions.assertTrue(currentBalance < initialBalance, "주식을 구매한 후 잔액이 줄어야 합니다.");
        }));

        // 현대 주식 판매 테스트
        tests.add(DynamicTest.dynamicTest("현대 주식을 2주 판매할 때 성공해야 한다", () -> {
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템 인스턴스 생성 및 현대 주식 5주 구매 
            stockTrading.buyStock("123456", "현대", 5); 
            double initialBalance = stockTrading.getAccountBalance("123456"); // 초기 잔액 조회

            // 현대 주식 2주 판매 시도
            boolean result = stockTrading.sellStock("123456", "현대", 2);

            // 주식 판매 성공 여부 및 잔액 검증
            Assertions.assertTrue(result, "주식 판매가 성공해야 합니다.");
            double currentBalance = stockTrading.getAccountBalance("123456"); // 현재 잔액 조회
            System.out.println("매도 후 현재 잔액: ₩" + currentBalance);
            Assertions.assertTrue(currentBalance > initialBalance, "주식을 판매한 후 잔액이 증가해야 합니다.");
    
            // 주식 수 검증
            int remainingStocks = stockTrading.getStockCount("123456", "현대");
            Assertions.assertEquals(3, remainingStocks, "2주를 판매한 후 남은 주식 수는 3이어야 합니다.");
        }));

        // 초기 잔액 확인 테스트
        tests.add(DynamicTest.dynamicTest("초기 잔액이 100,000달러여야 한다", () -> {
            
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템 인스턴스 생성

            // 잔액 조회
            double balance = stockTrading.getAccountBalance("123456");

            // 초기 잔액 검증
            Assertions.assertEquals(100000.0, balance, "초기 잔액은 100,000달러여야 합니다.");
            System.out.println("초기 잔액: ₩" + balance);
        }));

        return tests; // 모든 테스트 목록 반환
    }
}