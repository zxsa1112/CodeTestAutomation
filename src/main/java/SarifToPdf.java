import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SarifToPdf {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("사용법: java SarifToPdf <sarif_file_path> <output_pdf_path>");
            System.exit(1);
        }

        String sarifFilePath = args[0];
        String pdfFilePath = args[1];

        try {
            String sarifContent = new String(Files.readAllBytes(Paths.get(sarifFilePath)), StandardCharsets.UTF_8);
            JSONObject sarifJson = new JSONObject(sarifContent);
            List<String> summary = summarizeSarif(sarifJson);

            generatePdf(summary, pdfFilePath);
            System.out.println("PDF 보고서가 성공적으로 생성되었습니다: " + pdfFilePath);
        } catch (IOException e) {
            System.err.println("SARIF 파일 처리 또는 PDF 생성 중 오류가 발생했습니다:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<String> summarizeSarif(JSONObject sarifJson) {
        List<String> summary = new ArrayList<>();
        summary.add("CodeQL 분석 요약");
        summary.add("------------------------");
    
        JSONArray runs = sarifJson.getJSONArray("runs");
        // 중복 방지를 위한 Set 사용
        HashSet<String> uniqueProblems = new HashSet<>();
        int totalIssues = 0; // 총 문제 수 추적
        int duplicateCount = 0; // 중복 문제 수 추적
    
        for (int i = 0; i < runs.length(); i++) {
            JSONObject run = runs.getJSONObject(i);
            JSONArray results = run.getJSONArray("results");
            totalIssues += results.length(); // 발견된 문제 수 추가
    
            for (int j = 0; j < results.length(); j++) {
                JSONObject result = results.getJSONObject(j);
                String ruleId = result.getJSONObject("rule").getString("id");
                String message = result.getJSONObject("message").getString("text");
                String location = result.getJSONArray("locations").getJSONObject(0)
                        .getJSONObject("physicalLocation").getJSONObject("artifactLocation").getString("uri");
    
                String problemDescription = ruleId + message + location; // 중복 확인을 위한 문자열 생성
    
                // 중복 검사
                if (uniqueProblems.add(problemDescription)) { // Set에 추가하고 성공 여부를 체크
                    summary.add("문제 #" + (uniqueProblems.size())); // 문제 번호 업데이트
                    summary.add("규칙: " + translateRule(ruleId));
                    summary.add("설명: " + translateMessage(message));
                    summary.add("위치: " + location);
                    summary.add("권장 조치: " + suggestAction(ruleId));
                    summary.add("");
                } else {
                    duplicateCount++; // 중복 문제 수 증가
                }
            }
        }
    
        summary.add("발견된 총 문제 수: " + totalIssues); // 전체 문제 수 출력
        summary.add("중복 문제 수: " + duplicateCount); // 중복 문제 수 출력
        if (duplicateCount > 0) {
            summary.add("(같은 문제로 인한 중복 제거로 인해 " + duplicateCount + "개의 문제가 제외되었습니다.)");
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
        return "이 코드에 보안 취약점이 있을 수 있습니다: " + message;
    }

    private static String suggestAction(String ruleId) {
        switch (ruleId) {
            case "java/sql-injection":
                return "데이터베이스 쿼리에 사용자 입력을 직접 사용하지 마세요; 대신 준비된 구문을 사용하세요.";
            case "java/xss":
                return "사용자 입력을 화면에 출력할 때 항상 특수 문자를 이스케이프 처리하세요.";
            default:
                return "코드를 검토하고 보안 전문가와 상담하세요.";
        }
    }

    private static void generatePdf(List<String> content, String pdfFilePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // 프로젝트 내의 리소스 폴더에서 맑은 고딕 폰트 파일 로드
        try (InputStream fontStream = SarifToPdf.class.getResourceAsStream("/fonts/MALGUN.TTF")) {
            if (fontStream == null) {
                throw new IOException("폰트 파일을 찾을 수 없습니다.");
            }
            PDType0Font font = PDType0Font.load(document, fontStream);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);

                for (String line : content) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15);
                }

                contentStream.endText();
            }
        }

        document.save(pdfFilePath);
        document.close();
    }
}