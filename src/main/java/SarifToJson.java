import java.io.File; // 파일 관련 작업을 위한 클래스
import java.io.FileNotFoundException; // 파일을 찾을 수 없는 예외 처리를 위한 클래스
import java.io.IOException; // 입출력 관련 예외 처리를 위한 클래스

import com.fasterxml.jackson.databind.JsonNode; // JSON 데이터 구조를 표현하는 클래스
import com.fasterxml.jackson.databind.ObjectMapper; // JSON 데이터를 Java 객체로 변환하는 클래스

public class SarifToJson { // SarifToJson 클래스 정의
    public static void main(String[] args) { // 프로그램의 진입점, main 메서드 시작
        // SARIF 파일 경로가 제공되지 않은 경우 사용자에게 알림
        if (args.length == 0) { // 명령줄 인자가 없으면
            System.out.println("SARIF 파일 경로를 입력하세요."); // 경고 메시지 출력
            return; // 프로그램 종료
        }

        // 제공된 첫 번째 인자를 SARIF 파일 경로로 설정
        String sarifFilePath = args[0]; // SARIF 파일 경로
        File sarifFile = new File(sarifFilePath); // 파일 객체 생성
        // 파일 존재 여부 확인
        if (!sarifFile.exists()) { // 파일이 존재하지 않으면
            System.out.println("지정된 SARIF 파일이 존재하지 않습니다: " + sarifFilePath); // 경고 메시지 출력
            return; // 프로그램 종료
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper 객체 생성
            JsonNode sarifData = objectMapper.readTree(sarifFile); // SARIF 파일을 읽어 JsonNode로 변환

            JsonNode runs = sarifData.get("runs"); // SARIF 데이터에서 runs 노드 가져오기
            // runs 노드가 배열인지 확인
            if (runs != null && runs.isArray()) { // runs가 null이 아니고 배열인 경우
                for (JsonNode run : runs) { // 각 run 노드에 대해
                    JsonNode results = run.get("results"); // 현재 run의 results 노드 가져오기
                    // results 노드가 배열인지 확인
                    if (results != null && results.isArray()) { // results가 null이 아니고 배열인 경우
                        for (JsonNode result : results) { // 각 result에 대해
                            // ruleId와 message를 가져오고 없으면 "N/A"로 설정
                            String ruleId = result.has("ruleId") ? result.get("ruleId").asText() : "N/A"; // ruleId 가져오기
                            String message = result.has("message") && result.get("message").has("text") 
                                ? result.get("message").get("text").asText() : "N/A"; // message 가져오기
                            
                            // ruleId와 message 출력
                            System.out.println("Rule ID: " + ruleId); // ruleId 출력
                            System.out.println("Message: " + message); // message 출력
                        }
                    }
                }
            }

            // SARIF 데이터를 예쁘게 출력하기 위한 JSON 문자열 변환
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(sarifData); // JSON 문자열로 변환
            System.out.println(jsonString); // 변환된 JSON 문자열 출력

        } catch (FileNotFoundException e) { // 파일을 찾을 수 없는 경우
            System.out.println("파일을 찾을 수 없습니다: " + sarifFilePath); // 경고 메시지 출력
        } catch (IOException e) { // 입출력 오류 발생 시
            System.out.println("입출력 오류가 발생했습니다: " + e.getMessage()); // 오류 메시지 출력
            e.printStackTrace(); // 오류의 스택 트레이스 출력
        }
    }
}