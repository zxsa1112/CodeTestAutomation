import org.junit.Test;
import static org.junit.Assert.*;

public class GWTTest {
    @Test
    public void testExample() {
        // Given
        int input = 5;
        int expected = 10;

        // When
        int result = someFunction(input);

        // Then
        assertEquals(expected, result);
    }

    // 테스트할 메소드
    private int someFunction(int input) {
        return input * 2; // 예시 함수
    }
}
