package com.itesm.panoptimize.dto.contactevent;

import java.util.UUID;

public class Detail {
    private String eventType;
    private String instanceArn;
    private UUID contactId;
    private String channel;
    private QueueInfo queueInfo;
    private AgentInfo agentInfo;

    public String getEventType() { return eventType; }
    public void setEventType(String value) { this.eventType = value; }

    public String getInstanceArn() {
        return instanceArn;
    }

    public void setInstanceArn(String instanceArn) {
        this.instanceArn = instanceArn;
    }

    public UUID getContactId() { return contactId; }
    public void setContactId(UUID value) { this.contactId = value; }

    public String getChannel() { return channel; }
    public void setChannel(String value) { this.channel = value; }

    public QueueInfo getQueueInfo() { return queueInfo; }
    public void setQueueInfo(QueueInfo value) { this.queueInfo = value; }

    public AgentInfo getAgentInfo() { return agentInfo; }
    public void setAgentInfo(AgentInfo value) { this.agentInfo = value; }
}
