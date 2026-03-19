package com.Man10h.social_network_app.clean_architecture.application.port;

public interface MailGateway {
    void sendHtml(String to, String subject, String htmlContent);
}
