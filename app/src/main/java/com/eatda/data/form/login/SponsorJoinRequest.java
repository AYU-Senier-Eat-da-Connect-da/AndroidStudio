package com.eatda.data.form.login;

public class SponsorJoinRequest {
    private String sponsorName;
    private String sponsorEmail;
    private String sponsorPassword;
    private String sponsorNumber;
    private String sponsorAddress;
    private String fcmToken;

    public SponsorJoinRequest(String sponsorName, String sponsorEmail, String sponsorPassword, String sponsorNumber, String sponsorAddress, String fcmToken){
        this.sponsorName = sponsorName;
        this.sponsorEmail = sponsorEmail;
        this.sponsorPassword = sponsorPassword;
        this.sponsorNumber = sponsorNumber;
        this.sponsorAddress = sponsorAddress;
        this.fcmToken = fcmToken;
    }
}
