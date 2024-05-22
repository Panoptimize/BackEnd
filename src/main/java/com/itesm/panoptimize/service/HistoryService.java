package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.history.ContactDetailsDTO;
import com.itesm.panoptimize.dto.history.ContactHistoryDTO;
import com.itesm.panoptimize.dto.history.HistoryInsightsDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.model.ContactMetric;
import com.itesm.panoptimize.repository.ContactMetricRepository;
import com.itesm.panoptimize.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        return contacts.stream()
                .map(this::convertToContactHistoryDTO)
                .collect(Collectors.toList());
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

    public ContactDetailsDTO getContactDetails(int id){
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

        contactDetailsDTO.setWorkspaces(contact.getAgent().getRoutingProfileId());
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

        contactDetailsDTO.setInsights(calculateContactInsights(contact));

        return contactDetailsDTO;
    }

    private List<HistoryInsightsDTO> calculateContactInsights( Contact contact ){
        List<HistoryInsightsDTO> results = new ArrayList<>();

        //Calculate handle_time vs average workspace handle_time
        double ratioWorkspaceHandleTime = calculateWorkspaceHandleTimeRatio(contact.getAgent().getRoutingProfileId(),
                contact.getContactMetrics().getHandleTime());
        if(ratioWorkspaceHandleTime >= 10)
        {
            HistoryInsightsDTO workspaceHandleTimeRatio = new HistoryInsightsDTO();
            workspaceHandleTimeRatio.setInsight_id(0);
            workspaceHandleTimeRatio.setInsight_value(ratioWorkspaceHandleTime);
            results.add(workspaceHandleTimeRatio);
        }

        //Calculate speed_of_answer vs average workspace speed_of_answer
        double ratioWorkspaceSpeedOfAnswer = calculateWorkspaceSpeedOfAnswerRatio(contact.getAgent().getRoutingProfileId(),
                contact.getContactMetrics().getSpeedOfAnswer());
        if(ratioWorkspaceSpeedOfAnswer >= 10)
        {
            HistoryInsightsDTO workspaceSpeedOfAnswerRatio = new HistoryInsightsDTO();
            workspaceSpeedOfAnswerRatio.setInsight_id(1);
            workspaceSpeedOfAnswerRatio.setInsight_value(ratioWorkspaceSpeedOfAnswer);
            results.add(workspaceSpeedOfAnswerRatio);
        }

        //Calculate handle time vs average after_call_work_time
        double ratioHandleTimeACWT = calculateHandleTimeAfterCallRatio(contact.getAgent().getId(),
                contact.getContactMetrics().getHandleTime());
        if(ratioHandleTimeACWT >= 2){
            HistoryInsightsDTO handleTimeRatio = new HistoryInsightsDTO();
            handleTimeRatio.setInsight_id(2);
            handleTimeRatio.setInsight_value(ratioHandleTimeACWT);
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

    private double calculateWorkspaceHandleTimeRatio(String routingProfileId, int contactHandleTime){
        double avgWorkspaceHandleTime = contactRepository.avgWorkspaceHandleTime(routingProfileId);
        return((double) contactHandleTime - avgWorkspaceHandleTime) / avgWorkspaceHandleTime * 100;
    }

    private double calculateWorkspaceSpeedOfAnswerRatio(String routingProfileId, int contactSpeedOfAnswer){
        double avgWorkspaceSpeedOfAnswer = contactRepository.avgSpeedOfAnswerTime(routingProfileId);
        return((double) contactSpeedOfAnswer - avgWorkspaceSpeedOfAnswer) / avgWorkspaceSpeedOfAnswer * 100;
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
        contactToUpdate.setAgent(contact.getAgent());
        contactToUpdate.setContactMetrics(contact.getContactMetrics());
        contactToUpdate.setSatisfactionLevel(contact.getSatisfactionLevel());
        contactRepository.save(contactToUpdate);
        return contactToUpdate;
    }
    public ContactMetric getContactMetrics(Integer id){
        return contactRepository.findById(id).orElseThrow().getContactMetrics();
    }
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
