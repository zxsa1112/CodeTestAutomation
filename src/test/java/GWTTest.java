import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GWTTest {

    @Test
    public void testPrepareOrderRequestBody() {
        // Given: KISOrder 객체를 생성합니다.
        KISOrder kisOrder = new KISOrder("{CANO}", "{ACNT_PRDT_CD}", "{PDNO}", "{ORD_DVSN}", "{ORD_QTY}", "{ORD_UNPR}");

        // When: 주문 요청 본문을 준비합니다.
        String actualOrderRequestBody = kisOrder.prepareOrderRequestBody();

        // Then: 실제 주문 요청 본문이 예상과 일치하는지 확인합니다.
        String expectedOrderRequestBody = "{\"CANO\": \"{CANO}\", \"ACNT_PRDT_CD\": \"{ACNT_PRDT_CD}\", \"PDNO\": \"{PDNO}\", " +
                "\"ORD_DVSN\": \"{ORD_DVSN}\", \"ORD_QTY\": \"{ORD_QTY}\", \"ORD_UNPR\": \"{ORD_UNPR}\"}";

        assertEquals(actualOrderRequestBody, expectedOrderRequestBody, "주문 요청 본문이 일치하지 않습니다.");
    }
}