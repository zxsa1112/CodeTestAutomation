import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class StockStock {

    private String accountNumber; // 주문 계좌 번호
    private String productCode;    // 상품 코드
    private int quantity;          // 주문 수량
    private double price;          // 주문 가격

    // 생성자
    public StockStock(String accountNumber, String productCode, int quantity, double price) {
        this.accountNumber = accountNumber;
        this.productCode = productCode;
        this.quantity = quantity;
        this.price = price;
    }

    // 주문 정보를 출력하는 메소드
    public void printOrderDetails() {
        // 잘못된 변수명을 사용하여 컴파일 오류 발생
        System.out.printf("주문 계좌 번호: %s%n", accountNumber);
        System.out.printf("상품 코드: %s%n", productCode);
        System.out.printf("주문 수량: %d%n", quantity);
        System.out.printf("주문 가격: %.2f%n", price);

        // 정의되지 않은 메서드 호출로 인한 오류
        this.undefinedMethod(); // 이 줄에서 컴파일 오류 발생
    }

    // JSON 형식으로 주문 정보를 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("CANO", accountNumber);
        json.put("ACNT_PRDT_CD", productCode);
        json.put("ORD_QTY", quantity);
        json.put("ORD_UNPR", price);
        return json.toString();
    }

    // 주문 목록을 생성하는 메소드
    public static List<StockStock> createSampleOrders() {
        List<StockStock> orders = new ArrayList<>();
        orders.add(new StockStock("50118406", "03", 1, 370.0));
        orders.add(new StockStock("50118406", "04", 2, 500.0));
        orders.add(new StockStock("50118406", "05", 3, 1200.0));
        return orders;
    }

    public static void main(String[] args) {
        // 샘플 주문 생성
        List<StockStock> sampleOrders = createSampleOrders();

        // 주문 상세 출력
        for (StockStock order : sampleOrders) {
            order.printOrderDetails();
            System.out.println("주문 정보 (JSON): " + order.toJson());

            // 배열의 인덱스 범위 초과를 발생시키기 위한 코드
            int[] numbers = new int[5];
            System.out.println(numbers[10]); // 이 줄에서 ArrayIndexOutOfBoundsException 발생
            System.out.println("-----");
        }
    }
}