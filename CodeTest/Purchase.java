import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Purchase {

    private String customerName;
    private String item;
    private int amount;
    private double price;

    // 생성자
    public Purchase(String customerName, String item, int amount, double price) {
        this.customerName = customerName;
        this.item = item;
        this.amount = amount;
        this.price = price;
    }

    // 구매 정보를 출력하는 메소드
    public void printPurchaseDetails() {
        System.out.println("고객 이름: " + customerName);
        System.out.println("상품: " + item);
        System.out.println("수량: " + amount);
        System.out.println("가격: " + price);
    }

    // JSON 형식으로 구매 정보를 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("CustomerName", customerName);
        json.put("Item", item);
        json.put("Amount", amount);
        json.put("Price", price);
        return json.toString();
    }

    // 구매 목록을 생성하는 메소드
    public static List<Purchase> createSamplePurchases() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(new Purchase("John Doe", "Tablet", 2, 300.0));
        purchases.add(new Purchase("Jane Smith", "Phone", 1, 800.0));
        return purchases;
    }

    public static void main(String[] args) {
        // 샘플 구매 생성
        List<Purchase> samplePurchases = createSamplePurchases();

        // 구매 상세 출력
        for (Purchase purchase : samplePurchases) {
            purchase.printPurchaseDetails();
            System.out.println("구매 정보 (JSON): " + purchase.toJson());
            System.out.println("-----");
        }
    }
}
