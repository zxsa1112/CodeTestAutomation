import org.junit.Test;
import static org.junit.Assert.*;

public class GWTTest {

    @Test
    public void testPlaceOrder() {
        // Given
        StockOrder order = new StockOrder();
        order.setAccountValid(true); // 계좌가 유효하다고 가정

        // When
        order.placeOrder(10, 100.0); // 10주를 100.0에 주문

        // Then
        assertTrue(order.isOrderConfirmed()); // 주문이 확인되었는지 검증
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
        if (accountValid) {
            // 주문 로직 (간단히 주문 확인 상태만 변경)
            orderConfirmed = true; // 실제 로직 구현
        }
    }

    public boolean isOrderConfirmed() {
        return orderConfirmed;
    }
}
