import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GWTTest {

    @Test
    public void testPrepareOrderRequestBody() {
        // Given: 주문 요청 본문을 준비하는 메소드를 호출할 준비를 합니다.
        KISOrder kisOrder = new KISOrder(); // KISOrder 객체 생성

        // When: 주문 요청 본문을 준비합니다.
        String actualOrderRequestBody = kisOrder.prepareOrderRequestBody(); // prepareOrderRequestBody 메소드를 호출합니다.
        
        // Expected: 예상되는 주문 요청 본문을 정의합니다.
        String expectedOrderRequestBody = "{\"CANO\": \"50118406\", \"ACNT_PRDT_CD\": \"03\", \"PDNO\": \"101S06\", " +
                "\"ORD_DVSN\": \"00\", \"ORD_QTY\": \"1\", \"ORD_UNPR\": \"370\"}";

        // Then: 실제 주문 요청 본문이 예상과 일치하는지 확인합니다.
        assertEquals(expectedOrderRequestBody, actualOrderRequestBody, "주문 요청 본문이 일치하지 않습니다.");
    }
}
