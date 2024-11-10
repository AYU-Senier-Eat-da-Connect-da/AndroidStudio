package com.eatda.data.form.firbase;

public class FCMNotificationRequestDTO {
    private String title;
    private String body;

    public FCMNotificationRequestDTO(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
