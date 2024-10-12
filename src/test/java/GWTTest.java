import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

public class GWTTest {

    @TestFactory
    List<DynamicTest> testDynamicGWT() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        try {
            Class<?> testClass = Class.forName("StockTrading");
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.getName().startsWith("test")) {
                    dynamicTests.add(createDynamicTest(testInstance, method));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynamicTests;
    }

    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) {
        return DynamicTest.dynamicTest(testMethod.getName(), () -> {
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
            }
        });
    }
}