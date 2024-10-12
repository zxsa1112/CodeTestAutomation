import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;

    // 생성자
    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    // 예약 정보를 출력하는 메소드
    public void printReservationDetails() {
        System.out.println("예약 ID: " + reservationId);
        System.out.println("손님 이름: " + guestName);
        System.out.println("객실 유형: " + roomType);
        System.out.println("숙박 일수: " + nights);
    }

    // JSON 형식으로 예약 정보를 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("ReservationID", reservationId);
        json.put("GuestName", guestName);
        json.put("RoomType", roomType);
        json.put("Nights", nights);
        return json.toString();
    }

    // 예약 목록을 생성하는 메소드
    public static List<Reservation> createSampleReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation("RES001", "Alice", "Deluxe", 3));
        reservations.add(new Reservation("RES002", "Bob", "Standard", 2));
        return reservations;
    }

    public static void main(String[] args) {
        // 샘플 예약 생성
        List<Reservation> sampleReservations = createSampleReservations();

        // 예약 상세 출력
        for (Reservation reservation : sampleReservations) {
            reservation.printReservationDetails();
            System.out.println("예약 정보 (JSON): " + reservation.toJson());
            System.out.println("-----");
        }
    }
}
