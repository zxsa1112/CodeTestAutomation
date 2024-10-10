import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GWTTest {

    // 1. 주문이 성공하는 경우
    @Test
    public void testPlaceOrder_Success() {
        // Given
        boolean accountValid = true; // 계좌가 유효하다고 가정
        boolean orderConfirmed = false;

        // When
        orderConfirmed = placeOrder(accountValid, 10, 100.0); // 10주를 100.0에 주문

        // Then
        assertTrue(orderConfirmed); // 주문이 확인되었는지 검증
    }

    // 2. 계좌가 유효하지 않은 경우
    @Test
    public void testPlaceOrder_InvalidAccount() {
        // Given
        boolean accountValid = false; // 계좌가 유효하지 않음
        boolean orderConfirmed = false;

        // When
        orderConfirmed = placeOrder(accountValid, 10, 100.0); // 10주를 100.0에 주문

        // Then
        assertFalse(orderConfirmed); // 주문이 확인되지 않았는지 검증
    }

    // 3. 주문 수량이 0인 경우
    @Test
    public void testPlaceOrder_ZeroQuantity() {
        // Given
        boolean accountValid = true; // 계좌가 유효하다고 가정
        boolean orderConfirmed = false;

        // When
        orderConfirmed = placeOrder(accountValid, 0, 100.0); // 0주를 주문

        // Then
        assertFalse(orderConfirmed); // 주문이 확인되지 않았는지 검증
    }

    // 4. 주문 가격이 0인 경우
    @Test
    public void testPlaceOrder_ZeroPrice() {
        // Given
        boolean accountValid = true; // 계좌가 유효하다고 가정
        boolean orderConfirmed = false;

        // When
        orderConfirmed = placeOrder(accountValid, 10, 0.0); // 10주를 0.0에 주문

        // Then
        assertFalse(orderConfirmed); // 주문이 확인되지 않았는지 검증
    }

    // 주문 처리 메서드
    private boolean placeOrder(boolean accountValid, int quantity, double price) {
        return accountValid && quantity > 0 && price > 0;
    }
}
