import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TTTC8001R {
    public static void main(String[] args) throws IOException {

        // 계좌주문 체결내역 조회
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/trading/inquire-daily-ccld";
        String tr_id = "TTTC8001R";
        String data = "{\n" +
                "    \"CANO\": \"종합계좌번호\",\n" +
                "    \"ACNT_PRDT_CD\": \"계좌상품코드\",\n" +
                "    \"PWD\": \"비밀번호\",\n" +
                "    \"INQR_STRT_DT\": \"조회시작일자\",\n" +
                "    \"INQR_END_DT\": \"조회종료일자\",\n" +
                "    \"SLL_BUY_DVSN_CD\": \"매도매수구분코드\",\n" +
                "    \"INQR_DVSN\": \"조회구분\",\n" +
                "    \"PDNO\": \"상품번호\",\n" +
                "    \"CCLD_DVSN\": \"체결구분\",\n" +
                "    \"ORD_GNO_BRNO\": \"주문채번지점번호\",\n" +
                "    \"ODNO\": \"주문번호\",\n" +
                "    \"INQR_DVSN_3\": \"조회구분3\",\n" +
                "    \"INQR_DVSN_1\": \"조회구분1\",\n" +
                "    \"CTX_AREA_FK100\": \"연속조회검색조건100\",\n" +
                "    \"CTX_AREA_NK100\": \"연속조회키100\"\n" +
                "}";
        httpPostBodyConnection(url,data,tr_id);
    }
    public static void httpPostBodyConnection(String UrlData, String ParamData,String TrId) throws IOException {
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        URL url = null;
        HttpURLConnection conn = null;

        String responseData = "";
        BufferedReader br = null;

        StringBuffer sb = new StringBuffer();
        String returnData = "";

      try{
          url = new URL(totalUrl);
          conn = (HttpURLConnection) url.openConnection();
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
            byte request_data[] = ParamData.getBytes("utf-8");
            os.write(request_data);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        conn.connect();
        System.out.println("http 요청 방식" + "POST BODY JSON");
        System.out.println("http 요청 타입" + "application/json");
        System.out.println("http 요청 주소" + UrlData);
        System.out.println("");

        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      } catch (IOException e){
          br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
      } finally {
          try {
              sb = new StringBuffer();
              while ((responseData = br.readLine()) != null) {
                  sb.append(responseData);
              }
              returnData = sb.toString();
              String responseCode = String.valueOf(conn.getResponseCode());
              System.out.println("http 응답 코드 : " + responseCode);
              System.out.println("http 응답 데이터 : " + returnData);
              if (br != null){
                  br.close();
              }
          } catch (IOException e){
              throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
          }
      }
    }
}