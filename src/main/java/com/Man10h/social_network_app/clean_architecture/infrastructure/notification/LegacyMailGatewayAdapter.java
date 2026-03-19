package com.Man10h.social_network_app.clean_architecture.infrastructure.notification;

import com.Man10h.social_network_app.clean_architecture.application.port.MailGateway;
import org.springframework.stereotype.Component;

@Component
public class LegacyMailGatewayAdapter implements MailGateway {
    private final com.Man10h.social_network_app.service.MailService mailService;

    public LegacyMailGatewayAdapter(com.Man10h.social_network_app.service.MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void sendHtml(String to, String subject, String htmlContent) {
        mailService.sendMail(to, subject, htmlContent);
    }
}
