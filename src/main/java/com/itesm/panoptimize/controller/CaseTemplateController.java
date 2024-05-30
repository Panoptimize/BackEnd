package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.case_template.CaseTemplateDTO;
import com.itesm.panoptimize.dto.case_template.CreateCaseTemplateDTO;
import com.itesm.panoptimize.dto.case_template.UpdateCaseTemplateDTO;
import com.itesm.panoptimize.service.CaseTemplateService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get case template by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case template found"),
            @ApiResponse(responseCode = "400", description = "Case template not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CaseTemplateDTO> getCaseTemplate(@PathVariable Integer id) {
        CaseTemplateDTO caseTemplateDTO = caseTemplateService.getCaseTemplate(id);
        if (caseTemplateDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(caseTemplateDTO);
    }

    @GetMapping("/")
    public ResponseEntity<Page<CaseTemplateDTO>> getCaseTemplates(Pageable pageable) {
        return ResponseEntity.ok(caseTemplateService.getCaseTemplates(pageable));
    }

    @PostMapping("/{id}")
    public ResponseEntity<CaseTemplateDTO> createCaseTemplate(@RequestBody CreateCaseTemplateDTO createCaseTemplateDTO) {
        return ResponseEntity.ok(caseTemplateService.createCaseTemplate(createCaseTemplateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCaseTemplate(@PathVariable Integer id) {
        caseTemplateService.deleteCaseTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<CaseTemplateDTO> getCaseTemplate(@PathVariable Integer id, @RequestBody UpdateCaseTemplateDTO updateCaseTemplateDTO) {
        return ResponseEntity.ok(caseTemplateService.updateCaseTemplateDTO(id, updateCaseTemplateDTO));
    }
}
