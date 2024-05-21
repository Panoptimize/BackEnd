package com.itesm.panoptimize.service;

import com.itesm.panoptimize.model.Feedback;
import com.itesm.panoptimize.repository.FeedbackRepository;
import com.itesm.panoptimize.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback getFeedbackById(Integer id) {
        return feedbackRepository.findById(id).orElseThrow();
    }
    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
    public boolean deleteFeedback(Integer id) {
        boolean exists = feedbackRepository.existsById(id);
        feedbackRepository.deleteById(id);
        return exists;
    }
    public Feedback updateFeedback(Integer id, Feedback feedback) {
        Feedback feedbackToUpdate = feedbackRepository.findById(id)
                .orElseThrow();
        feedbackToUpdate.setContact(feedback.getContact());
        feedbackToUpdate.setName(feedback.getName());
        feedbackToUpdate.setDescription(feedback.getDescription());
        feedbackToUpdate.setUser(feedback.getUser());
        feedbackToUpdate.setPriority(feedback.getPriority());
        feedbackRepository.save(feedbackToUpdate);
        return feedbackToUpdate;
    }

}
