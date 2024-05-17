package com.itesm.panoptimize.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Rest controller for managing total contacts.
 * Handles HTTP requests for operations related to total contacts statistics.
 */
@RestController
@RequestMapping("/api/total-calls")
public class TotalContactsController {
    private final TotalContactsService totalContactsService;

    public TotalContactsController(TotalContactsService totalContactsService) {
        this.totalContactsService = totalContactsService;
    }

    /**
     * Endpoint to retrieve the monthly activity of contacts.
     * @return a list of integers representing the number of contacts per month
     */
    @GetMapping("/monthly-activity")
    public List<Integer> get() {
        return totalContactsService.countMonthlyContacts();
    }
}
