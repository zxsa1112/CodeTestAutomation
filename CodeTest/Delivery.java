import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Delivery {

    private String deliveryId;
    private String address;
    private String deliveryDate;

    // 생성자
    public Delivery(String deliveryId, String address, String deliveryDate) {
        this.deliveryId = deliveryId;
        this.address = address;
        this.deliveryDate = deliveryDate;
    }

    // 배송 정보를 출력하는 메소드
    public void printDeliveryDetails() {
        System.out.println("배송 ID: " + deliveryId);
        System.out.println("주소: " + address);
        System.out.println("배송 날짜: " + deliveryDate);
    }

    // JSON 형식으로 배송 정보를 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("DeliveryID", deliveryId);
        json.put("Address", address);
        json.put("DeliveryDate", deliveryDate);
        return json.toString();
    }

    // 배송 목록을 생성하는 메소드
    public static List<Delivery> createSampleDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery("DEL001", "서울시 강남구", "2024-10-20"));
        deliveries.add(new Delivery("DEL002", "서울시 송파구", "2024-10-21"));
        return deliveries;
    }

    public static void main(String[] args) {
        // 샘플 배송 생성
        List<Delivery> sampleDeliveries = createSampleDeliveries();

        // 배송 상세 출력
        for (Delivery delivery : sampleDeliveries) {
            delivery.printDeliveryDetails();
            System.out.println("배송 정보 (JSON): " + delivery.toJson());
            System.out.println("-----");
        }
    }
}
