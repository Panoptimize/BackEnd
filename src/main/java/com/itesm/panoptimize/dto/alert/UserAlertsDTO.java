package com.itesm.panoptimize.dto.alert;

public class UserAlertsDTO {
    private String recipient;
    private String sender;
    private String message;

    // Constructor
    public UserAlertsDTO(String recipient, String sender,String message) {
        this.recipient = recipient;
        this.sender = sender;
        this.message = message;
    }

    // Getters and setters
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
