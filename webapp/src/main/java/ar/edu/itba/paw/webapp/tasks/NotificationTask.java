package ar.edu.itba.paw.webapp.tasks;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.MailService;
import ar.edu.itba.paw.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class NotificationTask {

    private static final long TASK_DELAY = 10 * 60 * 1000L; // 10 minutes

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTask.class);

    private final MailService mailService;
    private final SubjectService subjectService;
    private final String baseUrl;

    @Autowired
    NotificationTask(MailService mailService, @Qualifier("mailMessageSource")MessageSource mailMessages, SubjectService subjectService, Environment env) {
        this.mailService = mailService;
        this.subjectService = subjectService;
        this.baseUrl = env.getRequiredProperty("baseUrl");
    }

    //@Scheduled(fixedDelay = TASK_DELAY)
    private void notifScheduledTask() {

        LOGGER.debug("Running notification task");

        Map<User, Set<Subject>> map = subjectService.getAllUserUnreviewedNotificationSubjects();

        if(map.isEmpty()) return;

        for(Map.Entry<User,Set<Subject>> mapEntry : map.entrySet()) {
            final User user = mapEntry.getKey();
            final Locale locale = user.getLocale().orElse(Locale.getDefault());
            final Set<Subject> subjects = mapEntry.getValue();

            final String logoUrl = baseUrl + "/img/uni.png";
            final Map<String,String> subjectUrls = new HashMap<>();
            for(Subject s : subjects) {
                subjectUrls.put(s.getId(), baseUrl + "/subject/" + s.getId());
            }

            mailService.sendReviewNotification(user.getEmail(), subjects, subjectUrls, logoUrl, locale);
        }

        LOGGER.info("Notified {} users to review their subjects", map.size());

        subjectService.updateUnreviewedNotificationTime();
    }
}
