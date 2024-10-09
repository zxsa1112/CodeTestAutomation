import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CodeValidatorTest {

    @Test
    public void testHttpPostBodyConnection_ValidInput() {
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/trading/inquire-psbl-order";
        String data = "{ \"CANO\": \"123456\", \"ACNT_PRDT_CD\": \"01\", \"ACNT_PWD\": \"password\", \"PDNO\": \"123\", \"ORD_UNPR\": \"1000\", \"ORD_DVSN\": \"01\", \"CMA_EVLU_AMT_ICLD_YN\": \"Y\", \"OVRS_ICLD_YN\": \"N\" }";
        String tr_id = "TTTC8908R";

        assertDoesNotThrow(() -> {
            String response = httpPostBodyConnection(url, data, tr_id);
            assertNotNull(response); // 응답이 null이 아님을 확인
        });
    }

    @Test
    public void testHttpPostBodyConnection_InvalidURL() {
        String url = "https://invalid.url.com"; // 잘못된 URL
        String data = "{ \"CANO\": \"123456\", \"ACNT_PRDT_CD\": \"01\", \"ACNT_PWD\": \"password\", \"PDNO\": \"123\", \"ORD_UNPR\": \"1000\", \"ORD_DVSN\": \"01\", \"CMA_EVLU_AMT_ICLD_YN\": \"Y\", \"OVRS_ICLD_YN\": \"N\" }";
        String tr_id = "TTTC8908R";

        assertThrows(IOException.class, () -> {
            httpPostBodyConnection(url, data, tr_id);
        });
    }

    @Test
    public void testHttpPostBodyConnection_EmptyData() {
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/trading/inquire-psbl-order";
        String data = ""; // 빈 데이터
        String tr_id = "TTTC8908R";

        assertThrows(IOException.class, () -> {
            httpPostBodyConnection(url, data, tr_id);
        });
    }

    // 테스트할 httpPostBodyConnection 메서드
    public String httpPostBodyConnection(String UrlData, String ParamData, String TrId) throws IOException {
        String totalUrl = UrlData.trim();
        URL url = new URL(totalUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte request_data[] = ParamData.getBytes("utf-8");
            os.write(request_data);
        }

        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error: " + responseCode + " " + conn.getResponseMessage());
        }

        StringBuilder responseData = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                responseData.append(line);
            }
        }
        return responseData.toString(); // 응답 데이터를 반환
    }
}
