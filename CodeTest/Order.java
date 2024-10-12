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
        // 수량이 0 이하이거나 가격이 0 이하일 경우 주문 실패
        if (quantity <= 0 || unitPrice <= 0) {
            return false; // 주문 실패
        }
        
        // 주문 정보를 콘솔에 출력
        System.out.println("주문 처리 중...");
        System.out.println("계좌 번호: " + accountNumber);
        System.out.println("상품 코드: " + productCode);
        System.out.println("주문 수량: " + quantity);
        System.out.println("단가: " + unitPrice);
        return true; // 주문 성공
    }
}