package com.itesm.panoptimize.service;

import java.util.ArrayList;
import java.util.List;

//TODO Creating the essential endpoints or getting the data from Connect.
// - Endpoint of KPI about Service Level (SL) -> 30%
// - Endpoint of KPI about First Contact Resolution -> 40%
// - Endpoint of KPI about Occupation -> 30%
public class CalculatePerformance {
    public static List<Double> performanceCalculation(List<Double> serviceLevel, List<Double> firstContactResolution, List<Double> ocupation) {

        List<Double> performance = new ArrayList<>();
        //Check
        int length_ServiceLevel_data = serviceLevel.size();
        int length_FirstContactResolution_data = firstContactResolution.size();
        int lengthOccupation = ocupation.size();

        //We suppose that the arrays have the same length, but we check just in case
        if (length_ServiceLevel_data != length_FirstContactResolution_data || length_ServiceLevel_data != lengthOccupation) {
            System.out.println("The arrays have different lengths");
            return performance;
        }
        for (int i = 0; i < length_ServiceLevel_data; i++) {
            double performance_data;
            double serviceLevel_percentage = serviceLevel.get(i) * 0.3;
            double firstContactResolution_percentage = firstContactResolution.get(i) * 0.4;
            double occupation_percentage = ocupation.get(i) * 0.3;
            performance_data = serviceLevel_percentage + firstContactResolution_percentage + occupation_percentage;
            performance.add(performance_data);
        }
        return performance;

    }
}
