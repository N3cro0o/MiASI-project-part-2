package pl.krasmap.submission.application.port.out;

import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.event.SubmissionAcceptedEvent;

public interface GetKrasnalInterface {
    void AddNewKrasnal(ReviewKrasnal krasnal);
    void OnSubmissionAccepted(SubmissionAcceptedEvent event);
}
