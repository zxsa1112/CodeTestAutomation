import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GWTTest {

    // 1. 주문이 성공하는 경우
    @Test
    public void testPlaceOrder_Success() {
        // Given
        StockOrder order = new StockOrder();
        order.setAccountValid(true); // 계좌가 유효하다고 가정

        // When
        order.placeOrder(10, 100.0); // 10주를 100.0에 주문

        // Then
        assertTrue(order.isOrderConfirmed()); // 주문이 확인되었는지 검증
    }

    // 2. 계좌가 유효하지 않은 경우
    @Test
    public void testPlaceOrder_InvalidAccount() {
        // Given
        StockOrder order = new StockOrder();
        order.setAccountValid(false); // 계좌가 유효하지 않음

        // When
        order.placeOrder(10, 100.0); // 10주를 100.0에 주문

        // Then
        assertFalse(order.isOrderConfirmed()); // 주문이 확인되지 않았는지 검증
    }

    // 3. 주문 수량이 0인 경우
    @Test
    public void testPlaceOrder_ZeroQuantity() {
        // Given
        StockOrder order = new StockOrder();
        order.setAccountValid(true); // 계좌가 유효하다고 가정

        // When
        order.placeOrder(0, 100.0); // 0주를 주문

        // Then
        assertFalse(order.isOrderConfirmed()); // 주문이 확인되지 않았는지 검증
    }

    // 4. 주문 가격이 0인 경우
    @Test
    public void testPlaceOrder_ZeroPrice() {
        // Given
        StockOrder order = new StockOrder();
        order.setAccountValid(true); // 계좌가 유효하다고 가정

        // When
        order.placeOrder(10, 0.0); // 10주를 0.0에 주문

        // Then
        assertFalse(order.isOrderConfirmed()); // 주문이 확인되지 않았는지 검증
    }
}

// 주식 주문을 나타내는 클래스
class StockOrder {
    private boolean accountValid;
    private boolean orderConfirmed;

    public void setAccountValid(boolean valid) {
        this.accountValid = valid;
    }

    public void placeOrder(int quantity, double price) {
        if (accountValid && quantity > 0 && price > 0) {
            // 주문 로직 (간단히 주문 확인 상태만 변경)
            orderConfirmed = true; // 실제 로직 구현
        } else {
            orderConfirmed = false; // 주문이 확인되지 않음
        }
    }

    public boolean isOrderConfirmed() {
        return orderConfirmed;
    }
}