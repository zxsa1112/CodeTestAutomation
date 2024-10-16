import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class SarifToJson {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("SARIF 파일 경로를 입력하세요.");
            return;
        }

        String sarifFilePath = args[0];
        try {
            // SARIF 파일 읽기
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode sarifData = objectMapper.readTree(new File(sarifFilePath));

            // SARIF 데이터 파싱 및 주요 정보 추출
            JsonNode runs = sarifData.get("runs");
            if (runs != null && runs.isArray()) {
                for (JsonNode run : runs) {
                    JsonNode results = run.get("results");
                    if (results != null && results.isArray()) {
                        for (JsonNode result : results) {
                            String ruleId = result.get("ruleId").asText();
                            String message = result.get("message").get("text").asText();
                            System.out.println("Rule ID: " + ruleId);
                            System.out.println("Message: " + message);
                        }
                    }
                }
            }

            // 필요한 경우 데이터를 JSON으로 변환 후 출력
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sarifData);
            System.out.println(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}