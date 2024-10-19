import java.io.File; // 파일을 다루기 위한 클래스
import java.io.IOException; // 입출력 관련 예외 처리를 위한 클래스

import com.fasterxml.jackson.databind.JsonNode; // JSON 데이터를 나타내는 클래스
import com.fasterxml.jackson.databind.ObjectMapper; // JSON을 Java 객체로 변환하는 클래스

public class JsonReader { // JsonReader 클래스 정의
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper(); // JSON 처리를 위한 ObjectMapper 객체 생성
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("results/sarif-results.sarif"));  // JSON 파일을 읽어 JsonNode 객체로 변환
            jsonNode.get("runs").forEach(run -> {   // JSON 데이터 처리 시작 runs 이라는 키에 해당하는 배열을 반복
                run.get("results").forEach(result -> {  // 각 run의 results 배열을 반복
                    System.out.println("Rule ID: " + result.get("ruleId").asText());    // 발견된 문제의 규칙 ID 출력
                    System.out.println("Message: " + result.get("message").get("text").asText());   // 발견된 문제의 메시지 출력
                });
            });
        } catch (IOException e) { // 예외가 발생할 경우
            e.printStackTrace(); // 에러 메시지를 출력
        }
    }
}