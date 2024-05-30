package com.itesm.panoptimize.dto.contact_event;

import java.util.UUID;

public class Detail {
    private String eventType;
    private String instanceARN;
    private UUID contactID;
    private String channel;
    private QueueInfo queueInfo;
    private AgentInfo agentInfo;

    public String getEventType() { return eventType; }
    public void setEventType(String value) { this.eventType = value; }

    public String getInstanceARN() {
        return instanceARN;
    }

    public void setInstanceARN(String instanceARN) {
        this.instanceARN = instanceARN;
    }

    public UUID getContactID() { return contactID; }
    public void setContactID(UUID value) { this.contactID = value; }

    public String getChannel() { return channel; }
    public void setChannel(String value) { this.channel = value; }

    public QueueInfo getQueueInfo() { return queueInfo; }
    public void setQueueInfo(QueueInfo value) { this.queueInfo = value; }

    public AgentInfo getAgentInfo() { return agentInfo; }
    public void setAgentInfo(AgentInfo value) { this.agentInfo = value; }
}
