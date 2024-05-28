package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.history.ContactHistoryDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.model.ContactMetric;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.ContactMetricRepository;
import com.itesm.panoptimize.repository.ContactRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final ContactRepository contactRepository;
    private final ContactMetricRepository contactMetricRepository;
    @Autowired
    public HistoryService(ContactRepository contactRepository,
                          ContactMetricRepository contactMetricRepository) {
        this.contactRepository = contactRepository;
        this.contactMetricRepository = contactMetricRepository;
    }

    public List<ContactHistoryDTO> getContactHistory() {
        List<Contact> contacts = contactRepository.findAll();

        List<ContactHistoryDTO> results = contacts.stream()
                .map(this::convertToContactHistoryDTO)
                .collect(Collectors.toList());

        return results;
    }

    private ContactHistoryDTO convertToContactHistoryDTO( Contact contact ){
        ContactHistoryDTO contactHistoryDTO = new ContactHistoryDTO();

        contactHistoryDTO.setContact_id(contact.getId());
        contactHistoryDTO.setDate(contact.getStartTime());
        contactHistoryDTO.setTime(contact.getStartTime());
        //contactHistoryDTO.setAgent_name(contact.getAgent().getFullName());
        //contactHistoryDTO.setResolution_status(contact.getContactMetrics().getContactStatus());

        return contactHistoryDTO;
    }

    private void getContactDetails(){

    }
    public Contact getContactById(Integer id){
        return contactRepository.findById(id).orElseThrow();
    }
    public Contact addContact(Contact contact){
        return contactRepository.save(contact);
    }
    public boolean deleteContact(Integer id){
        boolean exists = contactRepository.existsById(id);
        contactRepository.deleteById(id);
        return exists;
    }
    public Contact updateContact(Integer id, Contact contact){
        Contact contactToUpdate = contactRepository.findById(id).orElseThrow();
        contactToUpdate.setStartTime(contact.getStartTime());
        contactToUpdate.setEndTime(contact.getEndTime());
        //contactToUpdate.setId(contact.getAgent());
        //contactToUpdate.setContactMetrics(contact.getContactMetrics());
        //contactToUpdate.setSatisfactionLevel(contact.getSatisfactionLevel());
        contactRepository.save(contactToUpdate);
        return contactToUpdate;
    }
    //public ContactMetric getContactMetrics(Integer id){
        //return contactRepository.findById(id).orElseThrow().getContactMetrics();
   //}
    public ContactMetric addContactMetric(ContactMetric contactMetric){
        return contactMetricRepository.save(contactMetric);
    }
    public boolean deleteContactMetric(Integer id){
        boolean exists = contactMetricRepository.existsById(id);
        contactMetricRepository.deleteById(id);
        return exists;
    }
    public ContactMetric updateContactMetric(Integer id, ContactMetric contactMetric){
        ContactMetric contactMetricToUpdate = contactMetricRepository.findById(id).orElseThrow();
        contactMetricToUpdate.setContact(contactMetric.getContact());
        contactMetricToUpdate.setContactStatus(contactMetric.getContactStatus());
        contactMetricToUpdate.setSpeedOfAnswer(contactMetric.getSpeedOfAnswer());
        contactMetricToUpdate.setHandleTime(contactMetric.getHandleTime());
        contactMetricToUpdate.setAfterCallWorkTime(contactMetric.getAfterCallWorkTime());
        contactMetricToUpdate.setAbandoned(contactMetric.getAbandoned());
        contactMetricToUpdate.setFirstContactResolution(contactMetric.getFirstContactResolution());
        contactMetricToUpdate.setSentimentNegative(contactMetric.getSentimentNegative());
        contactMetricRepository.save(contactMetricToUpdate);
        return contactMetricToUpdate;
    }

}
