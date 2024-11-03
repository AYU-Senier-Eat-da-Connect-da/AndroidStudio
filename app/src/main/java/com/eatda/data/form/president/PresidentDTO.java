package com.eatda.data.form.president;

public class PresidentDTO {
    private Long id;
    private String presidentName;
    private String presidentEmail;
    private String presidentNumber;
    private int businessNumber;

    public Long getPresidentId() {
        return id;
    }

    public String getPresidentEmail() {
        return presidentEmail;
    }

    public String getPresidentName(){
        return this.presidentName;
    }

    public String getPresidentNumber() {
        return presidentNumber;
    }

    public int getBusinessNumber() {
        return businessNumber;
    }
}
