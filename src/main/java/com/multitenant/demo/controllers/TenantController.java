package com.multitenant.demo.controllers;

import com.multitenant.demo.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/1.0/tenant")
@Tag(name = "Multi Tenant API", description = "Api for creating, initiating and removing tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping()
    @Operation(summary = "Create new tenant and it's DB", description = "Create Tenant - Creates a new DB for the tenant with the tenantId as the name")
    @ApiResponse(responseCode = "200", description = "Created new tenant")
    public ResponseEntity<String> createTenant(@Parameter(description = "tenant id ", required = true)
                                    @RequestParam(name = "tenantId", required = true) String tenantId) {
        boolean success = tenantService.createTenant(tenantId);
        return success ? ResponseEntity.ok("Successfully created a tenant and it's DB") :
                (ResponseEntity<String>) ResponseEntity.internalServerError();
    }

    @DeleteMapping("/{tenantId}")
    @Operation(summary = "Deactivate Tenant", description = "Deactivates the tenant")
    @ApiResponse(responseCode = "200", description = "Deactivates the tenant, sets the active flag to false")
    public ResponseEntity<String> removeTenant(@Parameter(description = "tenantId to deactivate the tenant", required = true)
                                                       @PathVariable("tenantId") String tenantId) {
        boolean success = tenantService.removeTenant(tenantId);
        return success ? ResponseEntity.ok("Successfully deactivated the tenant") :
                (ResponseEntity<String>) ResponseEntity.internalServerError();
    }

    @PostMapping("/initiate")
    @Operation(summary = "Initiate tenant", description = "Initiates the tenant by creating the resource table(a sample table to showcase the multi-tenancy) in the tenant DB")
    @ApiResponse(responseCode = "200", description = "Initiated & Created Resource table for the tenant ")
    public ResponseEntity<String> initiateTenant(@Parameter(description = "tenant id to initiate and create sample table", required = true)
                                                         @RequestParam(name = "tenantId", required = true) String tenantId) {
        try {
            boolean success = tenantService.initializeTenantDatabase(tenantId);
            return success ? ResponseEntity.ok("Successfully initiated the tenant") :
                    (ResponseEntity<String>) ResponseEntity.internalServerError();
        } catch (SQLException e) {
            return (ResponseEntity<String>) ResponseEntity.internalServerError();
        }

    }
}
