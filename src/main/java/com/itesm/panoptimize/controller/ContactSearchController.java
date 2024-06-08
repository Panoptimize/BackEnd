package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.casetemplate.CaseTemplateDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsResponseDTO;
import com.itesm.panoptimize.service.ContactSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contacts")
public class ContactSearchController {

    private final ContactSearchService contactSearchService;

    @Autowired
    public ContactSearchController(ContactSearchService contactSearchService) {
        this.contactSearchService = contactSearchService;
    }

    @Operation(summary = "Search Contact", description = "This GET request serves the purpose of searching a Contact from the instance" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Contact found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaseTemplateDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Contact not found.",
                    content = @Content),
    })
    @PostMapping(value = "/search", produces = "application/json")
    public ResponseEntity<SearchContactsResponseDTO> searchContacts(@Valid @RequestBody SearchContactsDTO searchContactsDTO) {
        SearchContactsResponseDTO response = contactSearchService.searchContacts(searchContactsDTO);
        return ResponseEntity.ok(response);
    }
}


