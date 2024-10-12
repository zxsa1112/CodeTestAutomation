import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class GWTTest {

    @Test
    public void testPrepareOrderRequestBody() {
        // Given: 주문 정보가 담긴 Map 객체를 생성합니다.
        Map<String, String> orderData = new HashMap<>();
        orderData.put("CANO", "{CANO}");
        orderData.put("ACNT_PRDT_CD", "{ACNT_PRDT_CD}");
        orderData.put("PDNO", "{PDNO}");
        orderData.put("ORD_DVSN", "{ORD_DVSN}");
        orderData.put("ORD_QTY", "{ORD_QTY}");
        orderData.put("ORD_UNPR", "{ORD_UNPR}");

        // 주문 요청 본문을 준비하는 메소드
        String actualOrderRequestBody = prepareOrderRequestBody(orderData);

        // Then: 실제 주문 요청 본문이 예상과 일치하는지 확인합니다.
        String expectedOrderRequestBody = "{\"CANO\": \"{CANO}\", \"ACNT_PRDT_CD\": \"{ACNT_PRDT_CD}\", " +
                "\"PDNO\": \"{PDNO}\", \"ORD_DVSN\": \"{ORD_DVSN}\", \"ORD_QTY\": \"{ORD_QTY}\", \"ORD_UNPR\": \"{ORD_UNPR}\"}";

        assertEquals(actualOrderRequestBody, expectedOrderRequestBody, "주문 요청 본문이 일치하지 않습니다.");
    }

    // 주문 요청 본문을 생성하는 메소드
    private String prepareOrderRequestBody(Map<String, String> orderData) {
        return String.format("{\"CANO\": \"%s\", \"ACNT_PRDT_CD\": \"%s\", \"PDNO\": \"%s\", " +
                             "\"ORD_DVSN\": \"%s\", \"ORD_QTY\": \"%s\", \"ORD_UNPR\": \"%s\"}",
                             orderData.get("CANO"), orderData.get("ACNT_PRDT_CD"), orderData.get("PDNO"),
                             orderData.get("ORD_DVSN"), orderData.get("ORD_QTY"), orderData.get("ORD_UNPR"));
    }
}
