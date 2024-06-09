package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.casetemplate.CaseTemplateDTO;
import com.itesm.panoptimize.dto.casetemplate.CreateCaseTemplateDTO;
import com.itesm.panoptimize.dto.casetemplate.UpdateCaseTemplateDTO;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.service.CaseTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/case-template")
public class CaseTemplateController {

    private final CaseTemplateService caseTemplateService;

    public CaseTemplateController(CaseTemplateService caseTemplateService) {
        this.caseTemplateService = caseTemplateService;
    }

    @Operation(summary = "Get case template by id", description = "This GET request call serves the purpose of returning a case template by its id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Case template found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaseTemplateDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Case template not found.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CaseTemplateDTO> getCaseTemplate(@PathVariable Integer id) {
        CaseTemplateDTO caseTemplateDTO = caseTemplateService.getCaseTemplate(id);
        if (caseTemplateDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(caseTemplateDTO);
    }

    @Operation(summary = "Get page of case templates", description = "This GET request call serves the purpose of returning the case template." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Case template found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaseTemplateDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Case template not found.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<CaseTemplateDTO>> getCaseTemplates(Pageable pageable) {
        return ResponseEntity.ok(caseTemplateService.getCaseTemplates(pageable));
    }

    @Operation(summary = "Create a new Case Template",
            description = "This POST request call creates a new Case Template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Case Template created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaseTemplateDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<CaseTemplateDTO> createCaseTemplate(@RequestBody CreateCaseTemplateDTO createCaseTemplateDTO) {
        return ResponseEntity.ok(caseTemplateService.createCaseTemplate(createCaseTemplateDTO));
    }

    @Operation(summary = "Delete a case template by ID",
            description = "This DELETE request call deletes a case template by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Case Template deleted successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Case Template not found.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCaseTemplate(@PathVariable Integer id) {
        caseTemplateService.deleteCaseTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a case template item",
            description = "This PUT request call updates a case template item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Case template updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaseTemplateDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Case template not found.",
                    content = @Content)
    })
    @PutMapping("{id}")
    public ResponseEntity<CaseTemplateDTO> getCaseTemplate(@PathVariable Integer id, @RequestBody UpdateCaseTemplateDTO updateCaseTemplateDTO) {
        return ResponseEntity.ok(caseTemplateService.updateCaseTemplateDTO(id, updateCaseTemplateDTO));
    }
}
