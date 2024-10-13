import org.junit.jupiter.api.DynamicTest; // 동적 테스트를 생성하기 위한 클래스
import org.junit.jupiter.api.TestFactory; // 테스트 팩토리를 만들기 위한 클래스
import org.junit.jupiter.api.Assertions; // 테스트 결과를 확인하는 데 필요한 클래스
import java.util.ArrayList; // 동적 테스트를 저장할 리스트를 위한 클래스
import java.util.Collection; // 여러 객체를 모아서 저장할 수 있는 컬렉션 클래스
import java.lang.reflect.Method; // 리플렉션을 사용하여 메소드 정보를 가져오기 위한 클래스
import java.io.File; // 파일 작업을 위한 클래스

public class GWTTests {

    // 동적 테스트를 만드는 메소드
    @TestFactory
    Collection<DynamicTest> testDynamicGWT() {
        // 동적 테스트를 저장할 리스트 생성
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        // 로컬 테스트 시 현재 디렉토리 사용
        String workspacePath = System.getProperty("user.dir");

        // 파일 작업을 위한 경로 설정
        File workspace = new File(workspacePath);

        // .java 파일을 찾고 GWTTests.java는 제외
        File[] javaFiles = workspace.listFiles((dir, name) -> name.endsWith(".java") && !name.equals("GWTTests.java"));

        // 찾은 Java 파일에 대해 테스트 생성
        if (javaFiles != null) {
            for (File file : javaFiles) {
                String className = file.getName().replace(".java", ""); // 파일 이름에서 .java 제거
                // 각 클래스에 대한 테스트를 만들어 리스트에 추가
                dynamicTests.addAll(createTestsForClass(className));
            }
        }

        // 주식 거래 테스트 추가
        dynamicTests.addAll(createGWTTestsForStockTrading());

        // 생성된 동적 테스트 리스트 반환
        return dynamicTests;
    }

    // 주어진 클래스에 대한 테스트를 생성하는 메소드
    private Collection<DynamicTest> createTestsForClass(String className) {
        Collection<DynamicTest> tests = new ArrayList<>();

        try {
            // 클래스를 로드하고 인스턴스 생성
            Class<?> testClass = Class.forName(className);
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            // 클래스 내 모든 메소드에 대해
            for (Method method : testClass.getDeclaredMethods()) {
                // 메소드 이름이 "test"로 시작하는 경우
                if (method.getName().startsWith("test")) {
                    // 동적 테스트 생성
                    tests.add(createDynamicTest(testInstance, method));
                }
            }
        } catch (Exception e) {
            // 에러 발생 시 에러 메시지를 출력하고 실패 테스트 추가
            System.out.println("Error processing class " + className + ": " + e.getMessage());
            tests.add(DynamicTest.dynamicTest("Error in " + className, 
                () -> Assertions.fail("Error: " + e.getMessage())));
        }

        // 생성된 테스트 컬렉션을 반환
        return tests;
    }

    // 동적 테스트를 생성하는 메소드
    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) {
        return DynamicTest.dynamicTest("test " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName(), () -> {
            System.out.println("Given: Setting up for " + testMethod.getName());
            System.out.println("When: Executing " + testMethod.getName());

            // 테스트 메소드 실행
            Object result = testMethod.invoke(testInstance);
            
            System.out.println("Then: Verifying the result of " + testMethod.getName());
            
            // 결과에 따른 단언
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

    // 주식 거래 시스템에 대한 GWT 테스트를 생성하는 메소드
    private Collection<DynamicTest> createGWTTestsForStockTrading() {
        Collection<DynamicTest> tests = new ArrayList<>();

        // 삼성 주식을 구매하는 테스트
        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, buyStock이 호출되면, 성공해야 한다", () -> {
            // Given: 주식 거래 시스템을 사용하기 위한 객체를 생성합니다.
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템의 새로운 인스턴스를 만듭니다.
            double initialBalance = stockTrading.getAccountBalance("123456"); // 초기 잔액을 가져옵니다.
            
            // When: 삼성 주식 10주 구매 시도
            boolean result = stockTrading.buyStock("123456", "삼성", 10); // '123456' 계좌로 '삼성' 주식 10주를 구매합니다.

            // Then: 주식 구매 성공 여부 및 잔액 검증
            Assertions.assertTrue(result, "주식을 사는 것이 성공해야 합니다."); // 주식 구매가 성공했는지 확인합니다.
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") < initialBalance, "주식을 구매한 후 잔액이 줄어야 합니다."); // 주식을 구매한 후 잔액이 감소했는지 확인합니다.
        }));

        // 현대 주식을 판매하는 테스트
        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서 주식을 보유한 상태에서, sellStock이 호출되면, 성공해야 한다", () -> {
            // Given: 주식 거래 시스템을 사용하기 위한 객체를 생성하고 현대 주식 5주 구매
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템의 새로운 인스턴스를 만듭니다.
            stockTrading.buyStock("123456", "현대", 5); // '현대' 주식 5주를 '123456' 계좌로 구매합니다.
            double initialBalance = stockTrading.getAccountBalance("123456"); // 초기 잔액을 가져옵니다.

            // When: 현대 주식 2주 판매 시도
            boolean result = stockTrading.sellStock("123456", "현대", 2); // '123456' 계좌로 '현대' 주식 2주를 판매합니다.

            // Then: 주식 판매 성공 여부 및 잔액 검증
            Assertions.assertTrue(result, "주식을 파는 것이 성공해야 합니다."); // 주식 판매가 성공했는지 확인합니다.
            Assertions.assertTrue(stockTrading.getAccountBalance("123456") > initialBalance, "주식을 판매한 후 잔액이 증가해야 합니다."); // 주식을 판매한 후 잔액이 증가했는지 확인합니다.
            
            // Then: 주식 수가 올바르게 감소했는지 확인
            int remainingStocks = stockTrading.getStockCount("123456", "현대"); // '123456' 계좌에서 남은 '현대' 주식 수를 확인합니다.
            Assertions.assertEquals(3, remainingStocks, "2주를 판매한 후 남은 주식 수는 3이어야 합니다."); // 2주를 판매한 후 남은 주식 수가 3인지 확인합니다.
        }));

        // LG 주식의 초기 잔액을 확인하는 테스트
        tests.add(DynamicTest.dynamicTest("주어진 StockTrading 인스턴스에서, getAccountBalance가 호출되면, 올바른 잔액을 반환해야 한다", () -> {
            // Given: 주식 거래 시스템을 사용하기 위한 객체를 생성합니다.
            StockTrading stockTrading = new StockTrading(); // 주식 거래 시스템의 새로운 인스턴스를 만듭니다.

            // When: 잔액 조회 메소드 호출
            double balance = stockTrading.getAccountBalance("123456"); // '123456' 계좌의 잔액을 조회합니다.

            // Then: 초기 잔액 검증
            Assertions.assertEquals(100000.0, balance, "초기 잔액은 100,000달러여야 합니다."); // 초기 잔액이 100,000달러인지 확인합니다.
            System.out.println("LG 주식 초기 잔액: " + balance); // 테스트 결과 출력
        }));

        return tests; // 모든 테스트 목록을 반환합니다.
    }
}