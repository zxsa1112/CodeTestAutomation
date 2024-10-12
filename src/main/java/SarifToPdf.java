import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
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
        summary.add("CodeQL 분석 요약");
        summary.add("------------------------");

        JSONArray runs = sarifJson.getJSONArray("runs");
        for (int i = 0; i < runs.length(); i++) {
            JSONObject run = runs.getJSONObject(i);
            JSONArray results = run.getJSONArray("results");
            summary.add("발견된 총 문제 수: " + results.length());
            summary.add("");

            for (int j = 0; j < results.length(); j++) {
                JSONObject result = results.getJSONObject(j);
                String ruleId = result.getJSONObject("rule").getString("id");
                String message = result.getJSONObject("message").getString("text");
                String location = result.getJSONArray("locations").getJSONObject(0)
                        .getJSONObject("physicalLocation").getJSONObject("artifactLocation").getString("uri");

                summary.add("문제 #" + (j + 1));
                summary.add("규칙: " + translateRule(ruleId));
                summary.add("설명: " + translateMessage(message));
                summary.add("문제가 발생한 위치: " + location);
                summary.add("권장 조치: " + suggestAction(ruleId));
                summary.add("");
            }
        }

        return summary;
    }

    private static String translateRule(String ruleId) {
        switch (ruleId) {
            case "java/sql-injection":
                return "SQL 인젝션 취약점";
            case "java/xss":
                return "크로스 사이트 스크립팅 (XSS) 취약점";
            default:
                return "알 수 없는 규칙: " + ruleId;
        }
    }

    private static String translateMessage(String message) {
        return "이 코드는 보안 취약점이 있을 수 있습니다. " + message;
    }

    private static String suggestAction(String ruleId) {
        switch (ruleId) {
            case "java/sql-injection":
                return "데이터베이스 쿼리에 사용자 입력을 직접 사용하지 말고, 준비된 구문(Prepared Statement)을 사용하세요.";
            case "java/xss":
                return "사용자 입력을 화면에 출력할 때는 반드시 특수 문자를 이스케이프 처리하세요.";
            default:
                return "코드를 검토하고 보안 전문가와 상담하세요.";
        }
    }

    private static void generatePdf(List<String> content, String pdfFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // 파일 객체로 맑은 고딕 폰트 로드
        PDTrueTypeFont font = PDTrueTypeFont.loadTTF(document, new File("resources/fonts/MALGUN.TTF"));

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(font, 12); // 맑은 고딕 폰트 사용
            contentStream.newLineAtOffset(50, 700);

            for (String line : content) {
                contentStream.showText(line); // 텍스트 추가
                contentStream.newLineAtOffset(0, -15); // 줄 간격 조정
            }

            contentStream.endText(); // 텍스트 블록 종료
        }

        document.save(pdfFilePath); // PDF 파일 저장
        document.close(); // 문서 닫기
    }
}