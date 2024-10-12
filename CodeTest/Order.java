import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Order {

    private String orderId;
    private String product;
    private int quantity;
    private double unitPrice;

    // 생성자
    public Order(String orderId, String product, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // 주문 정보를 출력하는 메소드
    public void printOrderDetails() {
        System.out.println("주문 ID: " + orderId);
        System.out.println("상품: " + product);
        System.out.println("수량: " + quantity);
        System.out.println("단가: " + unitPrice);
    }

    // JSON 형식으로 주문 정보를 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("OrderID", orderId);
        json.put("Product", product);
        json.put("Quantity", quantity);
        json.put("UnitPrice", unitPrice);
        return json.toString();
    }

    // 주문 목록을 생성하는 메소드
    public static List<Order> createSampleOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("ORD001", "Laptop", 1, 1500.0));
        orders.add(new Order("ORD002", "Mouse", 5, 25.0));
        orders.add(new Order("ORD003", "Keyboard", 3, 45.0));
        return orders;
    }

    public static void main(String[] args) {
        // 샘플 주문 생성
        List<Order> sampleOrders = createSampleOrders();

        // 주문 상세 출력
        for (Order order : sampleOrders) {
            order.printOrderDetails();
            System.out.println("주문 정보 (JSON): " + order.toJson());
            System.out.println("-----");
        }
    }
}
