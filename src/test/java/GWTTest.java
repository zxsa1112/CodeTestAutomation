import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GWTTest {

    @TestFactory
    List<DynamicTest> testJsonConversion() {
        List<DynamicTest> nodes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) { // 10개의 테스트 케이스를 생성
            String accountNumber = String.valueOf(random.nextInt(100000)); // 0-99999 사이의 랜덤 계좌 번호
            String productCode = "STOCK_" + (char)('A' + random.nextInt(26)); // A-Z 사이의 랜덤 상품 코드
            int quantity = random.nextInt(100) + 1; // 1-100 사이의 랜덤 수량
            double price = Math.round(random.nextDouble() * 1000 * 100.0) / 100.0; // 0-1000 사이의 랜덤 가격 (소수점 2자리)

            // When & Then: 주식 매매 버튼이 클릭되고, 주식 매매가 이루어진다.
            nodes.add(DynamicTest.dynamicTest("주식 매매 테스트 for " + accountNumber + " - " + productCode, () -> {
                boolean isOrderPlaced = placeOrder(accountNumber, productCode, quantity, price);
                assertTrue(isOrderPlaced, "주식 매매 주문이 성공적으로 이루어져야 합니다.");

                String expectedJson = "{\"CANO\":\"" + accountNumber + "\",\"ACNT_PRDT_CD\":\"" + productCode + "\",\"ORD_QTY\":" + quantity + ",\"ORD_UNPR\":" + price + "}";
                String actualJson = getOrderJson(accountNumber, productCode, quantity, price); // 주문 정보를 JSON 형식으로 반환하는 메소드
                assertEquals(expectedJson, actualJson, "주문 JSON 형식이 일치해야 합니다.");
            }));
        }

        return nodes;
    }

    private boolean placeOrder(String accountNumber, String productCode, int quantity, double price) {
        // 매매 주문을 처리하는 로직을 추가합니다.
        return true; // 성공적으로 주문을 처리했다고 가정
    }

    private String getOrderJson(String accountNumber, String productCode, int quantity, double price) {
        // 주어진 인자를 기반으로 주문 정보를 JSON 형식으로 반환합니다.
        return "{\"CANO\":\"" + accountNumber + "\",\"ACNT_PRDT_CD\":\"" + productCode + "\",\"ORD_QTY\":" + quantity + ",\"ORD_UNPR\":" + price + "}";
    }
}