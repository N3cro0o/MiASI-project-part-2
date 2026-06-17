package pl.krasmap.submission.infrastructure.out;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.krasmap.krasnal.application.port.in.RequestKrasnalInterface;
import pl.krasmap.submission.application.domain.data.ReviewKrasnal;
import pl.krasmap.submission.application.domain.event.SubmissionAcceptedEvent;
import pl.krasmap.submission.application.port.out.GetKrasnalInterface;

@Component
public class GetKrasnalFromContextListener implements GetKrasnalInterface {

    private final RequestKrasnalInterface krasnalIn;

    public GetKrasnalFromContextListener(RequestKrasnalInterface krasnal){
        krasnalIn = krasnal;
    }

    @Override
    public void AddNewKrasnal(ReviewKrasnal krasnal) {
        krasnalIn.AddNewKrasnal(krasnal.name(), krasnal.description(), krasnal.position(), krasnal.category());
    }

    @Override
    @EventListener
    @Async
    public void OnSubmissionAccepted(SubmissionAcceptedEvent event) {
        System.out.println("Submission accepted, sending Krasnal add request");
        AddNewKrasnal(event.krasnal());
    }
}
