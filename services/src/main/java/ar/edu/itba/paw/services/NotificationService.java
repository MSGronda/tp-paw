package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class NotificationService {

    private static final long TASK_DELAY = 5 * 60 * 1000; // 5 minutes

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final MailService mailService;
    private final SubjectService subjectService;

    @Autowired
    NotificationService(MailService mailService, SubjectService subjectService) {
        this.mailService = mailService;
        this.subjectService = subjectService;
    }

    @Scheduled(fixedDelay = TASK_DELAY)
    private void notifScheduledTask() {
        Map<User, Set<Subject>> map = subjectService.getAllUserUnreviewedNotifSubjects();

        if(map.isEmpty()) return;

        for(Map.Entry<User,Set<Subject>> mapEntry : map.entrySet()) {
            final User user = mapEntry.getKey();
            final Set<Subject> subjects = mapEntry.getValue();

            final Map<String,Object> mailModel = new HashMap<>();

            //TODO: Find a way to get the base url and each user's locale.
            mailModel.put("baseUrl", "http://localhost:8080");
            mailModel.put("logoUrl", "http://localhost:8080/img/uni.png");
            mailModel.put("subjects", subjects);
            mailService.sendMail(user.getEmail(), "Review your subjects", "reviewnotif", mailModel, Locale.ENGLISH);
        }

        LOGGER.info("Notified {} users to review their subjects", map.size());

        subjectService.updateUnreviewedNotifTime();
    }
}
