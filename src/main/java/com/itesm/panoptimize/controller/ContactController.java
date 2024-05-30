package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact.CreateContactDTO;
import com.itesm.panoptimize.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(@PathVariable String id) {
        return ResponseEntity.ok(contactService.getContact(id));
    }

    @PostMapping("/")
    public ResponseEntity<ContactDTO> createContact(CreateContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.createContact(contactDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public ResponseEntity<Page<ContactDTO>> getAllContacts(Pageable pageable) {
        return ResponseEntity.ok(contactService.getAllContacts(pageable));
    }
}
