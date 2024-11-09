package com.eatda.data.form.login;

public class ChildJoinRequest {
    private String childName;
    private String childEmail;
    private String childPassword;
    private String childNumber;
    private String childAddress;
    private String fcmToken;

    public ChildJoinRequest(String childName, String childEmail, String childPassword, String childNumber, String childAddress, String fcmToken){
        this.childName = childName;
        this.childEmail = childEmail;
        this.childPassword = childPassword;
        this.childNumber = childNumber;
        this.childAddress = childAddress;
        this.fcmToken = fcmToken;
    }
}
