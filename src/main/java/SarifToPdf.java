import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SarifToPdf {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java SarifToPdf <sarif_file_path> <output_pdf_path>");
            System.exit(1);
        }

        String sarifFilePath = args[0];
        String pdfFilePath = args[1];

        try {
            String sarifContent = new String(Files.readAllBytes(Paths.get(sarifFilePath)));
            JSONObject sarifJson = new JSONObject(sarifContent);
            List<String> summary = summarizeSarif(sarifJson);

            generatePdf(summary, pdfFilePath);
            System.out.println("PDF report generated successfully: " + pdfFilePath);
        } catch (IOException e) {
            System.err.println("Error occurred while processing the SARIF file or generating the PDF:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<String> summarizeSarif(JSONObject sarifJson) {
        List<String> summary = new ArrayList<>();
        summary.add("CodeQL Analysis Summary");
        summary.add("------------------------");

        JSONArray runs = sarifJson.getJSONArray("runs");
        for (int i = 0; i < runs.length(); i++) {
            JSONObject run = runs.getJSONObject(i);
            JSONArray results = run.getJSONArray("results");
            summary.add("Total issues found: " + results.length());
            summary.add("");

            for (int j = 0; j < results.length(); j++) {
                JSONObject result = results.getJSONObject(j);
                String ruleId = result.getJSONObject("rule").getString("id");
                String message = result.getJSONObject("message").getString("text");
                String location = result.getJSONArray("locations").getJSONObject(0)
                        .getJSONObject("physicalLocation").getJSONObject("artifactLocation").getString("uri");

                summary.add("Issue #" + (j + 1));
                summary.add("Rule: " + ruleId);
                summary.add("Message: " + message);
                summary.add("Location: " + location);
                summary.add("");
            }
        }

        return summary;
    }

    private static void generatePdf(List<String> content, String pdfFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);

            for (String line : content) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.endText();
        }

        document.save(pdfFilePath);
        document.close();
    }
}