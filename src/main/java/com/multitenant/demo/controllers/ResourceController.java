package com.multitenant.demo.controllers;

import com.multitenant.demo.service.ResourceService;
import com.multitenant.demo.dto.faculty.ResourceRequestDTO;
import com.multitenant.demo.dto.faculty.ResourceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/resource")
@Tag(name = "Resource API", description = "A sample REST Resource CRUD API's")
public class ResourceController {
    @Autowired
    private ResourceService service;

    @GetMapping(value = "/", produces = "application/json")
    @Operation(summary = "Get All Resources", description = "Returns all the Resources")
    @ApiResponse(responseCode = "200", description = "Fetched all the resources successfully")
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {
        return ResponseEntity.ok(service.getAllFaculities());
    }

    @PostMapping(value = "/", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Create a resource", description = "Create a sample resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ResourceResponseDTO> createResource(@RequestBody(description = "Create a new sample resource", required = true,
                                                    content = @Content(schema = @Schema(implementation = ResourceRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody ResourceRequestDTO resourceRequestDTO) {
        return ResponseEntity.ok(service.addFaculty(resourceRequestDTO));
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Update Resource by id", description = "Update the sample resource by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ResourceResponseDTO> updateResource(
            @Parameter(description = "id to update the Resource", required = true) @PathVariable("id") Long id,
            @RequestBody(description = "update a sample Resource", required = true,
                    content = @Content(schema = @Schema(implementation = ResourceRequestDTO.class))) @org.springframework.web.bind.annotation.RequestBody ResourceRequestDTO resourceRequestDTO) {
        return ResponseEntity.ok((service.updateFaculty(id, resourceRequestDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Resource by Id", description = "Delete sample resource by sample Id")
    @ApiResponse(responseCode = "200", description = "Resource deleted successfully")
    public ResponseEntity<String> deleteResource(@Parameter(description = "id to delete the resource", required = true)
                                                    @PathVariable("id") Long id) {
        service.deleteFaculty(id);
        return ResponseEntity.ok("Deleted Resource Successfully");
    }
}
