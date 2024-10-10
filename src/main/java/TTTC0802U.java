package src.main.java;

import com.google.gson.Gson;  // Gson을 import 합니다.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TTTC0802U {
    public static void main(String[] args) throws IOException {
        // 국내 주식 주문
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/trading/order-cash";
        String tr_id = "TTTC0802U";

        // Gson을 사용하여 JSON 데이터 생성
        Gson gson = new Gson();
        OrderData orderData = new OrderData("종합계좌번호", "계좌상품코드", "계좌비밀번호", 
                                             "상품번호", "주문구분", "주문수량", 
                                             "주문단가", "연락전화번호");
        String data = gson.toJson(orderData); // 객체를 JSON 문자열로 변환

        httpPostBodyConnection(url, data, tr_id);
    }

    // 주문 데이터 클래스
    static class OrderData {
        private String CANO;
        private String ACNT_PRDT_CD;
        private String ACNT_PWD;
        private String PDNO;
        private String ORD_DVSN;
        private String ORD_QTY;
        private String ORD_UNPR;
        private String CTAC_TLNO;

        public OrderData(String CANO, String ACNT_PRDT_CD, String ACNT_PWD, String PDNO, 
                         String ORD_DVSN, String ORD_QTY, String ORD_UNPR, String CTAC_TLNO) {
            this.CANO = CANO;
            this.ACNT_PRDT_CD = ACNT_PRDT_CD;
            this.ACNT_PWD = ACNT_PWD;
            this.PDNO = PDNO;
            this.ORD_DVSN = ORD_DVSN;
            this.ORD_QTY = ORD_QTY;
            this.ORD_UNPR = ORD_UNPR;
            this.CTAC_TLNO = CTAC_TLNO;
        }
    }

    public static void httpPostBodyConnection(String UrlData, String ParamData, String TrId) throws IOException {
        String totalUrl = UrlData.trim();
    
        URL url = new URL(totalUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("authorization", "Bearer {TOKEN}");
        conn.setRequestProperty("appKey", "{Client_ID}");
        conn.setRequestProperty("appSecret", "{Client_Secret}");
        conn.setRequestProperty("personalSeckey", "{personalSeckey}");
        conn.setRequestProperty("tr_id", TrId);
        conn.setRequestProperty("tr_cont", " ");
        conn.setRequestProperty("custtype", "법인(B), 개인(P)");
        conn.setRequestProperty("seq_no", "법인(01), 개인( )");
        conn.setRequestProperty("mac_address", "{Mac_address}");
        conn.setRequestProperty("phone_num", "P01011112222");
        conn.setRequestProperty("ip_addr", "{IP_addr}");
        conn.setRequestProperty("hashkey", "{Hash값}");
        conn.setRequestProperty("gt_uid", "{Global UID}");
        conn.setDoOutput(true);
    
        try (OutputStream os = conn.getOutputStream()) {
            byte[] request_data = ParamData.getBytes("utf-8");
            os.write(request_data);
        }
    
        // 응답 코드 출력
        int responseCode = conn.getResponseCode();
        System.out.println("http 응답 코드 : " + responseCode);
    
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String responseData;
        while ((responseData = br.readLine()) != null) {
            sb.append(responseData);
        }
        String returnData = sb.toString();
        System.out.println("http 응답 데이터 : " + returnData);
        
        br.close();
    }
}