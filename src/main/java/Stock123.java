import java.util.ArrayList;
import java.util.List;

public class Stock123 {

    private String accountNumber;
    private String productCode;
    private int quantity;
    private double price;

    // 생성자
    public Stock123(String accountNumber, String productCode, int quantity, double price) {
        this.accountNumber = accountNumber;
        this.productCode = productCode;
        this.quantity = quantity;
        this.price = price;
    }

    // 주문 정보를 출력하는 메소드
    public void printOrderDetails() {
        System.out.println("주문 계좌 번호: " + accountNumber);
        System.out.println("상품 코드: " + productCode);
        System.out.println("주문 수량: " + quantity);
        System.out.println("주문 가격: " + price);
    }

    // 주문 목록을 생성하는 메소드
    public static List<Stock123> createSampleOrders() {
        List<Stock123> orders = new ArrayList<>();
        orders.add(new Stock123("50118406", "03", 1, 370.0));
        orders.add(new Stock123("50118406", "04", 2, 500.0));
        orders.add(new Stock123("50118406", "05", 3, 1200.0));
        return orders;
    }

    public static void main(String[] args) {
        // 샘플 주문 생성
        List<Stock123> sampleOrders = createSampleOrders();

        // 주문 상세 출력
        for (Stock123 order : sampleOrders) {
            order.printOrderDetails();
            System.out.println("-----");
        }
    }
}
