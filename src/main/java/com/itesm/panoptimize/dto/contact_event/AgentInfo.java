package com.itesm.panoptimize.dto.contact_event;

import java.time.OffsetDateTime;

public class AgentInfo {
    private String agentArn;
    private OffsetDateTime connectedToAgentTimestamp;

    public String getAgentArn() { return agentArn; }
    public void setAgentArn(String value) { this.agentArn = value; }

    public OffsetDateTime getConnectedToAgentTimestamp() { return connectedToAgentTimestamp; }
    public void setConnectedToAgentTimestamp(OffsetDateTime value) { this.connectedToAgentTimestamp = value; }
}
