import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.io.FileReader;

public class SarifToCsv {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java SarifToCsv <input_sarif_file> <output_csv_file>");
            return;
        }

        String sarifFilePath = args[0];
        String csvFilePath = args[1];

        try (FileReader reader = new FileReader(sarifFilePath);
             FileWriter csvWriter = new FileWriter(csvFilePath)) {

            JSONObject sarifJson = new JSONObject(new JSONTokener(reader));
            JSONArray runs = sarifJson.getJSONArray("runs");

            csvWriter.append("Rule,Message,Severity\n");

            for (int i = 0; i < runs.length(); i++) {
                JSONObject run = runs.getJSONObject(i);
                JSONArray results = run.getJSONArray("results");

                for (int j = 0; j < results.length(); j++) {
                    JSONObject result = results.getJSONObject(j);
                    String ruleId = result.getString("ruleId");
                    String message = result.getJSONObject("message").getString("text");
                    String severity = result.optString("level", "none");

                    csvWriter.append(ruleId).append(",");
                    csvWriter.append(message.replace("\n", " ").replace(",", " ")).append(",");
                    csvWriter.append(severity).append("\n");
                }
            }

            System.out.println("CSV file generated: " + csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
