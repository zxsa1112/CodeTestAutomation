import java.io.IOException; // PDF 문서 모델을 위한 클래스
import java.io.InputStream; // PDF 페이지 모델을 위한 클래스
import java.nio.charset.StandardCharsets; // 페이지에 내용을 추가하는 클래스
import java.nio.file.Files; // 사용자 정의 폰트를 위한 클래스
import java.nio.file.Paths; // JSON 배열을 다루기 위한 클래스
import java.util.ArrayList; // JSON 객체를 다루기 위한 클래스
import java.util.HashSet; // 예외 처리를 위한 클래스
import java.util.List; // 입력 스트림을 다루기 위한 클래스

import org.apache.pdfbox.pdmodel.PDDocument; // 문자 인코딩을 위한 클래스
import org.apache.pdfbox.pdmodel.PDPage; // 파일 작업을 위한 클래스
import org.apache.pdfbox.pdmodel.PDPageContentStream; // 파일 경로 작업을 위한 클래스
import org.apache.pdfbox.pdmodel.font.PDType0Font; // 동적 배열을 위한 클래스
import org.json.JSONArray; // 중복 방지를 위한 해시 세트 클래스
import org.json.JSONObject; // 리스트 인터페이스

public class SarifToPdf { // SarifToPdf 클래스 정의
    public static void main(String[] args) { // 프로그램의 진입점, main 메서드 시작
        // 프로그램 실행 시 필요한 인자 수 확인
        if (args.length < 2) { // 인자가 2개 미만일 경우
            System.err.println("사용법: java SarifToPdf <sarif_file_path> <output_pdf_path>"); // 사용법 출력
            System.exit(1); // 잘못된 사용법일 경우 프로그램 종료
        }

        // SARIF 파일 경로와 출력 PDF 파일 경로를 인자에서 읽어오기
        String sarifFilePath = args[0]; // SARIF 파일 경로
        String pdfFilePath = args[1]; // 출력 PDF 파일 경로

        try {
            // SARIF 파일을 UTF-8 인코딩으로 읽어 문자열로 변환
            String sarifContent = new String(Files.readAllBytes(Paths.get(sarifFilePath)), StandardCharsets.UTF_8); 
            // SARIF JSON 객체 생성
            JSONObject sarifJson = new JSONObject(sarifContent); // JSON 문자열을 JSONObject로 변환
            // SARIF 내용을 요약
            List<String> summary = summarizeSarif(sarifJson); // 요약 생성 메서드 호출

            // 요약 내용을 기반으로 PDF 생성
            generatePdf(summary, pdfFilePath); // PDF 생성 메서드 호출
            System.out.println("PDF 보고서가 성공적으로 생성되었습니다: " + pdfFilePath); // 성공 메시지 출력
        } catch (IOException e) { // 예외 발생 시 처리
            System.err.println("SARIF 파일 처리 또는 PDF 생성 중 오류가 발생했습니다:"); // 오류 메시지 출력
            e.printStackTrace(); // 스택 트레이스 출력
            System.exit(1); // 오류 발생 시 종료
        }
    }

