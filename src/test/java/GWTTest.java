import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.List;

public class GWTTest {

    @TestFactory
    List<DynamicTest> testJsonConversion() {
        List<DynamicTest> nodes = new ArrayList<>();

        // Given: 주식 매매를 위한 값이 주어진다.
        String[][] testData = {
            {"12345", "STOCK_A", "10", "100.0"},
            {"54321", "STOCK_B", "20", "150.0"},
            {"67890", "STOCK_C", "5", "200.0"}
        };

        for (String[] data : testData) {
            String accountNumber = data[0];
            String productCode = data[1];
            int quantity = Integer.parseInt(data[2]);
            double price = Double.parseDouble(data[3]);

            // When: 주식 매매 버튼이 클릭된다.
            nodes.add(DynamicTest.dynamicTest("주식 매매 테스트 for " + accountNumber + " - " + productCode, () -> {
                boolean isOrderPlaced = placeOrder(accountNumber, productCode, quantity, price);
                assertTrue(isOrderPlaced, "주식 매매 주문이 성공적으로 이루어져야 합니다.");
            }));

            // Then: 주식 매매가 이루어진다.
            nodes.add(DynamicTest.dynamicTest("주식 매매 결과 확인 for " + accountNumber + " - " + productCode, () -> {
                String expectedJson = "{\"CANO\":\"" + accountNumber + "\",\"ACNT_PRDT_CD\":\"" + productCode + "\",\"ORD_QTY\":" + quantity + ",\"ORD_UNPR\":" + price + "}";
                String actualJson = getOrderJson(accountNumber, productCode, quantity, price);
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