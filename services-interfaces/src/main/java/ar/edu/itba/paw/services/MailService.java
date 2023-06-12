package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.ReviewVote;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;

import java.util.Set;

public interface MailService {
    void sendVerification(final User to, final String verificationToken);
    void sendRecover(final User to, final String recoveryToken);
    void sendReviewNotification(
            final User to,
            final Set<Subject> subjects
    );
    void sendVoteNotification(
            final User to,
            final ReviewVote vote,
            final Review review,
            final Subject subject
    );
}
