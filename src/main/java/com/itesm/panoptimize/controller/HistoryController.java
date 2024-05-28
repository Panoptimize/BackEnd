package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.history.ContactHistoryDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.model.ContactMetric;
import com.itesm.panoptimize.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private HistoryService historyService;
    @Autowired
    public HistoryController(HistoryService historyService) {this.historyService = historyService; }

    @GetMapping("/contact/all")
    public ResponseEntity<Map<String, List<ContactHistoryDTO>>> getHistory(){
        List<ContactHistoryDTO> contactHistoryList = historyService.getContactHistory();
        Map<String, List<ContactHistoryDTO>> contacts = new HashMap<>();
        contacts.put("contacts", contactHistoryList);

        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/contact/:id")
    public ResponseEntity<Contact> getContactDetails(@PathVariable("id") Integer id){
        Contact contact = historyService.getContactById(id);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }
    @PostMapping("/contact/new")
    public ResponseEntity<String> addContact(){
        Contact contact = new Contact();
        historyService.addContact(contact);
        return new ResponseEntity<>("Contact added", HttpStatus.OK);
    }
    @DeleteMapping("/contact/delete/:id")
    public ResponseEntity<String> deleteContact(@PathVariable("id") Integer id){
        historyService.deleteContact(id);
        return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
    }
    @PatchMapping("/contact/update/:id")
    public ResponseEntity<String> updateContact(@PathVariable("id") Integer id){
        Contact contact = new Contact();
        historyService.addContact(contact);
        return new ResponseEntity<>("Contact updated", HttpStatus.OK);
    }

    @GetMapping("/contact/metrics/:id")
    public ResponseEntity<ContactMetric> getContactMetrics(@PathVariable("id") Integer id){
        ContactMetric contactMetric = historyService.getContactMetrics(id);
        return new ResponseEntity<>(contactMetric, HttpStatus.OK);
    }
    @PostMapping("/contact/metrics/new")
    public ResponseEntity<String> addContactMetrics(){
        ContactMetric contactMetric = new ContactMetric();
        historyService.addContactMetric(contactMetric);
        return new ResponseEntity<>("Contact added", HttpStatus.OK);
    }
    @DeleteMapping("/contact/metrics/delete/:id")
    public ResponseEntity<String> deleteContactMetrics(@PathVariable("id") Integer id){
        historyService.deleteContactMetric(id);
        return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
    }
    @PatchMapping("/contact/metrics/update/:id")
    public ResponseEntity<String> updateContactMetrics(@PathVariable("id") Integer id){
        ContactMetric contactMetric = new ContactMetric();
        historyService.addContactMetric(contactMetric);
        return new ResponseEntity<>("Contact updated", HttpStatus.OK);
    }


}
