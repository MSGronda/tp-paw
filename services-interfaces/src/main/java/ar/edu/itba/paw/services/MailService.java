package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface MailService {
    void sendVerification(String to, String verificationUrl, String logoUrl, Locale locale);
    void sendRecover(String to, String recoverUrl, String logoUrl, Locale locale);
    void sendReviewNotification(String to,
                                Set<Subject> subjects,
                                Map<String,String> subjectUrls,
                                String logoUrl,
                                Locale locale);
}
