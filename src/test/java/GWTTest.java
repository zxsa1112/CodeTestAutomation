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

        // 고정된 테스트 케이스
        String[][] testCases = {
            {"12345", "STOCK_A", "10", "100.0"},
            {"67890", "STOCK_B", "5", "250.75"},
            {"54321", "STOCK_C", "20", "300.50"},
            {"98765", "STOCK_D", "15", "150.00"},
            {"13579", "STOCK_E", "30", "80.25"},
            {"24680", "STOCK_F", "7", "60.90"},
            {"10293", "STOCK_G", "12", "45.00"},
            {"39481", "STOCK_H", "3", "99.99"},
            {"59384", "STOCK_I", "1", "1500.00"},
            {"74826", "STOCK_J", "25", "200.00"}
        };

        for (String[] testCase : testCases) {
            String accountNumber = testCase[0];
            String productCode = testCase[1];
            int quantity = Integer.parseInt(testCase[2]);
            double price = Double.parseDouble(testCase[3]);

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