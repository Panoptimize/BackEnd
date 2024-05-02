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


    public static List<Map<String, Double>> performanceCalculation(List<Map<String, Double>> agents) {

        List<Double> serviceLevel = new ArrayList<>(8);
        List<Double> firstContactResolution = new ArrayList<>(8);
        List<Double> ocupation = new ArrayList<>(8);

        //We suppose that the arrays have the same length for this iteration.
        for (Map<String, Double> data : agents) {
            for (Map.Entry<String, Double> entry : data.entrySet()) {

                //Simulation of data collection
                collectionSimulation(serviceLevel);
                collectionSimulation(firstContactResolution);
                collectionSimulation(ocupation);

                //calculating performance of values
                double performance = performanceCalculation(firstContactResolution,ocupation,serviceLevel);
                String agent_name = entry.getKey();
                Double newValue = performance;
                data.put(agent_name,newValue);
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

    private static  Double performanceCalculation(List<Double> serviceLevel, List<Double> firstContactResolution, List<Double> ocupation) {

        Double performance = 0.0;
        double firstContactSum = 0.0;
        double serviceSum = 0.0;
        double ocupationSum = 0.0;
        //Check
        int length_ServiceLevel_data = serviceLevel.size();
        int length_FirstContactResolution_data = firstContactResolution.size();
        int lengthOccupation = ocupation.size();

        //We suppose that the arrays have the same length, but we check just in case
        if (length_ServiceLevel_data != length_FirstContactResolution_data || length_ServiceLevel_data != lengthOccupation) {
            System.out.println("The arrays have different lengths");
            performance = 0.0;
            return performance;
        }

        //suming all the values
        for (Double valor : serviceLevel) {
            serviceSum += valor;
        }
        serviceSum  = (serviceSum/length_ServiceLevel_data);

        for (Double valor : firstContactResolution) {
            firstContactSum += valor;
        }
        firstContactSum  = (firstContactSum/length_ServiceLevel_data);


        for (Double valor : ocupation) {
            ocupationSum += valor;
        }
        ocupationSum  = (ocupationSum/length_ServiceLevel_data);


        //calculating performance
        double serviceLevel_percentage = serviceSum * 0.3;
        double firstContactResolution_percentage = firstContactSum * 0.4;
        double occupation_percentage = ocupationSum * 0.3;
        performance = serviceLevel_percentage + firstContactResolution_percentage + occupation_percentage;

        return performance;
    }
}


