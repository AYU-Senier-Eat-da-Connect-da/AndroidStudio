package com.eatda.login.form;

public class SponsorJoinRequest {
    private String sponsorName;
    private String sponsorEmail;
    private String sponsorPassword;
    private String sponsorNumber;
    private String sponsorAddress;

    public SponsorJoinRequest(String sponsorName, String sponsorEmail, String sponsorPassword, String sponsorNumber, String sponsorAddress){
        this.sponsorName = sponsorName;
        this.sponsorEmail = sponsorEmail;
        this.sponsorPassword = sponsorPassword;
        this.sponsorNumber = sponsorNumber;
        this.sponsorAddress = sponsorAddress;
    }
}
