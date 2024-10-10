import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class KISOrder {

    private static final String APP_KEY = System.getenv("KIS_APP_KEY");      // 환경 변수에서 APP_KEY 가져오기
    private static final String APP_SECRET = System.getenv("KIS_APP_SECRET"); // 환경 변수에서 APP_SECRET 가져오기
    private static final String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");   // 환경 변수에서 접근 토큰 가져오기
    private static final String HASH_KEY = System.getenv("HASH_KEY");           // 환경 변수에서 해시키 가져오기
    private static final String ORDER_URL = "https://openapivts.koreainvestment.com:29443/uapi/order";

    public static void main(String[] args) {
        try {
            // 1. 주문 요청 본문 준비
            String orderRequestBody = prepareOrderRequestBody();  // 주문 요청 본문 준비

            // 2. 해시키 생성 (이 경우는 해시키를 사용할 필요 없을 수도 있지만 예시로 포함)
            String hashKey = generateHashkey(orderRequestBody);
            // System.out.println("Hash Key: " + hashKey); // 해시 키 출력하지 않음

            // 3. 주문 요청 전송
            sendOrderRequest(orderRequestBody, ACCESS_TOKEN, HASH_KEY);  // 주문 요청 전송

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 주문 요청 본문 준비 메소드
    public static String prepareOrderRequestBody() {
        // 실제 주문 요청 본문을 JSON 형식으로 작성
        return "{\"ORD_PRCS_DVSN_CD\": \"02\", \"CANO\": \"50118406\", \"ACNT_PRDT_CD\": \"03\", " +
                "\"SLL_BUY_DVSN_CD\": \"02\", \"SHTN_PDNO\": \"101S06\", \"ORD_QTY\": \"1\", \"UNIT_PRICE\": \"370\", " +
                "\"NMPR_TYPE_CD\": \"\", \"KRX_NMPR_CNDT_CD\": \"\", \"CTAC_TLNO\": \"\", \"FUOP_ITEM_DVSN_CD\": \"\", " +
                "\"ORD_DVSN_CD\": \"02\"}";
    }

    // 해시키 생성 메소드
    public static String generateHashkey(String jsonBody) throws Exception {
        // 요청 본문을 해시값으로 변환
        String hashKey = sha256(jsonBody); // SHA-256 해시 생성
        return hashKey;
    }

    // SHA-256 해시 생성 메소드
    public static String sha256(String base) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(base.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void sendOrderRequest(String orderRequestBody, String accessToken, String hashKey) throws Exception {
        URL url = new URL(ORDER_URL); // 올바른 URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken); // 접근 토큰 추가
        conn.setRequestProperty("Hashkey", hashKey); // 해시 키 추가
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(orderRequestBody.getBytes());
            os.flush();
        }
        
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
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