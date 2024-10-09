import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class qwerqwer {
    public static void main(String[] args) throws IOException {

        // 매수가능 조회
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/trading/inquire-psbl-order";
        String tr_id = "TTTC8908R";
        String data = "{\n" +
                "    \"CANO\": \"종합계좌번호\",\n" +
                "    \"ACNT_PRDT_CD\": \"계좌상품코드\",\n" +
                "    \"ACNT_PWD\": \"계좌비밀번호\",\n" +
                "    \"PDNO\": \"상품번호\",\n" +
                "    \"ORD_UNPR\": \"주문단가\",\n" +
                "    \"ORD_DVSN\": \"주문구분\",\n" +
                "    \"CMA_EVLU_AMT_ICLD_YN\": \"CMA평가금액포함여부\",\n" +
                "    \"OVRS_ICLD_YN\": \"해외포함여부\"\n" +
                "}";
        httpPostBodyConnection(url,data,tr_id);
    }
    public static void httpPostBodyConnection(String UrlData, String ParamData,String TrId) throws IOException {
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        URL url = null;
        HttpURLConnection co = null;

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
              String responseCode = Sring.valueOf(conn.getResponseCode());
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