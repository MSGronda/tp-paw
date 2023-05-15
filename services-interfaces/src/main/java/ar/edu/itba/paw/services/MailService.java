package ar.edu.itba.paw.services;

import java.util.Locale;
import java.util.Map;

public interface MailService {
    void sendMail(final String to, final String subject, final String body);
    void sendMail(final String to, final String subject, final String template, final Map<String, Object> model, final Locale locale);
}
