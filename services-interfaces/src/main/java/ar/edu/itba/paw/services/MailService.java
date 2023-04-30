package ar.edu.itba.paw.services;

import java.util.Map;

public interface MailService {
    void sendMail(String to, String subject, String body);
    void sendMail(String to, String subject, String template, Map<String, Object> model);
}
