package com.multitenant.demo.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.multitenant.demo.annotations.PDFElement;
import com.multitenant.demo.database.entities.*;
import com.multitenant.demo.database.repositories.FacultyRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    FacultyRepository facultyRepository;

    public void generateFacultiesReport(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=students_report.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Faculties Report"));
        document.add(new Paragraph("Date: " + java.time.LocalDate.now()));
        document.add(new Paragraph("\n"));

        // Create a table with 5 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        // Set custom column widths
        float[] columnWidths = {2f, 2f, 2.5f, 2f};
        table.setWidths(columnWidths);

        Field[] fields = Resource.class.getDeclaredFields();
        for (Field field: fields) {
            if (field.isAnnotationPresent(PDFElement.class)) {
                PDFElement annotation = field.getAnnotation(PDFElement.class);
                String header = annotation.header().isEmpty() ? field.getName() : annotation.header();
                table.addCell(createHeaderWithHeight(header));
            }
        }

        List<Resource> faculties = facultyRepository.findAll();
        for (Resource resource : faculties) {

            for (Field field : fields) {
                if (field.isAnnotationPresent(PDFElement.class)) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(resource);
                        if (value != null) {
                            table.addCell(createCellWithHeight(value.toString()));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        document.add(table);
        document.close();
    }

    private PdfPCell createCellWithHeight(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setFixedHeight(30f);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell createHeaderWithHeight(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setFixedHeight(30f);
        return cell;
    }
}
