import java.io.BufferedReader;
import java.io.InputStreamReader;

public class KISOrder {

    // 주문 요청 본문 준비 메소드
    public String prepareOrderRequestBody() {
        // 실제 주문 요청 본문을 JSON 형식으로 작성
        return "{\"CANO\": \"50118406\", \"ACNT_PRDT_CD\": \"03\", \"PDNO\": \"101S06\", " +
                "\"ORD_DVSN\": \"00\", \"ORD_QTY\": \"1\", \"ORD_UNPR\": \"370\"}";
    }

    public static void main(String[] args) {
        try {
            KISOrder kisOrder = new KISOrder(); // KISOrder 객체 생성

            // 주문 요청 본문을 직접 정의합니다.
            String orderRequestBody = kisOrder.prepareOrderRequestBody();

            // 주문 요청 본문 출력
            System.out.println("Order Request Body: " + orderRequestBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}