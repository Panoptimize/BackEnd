package com.itesm.panoptimize.service;
import com.itesm.panoptimize.dto.dashboard.CallMetricsDTO;
import com.itesm.panoptimize.dto.dashboard.CustomerSatisfactionDTO;
import com.itesm.panoptimize.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateSatisfactionService {

    private final ContactRepository contactRepository;

    public CalculateSatisfactionService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;

    }

    public CustomerSatisfactionDTO getSatisfactionLevels(){
        List<Long> satisfactionLevels = new ArrayList<>();
        contactRepository.countBySatisfaction().forEach(satisfactionCountDTO -> {
            satisfactionLevels.add((Long) satisfactionCountDTO[1]);
        });
        CustomerSatisfactionDTO customerSatisfactionDTO = new CustomerSatisfactionDTO();
        customerSatisfactionDTO.setSatisfaction_levels(satisfactionLevels);
        return customerSatisfactionDTO;
    };

}