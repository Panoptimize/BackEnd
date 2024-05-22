package com.itesm.panoptimize.dto.user_type;

public class UserTypeUpdateDTO {
    private Integer id;
    private String type;

    public UserTypeUpdateDTO() {
    }

    public UserTypeUpdateDTO(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
