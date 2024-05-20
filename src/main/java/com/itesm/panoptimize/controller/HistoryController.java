package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.history.ContactHistoryDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, List<Contact>>> getHistory(){
        List<Contact> contactHistoryList = historyService.getContactHistory();
        Map<String, List<Contact>> contacts = new HashMap<>();
        contacts.put("contacts", contactHistoryList);

        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/contact/:id")
    public ResponseEntity<String> getContactDetails(@PathVariable("id") long id){
        String test = "";
        historyService.getContactDetails(id);
        return new ResponseEntity<>(test, HttpStatus.OK);
    }
}
