package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.service.TotalContactsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/total-calls")
public class TotalContactsController {
    private final TotalContactsService totalContactsService;

    public TotalContactsController(TotalContactsService totalContactsService) {
        this.totalContactsService = totalContactsService;
    }

    @GetMapping("/monthly-activity")
    public List<Integer> get() {
        return totalContactsService.countMonthlyContacts();
    }
}
