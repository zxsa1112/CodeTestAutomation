import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.io.File;

public class GWTTest {

    @TestFactory
    List<DynamicTest> testDynamicGWT() {
        List<DynamicTest> dynamicTests = new ArrayList<>();

        // GitHub 저장소의 루트 디렉토리 경로
        String repoPath = System.getenv("GITHUB_WORKSPACE");
        if (repoPath == null) {
            repoPath = "."; // 로컬 테스트를 위한 기본값
        }

        File dir = new File(repoPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".java"));

        if (files != null) {
            for (File file : files) {
                String className = file.getName().replace(".java", "");
                dynamicTests.addAll(createTestsForClass(className));
            }
        }

        return dynamicTests;
    }

    private List<DynamicTest> createTestsForClass(String className) {
        List<DynamicTest> tests = new ArrayList<>();

        try {
            Class<?> testClass = Class.forName(className);
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.getName().startsWith("test")) {
                    tests.add(createDynamicTest(testInstance, method));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tests;
    }

    private DynamicTest createDynamicTest(Object testInstance, Method testMethod) {
        return DynamicTest.dynamicTest(testMethod.getName(), () -> {
            // Given
            System.out.println("Given: " + getGivenDescription(testMethod));

            // When
            System.out.println("When: " + getWhenDescription(testMethod));
            Object result = testMethod.invoke(testInstance);

            // Then
            System.out.println("Then: " + getThenDescription(testMethod));
            if (result instanceof Boolean) {
                Assertions.assertTrue((Boolean) result, "Test " + testMethod.getName() + " failed");
            } else {
                System.out.println("Result: " + result);
            }
        });
    }

    private String getGivenDescription(Method method) {
        return "Setting up for " + method.getName();
    }

    private String getWhenDescription(Method method) {
        return "Executing " + method.getName();
    }

    private String getThenDescription(Method method) {
        return "Verifying the result of " + method.getName();
    }
}