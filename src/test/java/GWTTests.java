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
        return DynamicTest.dynamicTest(testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName(), () -> {
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
}