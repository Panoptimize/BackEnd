package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.history.ContactDetailsDTO;
import com.itesm.panoptimize.dto.history.ContactHistoryDTO;
import com.itesm.panoptimize.dto.history.HistoryInsightsDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.model.ContactMetric;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.ContactMetricRepository;
import com.itesm.panoptimize.repository.ContactRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final ContactRepository contactRepository;
    @Autowired
    public HistoryService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getContactHistory() {
        List<Contact> contacts = contactRepository.findAll();
        return contacts;
        /**
        return contacts.stream()
                .map(this::convertToContactHistoryDTO)
                .collect(Collectors.toList());
         */
    }

    private ContactHistoryDTO convertToContactHistoryDTO( Contact contact ){
        ContactHistoryDTO contactHistoryDTO = new ContactHistoryDTO();

        contactHistoryDTO.setContact_id(contact.getId());

        contactHistoryDTO.setDate(contact.getStartTime().toLocalDate());
        contactHistoryDTO.setTime(contact.getStartTime().toLocalTime());

        contactHistoryDTO.setAgent_name(contact.getAgent().getFullName());
        contactHistoryDTO.setResolution_status(contact.getContactMetrics().getContactStatus());
        contactHistoryDTO.setSatisfaction(contact.getSatisfactionLevel().name());

        Duration duration = Duration.between(contact.getStartTime(), contact.getEndTime());
        contactHistoryDTO.setDuration(LocalTime.of((int)duration.getSeconds()/3600,
                (int)(duration.getSeconds()%3600)/60, (int)duration.getSeconds() % 60));

        return contactHistoryDTO;
    }

    public ContactDetailsDTO getContactDetails(long id){
        Optional<Contact> contactOptional = contactRepository.findById(id);
        ContactDetailsDTO results = new ContactDetailsDTO();
        if(contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            results = convertToContactDetailsDTO(contact);
        }
        return results;
    }

    private ContactDetailsDTO convertToContactDetailsDTO( Contact contact ){
        ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();

        contactDetailsDTO.setFull_name(contact.getAgent().getFullName());
        contactDetailsDTO.setEmail(contact.getAgent().getEmail());
        //Workspaces = routing profile?
        contactDetailsDTO.setWorkspaces(contact.getAgent().getRouting_profile_id());
        contactDetailsDTO.setFcr(contact.getContactMetrics().getFirstContactResolution());
        contactDetailsDTO.setAfter_call_worktime(contact.getContactMetrics().getAfterCallWorkTime());
        contactDetailsDTO.setSatisfaction(contact.getSatisfactionLevel().name());

        Duration duration = Duration.between(contact.getStartTime(), contact.getEndTime());
        contactDetailsDTO.setDuration(LocalTime.of((int)duration.getSeconds()/3600,
                (int)(duration.getSeconds()%3600)/60, (int)duration.getSeconds() % 60));

        contactDetailsDTO.setStatus(contact.getContactMetrics().getContactStatus());
        contactDetailsDTO.setAnswer_speed(contact.getContactMetrics().getSpeedOfAnswer());
        contactDetailsDTO.setDate(contact.getStartTime().toLocalDate());
        contactDetailsDTO.setTime(contact.getStartTime().toLocalTime());



        return contactDetailsDTO;
    }

    private List<HistoryInsightsDTO> calculateContactInsights( Contact contact ){
        List<HistoryInsightsDTO> results = new ArrayList<HistoryInsightsDTO>();

        //Calculate handle time vs average after_call_work_time
        double ratio = calculateHandleTimeAfterCallRatio(contact.getAgent().getId(),
                contact.getContactMetrics().getHandleTime());
        if(ratio >= 2){
            HistoryInsightsDTO handleTimeRatio = new HistoryInsightsDTO();
            handleTimeRatio.setInsight_id(2);
            handleTimeRatio.setInsight_value(ratio);
            results.add(handleTimeRatio);
        }

        //Calculate # of FCR
        int firstContactCount = contactRepository.countFCRByAgent(contact.getAgent().getId());
        if(firstContactCount > 1){
            HistoryInsightsDTO countFCRInsight = new HistoryInsightsDTO();
            countFCRInsight.setInsight_id(3);
            countFCRInsight.setInsight_value(firstContactCount);
            results.add(countFCRInsight);
        }

        //Calculate satisfaction streak
        int streak = calculateSatisfactionStreak(contact.getAgent().getId(), contact.getId(),
                contact.getSatisfactionLevel().name());
        if(streak > 1){
            HistoryInsightsDTO satisfactionStreakInsight = new HistoryInsightsDTO();
            satisfactionStreakInsight.setInsight_id(4);
            satisfactionStreakInsight.setInsight_value(streak);
            results.add(satisfactionStreakInsight);
        }

        return results;
    }

    private int calculateSatisfactionStreak(int agentId, int contactId, String satisfaction_level){
        List<Contact> contacts = contactRepository.findPreviousContactIdsFromAgent(agentId, contactId);
        int streak = 0;
        for(Contact contact : contacts){
            if(contact.getSatisfactionLevel().name().equals(satisfaction_level)){
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    private double calculateHandleTimeAfterCallRatio(int agentId, int handle_time){
        double averageAfterCallWorkTime = contactRepository.avgAfterCallWorkTime(agentId);
        if(averageAfterCallWorkTime > handle_time) {return averageAfterCallWorkTime / handle_time;}
        else {return -1 * (averageAfterCallWorkTime/handle_time);}
    }

}
