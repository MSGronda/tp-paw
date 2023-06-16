package ar.edu.itba.paw.services.tasks;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.MailService;
import ar.edu.itba.paw.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class NotificationTask {
    private static final long TASK_DELAY = 10 * 60 * 1000L; // 10 minutes
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTask.class);

    private final MailService mailService;
    private final SubjectService subjectService;

    @Autowired
    NotificationTask(final MailService mailService, final SubjectService subjectService, final Environment env) {
        this.mailService = mailService;
        this.subjectService = subjectService;
    }

    //@Scheduled(fixedDelay = TASK_DELAY)
    private void notifScheduledTask() {
        LOGGER.debug("Running notification task");

        final Map<User, Set<Subject>> map = subjectService.getAllUserUnreviewedNotificationSubjects();
        if(map.isEmpty()) return;

        for(Map.Entry<User,Set<Subject>> mapEntry : map.entrySet()) {
            mailService.sendReviewNotification(mapEntry.getKey(), mapEntry.getValue());
        }

        LOGGER.info("Notified {} users to review their subjects", map.size());

        subjectService.updateUnreviewedNotificationTime();
    }
}
