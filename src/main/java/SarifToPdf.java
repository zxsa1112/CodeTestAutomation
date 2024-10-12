import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font; // 기본 폰트 임포트
import org.json.JSONArray;
import org.json.JSONObject;

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
            summary.add("Total Issues Found: " + results.length());
            summary.add("");

            for (int j = 0; j < results.length(); j++) {
                JSONObject result = results.getJSONObject(j);
                String ruleId = result.getJSONObject("rule").getString("id");
                String message = result.getJSONObject("message").getString("text");
                String location = result.getJSONArray("locations").getJSONObject(0)
                        .getJSONObject("physicalLocation").getJSONObject("artifactLocation").getString("uri");

                summary.add("Issue #" + (j + 1));
                summary.add("Rule: " + translateRule(ruleId));
                summary.add("Description: " + translateMessage(message));
                summary.add("Location: " + location);
                summary.add("Recommended Action: " + suggestAction(ruleId));
                summary.add("");
            }
        }

        return summary;
    }

    private static String translateRule(String ruleId) {
        switch (ruleId) {
            case "java/sql-injection":
                return "SQL Injection Vulnerability";
            case "java/xss":
                return "Cross-Site Scripting (XSS) Vulnerability";
            default:
                return "Unknown Rule: " + ruleId;
        }
    }

    private static String translateMessage(String message) {
        return "This code may have security vulnerabilities: " + message;
    }

    private static String suggestAction(String ruleId) {
        switch (ruleId) {
            case "java/sql-injection":
                return "Do not use user input directly in database queries; use prepared statements instead.";
            case "java/xss":
                return "Always escape special characters when outputting user input to the screen.";
            default:
                return "Review the code and consult with a security expert.";
        }
    }

    private static void generatePdf(List<String> content, String pdfFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA, 12); // 기본 Helvetica 폰트 사용
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);

            for (String line : content) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.endText(); // 텍스트 블록 종료
        }

        document.save(pdfFilePath);
        document.close();
    }
}