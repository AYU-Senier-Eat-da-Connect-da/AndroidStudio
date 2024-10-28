package com.eatda.data.form.childManagement;

public class ChildResponse {
    private Long id;
    private String childName;
    private String childEmail;
    private String childNumber;
    private String childAddress;

    public Long getId(){
        return this.id;
    }

    public String getName() {
        return this.childName;
    }

    public String getEmail(){
        return this.childEmail;
    }

    public String getChildNumber(){
        return this.childNumber;
    }

    public String getChildAddress(){
        return this.childAddress;
    }
}
