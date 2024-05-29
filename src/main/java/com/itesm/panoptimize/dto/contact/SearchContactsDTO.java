package com.itesm.panoptimize.dto.contact;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SearchContactsDTO {

    @NotNull(message = "Instance ID is required")
    private String instanceId;

    private Integer maxResults;
    private String nextToken;
    private SearchCriteria searchCriteria;
    private Sort sort;
    @NotNull(message = "Time range is required")
    private TimeRange timeRange;

    // Getters and Setters

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public static class SearchCriteria {
        private List<String> agentHierarchyGroups;
        private List<String> agentIds;
        private List<String> channels;
        private ContactAnalysis contactAnalysis;
        private List<String> initiationMethods;
        private List<String> queueIds;
        private SearchableContactAttributes searchableContactAttributes;

        // Getters and Setters

        public List<String> getAgentHierarchyGroups() {
            return agentHierarchyGroups;
        }

        public void setAgentHierarchyGroups(List<String> agentHierarchyGroups) {
            this.agentHierarchyGroups = agentHierarchyGroups;
        }

        public List<String> getAgentIds() {
            return agentIds;
        }

        public void setAgentIds(List<String> agentIds) {
            this.agentIds = agentIds;
        }

        public List<String> getChannels() {
            return channels;
        }

        public void setChannels(List<String> channels) {
            this.channels = channels;
        }

        public ContactAnalysis getContactAnalysis() {
            return contactAnalysis;
        }

        public void setContactAnalysis(ContactAnalysis contactAnalysis) {
            this.contactAnalysis = contactAnalysis;
        }

        public List<String> getInitiationMethods() {
            return initiationMethods;
        }

        public void setInitiationMethods(List<String> initiationMethods) {
            this.initiationMethods = initiationMethods;
        }

        public List<String> getQueueIds() {
            return queueIds;
        }

        public void setQueueIds(List<String> queueIds) {
            this.queueIds = queueIds;
        }

        public SearchableContactAttributes getSearchableContactAttributes() {
            return searchableContactAttributes;
        }

        public void setSearchableContactAttributes(SearchableContactAttributes searchableContactAttributes) {
            this.searchableContactAttributes = searchableContactAttributes;
        }
    }

    public static class ContactAnalysis {
        private Transcript transcript;
        private String matchType;

        // Getters and Setters

        public Transcript getTranscript() {
            return transcript;
        }

        public void setTranscript(Transcript transcript) {
            this.transcript = transcript;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }
    }

    public static class Transcript {
        private List<Criteria> criteria;
        private String matchType;

        // Getters and Setters

        public List<Criteria> getCriteria() {
            return criteria;
        }

        public void setCriteria(List<Criteria> criteria) {
            this.criteria = criteria;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }
    }

    public static class Criteria {
        private String matchType;
        private String participantRole;
        private List<String> searchText;

        // Getters and Setters

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }

        public String getParticipantRole() {
            return participantRole;
        }

        public void setParticipantRole(String participantRole) {
            this.participantRole = participantRole;
        }

        public List<String> getSearchText() {
            return searchText;
        }

        public void setSearchText(List<String> searchText) {
            this.searchText = searchText;
        }
    }

    public static class SearchableContactAttributes {
        private List<Criteria> criteria;
        private String matchType;

        // Getters and Setters

        public List<Criteria> getCriteria() {
            return criteria;
        }

        public void setCriteria(List<Criteria> criteria) {
            this.criteria = criteria;
        }

        public String getMatchType() {
            return matchType;
        }

        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }
    }

    public static class Sort {
        private String fieldName;
        private String order;

        // Getters and Setters

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }

    public static class TimeRange {
        private Long endTime;
        private Long startTime;
        private String type;

        // Getters and Setters

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
