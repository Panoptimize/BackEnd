package com.itesm.panoptimize.util;

public class Constants {
    public static final String BASE_ARN = "arn:aws:connect:us-east-1:674530197385";

    public static Double roundMetric(Double metric, Integer digits){
        if (metric == null || metric.isNaN() || metric.isInfinite()){
            return null;
        }
        double precisionValue = Math.pow(10, digits);
        metric= metric*precisionValue;
        metric= (double) Math.round(metric);
        metric= metric/precisionValue;
        return metric;
    }
    public static Double roundMetric(Double metric){
        return roundMetric(metric,2);
    }

    public static String extractId(String arn){
        return arn.substring(arn.lastIndexOf('/')+1);
    }

}
