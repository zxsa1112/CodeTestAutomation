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
        // 기존 코드 유지...
        // 이 부분은 그대로 두셔도 됩니다.
    }
}
