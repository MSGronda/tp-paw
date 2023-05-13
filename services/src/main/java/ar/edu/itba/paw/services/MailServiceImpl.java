package ar.edu.itba.paw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class MailServiceImpl implements MailService {
    private final Environment env;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine, Environment env) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.env = env;
    }

    @Async
    @Override
    public void sendMail(String to, String subject, String body) {
        sendMail(to, subject, body, false);
    }

    @Async
    @Override
    public void sendMail(String to, String subject, String template, Map<String, Object> model, Locale locale) throws MailException {
        final Context ctx = new Context(locale);
        ctx.setVariables(model);

        final String body = templateEngine.process(template, ctx);

        sendMail(to, subject, body, true);
    }

    private void sendMail(String to, String subject, String body, boolean html) {
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
