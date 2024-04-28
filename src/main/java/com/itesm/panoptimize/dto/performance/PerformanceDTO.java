package com.itesm.panoptimize.dto.performance;

//Este endpoint depende de datos de otros endpoints para hacer la ponderacion:
// - El endpoint de KPI de Nivel de servicio (SL) -> 30%
// - El endpoint de KPI de Resolución a Primer Contacto (FCR) -> 30%
// - El endpoint del KPI de Ocupación -> 40%
public class PerformanceDTO {
    private int fcr_KPI; //Extraido de otro endpoint, resolución primer contacto
    private int ocup_KPI; //Extraido de otro endpoint, occupation
    private int adhe_KPI; //Extraidoo de otro endpoint, adherence



    public PerformanceDTO(int fcr_KPI, int ocup_KPI, int adhe_KPI) {
        this.fcr_KPI = fcr_KPI;
        this.ocup_KPI = ocup_KPI;
        this.adhe_KPI = adhe_KPI;
    }

    public PerformanceDTO(){

    }

    public int getFcr_KPI() {
        return fcr_KPI;
    }

    public void setFcr_KPI(int fcr_KPI) {
        this.fcr_KPI = fcr_KPI;
    }

    public int getOcup_KPI() {
        return ocup_KPI;
    }

    public void setOcup_KPI(int ocup_KPI) {
        this.ocup_KPI = ocup_KPI;
    }

    public int getAdhe_KPI() {
        return adhe_KPI;
    }

    public void setAdhe_KPI(int adhe_KPI) {
        this.adhe_KPI = adhe_KPI;
    }




    }

