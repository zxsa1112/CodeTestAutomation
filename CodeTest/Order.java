import org.json.JSONObject;

public class Order {

    private String accountNumber; // 계좌 번호
    private String productCode;    // 상품 코드
    private int quantity;          // 주문 수량
    private double unitPrice;      // 단가

    // 생성자
    public Order(String accountNumber, String productCode, int quantity, double unitPrice) {
        this.accountNumber = accountNumber;
        this.productCode = productCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // 주문 정보를 JSON 형식으로 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("CANO", accountNumber);
        json.put("ACNT_PRDT_CD", productCode);
        json.put("ORD_QTY", quantity);
        json.put("ORD_UNPR", unitPrice);
        return json.toString();
    }

    // 주문 처리 메소드 (실제 로직을 구현하지 않고 항상 true 반환)
    public void placeOrder() {
        // 주문 정보를 콘솔에 출력
        System.out.println("주문 처리 중...");
        System.out.println("계좌 번호: " + accountNumber);
        System.out.println("상품 코드: " + productCode);
        System.out.println("주문 수량: " + quantity);
        System.out.println("단가: " + unitPrice);
        // 주문 성공 여부를 판단하는 로직은 GWTTest에서 처리하므로 여기서는 생략
    }

    /*public static void main(String[] args) {
        // 이 부분은 테스트 용도로 사용하지 않도록 주석 처리할 수 있습니다.
        // Order order = new Order("12345", "STOCK_A", 10, 100.0);
        // order.placeOrder();
    } */
}