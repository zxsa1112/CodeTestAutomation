import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("path/to/your/file.json"));
            // JSON 데이터 처리
            // 예를 들어, 각 발견된 문제를 출력
            jsonNode.get("runs").forEach(run -> {
                run.get("results").forEach(result -> {
                    System.out.println("Rule ID: " + result.get("ruleId").asText());
                    System.out.println("Message: " + result.get("message").get("text").asText());
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
