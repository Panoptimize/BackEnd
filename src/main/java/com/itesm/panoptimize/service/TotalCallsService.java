package com.itesm.panoptimize.service;
import com.itesm.panoptimize.model.Call;
import com.itesm.panoptimize.repository.CallRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TotalCallsService {
    private final CallRepository callRepository;

    public TotalCallsService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public List<Integer> countMonthlyCalls() {
        List<Call> calls = callRepository.findAll();
        int[] callsPerMonth = new int[12];
        for (Call call : calls) {
            int month = call.getStartTime().getMonthValue() - 1;
            callsPerMonth[month]++;
        }
        return Arrays.asList(Arrays.stream(callsPerMonth).boxed().toArray(Integer[]::new));
    }
}