import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class KISOrder {

    private static final String APP_KEY = System.getenv("KIS_APP_KEY");      // 환경 변수에서 APP_KEY 가져오기
    private static final String APP_SECRET = System.getenv("KIS_APP_SECRET"); // 환경 변수에서 APP_SECRET 가져오기
    private static final String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");   // 환경 변수에서 접근 토큰 가져오기
    private static final String ORDER_URL = "https://openapivts.koreainvestment.com:29443/uapi/domestic-stock/v1/trading/order-cash";

    public static void main(String[] args) {
        try {
            // 1. 주문 요청 본문 준비
            String orderRequestBody = prepareOrderRequestBody();  // 주문 요청 본문 준비

            // 2. 주문 요청 전송
            sendOrderRequest(orderRequestBody);  // 주문 요청 전송

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 주문 요청 본문 준비 메소드
    public static String prepareOrderRequestBody() {
        // 실제 주문 요청 본문을 JSON 형식으로 작성
        return "{\"CANO\": \"50118406\", \"ACNT_PRDT_CD\": \"03\", \"PDNO\": \"101S06\", " +
                "\"ORD_DVSN\": \"00\", \"ORD_QTY\": \"1\", \"ORD_UNPR\": \"370\"}";
    }

    public static void sendOrderRequest(String orderRequestBody) throws Exception {
        URL url = new URL(ORDER_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
        conn.setRequestProperty("appkey", APP_KEY);
        conn.setRequestProperty("appsecret", APP_SECRET);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(orderRequestBody.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            // 서버의 오류 응답 본문 읽기
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            System.err.println("Error Response: " + errorResponse.toString());
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        System.out.println("Response: " + response.toString());
    }
}