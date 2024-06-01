package com.itesm.panoptimize.dto.contactevent;

import java.time.OffsetDateTime;

public class QueueInfo {
    private String queueArn;
    private OffsetDateTime enqueueTimestamp;
    private String queueType;

    public String getQueueArn() { return queueArn; }
    public void setQueueArn(String value) { this.queueArn = value; }

    public OffsetDateTime getEnqueueTimestamp() { return enqueueTimestamp; }
    public void setEnqueueTimestamp(OffsetDateTime value) { this.enqueueTimestamp = value; }

    public String getQueueType() { return queueType; }
    public void setQueueType(String value) { this.queueType = value; }
}
