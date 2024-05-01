package com.itesm.panoptimize.dto.performance;

//Este endpoint depende de datos de otros endpoints para hacer la ponderacion.
//El to do esta en el servicio

import java.util.List;

public class PerformanceDTO {

    public List<Double> getPerformanceData() {
        return performanceData;
    }

    public void setPerformanceData(List<Double> performanceData) {
        this.performanceData = performanceData;
    }

    private List<Double> performanceData;

}

