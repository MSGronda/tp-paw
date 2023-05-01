package ar.edu.itba.paw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(String to, String subject, String body) {
        sendMail(to, subject, body, false);
    }

    @Override
    public void sendMail(String to, String subject, String template, Map<String, Object> model) throws MailException {
        final Context ctx = new Context();
        ctx.setVariables(model);

        final String body = templateEngine.process(template, ctx);

        sendMail(to, subject, body, true);
    }

    private void sendMail(String to, String subject, String body, boolean html) {
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "utf-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, html);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }

        mailSender.send(mimeMsg);
    }
}
