package com.itesm.panoptimize.dto.contact_event;

public class ContactEventDTO {
    private String id;
    private String detailType;
    private Detail detail;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getDetailType() { return detailType; }
    public void setDetailType(String value) { this.detailType = value; }

    public Detail getDetail() { return detail; }
    public void setDetail(Detail value) { this.detail = value; }
}