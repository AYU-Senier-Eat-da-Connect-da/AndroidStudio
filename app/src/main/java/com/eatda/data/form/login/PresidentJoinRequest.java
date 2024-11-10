package com.eatda.data.form.login;

public class PresidentJoinRequest {
    private String businessNumber;
    private String presidentName;
    private String presidentEmail;
    private String presidentPassword;
    private String presidentNumber;
    private String fcmToken;

    public PresidentJoinRequest(String presidentName, String presidentEmail, String presidentPassword,String presidentNumber, String presidentBusinessNumber, String fcmToken) {
        this.presidentName = presidentName;
        this.presidentEmail = presidentEmail;
        this.presidentPassword = presidentPassword;
        this.presidentNumber = presidentNumber;
        this.businessNumber = presidentBusinessNumber;
        this.fcmToken = fcmToken;
    }
}
