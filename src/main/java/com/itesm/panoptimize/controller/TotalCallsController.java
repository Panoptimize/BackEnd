package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.service.TotalCallsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/total-calls")
public class TotalCallsController {
    private final TotalCallsService totalCallsService;

    public TotalCallsController(TotalCallsService totalCallsService) {
        this.totalCallsService = totalCallsService;
    }

    @GetMapping("/monthly-activity")
    public List<Integer> get() {
        return totalCallsService.countMonthlyCalls();
    }
}
