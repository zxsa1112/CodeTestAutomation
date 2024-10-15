import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class SarifToCsv {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java SarifToCsv <input.sarif> <output.csv>");
            return;
        }

        String inputSarifFile = args[0]; // SARIF 파일 경로
        String outputCsvFile = args[1];  // CSV 파일 경로

        try (BufferedReader reader = new BufferedReader(new FileReader(inputSarifFile));
             FileWriter writer = new FileWriter(outputCsvFile)) {

            // SARIF 파일 읽기
            StringBuilder sarifContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sarifContent.append(line);
            }

            // JSON 파싱
            JSONObject sarifJson = new JSONObject(sarifContent.toString());
            JSONArray runs = sarifJson.getJSONArray("runs");

            // CSV 헤더 작성
            writer.append("RuleID,Message,File,StartLine\n");

            // SARIF 데이터에서 유의미한 정보 추출
            for (int i = 0; i < runs.length(); i++) {
                JSONArray results = runs.getJSONObject(i).getJSONArray("results");

                for (int j = 0; j < results.length(); j++) {
                    JSONObject result = results.getJSONObject(j);

                    // RuleID
                    String ruleId = result.getString("ruleId");

                    // Message
                    String message = result.getJSONObject("message").getString("text");

                    // File 및 Line 정보
                    JSONArray locations = result.getJSONArray("locations");
                    String file = locations.getJSONObject(0)
                                           .getJSONObject("physicalLocation")
                                           .getJSONObject("artifactLocation")
                                           .getString("uri");
                    int startLine = locations.getJSONObject(0)
                                             .getJSONObject("physicalLocation")
                                             .getJSONObject("region")
                                             .getInt("startLine");

                    // CSV에 쓰기
                    writer.append(ruleId).append(',')
                          .append(message.replace(",", " ")).append(',')
                          .append(file).append(',')
                          .append(String.valueOf(startLine)).append('\n');
                }
            }

            System.out.println("CSV file generated: " + outputCsvFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}