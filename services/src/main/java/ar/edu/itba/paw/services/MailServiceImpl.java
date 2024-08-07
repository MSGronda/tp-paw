package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
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
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private final Environment env;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource mailMessages;

    private final String baseUrl;
    private final String logoUrl;

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

        baseUrl = env.getRequiredProperty("baseUrl");
        logoUrl = baseUrl + "/static/uni.png";
    }

    @Async
    @Override
    public void sendVerification(final User to, final String verificationToken) {
        final Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("url", baseUrl + "/confirm?token=" + verificationToken);
        sendMail(to, "verification", mailModel);
    }

    @Async
    @Override
    public void sendRecover(final User to, final String recoveryToken, final Locale locale) {
        final Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("url", baseUrl + "/recover/" + recoveryToken);
        sendMail(to, "recovery", mailModel);
    }

    @Async
    @Override
    public void sendReviewNotification(
            final User to,
            final Set<Subject> subjects
    ) {
        final StringBuilder urlBuilder = new StringBuilder()
            .append(baseUrl)
                .append("/multi-review?r=");
        
        for(Subject subject : subjects) {
            urlBuilder.append(subject.getId())
                .append(" ");
        }
        final String url = urlBuilder.toString().trim();
        
        final Map<String,Object> mailModel = new HashMap<>();
        mailModel.put("subjects", subjects);
        mailModel.put("url", url);
        
        sendMail(to, "reviewnotif", mailModel);
    }

    private void sendMail(final String to, final String subject, final String body, final boolean html) {
        final MimeMessage mimeMsg = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "UTF-8");

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

    private void sendMail(
            final User to,
            final String template,
            final Map<String, Object> model
    ) throws MailException {
        sendMail(to, template, model, Locale.forLanguageTag(to.getLocale()));
    }
    
    private void sendMail(
        final User to,
        final String template,
        final Map<String, Object> model,
        final Locale locale
    ) {
        model.put("baseUrl", baseUrl);
        model.put("logoUrl", logoUrl);

        final Context ctx = new Context(locale);
        ctx.setVariables(model);
        
        final Locale nonNullLoc = locale == null ? Locale.getDefault() : locale;

        final String subject = mailMessages.getMessage(template + ".subject", null, nonNullLoc);
        final String body = templateEngine.process(template, ctx);

        sendMail(to.getEmail(), subject, body, true);
    }
}
