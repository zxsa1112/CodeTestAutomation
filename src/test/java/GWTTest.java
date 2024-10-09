import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GWTTest {

    private StockOrder order;

    @BeforeEach
    public void setUp() {
        order = new StockOrder();
    }

    // 1. 주문이 성공하는 경우
    @Test
    public void testPlaceOrder_Success() {
        // Given
        order.setAccountValid(true);

        // When
        order.placeOrder(10, 100.0);

        // Then
        assertTrue(order.isOrderConfirmed());
    }

    // 2. 계좌가 유효하지 않은 경우
    @Test
    public void testPlaceOrder_InvalidAccount() {
        // Given
        order.setAccountValid(false);

        // When
        order.placeOrder(10, 100.0);

        // Then
        assertFalse(order.isOrderConfirmed());
    }

    // 3. 주문 수량이 0인 경우
    @Test
    public void testPlaceOrder_ZeroQuantity() {
        // Given
        order.setAccountValid(true);

        // When
        order.placeOrder(0, 100.0);

        // Then
        assertFalse(order.isOrderConfirmed());
    }

    // 4. 주문 가격이 0인 경우
    @Test
    public void testPlaceOrder_ZeroPrice() {
        // Given
        order.setAccountValid(true);

        // When
        order.placeOrder(10, 0.0);

        // Then
        assertFalse(order.isOrderConfirmed());
    }
}