    // SARIF JSON 객체를 요약하는 메서드
    private static List<String> summarizeSarif(JSONObject sarifJson) {
        List<String> summary = new ArrayList<>(); // 요약을 담을 리스트 생성
        summary.add("CodeQL 분석 요약"); // 요약 제목 추가
        summary.add("------------------------"); // 구분선 추가

        JSONArray runs = sarifJson.getJSONArray("runs"); // 'runs' 배열 가져오기
        // 중복 방지를 위한 Set 사용
        HashSet<String> uniqueProblems = new HashSet<>(); // 중복 문제를 저장할 해시 세트
        int totalIssues = 0; // 총 문제 수 추적
        int duplicateCount = 0; // 중복 문제 수 추적

        for (int i = 0; i < runs.length(); i++) { // 각 run에 대해 반복
            JSONObject run = runs.getJSONObject(i); // 현재 run 객체 가져오기
            JSONArray results = run.getJSONArray("results"); // 'results' 배열 가져오기
            totalIssues += results.length(); // 발견된 문제 수 추가

            for (int j = 0; j < results.length(); j++) { // 각 result에 대해 반복
                JSONObject result = results.getJSONObject(j); // 현재 result 객체 가져오기
                String ruleId = result.getJSONObject("rule").getString("id"); // 규칙 ID 가져오기
                String message = result.getJSONObject("message").getString("text"); // 메시지 가져오기
                String location = result.getJSONArray("locations").getJSONObject(0) // 위치 가져오기
                        .getJSONObject("physicalLocation").getJSONObject("artifactLocation").getString("uri");

                // 중복 확인을 위한 문자열 생성
                String problemDescription = ruleId + message + location; // 문제 설명 생성

                // 중복 검사
                if (uniqueProblems.add(problemDescription)) { // Set에 추가하고 성공 여부를 체크
                    summary.add("문제 #" + (uniqueProblems.size())); // 문제 번호 업데이트
                    summary.add("규칙: " + translateRule(ruleId)); // 규칙 번역
                    summary.add("설명: " + translateMessage(message)); // 메시지 번역
                    summary.add("위치: " + location); // 위치 추가
                    summary.add("권장 조치: " + suggestAction(ruleId)); // 권장 조치 추가
                    summary.add(""); // 빈 줄 추가
                } else {
                    duplicateCount++; // 중복 문제 수 증가
                }
            }
        }

        // 총 발견된 문제 수와 중복 문제 수 출력
        summary.add("발견된 총 문제 수: " + totalIssues); // 전체 문제 수 출력
        summary.add("중복 문제 수: " + duplicateCount); // 중복 문제 수 출력
        if (duplicateCount > 0) { // 중복 문제 수가 0보다 클 경우
            summary.add("(같은 문제로 인한 중복 제거로 인해 " + duplicateCount + "개의 문제가 제외되었습니다.)"); // 중복 제거 안내
        }

        return summary; // 요약 리스트 반환
    }

    // 규칙 ID를 번역하는 메서드
    private static String translateRule(String ruleId) {
        switch (ruleId) { // 규칙 ID에 따른 처리
            case "java/sql-injection":
                return "SQL 인젝션 취약점"; // SQL 인젝션 규칙 번역
            case "java/xss":
                return "크로스 사이트 스크립팅 (XSS) 취약점"; // XSS 규칙 번역
            default:
                return "알 수 없는 규칙: " + ruleId; // 알 수 없는 규칙 처리
        }
    }

    // 메시지를 번역하는 메서드
    private static String translateMessage(String message) {
        return "이 코드에 보안 취약점이 있을 수 있습니다: " + message; // 메시지 포맷
    }

    // 규칙 ID에 따른 권장 조치를 제안하는 메서드
    private static String suggestAction(String ruleId) {
        switch (ruleId) { // 규칙 ID에 따른 처리
            case "java/sql-injection":
                return "데이터베이스 쿼리에 사용자 입력을 직접 사용하지 마세요; 대신 준비된 구문을 사용하세요."; // SQL 인젝션에 대한 권장 조치
            case "java/xss":
                return "사용자 입력을 화면에 출력할 때 항상 특수 문자를 이스케이프 처리하세요."; // XSS에 대한 권장 조치
            default:
                return "코드를 검토하고 보안 전문가와 상담하세요."; // 일반 권장 조치
        }
    }

    // PDF 문서를 생성하는 메서드
    private static void generatePdf(List<String> content, String pdfFilePath) throws IOException {
        PDDocument document = new PDDocument(); // 새로운 PDF 문서 객체 생성
        PDPage page = new PDPage(); // 새로운 페이지 생성
        document.addPage(page); // 문서에 페이지 추가

        // 프로젝트 내의 리소스 폴더에서 맑은 고딕 폰트 파일 로드
        try (InputStream fontStream = SarifToPdf.class.getResourceAsStream("/fonts/MALGUN.TTF")) {
            if (fontStream == null) { // 폰트 파일이 없을 경우 예외 처리
                throw new IOException("폰트 파일을 찾을 수 없습니다."); // 예외 발생
            }
            PDType0Font font = PDType0Font.load(document, fontStream); // 폰트 로드

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) { // 콘텐츠 스트림 생성
                contentStream.setFont(font, 12); // 폰트 설정
                contentStream.beginText(); // 텍스트 추가 시작
                contentStream.newLineAtOffset(50, 700); // 텍스트 시작 위치 조정

                // 요약 내용을 페이지에 추가
                for (String line : content) {
                    contentStream.showText(line); // 텍스트 추가
                    contentStream.newLineAtOffset(0, -15); // 줄 간격 조정
                }

                contentStream.endText(); // 텍스트 추가 종료
            }
        }

        document.save(pdfFilePath); // PDF 파일 저장
        document.close(); // 문서 객체 닫기
    }
}