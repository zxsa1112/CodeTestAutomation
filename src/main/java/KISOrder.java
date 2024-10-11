import java.util.HashMap;
import java.util.Map;

public class KISOrder {

    private String accountNumber; // 계좌 번호
    private String productCode;    // 상품 코드
    private String orderNumber;     // 주문 번호
    private String orderDivision;   // 주문 구분
    private String orderQuantity;    // 주문 수량
    private String orderUnitPrice;  // 주문 단가

    // 생성자
    public KISOrder(String accountNumber, String productCode, String orderNumber,
                    String orderDivision, String orderQuantity, String orderUnitPrice) {
        this.accountNumber = accountNumber;
        this.productCode = productCode;
        this.orderNumber = orderNumber;
        this.orderDivision = orderDivision;
        this.orderQuantity = orderQuantity;
        this.orderUnitPrice = orderUnitPrice;
    }

    // 주문 요청 본문 준비 메소드
    public String prepareOrderRequestBody() {
        // 실제 주문 요청 본문을 JSON 형식으로 작성
        return String.format(
            "{\"CANO\": \"%s\", \"ACNT_PRDT_CD\": \"%s\", \"PDNO\": \"%s\", " +
            "\"ORD_DVSN\": \"%s\", \"ORD_QTY\": \"%s\", \"ORD_UNPR\": \"%s\"}",
            accountNumber, productCode, orderNumber, orderDivision, orderQuantity, orderUnitPrice
        );
    }
}