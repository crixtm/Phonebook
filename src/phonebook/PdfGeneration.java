package phonebook;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PdfGeneration {
    void pdfGeneration(String fileName, String test_text) {
        Document document= new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName + ".pdf"));
            document.open();
            //the following block occurs exception, I don;t know why
//            Image image = Image.getInstance((getClass().getResource("/logo.jpg")));
//            image.scaleToFit(200, 86);
//            image.setAbsolutePosition(200f, 750f);
//            document.add(image);
            
            document.add(new Paragraph(
                    "\n\n\n\n\n\n\n\n" + test_text, FontFactory.getFont("font", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)));
            Chunk signature = new Chunk("generated with that contacts");
            Paragraph base = new Paragraph(signature);
            document.add(base);
            
            
        } catch (Exception e){
            e.printStackTrace();
        }
        document.close();
    }
}
