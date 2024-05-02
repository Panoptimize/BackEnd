package com.itesm.panoptimize.dto.performance;

//Este endpoint depende de datos de otros endpoints para hacer la ponderacion.
//El to do esta en el servicio

import java.util.List;
import java.util.Map;

public class PerformanceDTO {


    public List<Map<String, List<Double>>> getPerformanceData() {
        return performanceData;
    }

    public void setPerformanceData(List<Map<String, List<Double>>> performanceData) {
        this.performanceData = performanceData;
    }

    List<Map<String, List<Double>>> performanceData;

}

