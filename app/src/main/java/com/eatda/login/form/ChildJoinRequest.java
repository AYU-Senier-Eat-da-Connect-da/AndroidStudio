package com.eatda.login.form;

public class ChildJoinRequest {
    private String childName;
    private String childEmail;
    private String childPassword;
    private String childNumber;
    private String childAddress;
    public ChildJoinRequest(String childName, String childEmail, String childPassword, String childNumber, String childAddress){
        this.childName = childName;
        this.childEmail = childEmail;
        this.childPassword = childPassword;
        this.childNumber = childNumber;
        this.childAddress = childAddress;
    }
}
