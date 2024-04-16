package com.tec.panoptimize.service;
import org.springframework.stereotype.Service;
import com.tec.panoptimize.dto.agent.DashboardAgentsDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class DashboardAgentsService {

    public Map<String, Integer> calculateStatusCounts(List<DashboardAgentsDTO> agents) {
        Map<String, Integer> statusCounts = new HashMap<>();

        // Initialize counts
        statusCounts.put("AVAILABLE", 0);
        statusCounts.put("IN_CONTACT", 0);
        statusCounts.put("AFTER_CALL_WORK", 0);
        statusCounts.put("OFFLINE", 0);

        // Count agents by status
        for (DashboardAgentsDTO agent : agents) {
            switch (agent.getStatus()) {
                case AVAILABLE:
                    statusCounts.put("AVAILABLE", statusCounts.get("AVAILABLE") + 1);
                    break;
                case IN_CONTACT:
                    statusCounts.put("IN_CONTACT", statusCounts.get("IN_CONTACT") + 1);
                    break;
                case AFTER_CALL_WORK:
                    statusCounts.put("AFTER_CALL_WORK", statusCounts.get("AFTER_CALL_WORK") + 1);
                    break;
                case OFFLINE:
                    statusCounts.put("OFFLINE", statusCounts.get("OFFLINE") + 1);
                    break;
            }
        }

        return statusCounts;
    }
}