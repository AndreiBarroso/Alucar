package tcc.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.val;
import org.springframework.stereotype.Service;
import tcc.service.ReportService;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class ReportServiceImpl extends PdfPageEventHelper implements ReportService {

    @Override
    public ByteArrayInputStream createPDFReport(List<String> data, String reportName, String[] columnNames, BaseColor backgroundColor) throws DocumentException {
        val out = new ByteArrayOutputStream();
        val document = new Document(PageSize.A4.rotate(), 0, 0, 2, 2);
        val table = new PdfPTable(columnNames.length);
        try {
            // Set table properties
            table.setWidthPercentage(99);
            table.setSpacingBefore(0.8f);
            table.setSpacingAfter(0.8f);

            // Add the Logo and Title to the report
            val header = createPdfHeaderLogoAndTitle(reportName);

            // Create the table Header
            addTableHeader(table, columnNames, backgroundColor);

            // Add data to the table
            addRowsToReport(data, table);

            // Creates document
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(header);
            document.add(table);
            document.close();
        } catch(DocumentException e) {
            throw new ExceptionConverter(e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPTable createPdfHeaderLogoAndTitle(String reportName) {
        PdfPTable header = new PdfPTable(2);
        try {
            // set defaults
            header.setWidths(new int[]{30, 24});
            header.setTotalWidth(530);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(80);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
            header.getDefaultCell().setPaddingBottom(20);

            // add image
            Image logo = Image.getInstance(ReportServiceImpl.class.getResource("/files/logo.jpeg"));
            logo.setWidthPercentage(30);

            logo.setLeft(2);
            logo.setBottom(10);
            header.addCell(logo);

            // add text
            PdfPCell text = new PdfPCell();
            text.setPaddingBottom(30);
            text.setPaddingLeft(0);
            text.setBorder(Rectangle.BOTTOM);
            text.setBorderColor(BaseColor.LIGHT_GRAY);
            text.addElement(new Phrase("Alucar - Aluguel de Veículos. Relatório de " + reportName, new Font(Font.FontFamily.HELVETICA, 12)));
            text.addElement(new Phrase("https://alucar.com.br", new Font(Font.FontFamily.HELVETICA, 8)));
            header.addCell(text);

            header.addCell(logo);
        } catch(IOException | DocumentException e) {
            throw new ExceptionConverter(e);
        }

        return header;
    }

    private void addTableHeader(PdfPTable table, String[] columns, BaseColor backgroundColor) {
        val header = new PdfPCell();
        Stream.of(columns).forEach(column -> {
            header.setBackgroundColor(backgroundColor);
            header.setBorderWidth(1);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPhrase(new Phrase(column));
            table.addCell(header);
        });
    }

    private void addRowsToReport(List<String> data, PdfPTable table) {
        data.forEach(table::addCell);
    }
}
