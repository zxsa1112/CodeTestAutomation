import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class KISOrder {

    private static final String APP_KEY = System.getenv("KIS_APP_KEY");      // 환경 변수에서 앱 키 읽기
    private static final String APP_SECRET = System.getenv("KIS_APP_SECRET"); // 환경 변수에서 앱 시크릿 읽기
    private static final String TOKEN_URL = "https://openapi.koreainvestment.com:9443/oauth2/token"; // 실제 도메인
    private static final String HASHKEY_URL = "https://openapi.koreainvestment.com:9443/uapi/hashkey"; // 실제 도메인

    public static void main(String[] args) {
        try {
            // 1. 접근 토큰 발급
            String accessToken = getAccessToken();
            System.out.println("Access Token: " + accessToken);

            // 2. 주문 요청 본문 준비
            String orderRequestBody = prepareOrderRequestBody();  // 주문 요청 본문 준비

            // 3. 해시키 생성
            String hashKey = generateHashkey(orderRequestBody);
            System.out.println("Hash Key: " + hashKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 접근 토큰 발급 메소드
    public static String getAccessToken() throws Exception {
        String params = "grant_type=client_credentials&client_id=" + APP_KEY + "&client_secret=" + APP_SECRET;
        URL url = new URL(TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
            os.flush();
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        
        // JSON 응답 파싱
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getString("access_token"); // 접근 토큰 반환
    }

    // 주문 요청 본문 준비 메소드
    public static String prepareOrderRequestBody() {
        // 실제 주문 요청 본문을 JSON 형식으로 작성
        return "{\"ORD_PRCS_DVSN_CD\": \"02\", \"CANO\": \"계좌번호\", \"ACNT_PRDT_CD\": \"03\", " +
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
}
