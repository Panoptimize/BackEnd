package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsResponseDTO;
import com.itesm.panoptimize.service.ContactSearchService;
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

    @PostMapping(value = "/search", produces = "application/json")
    public ResponseEntity<SearchContactsResponseDTO> searchContacts(@Valid @RequestBody SearchContactsDTO searchContactsDTO) {
        SearchContactsResponseDTO response = contactSearchService.searchContacts(searchContactsDTO);
        return ResponseEntity.ok(response);
    }
}


