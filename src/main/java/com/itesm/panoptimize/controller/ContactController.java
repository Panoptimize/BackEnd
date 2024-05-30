package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact.CreateContactDTO;
import com.itesm.panoptimize.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(summary = "Get contact by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Contact found"),
                    @ApiResponse(responseCode = "404", description = "Contact not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(@PathVariable String id) {
        return ResponseEntity.ok(contactService.getContact(id));
    }

    @Operation(summary = "Create contact")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Contact created")
            }
    )
    @PostMapping("/")
    public ResponseEntity<ContactDTO> createContact(@RequestBody CreateContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.createContact(contactDTO));
    }

    @Operation(summary = "Update contact")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Contact updated"),
                    @ApiResponse(responseCode = "404", description = "Contact not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all contacts")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Contacts found")
            }
    )
    @GetMapping("/")
    public ResponseEntity<Page<ContactDTO>> getAllContacts(Pageable pageable) {
        return ResponseEntity.ok(contactService.getAllContacts(pageable));
    }
}
