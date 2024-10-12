import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Invoice {

    private String invoiceNumber;
    private String clientName;
    private double totalAmount;

    // 생성자
    public Invoice(String invoiceNumber, String clientName, double totalAmount) {
        this.invoiceNumber = invoiceNumber;
        this.clientName = clientName;
        this.totalAmount = totalAmount;
    }

    // 송장 정보를 출력하는 메소드
    public void printInvoiceDetails() {
        System.out.println("송장 번호: " + invoiceNumber);
        System.out.println("클라이언트 이름: " + clientName);
        System.out.println("총 금액: " + totalAmount);
    }

    // JSON 형식으로 송장 정보를 반환하는 메소드
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("InvoiceNumber", invoiceNumber);
        json.put("ClientName", clientName);
        json.put("TotalAmount", totalAmount);
        return json.toString();
    }

    // 송장 목록을 생성하는 메소드
    public static List<Invoice> createSampleInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice("INV001", "Client A", 1500.0));
        invoices.add(new Invoice("INV002", "Client B", 3000.0));
        return invoices;
    }

    public static void main(String[] args) {
        // 샘플 송장 생성
        List<Invoice> sampleInvoices = createSampleInvoices();

        // 송장 상세 출력
        for (Invoice invoice : sampleInvoices) {
            invoice.printInvoiceDetails();
            System.out.println("송장 정보 (JSON): " + invoice.toJson());
            System.out.println("-----");
        }
    }
}
