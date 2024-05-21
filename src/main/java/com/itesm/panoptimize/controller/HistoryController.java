package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.history.ContactDetailsDTO;
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
    final private HistoryService historyService;
    @Autowired
    public HistoryController(HistoryService historyService) {this.historyService = historyService; }

    @GetMapping("")
    public ResponseEntity<Map<String, List<ContactHistoryDTO>>> getHistory(){
        List<ContactHistoryDTO> contactHistoryList = historyService.getContactHistory();
        Map<String, List<ContactHistoryDTO>> contacts = new HashMap<>();
        contacts.put("contacts", contactHistoryList);

        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/:id")
    public ResponseEntity<ContactDetailsDTO> getContactDetails(@PathVariable("id") long id){
        return new ResponseEntity<>(historyService.getContactDetails(id), HttpStatus.OK);
    }
}
