import java.io.File;    // 동적 테스트를 생성하기 위한 클래스
import java.lang.reflect.Method; // 파일 작업을 위해 필요한 클래스
import java.util.ArrayList; // 메소드 정보를 가져오기 위한 클래스
import java.util.Collection; // 동적 테스트를 저장할 리스트 클래스

import org.junit.jupiter.api.Assertions; // 객체를 모아 저장하는 클래스
import org.junit.jupiter.api.DynamicTest; // 테스트 결과를 검증하기 위한 클래스
import org.junit.jupiter.api.TestFactory; // 동적 테스트를 생성하는 클래스

public class GWTTests {

    @TestFactory // 동적 테스트를 만드는 메소드
    Collection<DynamicTest> testDynamicGWT() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>(); // 동적 테스트를 저장할 리스트 생성

        File workspace = new File(System.getProperty("user.dir")); // 현재 작업 디렉토리를 가져온다
        File[] javaFiles = workspace.listFiles((dir, name) -> name.endsWith(".java") && !name.equals("GWTTests.java")); // .java 파일을 찾고 GWTTests.java는 제외

        if (javaFiles != null) { // 찾은 Java 파일에 대해 테스트 생성
            for (File file : javaFiles) {
                String className = file.getName().replace(".java", ""); // 파일 이름에서 .java를 제거하여 클래스 이름을 만든다
                dynamicTests.addAll(createTestsForClass(className)); // 해당 클래스에 대한 테스트를 추가
            }
        }

        dynamicTests.addAll(createGWTTestsForStockTrading()); // 주식 거래 관련 테스트 추가

        return dynamicTests; // 생성된 동적 테스트를 반환
    }

    private Collection<DynamicTest> createTestsForClass(String className) { // 주어진 클래스에 대한 테스트를 생성하는 메소드
        Collection<DynamicTest> tests = new ArrayList<>(); // 테스트를 저장할 리스트 생성

        try {
            Class<?> testClass = Class.forName(className); // 클래스 정보를 가져온다
            Object testInstance = testClass.getDeclaredConstructor().newInstance(); // 클래스의 인스턴스를 생성

            for (Method method : testClass.getDeclaredMethods()) { // 클래스의 모든 메소드를 확인
                if (method.getName().startsWith("test")) { // 메소드 이름이 test로 시작하면
                    tests.add(createDynamicTest(testInstance, method)); // 동적 테스트를 추가
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing class " + className + ": " + e.getMessage()); // 에러 메시지 출력
            tests.add(DynamicTest.dynamicTest("Error in " + className, 
                () -> Assertions.fail("Error: " + e.getMessage())));
        }

        return tests; // 생성된 테스트를 반환
    }

    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) { // 동적 테스트를 생성하는 메소드
        return DynamicTest.dynamicTest("test " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName(), () -> {
            System.out.println("Given: Setting up for " + testMethod.getName()); // 테스트 메소드가 설정되는 단계
            System.out.println("When: Executing " + testMethod.getName()); // 테스트 메소드가 실행되는 단계

            Object result = testMethod.invoke(testInstance); // 테스트 메소드를 실행하여 결과를 얻는다

            System.out.println("Then: Verifying the result of " + testMethod.getName()); // 테스트 결과를 검증하는 단계

            if (result instanceof Boolean) { // 결과에 따라 다른 검증을 수행
                Assertions.assertTrue((Boolean) result, "Test " + testMethod.getName() + " failed"); // Boolean 결과 검증
            } else if (result instanceof Double) {
                Assertions.assertTrue((Double) result > 0, "Test " + testMethod.getName() + " failed"); // Double 결과 검증
            } else {
                Assertions.assertNotNull(result, "Test " + testMethod.getName() + " returned null"); // 결과가 null이 아닌지 검증
            }
        });
    }

    private Collection<DynamicTest> createGWTTestsForStockTrading() { // 주식 거래에 대한 테스트 생성
        Collection<DynamicTest> tests = new ArrayList<>(); // 테스트를 저장할 리스트 생성
        StockTrading stockTrading = new StockTrading("123456"); // 주식 거래 인스턴스 생성

        // 매수 테스트
        tests.add(DynamicTest.dynamicTest("Test Buy Stock", () -> {
            Assertions.assertTrue(stockTrading.testBuyStock(), "Buy Stock Test Failed");
        }));

        // 매도 테스트
        tests.add(DynamicTest.dynamicTest("Test Sell Stock", () -> {
            Assertions.assertTrue(stockTrading.testSellStock(), "Sell Stock Test Failed");
        }));

        // 포트폴리오 및 잔액 확인
        tests.add(DynamicTest.dynamicTest("Test Portfolio Display", () -> {
            stockTrading.displayPortfolio(); // 포트폴리오 출력
            Assertions.assertTrue(stockTrading.getStockCount("현대") > 0, "Portfolio Count Test Failed"); // 현대 주식 보유 수량 검증
        }));

        return tests; // 생성된 테스트를 반환
    }
}