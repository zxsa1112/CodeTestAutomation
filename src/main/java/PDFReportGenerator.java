import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font; // 기본 폰트 임포트

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PDFReportGenerator {
    public static void main(String[] args) {
        String sarifFilePath = "results/java.sarif"; // SARIF 파일 경로
        String pdfFilePath = "generated_report.pdf"; // 생성할 PDF 파일 경로

        try {
            // SARIF 파일 읽기
            String sarifContent = new String(Files.readAllBytes(Paths.get(sarifFilePath)));

            // PDF 문서 생성
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // 페이지에 내용 추가
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12); // 기본 Helvetica 폰트 사용
            contentStream.newLineAtOffset(50, 700); // 텍스트 시작 위치 조정

            // SARIF 내용을 PDF에 추가 (필요에 따라 줄바꿈 추가)
            for (String line : sarifContent.split("\n")) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15); // 줄 간격 조정
            }
            
            contentStream.endText();
            contentStream.close();

            // PDF 파일 저장
            document.save(pdfFilePath);
            document.close();

            System.out.println("PDF report generated successfully: " + pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}