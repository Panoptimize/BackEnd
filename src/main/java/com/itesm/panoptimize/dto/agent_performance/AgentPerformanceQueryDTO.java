package com.itesm.panoptimize.dto.agent_performance;

public interface AgentPerformanceQueryDTO {
    Double getAvgAfterContactWorkTime();
    Double getAvgHandleTime();
    Double getAvgAbandonTime();
    Double getAvgHoldTime();
}
