package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.global_metric.GlobalMetricCreateDTO;
import com.itesm.panoptimize.dto.global_metric.GlobalMetricDTO;
import com.itesm.panoptimize.dto.global_metric.GlobalMetricUpdateDTO;
import com.itesm.panoptimize.service.GlobalMetricService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/global-metric")
public class GlobalMetricController {
    private final GlobalMetricService globalMetricService;

    @Autowired
    public GlobalMetricController(GlobalMetricService globalMetricService, ModelMapper modelMapper) {
        this.globalMetricService = globalMetricService;
    }


    @GetMapping("/")
    public ResponseEntity<Page<GlobalMetricDTO>> getGlobalMetrics(Pageable pageable) {
        Page<GlobalMetricDTO> globalMetrics = globalMetricService.getGlobalMetrics(pageable);
        if (globalMetrics.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(globalMetrics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalMetricDTO> getGlobalMetricById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(globalMetricService.getGlobalMetricById(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createGlobalMetric(@RequestBody GlobalMetricCreateDTO globalMetric) {
        GlobalMetricDTO createdGlobalMetric = globalMetricService.createGlobalMetric(globalMetric);
        if (createdGlobalMetric == null) {
            return ResponseEntity.badRequest().body("The global metric already exists.");
        }
        return ResponseEntity.ok(createdGlobalMetric);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalMetricUpdateDTO> updateGlobalMetric(@PathVariable Integer id, @RequestBody GlobalMetricUpdateDTO globalMetric) {
        return ResponseEntity.ok(globalMetricService.updateGlobalMetric(id, globalMetric));
    }
}
