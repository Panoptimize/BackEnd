package com.itesm.panoptimize.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

//TODO Creating the essential endpoints or getting the data from Connect.
// - Endpoint of KPI about Service Level (SL) -> 30%
// - Endpoint of KPI about First Contact Resolution -> 40%
// - Endpoint of KPI about Occupation -> 30%




public class CalculatePerformance {


    public static List<Map<String, List<Double>>> performanceCalculation(List<Map<String, List<Double>>> agents) {

        List<Double> serviceLevel = new ArrayList<>(8);
        List<Double> firstContactResolution = new ArrayList<>(8);
        List<Double> ocupation = new ArrayList<>(8);

        //We suppose that the arrays have the same length for this iteration.
        for (Map<String, List<Double>> data : agents) {
            for (Map.Entry<String, List<Double>> entry : data.entrySet()) {

                //Simulation of data collection
                collectionSimulation(serviceLevel);
                collectionSimulation(firstContactResolution);
                collectionSimulation(ocupation);

                //calculating performance of values
                List <Double> performance = performanceCalculation(firstContactResolution,ocupation,serviceLevel);
                String agent_name = entry.getKey();
                List<Double> newList = performance;
                data.put(agent_name,newList);
            }
        }
        return agents;

    }

    //Simulates the collection of data with the endpoints
    private static List<Double> collectionSimulation(List<Double> data_to_fill){
        Random rand = new Random();

        for (int i = 0; i < 8; i++) {
            double randValue = 1 + rand.nextDouble() * 99;
            data_to_fill.add(randValue);
        }

        return data_to_fill;
    }

    private static  List<Double> performanceCalculation(List<Double> serviceLevel, List<Double> firstContactResolution, List<Double> ocupation) {

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


