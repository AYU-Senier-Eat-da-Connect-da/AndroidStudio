package com.eatda.data.form.sponsor;

public class SponsorDTO {
    private Long id;
    private String sponsorName;
    private String sponsorEmail;
    private String sponsorAddress;
    private int sponsorAmount;
    private String sponsorNumber;
    private String fcmToken;

    public Long getSponsorId(){
        return this.id;
    }

    public String getSponsorName() {
        return this.sponsorName;
    }

    public String getSponsorNumber(){
        return this.sponsorNumber;
    }

    public String getSponsorEmail(){
        return this.sponsorEmail;
    }

    public String getSponsorAddress(){
        return this.sponsorAddress;
    }

    public Long getId() {
        return id;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public int getSponsorAmount(){
        return this.sponsorAmount;
    }
}
