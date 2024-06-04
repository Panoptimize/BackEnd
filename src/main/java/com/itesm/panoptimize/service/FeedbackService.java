package com.itesm.panoptimize.service;

import com.itesm.panoptimize.model.Note;
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

    public Note getFeedbackById(Integer id) {
        return feedbackRepository.findById(id).orElseThrow();
    }
    public Note addFeedback(Note note) {
        return feedbackRepository.save(note);
    }
    public boolean deleteFeedback(Integer id) {
        boolean exists = feedbackRepository.existsById(id);
        feedbackRepository.deleteById(id);
        return exists;
    }
    public Note updateFeedback(Integer id, Note note) {
        Note noteToUpdate = feedbackRepository.findById(id)
                .orElseThrow();

        if(note.getName() != null)
            noteToUpdate.setName(note.getName());
        if(note.getDescription() != null)
            noteToUpdate.setDescription(note.getDescription());
        if(note.getPriority() != null)
            noteToUpdate.setPriority(note.getPriority());
        if(note.getSolved() != null)
            noteToUpdate.setSolved(note.getSolved());
        if(note.getAgentPerformance() != null)
            noteToUpdate.setAgentPerformance(note.getAgentPerformance());

        feedbackRepository.save(noteToUpdate);
        return noteToUpdate;
    }

}
