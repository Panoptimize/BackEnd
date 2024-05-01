package com.itesm.panoptimize.service;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
@Service
public class TotalContactsService {
    private final ContactRepository contactRepository;

    @Autowired
    public TotalContactsService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Method to load dummy data into the database.
     * Generates random contacts and saves them to the database to simulate actual data.
     */
    @PostConstruct
    public void loadDummyData() {
        Random random = new Random();
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            cal.setTime(new Date()); // Establece la fecha actual
            int randomMonth = random.nextInt(12); // Genera un mes aleatorio (0-11)
            cal.set(Calendar.MONTH, randomMonth);
            cal.set(Calendar.DAY_OF_MONTH, random.nextInt(cal.getActualMaximum(Calendar.DAY_OF_MONTH)) + 1); // Día aleatorio para el mes
            Date randomStartTime = cal.getTime();
            Date randomEndTime = new Date(randomStartTime.getTime() + 3600000); // Añade una hora para el endTime

            Contact contact = new Contact();
            contact.setStartTime(randomStartTime);
            contact.setEndTime(randomEndTime);
            contact.setResolutionStatus(random.nextBoolean() ? "Solved" : "Not Solved");
            contact.setFirstContactResolution(random.nextBoolean());
            contact.setSentimentNegative(random.nextFloat());
            contact.setSentimentPositive(random.nextFloat());
            contact.setAgentId(1);
            contact.setSatisfaction(random.nextInt(5) + 1);
            contactRepository.save(contact  );
        }
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

