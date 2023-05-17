package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
public class MailServiceImpl implements MailService {
    private final Environment env;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource mailMessages;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(
            final JavaMailSender mailSender,
            final SpringTemplateEngine templateEngine,
            final Environment env,
            @Qualifier("mailMessageSource") final MessageSource mailMessages
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.env = env;
        this.mailMessages = mailMessages;
    }


    private void sendMail(final String to, final String subject, final String body) {
        sendMail(to, subject, body, false);
    }


    private void sendMail(final String to, final String subject, final String template,
                         final Map<String, Object> model, final Locale locale) throws MailException {
        final Context ctx = new Context(locale);
        ctx.setVariables(model);

        final String body = templateEngine.process(template, ctx);

        sendMail(to, subject, body, true);
    }

    @Async
    @Override
    public void sendVerification(String to, String verificationUrl, String logoUrl, Locale locale) {
        final String subject = mailMessages.getMessage("confirmation.subject", null, locale);

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", logoUrl);
        mailModel.put("url", verificationUrl);
        sendMail(to, subject, "verification", mailModel, locale);
    }

    @Async
    @Override
    public void sendRecover(String to, String recoverUrl, String logoUrl, Locale locale) {
        final String subject = mailMessages.getMessage("recovery.subject", null, locale);

        Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", logoUrl);
        mailModel.put("url", recoverUrl);
        sendMail(to, subject, "recovery", mailModel, locale);
    }

    @Async
    @Override
    public void sendReviewNotification(
            String to,
            Set<Subject> subjects,
            Map<String,String> subjectUrls,
            String logoUrl,
            Locale locale
    ) {
        final String subject = mailMessages.getMessage("reviewnotif.subject", null, locale);

        final Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("logoUrl", logoUrl);
        mailModel.put("subjects", subjects);
        mailModel.put("subjectUrls", subjectUrls);
        sendMail(to, subject, "reviewnotif", mailModel, locale);
    }

    private void sendMail(final String to, final String subject, final String body, final boolean html) {
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "UTF-8");

        try {
            helper.setFrom(env.getRequiredProperty("mail.username"), env.getRequiredProperty("mail.displayname"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, html);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.error("Failed to send mail to {}", to);
            throw new IllegalStateException(e);
        }

        mailSender.send(mimeMsg);
        LOGGER.info("Mail sent to {} successfully", to);
    }
}
