package com.itesm.panoptimize.service;
import com.itesm.panoptimize.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TotalContactsService {
    private final ContactRepository contactRepository;

    @Autowired
    public TotalContactsService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Integer> countMonthlyContacts() {
        try {
            List<Object[]> results = contactRepository.countMonthlyContacts();
            List<Integer> contactsPerMonth = new ArrayList<>(Collections.nCopies(12, 0));
            results.forEach(result -> {
                int month = (Integer) result[0] - 1; // Months from 0 to 11
                Integer count = ((Number) result[1]).intValue();
                contactsPerMonth.set(month, count);
            });
            return contactsPerMonth;
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error retrieving monthly contacts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}