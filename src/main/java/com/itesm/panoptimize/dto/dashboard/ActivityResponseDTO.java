package com.itesm.panoptimize.dto.dashboard;

import java.util.List;

public class ActivityResponseDTO {
    List<ActivityDTO> activities;

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }
}
