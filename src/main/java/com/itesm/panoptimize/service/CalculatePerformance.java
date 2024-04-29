package com.itesm.panoptimize.service;

import java.util.ArrayList;
import java.util.List;

//TODO Creación de endpoints esenciales para performance.
// - El endpoint de KPI de Nivel de servicio (SL) -> 30%
// - El endpoint de KPI de Resolución a Primer Contacto (FCR) -> 40%
// - El endpoint del KPI de Ocupación -> 30%
public class CalculatePerformance {
    public static List<Double> performanceCalculation(List<Double> serviceLevel, List<Double> firstContactResolution, List<Double> ocupation){

        List<Double> performance = new ArrayList<>();
        //Chequeo de longitud
        int lenSl = serviceLevel.size();
        int lenFCR = firstContactResolution.size();
        int lenOC = ocupation.size();

        //Suponiendo que las longitudes/tamaños son iguales
        if ((lenSl == lenFCR) && (lenOC == lenFCR)){
            System.out.println("The data samples are not of the same size.");
            return performance;
        } else{
            for (int i=0;i<=lenSl;i++){
                Double pData = 0.0;
                double slPond = serviceLevel.get(i) * 0.3;
                double fcrPond = firstContactResolution.get(i) * 0.4;
                double  ocuPond = ocupation.get(i) * 0.3;
                pData = slPond + fcrPond + ocuPond;
                performance.add(pData);
            }
            return performance;

        }

    }
}
