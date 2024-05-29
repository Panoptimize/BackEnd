package com.itesm.panoptimize.service;

import com.itesm.panoptimize.model.Notes;
import com.itesm.panoptimize.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Notes getFeedbackById(Integer id) {
        return feedbackRepository.findById(id).orElseThrow();
    }
    public Notes addFeedback(Notes notes) {
        return feedbackRepository.save(notes);
    }
    public boolean deleteFeedback(Integer id) {
        boolean exists = feedbackRepository.existsById(id);
        feedbackRepository.deleteById(id);
        return exists;
    }
    public Notes updateFeedback(Integer id, Notes notes) {
        Notes notesToUpdate = feedbackRepository.findById(id)
                .orElseThrow();
        notesToUpdate.setContact(notes.getContact());
        notesToUpdate.setName(notes.getName());
        notesToUpdate.setDescription(notes.getDescription());
        notesToUpdate.setUser(notes.getUser());
        notesToUpdate.setPriority(notes.getPriority());
        feedbackRepository.save(notesToUpdate);
        return notesToUpdate;
    }

}
