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

    // 주문 처리 메소드
    public boolean placeOrder() {
        // 여기에 실제 주문 처리 로직을 추가
        // 예를 들어, 데이터베이스에 주문 저장 또는 외부 API 호출
        return true; // 주문이 성공적으로 처리되면 true 반환
    }

    public static void main(String[] args) {
        // 예시로 주식 매매를 처리
        String accountNumber = "12345";
        String productCode = "STOCK_A";
        int quantity = 10;
        double unitPrice = 100.0;

        Order order = new Order(accountNumber, productCode, quantity, unitPrice);
        
        // 주문 처리
        boolean isOrderPlaced = order.placeOrder();
        if (isOrderPlaced) {
            String orderJson = order.toJson();
            System.out.println("주문 정보 JSON: " + orderJson);
        } else {
            System.out.println("주문 실패");
        }
    }
}