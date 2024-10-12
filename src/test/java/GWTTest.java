import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.util.ArrayList;
import java.util.List;

public class GWTTest {

    @TestFactory
    List<DynamicTest> testJsonConversion() {
        List<DynamicTest> nodes = new ArrayList<>();

        // Given
        String accountNumber = null;
        String productCode = null;
        int quantity = 0;
        double price = 0.0;

        // When & Then
        nodes.add(DynamicTest.dynamicTest("Test for Stock123", () -> {
            Object order = new Stock123(accountNumber, productCode, quantity, price);
            String expectedJson = "{\"CANO\":\"null\",\"ACNT_PRDT_CD\":\"null\",\"ORD_QTY\":0,\"ORD_UNPR\":0.0}";
            assertEquals(expectedJson, order.toJson(), "JSON 출력이 일치하지 않습니다.");
        }));

        nodes.add(DynamicTest.dynamicTest("Test for Invoice", () -> {
            Object order = new Invoice(accountNumber, productCode, quantity, price);
            String expectedJson = "{\"CANO\":\"null\",\"ACNT_PRDT_CD\":\"null\",\"ORD_QTY\":0,\"ORD_UNPR\":0.0}";
            assertEquals(expectedJson, order.toJson(), "JSON 출력이 일치하지 않습니다.");
        }));

        nodes.add(DynamicTest.dynamicTest("Test for Order", () -> {
            Object order = new Order(accountNumber, productCode, quantity, price);
            String expectedJson = "{\"CANO\":\"null\",\"ACNT_PRDT_CD\":\"null\",\"ORD_QTY\":0,\"ORD_UNPR\":0.0}";
            assertEquals(expectedJson, order.toJson(), "JSON 출력이 일치하지 않습니다.");
        }));

        return nodes;
    }
}