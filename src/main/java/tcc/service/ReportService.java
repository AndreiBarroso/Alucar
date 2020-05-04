package tcc.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ReportService {
    /**
     * Creates pdf reports for Membros
     *
     * @param data the table to print
     * @param columnNames a String array with the columns names
     * @return pdf report
     * @throws DocumentException Class for communicating failures that occur the implementation is manipulating the
     *                           underlying documents. Such failures include errors during parsing, addition of new
     *                           elements, etc.
     */
    ByteArrayInputStream createPDFReport(List<String> data, String reportName, String[] columnNames, BaseColor backgroundColor) throws DocumentException;

}
