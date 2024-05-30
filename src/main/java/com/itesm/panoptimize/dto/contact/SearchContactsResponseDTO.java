package com.itesm.panoptimize.dto.contact;

import java.util.List;

public class SearchContactsResponseDTO {

    private List<ContactSummaryDTO> contacts;
    private String nextToken;
    private Long totalCount;

    // Getters and Setters

    public List<ContactSummaryDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactSummaryDTO> contacts) {
        this.contacts = contacts;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public static class ContactSummaryDTO {
        private String contactId;
        private String channel;
        private Long initiationTimestamp;
        private String sentiment; // Nuevo campo
        private String agentId; // Nuevo campo

        // Getters y Setters

        public String getContactId() {
            return contactId;
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public Long getInitiationTimestamp() {
            return initiationTimestamp;
        }

        public void setInitiationTimestamp(Long initiationTimestamp) {
            this.initiationTimestamp = initiationTimestamp;
        }

        public String getSentiment() {
            return sentiment;
        }

        public void setSentiment(String sentiment) {
            this.sentiment = sentiment;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }
    }
}