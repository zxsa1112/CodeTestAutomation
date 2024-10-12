import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SarifToPdf {
    public static void main(String[] args) {
        // SARIF 파일 경로
        String sarifFilePath = System.getenv("SARIF_FILE");
        String pdfFilePath = "build/reports/pdf/report.pdf"; // PDF 파일 경로

        try {
            String sarifContent = new String(Files.readAllBytes(Paths.get(sarifFilePath)));

            // PDF 문서 생성
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 750);
            contentStream.showText(sarifContent); // SARIF 내용 추가
            contentStream.endText();
            contentStream.close();

            document.save(pdfFilePath);
            document.close();
            System.out.println("PDF generated successfully: " + pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}