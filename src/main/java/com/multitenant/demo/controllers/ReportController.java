package com.multitenant.demo.controllers;

import com.itextpdf.text.DocumentException;
import com.multitenant.demo.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Tag(name = "Reports API", description = "Reports Apis to create pdf")
public class ReportController {
    @Autowired
    private ReportService service;

    @GetMapping("/report/faculties")
    @Operation(summary = "Report for faculties", description = "Generating PDF report for faculties")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created pdf"),
            @ApiResponse(responseCode = "503", description = "Internal Server Error")
    })
    public void generateFacultiesReport(HttpServletResponse response) {
        try {
            service.generateFacultiesReport(response);
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
